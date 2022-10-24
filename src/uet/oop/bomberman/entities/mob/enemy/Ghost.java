package uet.oop.bomberman.entities.mob.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Map;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Ghost extends Enemy{
    public Ghost(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setGoUp();
    }

    @Override
    public void moveEnemy() {
        if (state == 100|| collision) {
            int random = ThreadLocalRandom.current().nextInt(0, 4);
            switch (random) {
                case 0:
                    setGoLeft();
                    break;
                case 1:
                    setGoRight();
                    break;
                case 2:
                    setGoDown();
                    break;
                case 3:
                    setGoUp();
                    break;
            }
        }
    }

    public void update() {
        state++;
        if(state > 100) state = 0;
        if(goLeft||goDown){
            Image image = Sprite.movingSprite(Sprite.ghost_left1, Sprite.ghost_left2, Sprite.ghost_left3, state, 50).getFxImage();
            setImg(image);
        }
        if(goRight||goUp){
            Image image = Sprite.movingSprite(Sprite.ghost_right1, Sprite.ghost_right2, Sprite.ghost_right3, state, 50).getFxImage();
            setImg(image);
        }
    }

    @Override
    public boolean checkCollision(ArrayList<Tile> tiles) {
        collision = false;
        for (Entity obj : tiles) {
            if(obj instanceof Grass) continue;
            if(this.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())){
                collision = true;
                if(obj instanceof Brick){
                    collision = false;
                    return false;
                }
                return true;
            }
        }
        return false;
    }

}
