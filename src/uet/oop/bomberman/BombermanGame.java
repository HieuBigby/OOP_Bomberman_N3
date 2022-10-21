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
import uet.oop.bomberman.entities.mob.enemy.Balloon;
import uet.oop.bomberman.entities.mob.enemy.Doll;
import uet.oop.bomberman.entities.mob.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Item.BombItem;
import uet.oop.bomberman.entities.tile.Item.FlameItem;
import uet.oop.bomberman.entities.tile.Item.Item;
import uet.oop.bomberman.entities.tile.Item.SpeedItem;
import uet.oop.bomberman.entities.tile.Tile;
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
    public static int BOMBER_SPEED = 2;
    public static final int BALlOON_SPEED = 1;

    public static final int ONEAL_SPEED = 1;

    public static final int DOLL_SPEED = 1;
    private GraphicsContext gc;
    private Canvas canvas;
    private char[][] map;

    private int[][] mapOneal;
    private  List<Bomb> bombs = new ArrayList<>();
    private List<Balloon> balloons = new ArrayList<>();
    private List<Oneal> onealList = new ArrayList<>();
    private List<Doll> dolls = new ArrayList<>();
    private ArrayList<Tile> tiles = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();

    Bomber bomberman;



    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Tạo map
        createMap();

        Map.Instance.entityList = this.tiles;
//        Map.Instance.getEntityAt(0, 0);

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
        stage.setTitle("Bomberman Made Hieu x Sang");
        stage.show();
        bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                render();
                update();
            }
        };
        timer.start();
    }



    // Cài đặt input
    public void setInput(Scene scene){
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:    bomberman.setGoUp(); break;
                case S:  bomberman.setGoDown(); break;
                case A:  bomberman.setGoLeft(); break;
                case D: bomberman.setGoRight(); break;
                case SPACE: tiles.add(bomberman.setBomb(gc)); break;
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
//            map = new char[mapHeight][mapWidth];
            Map.Instance.board = new char[mapHeight][mapWidth];
            mapOneal = new int[mapHeight][mapWidth];
            scanner.nextLine();

            for(int i = 0; i < mapHeight; i++){
                String line = scanner.nextLine();

                for(int j = 0; j < line.length(); j++){
                    Character entityName = line.charAt(j);
//                    map[i][j] = entityName;
                    Map.Instance.board[i][j] = entityName;
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
                    tiles.add((Tile) object);
//                    stillObjects.add(object);
                    if(entityName == 's'){
                        SpeedItem speedItem = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage());
                        items.add(speedItem);
                    }
                    if(entityName == 'b'){
                        BombItem bombItem = new BombItem(j, i, Sprite.powerup_bombs.getFxImage());
                        items.add(bombItem);
                    }
                    if(entityName == 'f'){
                        FlameItem flameItem = new FlameItem(j, i, Sprite.powerup_flames.getFxImage());
                        items.add(flameItem);
                    }
                    if(entityName == '1'){
                        Balloon balloon = new Balloon(j, i, Sprite.balloom_left1.getFxImage());
                        balloons.add(balloon);
                    }
                    if(entityName == '2'){
                        Oneal oneal = new Oneal(j, i, Sprite.oneal_left1.getFxImage());
                        onealList.add(oneal);
                    }
                    if(entityName == '3'){
                        Doll doll = new Doll(j, i, Sprite.doll_left1.getFxImage());
                        dolls.add(doll);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update() {
//        entities.forEach(Entity::update);
        tiles.forEach(Tile::update);
        bombs.forEach(Entity::update);
        balloons.forEach(Balloon::update);
        onealList.forEach(Oneal::update);
        items.forEach(Item::update);
        dolls.forEach(Doll::update);
        bomberman.update();
        for(Balloon balloon : balloons){
            balloon.moveBalloon();
            balloon.moveHandler(BALlOON_SPEED, tiles, map);
        }
        for (Oneal oneal : onealList) {
            oneal.moveOneal(bomberman, mapOneal);
            oneal.moveHandler(ONEAL_SPEED, tiles, map);
        }
        for(Doll doll: dolls){
            doll.moveDoll();
            doll.moveHandler(DOLL_SPEED, tiles, map);
        }
        bomberman.moveHandler(BOMBER_SPEED, tiles, Map.Instance.board);
        items.remove(bomberman.checkUseItem(items));
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//        tiles.forEach(g -> g.render(gc));
//        stillObjects.forEach(g -> g.render(gc));
        for(int i = 0; i < tiles.size(); i++){
            Tile tile = tiles.get(i);
            tile.render(gc);
            if(tile instanceof Brick)
            {
                Brick brick = (Brick) tile;
                // Phá tường thay bằng cỏ
                if(brick.finished)
                {
                    tiles.remove(brick);
                    i--;
                    tiles.add(new Grass(brick.getBoardPos().getY(), brick.getBoardPos().getX()
                            , Sprite.grass.getFxImage()));
                }
            }
            if(tile instanceof Bomb)
            {
                Bomb bomb = (Bomb) tile;
                if(bomb.finished) {
                    tiles.remove(bomb);
                    i--;
                }
            }
        }

        bomberman.render(gc);

//        entities.forEach(g -> g.render(gc));
        balloons.forEach(g -> g.render(gc));
        onealList.forEach(g -> g.render(gc));
        dolls.forEach(g -> g.render(gc));
        items.forEach(g -> g.render(gc));
    }

}

