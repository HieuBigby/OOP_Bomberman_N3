package uet.oop.bomberman.entities.mob.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.mob.Mob;
import uet.oop.bomberman.graphics.Sprite;

public class Doll extends Mob {
    public Doll(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        goLeft = true;
    }
    public void update() {
        state++;
        if(state > 100) state = 0;
        if(goLeft||goDown){
            Image image = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, state, 50).getFxImage();
            setImg(image);
        }
        if(goRight||goUp){
            Image image = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, state, 50).getFxImage();
            setImg(image);
        }
    }
    public void moveDoll(){
        if(isGoLeft() && collision) {
            setGoRight();
        }else
        if(isGoRight()&& collision){
            setGoLeft();
        }
    }
}
