package uet.oop.bomberman.entities;


import javafx.scene.image.Image;

public class Balloon extends Entity{
    public boolean goUpBalloon, goDownBalloon,goLeftBalloon = true, goRightBalloon;
    public boolean collision = false;
    public Balloon(int x, int y, Image img) {
        super(x, y, img);
        // Thu nhỏ collide box để tránh va chạm với vật thể khác ngay khi vào game
        this.collideBox.setFitHeight(25);
        this.collideBox.setFitWidth(25);
        //
        this.setCollideBox(this.x + 1,  this.y + 1);
    }
    @Override
    public void update() {
    }
    public void move(int dx, int dy) {

        this.x += dx;
        this.y += dy;

        setCollideBox(this.x + 1, this.y + 1);
    }
    public void setGoLeft(){
        goLeftBalloon = true;
        goRightBalloon = false;
        goDownBalloon = false;
        goUpBalloon = false;
    }
    public void setGoRight(){
        goLeftBalloon = false;
        goRightBalloon = true;
        goDownBalloon = false;
        goUpBalloon = false;
    }

    public boolean isGoUpBalloon() {
        return goUpBalloon;
    }

    public boolean isGoDownBalloon() {
        return goDownBalloon;
    }

    public boolean isGoLeftBalloon() {
        return goLeftBalloon;
    }

    public boolean isGoRightBalloon() {
        return goRightBalloon;
    }

    public void setGoUp(){
        goLeftBalloon = false;
        goRightBalloon = false;
        goDownBalloon = false;
        goUpBalloon = true;
    }
    public void setGoDown(){
        goLeftBalloon = false;
        goRightBalloon = false;
        goDownBalloon = true;
        goUpBalloon = false;
    }
    public void setImage(Image img){
        super.setImg(img);
    }
}
