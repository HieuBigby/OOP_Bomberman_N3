package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.mob.Bomber;
import uet.oop.bomberman.entities.mob.enemy.*;
import uet.oop.bomberman.entities.tile.*;
import uet.oop.bomberman.entities.tile.Item.*;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class BombermanGame extends Application {

    public int currentLevel, mapWidth, mapHeight;

    public static int BOMBER_SPEED = 2;


    private GraphicsContext gc;
    private Canvas canvas;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private  List<Bomb> bombs = new ArrayList<>();
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
        Map.Instance.enemies = this.enemies;
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
        Camera camera = new PerspectiveCamera();
        scene.setCamera(camera);
        scene.setFill(Color.SILVER);

        // Them scene vao stage
        stage.setScene(scene);
        stage.setTitle("Bomberman Made Hieu x Sang");
        stage.show();
        bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                render(camera);
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
                case SPACE:
                    if(!Map.Instance.hasBomb())
                        tiles.add(bomberman.setBomb(gc));
                    break;
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

            Map.Instance.board = new char[mapHeight][mapWidth];
            Map.Instance.boardInt = new int[mapHeight][mapWidth];
            scanner.nextLine();

            for(int i = 0; i < mapHeight; i++){
                String line = scanner.nextLine();

                for(int j = 0; j < line.length(); j++){
                    Character entityName = line.charAt(j);

                    Map.Instance.board[i][j] = entityName;
                    Entity object;
                    if(entityName == '#'){
                        object = new Wall(j, i, Sprite.wall.getFxImage());
                        Map.Instance.boardInt[i][j] = 0;
                    }else if(entityName == '*'){
                        object = new Brick(j, i, Sprite.brick.getFxImage());
                        Map.Instance.boardInt[i][j] = 0;
                    }else{
                        object = new Grass(j, i, Sprite.grass.getFxImage());
                        Map.Instance.boardInt[i][j] = 1;
                    }
                    tiles.add((Tile) object);
                    if(entityName == 's'){
//                        SpeedItem speedItem = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage());
//                        items.add(speedItem);
                        tiles.add(new Brick(j, i, Sprite.brick.getFxImage(), ItemType.SpeedItem));
                    }
                    if(entityName == 'b'){
//                        BombItem bombItem = new BombItem(j, i, Sprite.powerup_bombs.getFxImage());
//                        items.add(bombItem);
                        tiles.add(new Brick(j, i, Sprite.brick.getFxImage(), ItemType.BombItem));

                    }
                    if(entityName == 'f'){
//                        FlameItem flameItem = new FlameItem(j, i, Sprite.powerup_flames.getFxImage());
//                        items.add(flameItem);
                        tiles.add(new Brick(j, i, Sprite.brick.getFxImage(), ItemType.FlameItem));
                    }
                    if(entityName == 'w'){
//                        WallPassItem wallPassItem = new WallPassItem(j, i, Sprite.powerup_wallpass.getFxImage());
//                        items.add(wallPassItem);
                        tiles.add(new Brick(j, i, Sprite.brick.getFxImage(), ItemType.WallPassItem));
                    }
                    if(entityName == '1'){
                        Balloon balloon = new Balloon(j, i, Sprite.balloom_left1.getFxImage());
                        enemies.add(balloon);
                    }
                    if(entityName == '2'){
                        Oneal oneal = new Oneal(j, i, Sprite.oneal_left1.getFxImage());
                        enemies.add(oneal);
                    }
                    if(entityName == '3'){
                        Doll doll = new Doll(j, i, Sprite.doll_left1.getFxImage());
                        enemies.add(doll);
                    }
                    if(entityName == '4'){
                        Ghost ghost = new Ghost(j, i, Sprite.ghost_left1.getFxImage());
                        enemies.add(ghost);
                    }
                    if(entityName == '5'){
                        Kondoria kondoria = new Kondoria(j, i, Sprite.kondoria_left1.getFxImage());
                        enemies.add(kondoria);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeBomber(){
//        System.out.println("Xóa bomber");
        bomberman.destroy();
    }

    public void update() {
        for (Tile tile : tiles){
            tile.update();
            if(tile instanceof Bomb){
                if(((Bomb) tile).hitBomber){
                    removeBomber();

                }
            }
        }
        for(Enemy enemy : enemies)
        {
            enemy.update();
            enemy.moveEnemy();
            enemy.moveHandler(enemy.speed, tiles);

        }

        bomberman.update();

        bomberman.moveHandler(BOMBER_SPEED, tiles);
        bomberman.meetingEnemy(enemies);

        if(bomberman.destroyFinished){
            System.out.println("Xóa bomber cũ bằng bomber mới");
            bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        }
        items.remove(bomberman.checkUseItem(items));
    }

    public void render(Camera camera) {

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if(bomberman.getX() > Sprite.SCALED_SIZE * mapWidth/2){
            camera.translateXProperty().set(bomberman.getX() - Sprite.SCALED_SIZE * mapWidth/2);
        }

//        tiles.forEach(g -> g.render(gc));
        for(int i = 0; i < tiles.size(); i++){
            Tile tile = tiles.get(i);
            tile.render(gc);
            if(tile instanceof Brick)
            {
                Brick brick = (Brick) tile;
                // Phá tường thay bằng cỏ
                if(brick.destroyFinished)
                {
                    tiles.remove(brick);
                    i--;
                    tiles.add(new Grass(brick.getBoardPos().getY(), brick.getBoardPos().getX()
                            , Sprite.grass.getFxImage()));

                    Item item = brick.revealHiddenItem();
                    if(item != null){
                        items.add(item);
                    }
                }
            }
            if(tile instanceof Bomb)
            {
                Bomb bomb = (Bomb) tile;
                if(bomb.destroyFinished) {
                    tiles.remove(bomb);
                    i--;
                }
            }
        }

        if(!bomberman.destroyFinished)
            bomberman.render(gc);

        for(int i = 0; i < enemies.size(); i++){
            Enemy enemy = enemies.get(i);
            enemy.render(gc);

            if(enemy.destroyFinished){
                enemies.remove(enemy);
                i--;
            }
        }
//        enemies.forEach(g -> g.render(gc));
        items.forEach(g -> g.render(gc));
    }

}

