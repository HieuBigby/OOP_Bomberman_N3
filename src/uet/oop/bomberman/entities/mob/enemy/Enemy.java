package uet.oop.bomberman.entities.mob.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.mob.Mob;
import uet.oop.bomberman.graphics.Sound;

public abstract class Enemy extends Mob {
    protected int speed = 1;
    private boolean soundPLayed = false;

    public int getSpeed() {
        return speed;
    }

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);

        // Xét collide box cho enemy
        this.collideBox.setFitHeight(30);
        this.collideBox.setFitWidth(30);

        this.setCollideBox(this.x + 1, this.y + 1);
    }

    @Override
    public void destroy() {
        if (!soundPLayed) {
            Sound.sound.playSound("hitEnemy");
            soundPLayed = true;
        }
        destroy = true;
    }

    public abstract void moveEnemy();
}
