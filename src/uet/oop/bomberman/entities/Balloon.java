package uet.oop.bomberman.entities;


import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public class Balloon extends Entity{
    public boolean collision = false;
    public Balloon(int x, int y, Image img) {
        super(x, y, img);
        // Thu nhỏ collide box để tránh va chạm với vật thể khác ngay khi vào game
        this.collideBox.setFitHeight(25);
        this.collideBox.setFitWidth(25);
        //
        this.setCollideBox(this.x + 1,  this.y + 1);
    }
    @Override
    public void update() {
    }
}
