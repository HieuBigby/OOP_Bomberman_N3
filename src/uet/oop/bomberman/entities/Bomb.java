package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.mob.AdjacentPos;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;



public class Bomb extends Tile {
    public int explodeTime = 120;
    public int remnantTime = 50;
    public boolean exploded = false;
    public boolean finished = false;

    public int flameRendered = 0;

    public Bomb(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        if(explodeTime > 0) {
            explodeTime--;
//            System.out.println("Explode time left: " + explodeTime);
        }
        else {
//            System.out.println("Time out");
            if(!exploded){
                explosion();
//                System.out.println("Exploded");
            }
            if(remnantTime > 0){
                remnantTime--;
            }else{
                finished = true;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        state++;
        if(state > 100) state = 0;
        if(exploded) {
            this.img = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1
                    , Sprite.bomb_exploded2, state, 60).getFxImage();
            renderFlame(gc, state);
        } else {

            this.img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, state, 60).getFxImage();
        }

        gc.drawImage(img, x, y);
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public void renderFlame(GraphicsContext gc, int state)
    {
        renderFlame(AdjacentPos.LEFT, gc);
        renderFlame(AdjacentPos.RIGHT, gc);
        renderFlame(AdjacentPos.UP, gc);
        renderFlame(AdjacentPos.DOWN, gc);
    }

    public void renderFlame(AdjacentPos adjacentPos, GraphicsContext gc)
    {
        BoxPos boardPos = getBoardPos();
        BoxPos collidePos = new BoxPos(clamp(boardPos.getX() + (adjacentPos == AdjacentPos.UP ? -1
                : adjacentPos == AdjacentPos.DOWN ? 1 : 0), 0, Map.Instance.getHeight()),
                clamp(boardPos.getY() + (adjacentPos == AdjacentPos.LEFT ? -1
                : adjacentPos == AdjacentPos.RIGHT ? 1 : 0),  0, Map.Instance.getWidth()));

        if(Map.Instance.board[collidePos.x][collidePos.y] != '#')
        {
            Image flameImgPos;
            switch (adjacentPos)
            {
                case UP:
                    flameImgPos = Sprite.movingSprite(Sprite.explosion_vertical_top_last, Sprite.explosion_vertical_top_last1
                            , Sprite.explosion_vertical_top_last2, state, 60).getFxImage();
                    break;
                case DOWN:
                    flameImgPos = Sprite.movingSprite(Sprite.explosion_vertical_down_last, Sprite.explosion_vertical_down_last1
                            , Sprite.explosion_vertical_down_last2, state, 60).getFxImage();
                    break;
                case LEFT:
                    flameImgPos = Sprite.movingSprite(Sprite.explosion_horizontal_left_last, Sprite.explosion_horizontal_left_last1
                            , Sprite.explosion_horizontal_left_last2, state, 60).getFxImage();
                    break;
                default: // Right
                    flameImgPos = Sprite.movingSprite(Sprite.explosion_horizontal_right_last, Sprite.explosion_horizontal_right_last1
                            , Sprite.explosion_horizontal_right_last2, state, 60).getFxImage();
                    break;
            }

            gc.drawImage(flameImgPos, collidePos.y * Sprite.SCALED_SIZE, collidePos.x * Sprite.SCALED_SIZE);

            flameRendered++;
            if(flameRendered > 4) return;

            Map.Instance.removeAt(collidePos.x, collidePos.y);
        }

//        flameRendered++;
//        if(flameRendered > 4) return;

//        System.out.println(adjacentPos + " flame: " + collidePos.x + ", " + collidePos.y + " collide with "
//                + Map.Instance.board[collidePos.x][collidePos.y]);
    }


    protected void explosion() {
        exploded = true;
    }
}
