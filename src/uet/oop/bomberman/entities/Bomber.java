package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class Bomber extends Entity {

    public Bomber(int x, int y, Image img) {
        super( x, y, img);

        // Thu nhỏ collide box để tránh va chạm với vật thể khác ngay khi vào game
        this.collideBox.setFitHeight(26);
        this.collideBox.setFitWidth(20);

        this.setCollideBox(this.x + 1,  this.y + 3);
    }

    @Override
    public void update() {

    }

    public void move(int dx, int dy) {

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 3);
    }

    public void setBomb(){};
}
