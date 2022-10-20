package uet.oop.bomberman.entities.mob;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public class Mob extends Entity {
    public boolean goUp, goDown, goLeft = true, goRight;

    public Mob(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }
    public void move(int dx, int dy) {

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 1);
    }

    public boolean isGoUp() {
        return goUp;
    }

    public void setGoUp() {
        prev = "Up";
        goLeft = false;
        goRight = false;
        goDown = false;
        goUp = true;
    }

    public boolean isGoDown() {
        return goDown;
    }

    public void setGoDown() {
        prev = "Down";
        goLeft = false;
        goRight = false;
        goDown = true;
        goUp = false;
    }

    public boolean isGoLeft() {
        return goLeft;
    }

    public void setGoLeft() {
        prev = "Left";
        goLeft = true;
        goRight = false;
        goDown = false;
        goUp = false;
    }

    public boolean isGoRight() {
        return goRight;
    }

    public void setGoRight() {
        prev = "Right";
        goLeft = false;
        goRight = true;
        goDown = false;
        goUp = false;
    }
    @Override
    public void update() {

    }
}
