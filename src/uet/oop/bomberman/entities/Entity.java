package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uet.oop.bomberman.entities.mob.AdjacentPos;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    protected int x;
    //Tọa độ Y tính từ góc trái trên trong Canvas
    protected int y;
    public int state = 0;

    // trạng thái di chuyển
    public String prev;

    protected Image img;
    public ImageView collideBox;


    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity(int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
        collideBox = new ImageView(img);
        setCollideBox(this.x, this.y);
    }

    public void setCollideBox(double x, double y) {
        this.collideBox.setX(x);
        this.collideBox.setY(y);
    }

    public BoxPos getCenterBoxPos() {
//        System.out.println(this.collideBox.getBoundsInLocal().getWidth());
        return new BoxPos((int) (this.x + this.collideBox.getBoundsInLocal().getWidth() / 2),
                (int) (this.y + this.collideBox.getBoundsInLocal().getHeight() / 2));
    }

    public BoxPos getBoardPos()
    {
        BoxPos centerPos = getCenterBoxPos();
        return new BoxPos(centerPos.y / Sprite.SCALED_SIZE, centerPos.x / Sprite.SCALED_SIZE);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void render(GraphicsContext gc) {
        state++;
        if(state > 100) state = 0;
        gc.drawImage(img, x, y);
    }

    public abstract void update();

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }
//    public void destroy(){
//        this.destroy = true;
//    }



}

