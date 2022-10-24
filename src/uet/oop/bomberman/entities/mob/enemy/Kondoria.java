package uet.oop.bomberman.entities.mob.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Kondoria extends Enemy{
    public Kondoria(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setGoLeft();
    }

    @Override
    public void moveEnemy() {
        if (collision) {
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

    @Override
    public void update() {
        state++;
        if (state > 100) state = 0;
        if (goLeft || goDown) {
            Image image = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, state, 50).getFxImage();
            setImg(image);
        }
        if (goRight || goUp) {
            Image image = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, state, 50).getFxImage();
            setImg(image);
        }
    }

}
