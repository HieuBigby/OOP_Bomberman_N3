package uet.oop.bomberman.graphics;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import uet.oop.bomberman.BombermanGame;


public class Menu {
    public static Menu menu = new Menu();
    public static ImageView status, sound;
    public static Text level, life, bomb, time;
    public static boolean muteSound = false;

    public static void createMenu(Group root,ImageView image) {
        level = new Text("Level: 1");
        level.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        level.setFill(Color.WHITE);
        level.setX(416);
        level.setY(20);
        life = new Text("Life: 3");
        life.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        life.setFill(Color.WHITE);
        life.setX(512);
        life.setY(20);
        bomb = new Text("Bombs: 20");
        bomb.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        bomb.setFill(Color.WHITE);
        bomb.setX(608);
        bomb.setY(20);
        time = new Text("Times: 120");
        time.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        time.setFill(Color.WHITE);
        time.setX(704);
        time.setY(20);

        Image newGame = new Image("image/newGame.png");
        status = new ImageView(newGame);
        status.setX(-65);
        status.setY(-10);
        status.setScaleX(0.5);
        status.setScaleY(0.5);
        Image soundOn = new Image("image/soundOn.png");
        sound = new ImageView(soundOn);
        sound.setX(900);
        sound.setY(-15);
        sound.setScaleX(0.4);
        sound.setScaleY(0.4);
        Pane pane = new Pane();
        pane.getChildren().addAll(level, life, bomb, time, status, sound);
        pane.setMinSize(992, 32);
        pane.setMaxSize(992, 480);
        pane.setStyle("-fx-background-color: #353535");

        root.getChildren().add(pane);

        status.setOnMouseClicked(event -> {
            if(BombermanGame.statusGame == "new"){
                root.getChildren().remove(image);
                BombermanGame.statusGame = "playing";
                Image img = new Image("image/pauseGame.png");
                status.setImage(img);
            }else if(BombermanGame.statusGame == "playing"){
                Image img = new Image("image/playGame.png");
                status.setImage(img);
                BombermanGame.statusGame = "pause";
            }else if(BombermanGame.statusGame == "pause"){
                Image img = new Image("image/pauseGame.png");
                status.setImage(img);
                BombermanGame.statusGame = "playing";
            }
            update();
        });
        sound.setOnMouseClicked(event -> {
            if(muteSound){
                muteSound = false;
                sound.setImage(new Image("image/soundOn.png"));
            }else {
                muteSound = true;
                sound.setImage(new Image("image/soundMute.png"));
            }
        });
    }

    public static void update() {
        level.setText("Level: 1" );
        life.setText("Life: "+ BombermanGame.bomberLife);
        bomb.setText("Bombs: " + BombermanGame.remainBomb);
        time.setText("Times: " + BombermanGame.remainTime);
        if(BombermanGame.statusGame == "lose"){
            Image img = new Image("image/newGame.png");
            status.setImage(img);
            BombermanGame.statusGame = "new";
        }
    }
}
