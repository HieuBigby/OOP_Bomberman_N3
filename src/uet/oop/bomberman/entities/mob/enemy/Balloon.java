package uet.oop.bomberman.entities.mob.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Map;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Balloon extends Enemy {

    public Balloon(int x, int y, Image img) {
        super(x, y, img);
        goLeft = true;
        this.symbol = '1';
    }

    @Override
    public void update() {
        super.update();
//        System.out.println("Destroy time: " + destroy);
        if (goLeft || goDown) {
            Image image = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, state, 50).getFxImage();
            setImg(image);
        }
        if (goRight || goUp) {
            Image image = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, state, 50).getFxImage();
            setImg(image);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (destroy) {
            setImg(Sprite.balloom_dead.getFxImage());
        }
        super.render(gc);
    }

    /**
     * Tạo move cho Ballon kiểu đi vòng quanh
     */
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
}