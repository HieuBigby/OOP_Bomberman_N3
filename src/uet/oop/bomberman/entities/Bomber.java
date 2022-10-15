package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

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
        state++;
        if(state > 100) state = 0;
    if(goLeft){
        Image image = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1, Sprite.player_left_2, state, 20).getFxImage();
        setImg(image);
    }
    if(goRight){
        Image image = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, state, 20).getFxImage();
        setImg(image);
    }
    if(goUp){
        Image image = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1, Sprite.player_up_2, state, 20).getFxImage();
        setImg(image);
    }
    if(goDown){
        Image image = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1, Sprite.player_down_2, state, 20).getFxImage();
        setImg(image);
    }
    }

    public void move(int dx, int dy) {

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 3);
    }

    public void setBomb(){};
}
