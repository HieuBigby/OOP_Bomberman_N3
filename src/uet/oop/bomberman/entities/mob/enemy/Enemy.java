package uet.oop.bomberman.entities.mob.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.mob.Mob;

public abstract class Enemy extends Mob {
    public int speed = 1;
    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);

        // xét collide box cho enemy
        this.collideBox.setFitHeight(30);
        this.collideBox.setFitWidth(30);

        this.setCollideBox(this.x + 1,  this.y + 1);
    }

    public abstract void moveEnemy();
}
