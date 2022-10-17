package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public class Oneal extends Entity {
    public Oneal(int x, int y, Image img) {
        super(x, y, img);
        // Thu nhỏ collide box để tránh va chạm với vật thể khác ngay khi vào game
        this.collideBox.setFitHeight(25);
        this.collideBox.setFitWidth(25);
        //
        this.setCollideBox(this.x + 1,  this.y + 1);
    }

    @Override
    public void update() {
        state++;
        if(state > 100) state = 0;
        if(goLeft||goDown){
            Image image = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, state, 50).getFxImage();
            setImg(image);
        }
        if(goRight||goUp){
            Image image = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, state, 50).getFxImage();
            setImg(image);
        }
    }
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
        return map[normalizedPos.y][normalizedPos.x] != '#'||
                map[normalizedPos.y][normalizedPos.x] !='*';
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
}
