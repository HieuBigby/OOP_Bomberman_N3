package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends Entity {

    public Bomber(int x, int y, Image img) {
        super( x, y, img);

        // Thu nhỏ collide box để tránh va chạm với vật thể khác ngay khi vào game
        this.collideBox.setFitHeight(28);
        this.collideBox.setFitWidth(20);

        this.setCollideBox(this.x + 1,  this.y + 1);
    }

    @Override
    public void update() {

    }

    public void move(int dx, int dy) {

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 1);
    }
}
