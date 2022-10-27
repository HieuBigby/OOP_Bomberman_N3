package uet.oop.bomberman.entities.tile;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public class Tile extends Entity {
    protected int remainTime = 50;
    protected boolean destroy = false;
    protected boolean destroyFinished = false;

    public boolean isDestroyFinished() {
        return destroyFinished;
    }

    public Tile(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }

    public void destroy() {
        this.destroy = true;
    }
}
