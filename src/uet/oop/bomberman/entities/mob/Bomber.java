package uet.oop.bomberman.entities.mob;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Map;
import uet.oop.bomberman.entities.mob.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Item.Item;
import uet.oop.bomberman.entities.tile.Item.SpeedItem;
import uet.oop.bomberman.entities.tile.Item.WallPassItem;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Bomber extends Mob {
    public BoxPos lastMapPos;
    public boolean moveOutOfBomb = true;

    public static boolean wallPass = false;

    public Bomber(int x, int y, Image img) {
        super(x, y, img);

        // Thu nhỏ collide box để tránh va chạm với vật thể khác ngay khi vào game
        this.collideBox.setFitHeight(26);
        this.collideBox.setFitWidth(20);

        this.setCollideBox(this.x + 1, this.y + 3);
        lastMapPos = getPositionInMap();
    }

    @Override
    public void setCollision(boolean collision) {
        super.setCollision(collision);
        moveOutOfBomb = !collision;
    }

//    public void currentlyCollideWithBomb()

    @Override
    public void update() {
//        if(!collision)
//        {
//            moveOutOfBomb = true;
//        }
//        state++;
//        if(state > 100) state = 0;
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

    public void move(int dx, int dy) {

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 3);
    }
    @Override
    public boolean checkCollision(ArrayList<Tile> tiles){
        collision = false;
        for (Entity obj : tiles) {
            if(obj instanceof Grass) continue;
            if(this.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())){
                collision = true;
                    slideWhenCollide(obj);
                    if(wallPass&& obj instanceof Brick){
                        collision = false;
                        return false;
                    }
                    if(obj instanceof Bomb)
                    {
//                        System.out.println("Va chạm với bomb");
                        System.out.println("Bomber chạy ra khỏi bomb: " + moveOutOfBomb);
                        // Chưa chạy ra khỏi bomb là chưa có va chạm
                        if(moveOutOfBomb)
                        {
                            return true;
                        }
                        moveOutOfBomb = false;
                        return false;
                    }


                return true;

            }
            else
            {
                if(obj instanceof Bomb)
                        moveOutOfBomb = true;
            }

        }
        return false;
    }
    public boolean meetingEnemy(List<Enemy> enemies) {
        for(Enemy enemy : enemies) {
            if(this.collideBox.getBoundsInParent().intersects(enemy.collideBox.getBoundsInParent())){
                System.out.println("meeting Enemy");
                isAlive = false;
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
                    BombermanGame.BOMBER_SPEED = 3;
                }
                if(obj instanceof WallPassItem){
                    Bomber.wallPass = true;
                }
                return obj;
            }
        }
        return null;
    }

    @Deprecated
    // (nên dùng getBoardPos)
    public BoxPos getPositionInMap(){
        BoxPos boxCenter = getCenterBoxPos();
//        System.out.println(boxCenter.x / Sprite.SCALED_SIZE + ", " + boxCenter.y / Sprite.SCALED_SIZE);
        return new BoxPos(boxCenter.x / Sprite.SCALED_SIZE, boxCenter.y / Sprite.SCALED_SIZE);
    }

    // Lấy sự kiện bomber di chuyển đến ô khác để cập nhật lại map
    public boolean mapPosChange(){
        BoxPos currentMapPos = getPositionInMap();
        if(!currentMapPos.equals(lastMapPos)){
//            System.out.println("Map pos change to " + currentMapPos.x + " : " + currentMapPos.y);
            updateMap(currentMapPos.x, currentMapPos.y);
        }
        lastMapPos = currentMapPos;
        return true;
    }

    /**
     * cập nhật lại vị trí bomber trên map
     */
    public void updateMap(int newX, int newY)
    {
//        if(Map.Instance.board[lastMapPos.y][lastMapPos.x] == 'B')
//            moveOutOfBomb = true;
        Map.Instance.board[lastMapPos.y][lastMapPos.x] = ' ';
        Map.Instance.board[newY][newX] = 'p';
    }


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
        BoxPos boxPos = this.getCenterBoxPos();
        return new Bomb(boxPos.x / Sprite.SCALED_SIZE, boxPos.y / Sprite.SCALED_SIZE, Sprite.bomb_exploded.getFxImage());
    }
}
