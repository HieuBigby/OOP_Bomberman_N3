package uet.oop.bomberman.entities.mob.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Map;
import uet.oop.bomberman.entities.mob.Mob;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;

public class Doll extends Enemy {
    public Doll(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        goLeft = true;
    }

    @Override
    public void moveEnemy() {
        if(isGoLeft() && collision) {
            setGoRight();
        }else
        if(isGoRight()&& collision){
            setGoLeft();
        }
    }

    public void update() {
//        state++;
//        if(state > 100) state = 0;
        super.update();

        if(goLeft||goDown){
            Image image = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, state, 50).getFxImage();
            setImg(image);
        }
        if(goRight||goUp){
            Image image = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, state, 50).getFxImage();
            setImg(image);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if(destroy){
            setImg(Sprite.doll_dead.getFxImage());
        }

        super.render(gc);
    }
}
