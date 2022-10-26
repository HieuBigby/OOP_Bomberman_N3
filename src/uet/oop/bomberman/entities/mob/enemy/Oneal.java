package uet.oop.bomberman.entities.mob.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.BFS;
import uet.oop.bomberman.entities.BoxPos;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Map;
import uet.oop.bomberman.entities.mob.AdjacentPos;
import uet.oop.bomberman.entities.mob.Bomber;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;

public class Oneal extends Enemy {
    public Oneal(int x, int y, Image img) {
        super(x, y, img);
        this.setCollideBox(this.x + 1, this.y + 3);
    }

    @Override
    public void moveEnemy() {
        int[] start = new int[]{getCenterBoxPos().getY() / 32, getCenterBoxPos().getX() / 32};
//        int[] end = new int[]{bomberman.getCenterBoxPos().getY() / 32, bomberman.getCenterBoxPos().getX() / 32};
        int[] end = Map.Instance.getBomberPosition();
        int[] result = BFS.shortestPath(Map.Instance.board, start, end);
        if (!result.equals(start)) {
            if (result[0] < start[1]) {
                setGoLeft();
            } else if (result[0] > start[1]) {
                setGoRight();
            } else if (result[1] < start[0]) {
                setGoUp();
            } else if (result[1] > start[0]) {
                setGoDown();
            }
        }
    }

    @Override
    public void update() {
//        state++;
//        if (state > 100) state = 0;
        super.update();
        if (goLeft || goDown) {
            Image image = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, state, 50).getFxImage();
            setImg(image);
        }
        if (goRight || goUp) {
            Image image = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, state, 50).getFxImage();
            setImg(image);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if(destroy){
            setImg(Sprite.oneal_dead.getFxImage());
        }
        super.render(gc);
    }

    @Override
    public boolean checkCollision(ArrayList<Tile> tiles) {
        collision = false;
        for (Entity obj : tiles) {
            if (obj instanceof Grass) continue;
            if (this.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())) {
                collision = true;
                slideWhenCollide(obj);
                return true;
            }
        }
        return false;
    }


}
