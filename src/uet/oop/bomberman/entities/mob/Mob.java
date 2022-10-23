package uet.oop.bomberman.entities.mob;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Map;
import uet.oop.bomberman.entities.mob.enemy.Balloon;
import uet.oop.bomberman.entities.mob.enemy.Ghost;
import uet.oop.bomberman.entities.mob.enemy.Kondoria;
import uet.oop.bomberman.entities.mob.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Item.SpeedItem;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;

public abstract class Mob extends Entity {
    public boolean collision = false;
    public boolean goUp, goDown, goLeft, goRight;



    public boolean isAlive = true;

    public Mob(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        // Thu nhỏ collide box để tránh va chạm với vật thể khác ngay khi vào game
        this.collideBox.setFitHeight(25);
        this.collideBox.setFitWidth(25);
        //
        this.setCollideBox(this.x + 1,  this.y + 3);
    }
    public void move(int dx, int dy) {

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 1);
    }
    // Xử lý di chuyển Mob
    public void moveHandler(int speed, ArrayList<Tile> tiles) {
        int dx = 0, dy = 0;
        double lastX = collideBox.getX();
        double lastY = collideBox.getY();

        if (goUp) {
            setCollideBox(lastX, lastY - speed);
            if(!checkCollision(tiles))
                dy -= speed;
            else {
                setCollideBox(lastX, lastY);
            }

        }
        if (goDown){
            setCollideBox(lastX, lastY + speed);
            if(!checkCollision(tiles))
                dy += speed;
            else {
                setCollideBox(lastX, lastY);
            }
        }
        if (goRight){
            setCollideBox(lastX  + speed, lastY);
            if(!checkCollision(tiles))
                dx += speed;
            else {
                setCollideBox(lastX, lastY);

            }
        }
        if (goLeft) {
            setCollideBox(lastX  - speed, lastY);
            if(!checkCollision(tiles))
                dx -= speed;
            else {
                setCollideBox(lastX, lastY);
            }
        }
        move(dx, dy);
    }
    // Kiểm tra va chạm giữa mob và các thực thể khác
    public boolean checkCollision(ArrayList<Tile> tiles){
        collision = false;
        for (Entity obj : tiles) {
            if(obj instanceof Grass) continue;
            if(this.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())){
                collision = true;
                return true;
            }
        }
        return false;
    };
    // Kiểm tra xem có lối đi trống cho bomber
    public boolean isEmptySpace(BoxPos boxPos, AdjacentPos adjacentPos) {
        if(adjacentPos == AdjacentPos.LEFT){
            boxPos.x = (int) (boxPos.x - collideBox.getFitWidth() / 2) - 1;
        } else if (adjacentPos == AdjacentPos.RIGHT) {
            boxPos.x = (int) (boxPos.x + collideBox.getFitWidth() / 2) + 1;
        } else if (adjacentPos == AdjacentPos.UP) {
            boxPos.y = (int) (boxPos.y - collideBox.getFitHeight() / 2) - 1;
        } else if (adjacentPos == AdjacentPos.DOWN) {
            boxPos.y = (int) (boxPos.y + collideBox.getFitHeight() / 2) + 1;
        }
        BoxPos normalizedPos = new BoxPos(boxPos.x / Sprite.SCALED_SIZE, boxPos.y / Sprite.SCALED_SIZE);
//        System.out.println("Checking position: " + normalizedPos.y + ", " + normalizedPos.x
//                + ": " + (map[normalizedPos.y][normalizedPos.x] == ' '));
        if (Map.Instance.board == null) {
            return false;
        }
        return (Map.Instance.board[normalizedPos.y][normalizedPos.x] == ' '
                || Map.Instance.board[normalizedPos.y][normalizedPos.x] == 'p');
    }

    // Dịch vị trí nhân vật khi va chạm với vật thể khác trong map
    public void slideWhenCollide(Entity other) {
        BoxPos thisPos = this.getCenterBoxPos();
        BoxPos otherPos = other.getCenterBoxPos();
        if (thisPos.x < otherPos.x && thisPos.y < otherPos.y) {
//            System.out.println("Top left");
            // Xử lý ở vị trí trên bên trái với vật thể va chạm
            if (goDown) {
                if (isEmptySpace(thisPos, AdjacentPos.LEFT)) {
                    move(-1, 0);
                }
            } else if (goRight) {
                if (isEmptySpace(thisPos, AdjacentPos.UP)) {
                    move(0, -1);
                }
            }
        } else if (thisPos.x > otherPos.x && thisPos.y < otherPos.y) {
//            System.out.println("Top right");
            // Xử lý ở vị trí trên bên phải với vật thể va chạm
            if (goDown) {
                if (isEmptySpace(thisPos, AdjacentPos.RIGHT)) {
                    move(1, 0);
                }
            } else if (goLeft) {
                if (isEmptySpace(thisPos, AdjacentPos.UP)) {
                    move(0, -1);
                }

            }
        } else if (thisPos.x < otherPos.x && thisPos.y > otherPos.y) {
//            System.out.println("Bot left");
            // Xử lý ở vị trí dưới bên trái với vật thể va chạm
            if (goUp) {
                if (isEmptySpace(thisPos, AdjacentPos.LEFT)) {
                    move(-1, 0);
                }
            } else if (goRight) {
                if (isEmptySpace(thisPos, AdjacentPos.DOWN)) {
                    move(0, 1);
                }
            }
        } else if (thisPos.x > otherPos.x && thisPos.y > otherPos.y) {
//            System.out.println("Bot right");
            // Xử lý ở vị trí dưới bên phải với vật thể va chạm
            if (goUp) {
                if (isEmptySpace(thisPos, AdjacentPos.RIGHT)) {
                    move(1, 0);
                }
            }
            else if (goLeft) {
                if (isEmptySpace(thisPos, AdjacentPos.DOWN)) {
                    move(0, 1);
                }
            }
        }
    }

    public boolean isGoUp() {
        return goUp;
    }

    public void setGoUp() {
        prev = "Up";
        goLeft = false;
        goRight = false;
        goDown = false;
        goUp = true;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public boolean isGoDown() {
        return goDown;
    }

    public void setGoDown() {
        prev = "Down";
        goLeft = false;
        goRight = false;
        goDown = true;
        goUp = false;
    }

    public boolean isGoLeft() {
        return goLeft;
    }

    public void setGoLeft() {
        prev = "Left";
        goLeft = true;
        goRight = false;
        goDown = false;
        goUp = false;
    }

    public boolean isGoRight() {
        return goRight;
    }

    public void setGoRight() {
        prev = "Right";
        goLeft = false;
        goRight = true;
        goDown = false;
        goUp = false;
    }
    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    @Override
    public void update() {

    }
}
