package uet.oop.bomberman.entities.mob;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.BoxPos;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Map;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;

/**
 * Class dành cho các đối tương có thể di chuyển
 */
public abstract class Mob extends Entity {
    private BoxPos lastMapPos;
    protected char symbol;
    protected boolean collision = false;
    protected boolean goUp, goDown, goLeft, goRight;
    protected boolean destroy = false;
    private boolean destroyFinished = false;
    protected int destroyTime = 50;


    public Mob(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        lastMapPos = getBoardPos();
    }

    public boolean isDestroy() {
        return destroy;
    }

    public boolean isDestroyFinished() {
        return destroyFinished;
    }

    public void setGoUp(boolean goUp) {
        this.goUp = goUp;
    }

    public void setGoDown(boolean goDown) {
        this.goDown = goDown;
    }

    public void setGoLeft(boolean goLeft) {
        this.goLeft = goLeft;
    }

    public void setGoRight(boolean goRight) {
        this.goRight = goRight;
    }

    public boolean isGoUp() {
        return goUp;
    }

    public void setGoUp() {
        prev = "Up";
        goLeft = false;
        goRight = false;
        goDown = false;
        goUp = true;
    }

    public boolean isGoDown() {
        return goDown;
    }

    public void setGoDown() {
        prev = "Down";
        goLeft = false;
        goRight = false;
        goDown = true;
        goUp = false;
    }

    public boolean isGoLeft() {
        return goLeft;
    }

    public void setGoLeft() {
        prev = "Left";
        goLeft = true;
        goRight = false;
        goDown = false;
        goUp = false;
    }

    public boolean isGoRight() {
        return goRight;
    }

    public void setGoRight() {
        prev = "Right";
        goLeft = false;
        goRight = true;
        goDown = false;
        goUp = false;
    }

    public void destroy(){
        this.destroy = true;
    }

