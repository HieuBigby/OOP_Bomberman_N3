package uet.oop.bomberman.entities.mob;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.BoxPos;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Map;
import uet.oop.bomberman.entities.mob.enemy.Enemy;
import uet.oop.bomberman.entities.tile.*;
import uet.oop.bomberman.entities.tile.Item.*;
import uet.oop.bomberman.graphics.Sound;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Bomber extends Mob {

    public boolean moveOutOfBomb = true;
    public int speed = 2;


    public boolean wallPass = false;
    public boolean flameX2 = false;
    public boolean multipleBomb = false;
    public static boolean soundCompletePLayed = false;

    public Bomber(int x, int y, Image img) {
        super(x, y, img);

        // Thu nhỏ collide box để tránh va chạm với vật thể khác ngay khi vào game
        this.collideBox.setFitHeight(26);
        this.collideBox.setFitWidth(20);

        this.setCollideBox(this.x + 1, this.y + 3);
        this.destroyTime = 100;
        this.symbol = 'p';

    }

//    @Override
//    public void setCollision(boolean collision) {
//        super.setCollision(collision);
//        moveOutOfBomb = !collision;
//    }

//    public void currentlyCollideWithBomb()

    @Override
    public void update() {
//        if(!collision)
//        {
//            moveOutOfBomb = true;
//        }
//        state++;
//        if(state > 100) state = 0;
        super.update();
        if(goLeft){
            Image image = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1, Sprite.player_left_2, state, 20).getFxImage();
            setImg(image);
        }
        else if(goRight){
            Image image = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, state, 20).getFxImage();
            setImg(image);
        }
        else if(goUp){
            Image image = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1, Sprite.player_up_2, state, 20).getFxImage();
            setImg(image);
        }
        else if(goDown){
            Image image = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1, Sprite.player_down_2, state, 20).getFxImage();
            setImg(image);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if(destroy){
            this.img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2
                    , Sprite.player_dead3, state, 60).getFxImage();
        }
        super.render(gc);
    }

    public void move(int dx, int dy) {
        if(destroy) return;

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 3);
        checkMapPosChange();
    }
    @Override
    public boolean checkCollision(ArrayList<Tile> tiles) {
        collision = false;
        boolean collideWithWall = false;
        boolean collideWithBrick = false;
        boolean collideWithBoom = false;
        Wall wallObj = null;

        for (Entity obj : tiles) {
            if (obj instanceof Portal
                    && this.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())) {
                BombermanGame.statusGame = "win";
            }
            if (obj instanceof Grass) continue;

            if (this.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())) {
//                collision = true;
//                if(wallPass && obj instanceof Brick){
//                    System.out.println("Chạm brick");
//                    collision = false;
////                    return false;
//                }

                if (obj instanceof Brick) {
                    System.out.println("Cham brick");
                    collideWithBrick = true;
//                    if(wallPass) return false;
//                    return true;
                }

                if (obj instanceof Wall) {
                    System.out.println("Chạm wall");
                    collideWithWall = true;
                    wallObj = (Wall) obj;
//                    slideWhenCollide(obj);
//                    return true;
                }


                if (obj instanceof Bomb) {
                    collideWithBoom = true;
//                        System.out.println("Va chạm với bomb");
//                    System.out.println("Bomber chạy ra khỏi bomb: " + moveOutOfBomb);
                    // Chưa chạy ra khỏi bomb là chưa có va chạm
//                    if(moveOutOfBomb)
//                    {
//                        return true;
//                    }
//                    moveOutOfBomb = false;
//                    return false;
                }

//                if(collision)
//                    slideWhenCollide(obj);

//                return true;
//                return true;
            } else {
                if (obj instanceof Bomb) {
                    Bomb bomb = (Bomb) obj;
                    if (!bomb.bomberOut) {
                        moveOutOfBomb = true;
                        bomb.bomberOut = true;
                    }
                }
            }
        }
        if (collideWithWall) {
            if (wallObj != null)
                slideWhenCollide(wallObj);
            return true;
        }
        if (collideWithBrick && !wallPass) return true;
        if (collideWithBoom) {
            if (moveOutOfBomb) {
                return true;
            }
            moveOutOfBomb = false;
            return false;
        }
        return false;
    }


    public boolean meetingEnemy(List<Enemy> enemies) {
        for(Enemy enemy : enemies) {
            if(this.collideBox.getBoundsInParent().intersects(enemy.collideBox.getBoundsInParent())){
//                System.out.println("meeting Enemy");
                this.destroy();
                return true;
            }
        }
        return false;
    }
    // Xử lý bomber ăn item trong map
    public Item checkUseItem(ArrayList<Item> items){
        for (Item obj : items) {
            if(this.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())){
                if(obj instanceof SpeedItem){
                    this.speed = 3;
                }
                if(obj instanceof WallPassItem){
                    this.wallPass = true;
                }
                if(obj instanceof FlameItem)
                {
                    this.flameX2 = true;
                }
                if(obj instanceof BombItem)
                {
                    this.multipleBomb = true;
                }
                return obj;
            }
        }
        return null;
    }

//    @Deprecated
//    // (nên dùng getBoardPos)
//    public BoxPos getPositionInMap(){
//        BoxPos boxCenter = getCenterBoxPos();
////        System.out.println(boxCenter.x / Sprite.SCALED_SIZE + ", " + boxCenter.y / Sprite.SCALED_SIZE);
//        return new BoxPos(boxCenter.x / Sprite.SCALED_SIZE, boxCenter.y / Sprite.SCALED_SIZE);
//    }

    // Lấy sự kiện bomber di chuyển đến ô khác để cập nhật lại map



    /**
     * Kiểm tra xem bomber đã di chuyển ra khỏi bomb chưa (còn lỗi)
     * @return
     */
    public boolean moveOutOfBomb(){
        BoxPos boardPos = getBoardPos();
//        System.out.println("Có bomb ở vị trí " + boardPos.x + ", " + boardPos.y + ": "
//                + Map.Instance.hasBombAt(boardPos.x, boardPos.y));
        return !Map.Instance.hasBombAt(boardPos.x, boardPos.y);
    }

    /**
     * Đặt bom
     */
    public Bomb setBomb(GraphicsContext gc) {
        System.out.println("Bomb spawn at " + this.x + ", " + this.y);
        moveOutOfBomb = false;
//        BoxPos boxPos = this.getCenterBoxPos();
        BoxPos boardPos = this.getBoardPos();
//        if(flameX2){
            return new Bomb(boardPos.y, boardPos.x, Sprite.bomb.getFxImage(), flameX2);
//        }
//        return new Bomb(boxPos.x / Sprite.SCALED_SIZE, boxPos.y / Sprite.SCALED_SIZE, Sprite.bomb_exploded.getFxImage());
    }
}
