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
    private static ImageView statusGame;
    public static Text level, bomb, time;

    public static String status = "new";
    public static Menu menu = new Menu();

    public static void createMenu(Group root,ImageView image) {
        level = new Text("Level: 1");
        level.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        level.setFill(Color.WHITE);
        level.setX(416);
        level.setY(20);
        bomb = new Text("Bombs: 20");
        bomb.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        bomb.setFill(Color.WHITE);
        bomb.setX(512);
        bomb.setY(20);
        time = new Text("Times: 120");
        time.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        time.setFill(Color.WHITE);
        time.setX(608);
        time.setY(20);

        Image newGame = new Image("image/newGame.png");
        statusGame = new ImageView(newGame);
        statusGame.setX(-75);
        statusGame.setY(-10);
        statusGame.setScaleX(0.5);
        statusGame.setScaleY(0.5);

        Pane pane = new Pane();
        pane.getChildren().addAll(level, bomb, time, statusGame);
        pane.setMinSize(992, 32);
        pane.setMaxSize(992, 480);
        pane.setStyle("-fx-background-color: #353535");

        root.getChildren().add(pane);

        statusGame.setOnMouseClicked(event -> {
            if(status == "new"){
                root.getChildren().remove(image);
                status = "playing";
                Image img = new Image("image/pauseGame.png");
                statusGame.setImage(img);
            }else if(status == "playing"){
                Image img = new Image("image/playGame.png");
                statusGame.setImage(img);
                status = "pause";
            }else if(status == "pause"){
                Image img = new Image("image/pauseGame.png");
                statusGame.setImage(img);
                status = "playing";
            }
            update();
        });

    }

    public static void update() {
        level.setText("Level: 1" );
        bomb.setText("Bombs: " + BombermanGame.remainBomb);
        time.setText("Times: " + BombermanGame.remainTime);
    }
}
