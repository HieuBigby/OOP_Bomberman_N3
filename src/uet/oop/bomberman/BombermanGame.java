package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    public static final int BALlOON_SPEED = 2;
    
    private GraphicsContext gc;
    private Canvas canvas;
    private List<Entity> entities = new ArrayList<>();
    private List<Balloon> balloons = new ArrayList<>();
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
                moveHandler(bomberman, BOMBER_SPEED, goUp, goDown, goLeft, goRight);
                for (Balloon balloon : balloons) {
                    moveHandler(balloon, BALlOON_SPEED,
                            balloon.goUpBalloon, balloon.goDownBalloon,
                            balloon.goLeftBalloon, balloon.goRightBalloon);
                }
            }
        };
        timer.start();
    }

    // Xử lý di chuyển Entity
    public void moveHandler(Entity entity, int speed,boolean Up, boolean Down, boolean Left, boolean Right){
        int dx = 0, dy = 0;
        double lastX = entity.collideBox.getX();
        double lastY = entity.collideBox.getY();

        if (Up) {
            entity.setCollideBox(lastX, lastY - speed);
            if(!checkCollision(entity))
                dy -= speed;
            else {
                entity.setCollideBox(lastX, lastY);
            }
        }
        if (Down){
            entity.setCollideBox(lastX, lastY + speed);
            if(!checkCollision(entity))
                dy += speed;
            else {
                entity.setCollideBox(lastX, lastY);
            }
        }
        if (Right){
            entity.setCollideBox(lastX  + speed, lastY);
            if(!checkCollision(entity))
                dx += speed;
            else {
                entity.setCollideBox(lastX, lastY);
            }
        }
        if (Left) {
            entity.setCollideBox(lastX  - speed, lastY);
            if(!checkCollision(entity))
                dx -= speed;
            else {
                entity.setCollideBox(lastX, lastY);
            }
        }

        entity.move(dx, dy);
    }

    // Cài đặt input
    public void setInput(Scene scene){
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:    goUp = true; break;
                case S:  goDown = true; break;
                case A:  goLeft = true; break;
                case D: goRight = true; break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case W:    goUp = false; break;
                case S:  goDown = false; break;
                case A:  goLeft = false; break;
                case D: goRight = false; break;
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
                    if(entityName == '1'){
                        Balloon balloon = new Balloon(j, i, Sprite.balloom_left1.getFxImage());
                        balloons.add(balloon);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    // tạo move cho Ballon kiểu đi vòng quanh
    public void moveBalloon1(Balloon balloon) {
            if(balloon.isGoLeftBalloon() && balloon.collision) {
                balloon.setGoDown();
            }else
            if(balloon.isGoDownBalloon()&& balloon.collision){
                balloon.setGoRight();
            } else
            if(balloon.isGoRightBalloon()&& balloon.collision){
                balloon.setGoUp();
            }else
            if(balloon.isGoUpBalloon()&& balloon.collision){
                balloon.setGoLeft();
            }
    }
    // tạo move cho Balloon kiểu left <-> right
    public void moveBalloon2(Balloon balloon) {
        if(balloon.isGoLeftBalloon() && balloon.collision) {
            balloon.setGoRight();
        }else
        if(balloon.isGoRightBalloon()&& balloon.collision){
            balloon.setGoLeft();
        }
    }

    // Kiểm tra va chạm giữa bomber và các thực thể khác
    public boolean checkCollision(Entity entity){
        if(entity instanceof Balloon){
            ((Balloon) entity).collision = false;
        }
        for (Entity obj : stillObjects) {
            if(obj instanceof Grass ) continue;
            if(entity.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())){
                if(entity instanceof Balloon){
                    ((Balloon) entity).collision = true;
                }
                return true;
            }
        }
        return false;
    }

    public void update() {
        entities.forEach(Entity::update);
        for(int i=0; i<balloons.size(); i++){
            if(i % 2 ==0){
                moveBalloon1(balloons.get(i));
            } else {
                moveBalloon2(balloons.get(i));
            }

        }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        balloons.forEach(g -> g.render(gc));
    }
}

