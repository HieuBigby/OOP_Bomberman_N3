package uet.oop.bomberman.entities.tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

public class Brick extends Tile {
    public boolean destroyed = false;
    public boolean finished = false;
    public int remnantTime = 50;

    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public void destroy()
    {
//        System.out.println("Đã xóa brick");
        this.destroyed = true;
    }

    @Override
    public void update() {
        if(destroyed) {
            if (remnantTime > 0) {
                remnantTime--;
//                System.out.println("Thời gian còn lại của brick: " + remnantTime);
            } else {
                finished = true;
            }
        }
    }



    @Override
    public void render(GraphicsContext gc) {
        if(destroyed)
        {
//            System.out.println("Đang chạy animation phá hủy: " + state);
            this.img = Sprite.movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1
                    , Sprite.brick_exploded2, state, 60).getFxImage();
        }
        gc.drawImage(Sprite.grass.getFxImage(), this.x, this.y);
        super.render(gc);
    }
}