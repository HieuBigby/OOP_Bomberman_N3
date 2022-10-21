package uet.oop.bomberman.entities.mob;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.mob.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Item.Item;
import uet.oop.bomberman.entities.tile.Item.SpeedItem;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;

public class Bomber extends Mob {

    public Bomber(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        state++;
        if(state > 100) state = 0;
        if(goLeft){
            Image image = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1, Sprite.player_left_2, state, 20).getFxImage();
            setImg(image);
        }
        if(goRight){
            Image image = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, state, 20).getFxImage();
            setImg(image);
        }
        if(goUp){
            Image image = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1, Sprite.player_up_2, state, 20).getFxImage();
            setImg(image);
        }
        if(goDown){
            Image image = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1, Sprite.player_down_2, state, 20).getFxImage();
            setImg(image);
        }
    }

    public void move(int dx, int dy) {

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 3);
    }
    // Xử lý bomber ăn item trong map
    public Item checkUseItem(ArrayList<Item> items){
        for (Item obj : items) {
            if(this.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())){
                if(obj instanceof SpeedItem){
                    BombermanGame.BOMBER_SPEED = 3;
                }
                return obj;
            }
        }
        return null;
    }
    // Kiểm tra xem có lối đi trống cho bomber
    public boolean isEmptySpace(BoxPos boxPos, AdjacentPos adjacentPos, char[][] map) {
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
        if (map == null) {
            return false;
        }
        return map[normalizedPos.y][normalizedPos.x] == ' ';
    }

    // Dịch vị trí nhân vật khi va chạm với vật thể khác trong map
    public void slideWhenCollide(Entity other, char[][] map) {
        BoxPos thisPos = this.getCenterBoxPos();
        BoxPos otherPos = other.getCenterBoxPos();
        if (thisPos.x < otherPos.x && thisPos.y < otherPos.y) {
//            System.out.println("Top left");
            // Xử lý ở vị trí trên bên trái với vật thể va chạm
            if (goDown) {
                if (isEmptySpace(thisPos, AdjacentPos.LEFT, map)) {
                    move(-1, 0);
                }
            } else if (goRight) {
                if (isEmptySpace(thisPos, AdjacentPos.UP, map)) {
                    move(0, -1);
                }
            }
        } else if (thisPos.x > otherPos.x && thisPos.y < otherPos.y) {
//            System.out.println("Top right");
            // Xử lý ở vị trí trên bên phải với vật thể va chạm
            if (goDown) {
                if (isEmptySpace(thisPos, AdjacentPos.RIGHT, map)) {
                    move(1, 0);
                }
            } else if (goLeft) {
                if (isEmptySpace(thisPos, AdjacentPos.UP, map)) {
                    move(0, -1);
                }

            }
        } else if (thisPos.x < otherPos.x && thisPos.y > otherPos.y) {
//            System.out.println("Bot left");
            // Xử lý ở vị trí dưới bên trái với vật thể va chạm
            if (goUp) {
                if (isEmptySpace(thisPos, AdjacentPos.LEFT, map)) {
                    move(-1, 0);
                }
            } else if (goRight) {
                if (isEmptySpace(thisPos, AdjacentPos.DOWN, map)) {
                    move(0, 1);
                }
            }
        } else if (thisPos.x > otherPos.x && thisPos.y > otherPos.y) {
//            System.out.println("Bot right");
            // Xử lý ở vị trí dưới bên phải với vật thể va chạm
            if (goUp) {
                if (isEmptySpace(thisPos, AdjacentPos.RIGHT, map)) {
                    move(1, 0);
                }
            }
            else if (goLeft) {
                if (isEmptySpace(thisPos, AdjacentPos.DOWN, map)) {
                    move(0, 1);
                }
            }
        }
    }


    public void setBomb() {
    }
}
