package uet.oop.bomberman.entities.mob;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.BoxPos;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.mob.enemy.Enemy;
import uet.oop.bomberman.entities.tile.*;
import uet.oop.bomberman.entities.tile.Item.*;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Bomber extends Mob {
    private boolean moveOutOfBomb = true;
    private int speed = 2;
    private boolean wallPass = false;
    private boolean flameX2 = false;
    private boolean multipleBomb = false;

    public int getSpeed() {
        return speed;
    }

    public boolean canPlaceMultipleBomb() {
        return multipleBomb;
    }

    public Bomber(int x, int y, Image img) {
        super(x, y, img);

        // Thu nhỏ collide box để tránh va chạm với vật thể khác ngay khi vào game
        this.collideBox.setFitHeight(26);
        this.collideBox.setFitWidth(20);

        this.setCollideBox(this.x + 1, this.y + 3);
        this.destroyTime = 100;
        this.symbol = 'p';
    }

    @Override
    public void update() {
        super.update();
        if (goLeft) {
            Image image = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1, Sprite.player_left_2, state, 20).getFxImage();
            setImg(image);
        } else if (goRight) {
            Image image = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, state, 20).getFxImage();
            setImg(image);
        } else if (goUp) {
            Image image = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1, Sprite.player_up_2, state, 20).getFxImage();
            setImg(image);
        } else if (goDown) {
            Image image = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1, Sprite.player_down_2, state, 20).getFxImage();
            setImg(image);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (destroy) {
            this.img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2
                    , Sprite.player_dead3, state, 60).getFxImage();
        }
        super.render(gc);
    }

    @Override
    public void move(int dx, int dy) {
        if (destroy) return;

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 3);
        checkMapPosChange();
    }

    /**
     * Xử lý bomber va chạm với các vật thể tĩnh.
     */
    @Override
    public boolean checkCollision(ArrayList<Tile> tiles) {
        collision = false;
        boolean collideWithWall = false;
        boolean collideWithBrick = false;
        boolean collideWithBoom = false;
        Wall collideWallObj = null;
        Brick collideBrickObj = null;

        for (Entity obj : tiles) {
            if (obj instanceof Portal
                    && this.collideBox.getBoundsInParent().intersects(obj.getCollideBox().getBoundsInParent())) {
                BombermanGame.statusGame = "win";
            }
            if (obj instanceof Grass) continue;
            if (this.collideBox.getBoundsInParent().intersects(obj.getCollideBox().getBoundsInParent())) {
                if (obj instanceof Brick) {
//                    System.out.println("Cham brick");
                    collideWithBrick = true;
                    collideBrickObj = (Brick) obj;
                }
                if (obj instanceof Wall) {
//                    System.out.println("Chạm wall");
                    collideWithWall = true;
                    collideWallObj = (Wall) obj;
                }
                if (obj instanceof Bomb) {
                    collideWithBoom = true;
//                    System.out.println("Va chạm với bomb");
//                    System.out.println("Bomber chạy ra khỏi bomb: " + moveOutOfBomb);
                }
            } else {
                if (obj instanceof Bomb) {
                    Bomb bomb = (Bomb) obj;
                    if (!bomb.isBomberOut()) {
                        moveOutOfBomb = true;
                        bomb.setBomberOut(true);
                    }
                }
            }
        }
        // Va chạm với tường có độ ưu tiên cao nhất
        if (collideWithWall) {
            if (collideWallObj != null)
                slideWhenCollide(collideWallObj);
            return true;
        }
        if (collideWithBrick && !wallPass) {
            if (collideWallObj != null)
                slideWhenCollide(collideWallObj);
            return true;
        }
        if (collideWithBoom) {
            if (moveOutOfBomb) {
                return true;
            }
            moveOutOfBomb = false;
            return false;
        }
        return false;
    }


    /**
     * Xử lý bomber va chạm với enemy.
     */
    public boolean meetingEnemy(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (this.collideBox.getBoundsInParent().intersects(enemy.getCollideBox().getBoundsInParent())) {
//                System.out.println("meeting Enemy");
                this.destroy();
                return true;
            }
        }
        return false;
    }

    /**
     * Xử lý bomber ăn item trong map
     */
    public Item checkUseItem(ArrayList<Item> items) {
        for (Item obj : items) {
            if (this.collideBox.getBoundsInParent().intersects(obj.getCollideBox().getBoundsInParent())) {
                if (obj instanceof SpeedItem) {
                    this.speed = 3;
                }
                if (obj instanceof WallPassItem) {
                    this.wallPass = true;
                }
                if (obj instanceof FlameItem) {
                    this.flameX2 = true;
                }
                if (obj instanceof BombItem) {
                    this.multipleBomb = true;
                }
                return obj;
            }
        }
        return null;
    }

    /**
     * Đặt bom
     */
    public Bomb setBomb(GraphicsContext gc) {
//        System.out.println("Bomb spawn at " + this.x + ", " + this.y);
        moveOutOfBomb = false;
        BoxPos boardPos = this.getBoardPos();
        return new Bomb(boardPos.y, boardPos.x, Sprite.bomb.getFxImage(), flameX2);
    }
}
