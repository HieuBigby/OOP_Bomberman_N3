package uet.oop.bomberman.entities.mob.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Balloon extends Enemy {
    public Balloon(int x, int y, Image img) {
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
            Image image = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, state, 50).getFxImage();
            setImg(image);
        }
        if(goRight||goUp){
            Image image = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, state, 50).getFxImage();
            setImg(image);
        }
    }
    // tạo move cho Ballon kiểu đi vòng quanh
    public void moveBalloon(int type) {
        if(type % 2 == 0){
            // Go around
            if(isGoLeft() && collision) {
                setGoDown();
            }else
            if(isGoDown()&& collision){
                setGoRight();
            } else
            if(isGoRight()&& collision){
                setGoUp();
            }else
            if(isGoUp()&& collision){
                setGoLeft();
            }
        }else {
            // left <-> right
            if(isGoLeft() && collision) {
                setGoRight();
            }else
            if(isGoRight()&& collision){
                setGoLeft();
            }
        }

    }

    public void move(){
        x++;
    }
}