package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.mob.Bomber;
import uet.oop.bomberman.entities.mob.Mob;
import uet.oop.bomberman.entities.mob.enemy.Balloon;
import uet.oop.bomberman.entities.mob.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Wall;
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
    public static final int BOMBER_SPEED = 2;
    public static final int BALlOON_SPEED = 1;

    public static final int ONEAL_SPEED = 1;
    private GraphicsContext gc;
    private Canvas canvas;
    private char[][] map;

    private int[][] mapOneal;
    private List<Balloon> balloons = new ArrayList<>();
    private List<Oneal> onealList = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();

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
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                render();
                update();
                moveHandler(bomberman, BOMBER_SPEED, bomberman.goUp, bomberman.goDown, bomberman.goLeft, bomberman.goRight);
                for (Balloon balloon : balloons) {
                    moveHandler(balloon, BALlOON_SPEED,
                            balloon.goUp, balloon.goDown,
                            balloon.goLeft, balloon.goRight);
                }
                for (Oneal oneal : onealList) {
                    moveHandler(oneal, ONEAL_SPEED,
                            oneal.goUp, oneal.goDown,
                            oneal.goLeft, oneal.goRight);
                }
            }
        };
        timer.start();
    }

    // Xử lý di chuyển Mob
    public void moveHandler(Mob mob, int speed, boolean Up, boolean Down, boolean Left, boolean Right){
        int dx = 0, dy = 0;
        double lastX = mob.collideBox.getX();
        double lastY = mob.collideBox.getY();

        if (Up) {
            mob.setCollideBox(lastX, lastY - speed);
            if(!checkCollision(mob))
                dy -= speed;
            else {
                mob.setCollideBox(lastX, lastY);
            }

        }
        if (Down){
            mob.setCollideBox(lastX, lastY + speed);
            if(!checkCollision(mob))
                dy += speed;
            else {
                mob.setCollideBox(lastX, lastY);
            }
        }
        if (Right){
            mob.setCollideBox(lastX  + speed, lastY);
            if(!checkCollision(mob))
                dx += speed;
            else {
                mob.setCollideBox(lastX, lastY);

            }
        }
        if (Left) {
            mob.setCollideBox(lastX  - speed, lastY);
            if(!checkCollision(mob))
                dx -= speed;
            else {
                mob.setCollideBox(lastX, lastY);
            }
        }
        mob.move(dx, dy);
    }

    // Cài đặt input
    public void setInput(Scene scene){
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:    bomberman.setGoUp(); break;
                case S:  bomberman.setGoDown(); break;
                case A:  bomberman.setGoLeft(); break;
                case D: bomberman.setGoRight(); break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case W:    bomberman.goUp = false; break;
                case S:  bomberman.goDown = false; break;
                case A:  bomberman.goLeft = false; break;
                case D: bomberman.goRight = false; break;
            }
        });
    }

    public void createMap() {
        try (Scanner scanner = new Scanner(new File("res/levels/Level1.txt"))) {
            currentLevel = scanner.nextInt();
            mapHeight = scanner.nextInt();
            mapWidth = scanner.nextInt();
            System.out.println("Create map size: " + mapWidth + ", " + mapHeight);
            map = new char[mapHeight][mapWidth];
            mapOneal = new int[mapHeight][mapWidth];
            scanner.nextLine();

            for(int i = 0; i < mapHeight; i++){
                String line = scanner.nextLine();

                for(int j = 0; j < line.length(); j++){
                    Character entityName = line.charAt(j);
                    map[i][j] = entityName;
                    Entity object;
                    if(entityName == '#'){
                        object = new Wall(j, i, Sprite.wall.getFxImage());
                        mapOneal[i][j] = 0;
                    }else if(entityName == '*'){
                        object = new Brick(j, i, Sprite.brick.getFxImage());
                        mapOneal[i][j] = 0;
                    }else{
                        object = new Grass(j, i, Sprite.grass.getFxImage());
                        mapOneal[i][j] = 1;
                    }
                    stillObjects.add(object);
                    if(entityName == '1'){
                        Balloon balloon = new Balloon(j, i, Sprite.balloom_left1.getFxImage());
                        balloons.add(balloon);
                    }
                    if(entityName == '2'){
                        Oneal oneal = new Oneal(j, i, Sprite.oneal_left1.getFxImage());
                        onealList.add(oneal);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra va chạm giữa bomber và các thực thể khác
    public boolean checkCollision(Mob mob){
        if(mob instanceof Balloon){
            ((Balloon) mob).collision = false;
        }
        for (Entity obj : stillObjects) {
            if(obj instanceof Grass) continue;
            if(mob.collideBox.getBoundsInParent().intersects(obj.collideBox.getBoundsInParent())){
                if(mob instanceof Balloon){
                    ((Balloon) mob).collision = true;
                }
                if(mob instanceof Bomber)
                    ((Bomber) mob).slideWhenCollide(obj, map);
                if(mob instanceof Oneal)
                    ((Oneal) mob).slideWhenCollide(obj, map);
                return true;

            }

        }
        return false;
    }

    public void update() {
        balloons.forEach(Balloon::update);
        onealList.forEach(Oneal::update);
        bomberman.update();
        for(int i=0; i<balloons.size(); i++){
            balloons.get(i).moveBalloon(i);
        }
        for (Oneal oneal : onealList) {
            oneal.moveOneal(bomberman, mapOneal);
        }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        bomberman.render(gc);
        balloons.forEach(g -> g.render(gc));
        onealList.forEach(g -> g.render(gc));
    }

}