    public void move(int dx, int dy) {

        if(destroy) return;

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 1);
        checkMapPosChange();
    }

    /**
     * Kiểm tra nếu thay đổi vị trí thì cập nhật lại map
     */
    public void checkMapPosChange(){
        BoxPos currentMapPos = getBoardPos();
        if(!currentMapPos.equals(lastMapPos)){
//            System.out.println("Map pos change to " + currentMapPos.x + " : " + currentMapPos.y);
            updateMap(currentMapPos.x, currentMapPos.y);
        }
        lastMapPos = currentMapPos;
    }

    /**
     * Cập nhật lại vị trí trên map
     */
    public void updateMap(int newX, int newY)
    {
        Map.Instance.board[lastMapPos.x][lastMapPos.y] = ' ';
        Map.Instance.board[newX][newY] = symbol;
    }

    /**
     * Xử lý di chuyển
     */
    public void moveHandler(int speed, ArrayList<Tile> tiles) {
        int dx = 0, dy = 0;
        double lastX = collideBox.getX();
        double lastY = collideBox.getY();

        if (goUp) {
            setCollideBox(lastX, lastY - speed);
            if(!checkCollision(tiles))
                dy -= speed;
            else {
                setCollideBox(lastX, lastY);
            }

        }
        if (goDown){
            setCollideBox(lastX, lastY + speed);
            if(!checkCollision(tiles))
                dy += speed;
            else {
                setCollideBox(lastX, lastY);
            }
        }
        if (goRight){
            setCollideBox(lastX  + speed, lastY);
            if(!checkCollision(tiles))
                dx += speed;
            else {
                setCollideBox(lastX, lastY);

            }
        }
        if (goLeft) {
            setCollideBox(lastX  - speed, lastY);
            if(!checkCollision(tiles))
                dx -= speed;
            else {
                setCollideBox(lastX, lastY);
            }
        }
        move(dx, dy);
    }

    /**
     * Kiểm tra va chạm giữa mob và các thực thể khác.
     */
    public boolean checkCollision(ArrayList<Tile> tiles){
        collision = false;
        for (Entity obj : tiles) {
            if(obj instanceof Grass) continue;
            if(this.collideBox.getBoundsInParent().intersects(obj.getCollideBox().getBoundsInParent())){
                collision = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem có lối đi trống ở các vị trí lân cận của đối tượng.
     */
    public boolean isEmptySpace(BoxPos boxPos, AdjacentPos adjacentPos) {
        if(adjacentPos == AdjacentPos.LEFT){
            boxPos.x = (int) (boxPos.x - collideBox.getFitWidth() / 2) - 1;
        } else if (adjacentPos == AdjacentPos.RIGHT) {
            boxPos.x = (int) (boxPos.x + collideBox.getFitWidth() / 2) + 1;
        } else if (adjacentPos == AdjacentPos.UP) {
            boxPos.y = (int) (boxPos.y - collideBox.getFitHeight() / 2) - 1;
        } else if (adjacentPos == AdjacentPos.DOWN) {
            boxPos.y = (int) (boxPos.y + collideBox.getFitHeight() / 2) + 1;
        }
        BoxPos normalizedPos = new BoxPos(boxPos.x / Sprite.SCALED_SIZE, boxPos.y / Sprite.SCALED_SIZE);
//        System.out.println("Checking position: " + normalizedPos.y + ", " + normalizedPos.x
//                + ": " + (map[normalizedPos.y][normalizedPos.x] == ' '));
        if (Map.Instance.board == null) {
            return false;
        }
        return (Map.Instance.board[normalizedPos.y][normalizedPos.x] == ' '
                || Map.Instance.board[normalizedPos.y][normalizedPos.x] == 'p'
                || Map.Instance.board[normalizedPos.y][normalizedPos.x] == '2');
    }

    /**
     * Dịch vị trí của đối tượng khi di ch uyểnva chạm với vật thể khác trong map.
     */
    public void slideWhenCollide(Entity other) {
        BoxPos thisPos = this.getCenterBoxPos();
        BoxPos otherPos = other.getCenterBoxPos();
        if (thisPos.x < otherPos.x && thisPos.y < otherPos.y) {
//            System.out.println("Top left");
            // Xử lý ở vị trí trên bên trái với vật thể va chạm
            if (goDown) {
                if (isEmptySpace(thisPos, AdjacentPos.LEFT)) {
                    move(-1, 0);
                }
            } else if (goRight) {
                if (isEmptySpace(thisPos, AdjacentPos.UP)) {
                    move(0, -1);
                }
            }
        } else if (thisPos.x > otherPos.x && thisPos.y < otherPos.y) {
//            System.out.println("Top right");
            // Xử lý ở vị trí trên bên phải với vật thể va chạm
            if (goDown) {
                if (isEmptySpace(thisPos, AdjacentPos.RIGHT)) {
                    move(1, 0);
                }
            } else if (goLeft) {
                if (isEmptySpace(thisPos, AdjacentPos.UP)) {
                    move(0, -1);
                }

            }
        } else if (thisPos.x < otherPos.x && thisPos.y > otherPos.y) {
//            System.out.println("Bot left");
            // Xử lý ở vị trí dưới bên trái với vật thể va chạm
            if (goUp) {
                if (isEmptySpace(thisPos, AdjacentPos.LEFT)) {
                    move(-1, 0);
                }
            } else if (goRight) {
                if (isEmptySpace(thisPos, AdjacentPos.DOWN)) {
                    move(0, 1);
                }
            }
        } else if (thisPos.x > otherPos.x && thisPos.y > otherPos.y) {
//            System.out.println("Bot right");
            // Xử lý ở vị trí dưới bên phải với vật thể va chạm
            if (goUp) {
                if (isEmptySpace(thisPos, AdjacentPos.RIGHT)) {
                    move(1, 0);
                }
            }
            else if (goLeft) {
                if (isEmptySpace(thisPos, AdjacentPos.DOWN)) {
                    move(0, 1);
                }
            }
        }
    }


    @Override
    public void update() {
        if(destroy) {
            if (destroyTime > 0) {
                destroyTime--;
//                System.out.println("Destroy time: " + destroyTime);
            } else {
                destroyFinished = true;
            }
        }
    }
}
