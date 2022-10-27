package uet.oop.bomberman.entities.mob.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Random;

public class Doll extends Enemy {
    private int sprintTime = 100;
    private Random sprintRand = new Random();

    public Doll(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        goLeft = true;
        this.symbol = '3';
    }

    @Override
    public void moveEnemy() {
        if (isGoLeft() && collision) {
            setGoRight();
        } else if (isGoRight() && collision) {
            setGoLeft();
        }
    }

    public void update() {
        super.update();

        int sprintValue = sprintRand.nextInt(100);
        if (sprintValue < 20) { // 20%
            this.speed = 3;
        } else {
            this.speed = 1;
        }

        if (goLeft || goDown) {
            Image image = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, state, 50).getFxImage();
            setImg(image);
        }
        if (goRight || goUp) {
            Image image = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, state, 50).getFxImage();
            setImg(image);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (destroy) {
            setImg(Sprite.doll_dead.getFxImage());
        }
        super.render(gc);
    }
}
