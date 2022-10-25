package uet.oop.bomberman.entities.tile;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public class Tile extends Entity {
    public int remainTime = 50;
    public boolean destroy = false;
    public boolean destroyFinished = false;
    public Tile(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }

    public void destroy()
    {
//        System.out.println("Đã xóa brick");
        this.destroy = true;
    }
}
