package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.mob.AdjacentPos;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sound;
import uet.oop.bomberman.graphics.Sprite;

import static uet.oop.bomberman.entities.BoxPos.clamp;


public class Bomb extends Tile {
    private int explodeTime = 120;
    private boolean exploded = false;
    private boolean doubleExplode = false;
    private boolean soundPlayed = false;
    private boolean bomberOut = false;

    public boolean isBomberOut() {
        return bomberOut;
    }

    public void setBomberOut(boolean bomberOut) {
        this.bomberOut = bomberOut;
    }

    public Bomb(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Bomb(int xUnit, int yUnit, Image img, boolean doubleExplode) {
        super(xUnit, yUnit, img);
        this.doubleExplode = doubleExplode;
    }

    @Override
    public void update() {
        if (explodeTime > 0) {
            explodeTime--;
            if (!soundPlayed && explodeTime == 20) {
                Sound.sound.playSound("exploded");
                soundPlayed = true;
            }
//            System.out.println("Explode time left: " + explodeTime);
        } else {
//            System.out.println("Time out");
            if (!exploded) {
                exploded = true;
//                System.out.println("Exploded");
            }
            if (remainTime > 0) {
                remainTime--;
            } else {
                destroyFinished = true;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (exploded) {
            this.img = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1
                    , Sprite.bomb_exploded2, state, 60).getFxImage();
            renderFlame(gc, doubleExplode);
        } else {

            this.img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, state, 60).getFxImage();
        }
        super.render(gc);
    }

    /**
     * Render flame của bomb.
     */
    public void renderFlame(GraphicsContext gc, boolean x2) {
        renderFlame(AdjacentPos.LEFT, gc, x2);
        renderFlame(AdjacentPos.RIGHT, gc, x2);
        renderFlame(AdjacentPos.UP, gc, x2);
        renderFlame(AdjacentPos.DOWN, gc, x2);
    }

    /**
     * Render flame của bomb theo hướng.
     */
    public void renderFlame(AdjacentPos adjacentPos, GraphicsContext gc, boolean x2) {
        BoxPos boardPos = getBoardPos();
        BoxPos flamePos = new BoxPos(clamp(boardPos.getX() + (adjacentPos == AdjacentPos.UP ? -1
                : adjacentPos == AdjacentPos.DOWN ? 1 : 0), 0, Map.Instance.getHeight()),
                clamp(boardPos.getY() + (adjacentPos == AdjacentPos.LEFT ? -1
                        : adjacentPos == AdjacentPos.RIGHT ? 1 : 0), 0, Map.Instance.getWidth()));

        BoxPos lastFlamePos = flamePos.clone(adjacentPos);

        // Chỉ khi điểm va chạm khác tường thì mới cho render
        if (Map.Instance.board[flamePos.x][flamePos.y] != '#') {
            Image lastFlameImg;
            Image middleFlameImg;
            switch (adjacentPos) {
                case UP:
                    lastFlameImg = Sprite.movingSprite(Sprite.explosion_vertical_top_last, Sprite.explosion_vertical_top_last1
                            , Sprite.explosion_vertical_top_last2, state, 60).getFxImage();
                    middleFlameImg = Sprite.movingSprite(Sprite.explosion_vertical, Sprite.explosion_vertical1,
                            Sprite.explosion_vertical2, state, 60).getFxImage();
                    break;
                case DOWN:
                    lastFlameImg = Sprite.movingSprite(Sprite.explosion_vertical_down_last, Sprite.explosion_vertical_down_last1
                            , Sprite.explosion_vertical_down_last2, state, 60).getFxImage();
                    middleFlameImg = Sprite.movingSprite(Sprite.explosion_vertical, Sprite.explosion_vertical1,
                            Sprite.explosion_vertical2, state, 60).getFxImage();
                    break;
                case LEFT:
                    lastFlameImg = Sprite.movingSprite(Sprite.explosion_horizontal_left_last, Sprite.explosion_horizontal_left_last1
                            , Sprite.explosion_horizontal_left_last2, state, 60).getFxImage();
                    middleFlameImg = Sprite.movingSprite(Sprite.explosion_horizontal, Sprite.explosion_horizontal1,
                            Sprite.explosion_horizontal2, state, 60).getFxImage();
                    break;
                default: // Right
                    lastFlameImg = Sprite.movingSprite(Sprite.explosion_horizontal_right_last, Sprite.explosion_horizontal_right_last1
                            , Sprite.explosion_horizontal_right_last2, state, 60).getFxImage();
                    middleFlameImg = Sprite.movingSprite(Sprite.explosion_horizontal, Sprite.explosion_horizontal1,
                            Sprite.explosion_horizontal2, state, 60).getFxImage();
                    break;
            }

            if (x2) {
                gc.drawImage(middleFlameImg, flamePos.y * Sprite.SCALED_SIZE, flamePos.x * Sprite.SCALED_SIZE);
                gc.drawImage(lastFlameImg, lastFlamePos.y * Sprite.SCALED_SIZE, lastFlamePos.x * Sprite.SCALED_SIZE);
            } else { // render flame cuối
                gc.drawImage(lastFlameImg, flamePos.y * Sprite.SCALED_SIZE, flamePos.x * Sprite.SCALED_SIZE);
            }

            Map.Instance.removeAt(boardPos.x, boardPos.y);
            Map.Instance.removeAt(flamePos.x, flamePos.y);
            if (x2) {
                Map.Instance.removeAt(lastFlamePos.x, lastFlamePos.y);
            }
        }
    }
}
