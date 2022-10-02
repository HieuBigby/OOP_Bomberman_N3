package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class BombermanGame extends Application {
    public int currentLevel, mapWidth, mapHeight;
//    public static final int WIDTH = 20;
//    public static final int HEIGHT = 15;
    public static final int BOMBER_SPEED = 3;
    
    private GraphicsContext gc;
    private Canvas canvas;
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();

    boolean goUp, goDown, goRight, goLeft;
    Bomber bomberman;


    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Tạo map
        createMap();

        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * mapWidth, Sprite.SCALED_SIZE * mapHeight);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);
        setInput(scene);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                update();
                inputHandler();
            }
        };
        timer.start();
    }

    // Xử lý input khi điều kiển bomber
    public void inputHandler(){
        int dx = 0, dy = 0;
        double lastX = bomberman.collideBox.getX();
        double lastY = bomberman.collideBox.getY();

        if (goUp) {
            bomberman.setCollideBox(lastX, lastY - BOMBER_SPEED);
            if(!checkCollision())
                dy -= BOMBER_SPEED;
            else {
                bomberman.setCollideBox(lastX, lastY);
            }
        }
        if (goDown){
            bomberman.setCollideBox(lastX, lastY + BOMBER_SPEED);
            if(!checkCollision())
                dy += BOMBER_SPEED;
            else {
                bomberman.setCollideBox(lastX, lastY);
            }
        }
        if (goRight){
            bomberman.setCollideBox(lastX  + BOMBER_SPEED, lastY);
            if(!checkCollision())
                dx += BOMBER_SPEED;
            else {
                bomberman.setCollideBox(lastX, lastY);
            }
        }
        if (goLeft) {
            bomberman.setCollideBox(lastX  - BOMBER_SPEED, lastY);
            if(!checkCollision())
                dx -= BOMBER_SPEED;
            else {
                bomberman.setCollideBox(lastX, lastY);
            }
        }

        bomberman.move(dx, dy);
    }

    // Cài đặt input
    public void setInput(Scene scene){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:    goUp = true; break;
                    case S:  goDown = true; break;
                    case A:  goLeft = true; break;
                    case D: goRight = true; break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:    goUp = false; break;
                    case S:  goDown = false; break;
                    case A:  goLeft = false; break;
                    case D: goRight = false; break;
                }
            }
        });
    }

    public void createMap() {
        try (Scanner scanner = new Scanner(new File("res/levels/Level1.txt"))) {
            currentLevel = scanner.nextInt();
            mapHeight = scanner.nextInt();
            mapWidth = scanner.nextInt();
            scanner.nextLine();

            for(int i = 0; i < mapHeight; i++){
                String line = scanner.nextLine();

                for(int j = 0; j < line.length(); j++){
                    Character entityName = line.charAt(j);
                    Entity object;
                    if(entityName == '#'){
                        object = new Wall(j, i, Sprite.wall.getFxImage());
                    }else if(entityName == '*'){
                        object = new Brick(j, i, Sprite.brick.getFxImage());
                    }else{
                        object = new Grass(j, i, Sprite.grass.getFxImage());
                    }
                    stillObjects.add(object);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra va chạm giữa bomber và các thực thể khác
    public boolean checkCollision(){
        if(stillObjects.size() == 0) return false;

        for (Entity obj : stillObjects) {
            if(obj instanceof Grass || obj instanceof Bomber) continue;
            if(bomberman.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())){
                System.out.println("Collide with " + obj.toString());
                return true;
            }
        }
        return false;
    }

    public void update() {
        entities.forEach(Entity::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
    }
}

