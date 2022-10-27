package uet.oop.bomberman.entities.tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.BoxPos;
import uet.oop.bomberman.entities.tile.Item.*;
import uet.oop.bomberman.graphics.Sprite;

public class Brick extends Tile {
    private ItemType hiddenItem;

    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Brick(int xUnit, int yUnit, Image img, ItemType hiddenItem) {
        super(xUnit, yUnit, img);
        this.hiddenItem = hiddenItem;
    }

    @Override
    public void update() {
        if (destroy) {
            if (remainTime > 0) {
                remainTime--;
//                System.out.println("Thời gian còn lại của brick: " + remnantTime);
            } else {
                destroyFinished = true;
            }
        }
    }

    /**
     * Hiện item khi bomber phá tường.
     */
    public Item revealHiddenItem() {
        if (hiddenItem == null) {
            return null;
        }
        BoxPos boardPos = getBoardPos();
        switch (hiddenItem) {
            case BombItem:
                return new BombItem(boardPos.y, boardPos.x, Sprite.powerup_bombs.getFxImage());
            case SpeedItem:
                return new SpeedItem(boardPos.y, boardPos.x, Sprite.powerup_speed.getFxImage());
            case FlameItem:
                return new FlameItem(boardPos.y, boardPos.x, Sprite.powerup_flames.getFxImage());
            case WallPassItem:
                return new WallPassItem(boardPos.y, boardPos.x, Sprite.powerup_wallpass.getFxImage());
            default:
                return null;
        }
    }


    @Override
    public void render(GraphicsContext gc) {
        if (destroy) {
//            System.out.println("Đang chạy animation phá hủy: " + state);
            this.img = Sprite.movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1
                    , Sprite.brick_exploded2, state, 60).getFxImage();
        }
        super.render(gc);
    }
}