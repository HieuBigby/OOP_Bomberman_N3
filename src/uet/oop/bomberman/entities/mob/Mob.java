package uet.oop.bomberman.entities.mob;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Map;
import uet.oop.bomberman.entities.mob.enemy.Balloon;
import uet.oop.bomberman.entities.mob.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Item.SpeedItem;
import uet.oop.bomberman.entities.tile.Tile;

import java.util.ArrayList;

public class Mob extends Entity {
    public boolean collision = false;
    public boolean goUp, goDown, goLeft, goRight;

    public Mob(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        // Thu nhỏ collide box để tránh va chạm với vật thể khác ngay khi vào game
        this.collideBox.setFitHeight(25);
        this.collideBox.setFitWidth(25);
        //
        this.setCollideBox(this.x + 1,  this.y + 3);
    }
    public void move(int dx, int dy) {

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 1);
    }
    // Xử lý di chuyển Mob
    public void moveHandler(int speed, ArrayList<Tile> tiles, char[][]map) {
        int dx = 0, dy = 0;
        double lastX = collideBox.getX();
        double lastY = collideBox.getY();

        if (goUp) {
            setCollideBox(lastX, lastY - speed);
            if(!checkCollision(tiles, map))
                dy -= speed;
            else {
                setCollideBox(lastX, lastY);
            }

        }
        if (goDown){
            setCollideBox(lastX, lastY + speed);
            if(!checkCollision(tiles, map))
                dy += speed;
            else {
                setCollideBox(lastX, lastY);
            }
        }
        if (goRight){
            setCollideBox(lastX  + speed, lastY);
            if(!checkCollision(tiles, map))
                dx += speed;
            else {
                setCollideBox(lastX, lastY);

            }
        }
        if (goLeft) {
            setCollideBox(lastX  - speed, lastY);
            if(!checkCollision(tiles, map))
                dx -= speed;
            else {
                setCollideBox(lastX, lastY);
            }
        }
        move(dx, dy);
    }
    // Kiểm tra va chạm giữa bomber và các thực thể khác
    public boolean checkCollision(ArrayList<Tile> tiles, char[][] map){
                collision = false;
        for (Entity obj : tiles) {
            if(obj instanceof Grass) continue;
            if(this.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())){
                    collision = true;
                if(this instanceof Bomber){
                    Bomber bomber = (Bomber) this;
                    bomber.slideWhenCollide(obj, Map.Instance.board);
                    if(obj instanceof Bomb)
                    {
//                        System.out.println("Va chạm với bomb");
                        System.out.println("Bomber chạy ra khỏi bomb: " + bomber.moveOutOfBomb);
                        // Chưa chạy ra khỏi bomb là chưa có va chạm
                        if(bomber.moveOutOfBomb)
                        {
                            return true;
                        }
                        bomber.moveOutOfBomb = false;
                        return false;
//                        return bomber.moveOutOfBomb();
                    }
//                    else {
//                        System.out.println("Sao không true");
//                        bomber.moveOutOfBomb = true;
//                    }
                }

                if(this instanceof Oneal)
                    ((Oneal) this).slideWhenCollide(obj, map);
                return true;

            }
            else
            {
                if(this instanceof Bomber){
                    if(obj instanceof Bomb) {
                        ((Bomber) this).moveOutOfBomb = true;
                    }
                }
            }

        }
        return false;
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

    public void setCollision(boolean collision) {
        this.collision = collision;
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
    @Override
    public void update() {

    }
}
