package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    protected int x;
    //Tọa độ Y tính từ góc trái trên trong Canvas
    protected int y;
    public int state = 0;

    public boolean goUp, goDown, goLeft = true, goRight;


    protected Image img;
    public ImageView collideBox;

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity( int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
        collideBox = new ImageView(img);
        setCollideBox(this.x, this.y);
    }

    public void setCollideBox(double x, double y)
    {
        this.collideBox.setX(x);
        this.collideBox.setY(y);
    }

    public BoxPos getCenterBoxPos()
    {
//        System.out.println(this.collideBox.getBoundsInLocal().getWidth());
        return new BoxPos((int) (this.x + this.collideBox.getBoundsInLocal().getWidth() / 2),
                (int) (this.y + this.collideBox.getBoundsInLocal().getHeight() / 2));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }
    public abstract void update();

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public void move(int dx, int dy) {

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 1);
    }

    public boolean isGoUp() {
        return goUp;
    }

    public void setGoUp() {
        goLeft = false;
        goRight = false;
        goDown = false;
        goUp = true;
    }

    public boolean isGoDown() {
        return goDown;
    }

    public void setGoDown() {
        goLeft = false;
        goRight = false;
        goDown = true;
        goUp = false;
    }

    public boolean isGoLeft() {
        return goLeft;
    }

    public void setGoLeft() {
        goLeft = true;
        goRight = false;
        goDown = false;
        goUp = false;
    }

    public boolean isGoRight() {
        return goRight;
    }

    public void setGoRight() {
        goLeft = false;
        goRight = true;
        goDown = false;
        goUp = false;
    }

    public class BoxPos{
        int x, y;

        public BoxPos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "BoxPos{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
