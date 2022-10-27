package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.mob.Bomber;
import uet.oop.bomberman.entities.mob.enemy.*;
import uet.oop.bomberman.entities.tile.*;
import uet.oop.bomberman.entities.tile.Item.*;
import uet.oop.bomberman.graphics.Sound;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class BombermanGame extends Application {
    public int currentLevel, mapWidth, mapHeight;

    private boolean soundLosePlayed = false;
    private boolean soundCompletePlayed = false;

    public static int remainBomb = 50;
    public static int remainTime = 180;

    public static int bomberLife = 3;
    private long previousTime = 0;

    public static String statusGame = "new";
    public static int level = 1;

    private GraphicsContext gc;
    private Canvas canvas;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private List<Bomb> bombs = new ArrayList<>();
    private ArrayList<Tile> tiles = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private Bomber bomberman;


    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Tạo map
        createMap(1);

        // Tham chiếu đến map
        Map.Instance.stillObjects = this.tiles;
        Map.Instance.enemies = this.enemies;

        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * mapWidth, Sprite.SCALED_SIZE * mapHeight);
        canvas.setTranslateY(32);
        gc = canvas.getGraphicsContext2D();
        Image background = new Image("image/background.png");
        ImageView backgroundView = new ImageView(background);
        backgroundView.setX(-310);
        backgroundView.setY(-135);
        backgroundView.setScaleX(0.63);
        backgroundView.setScaleY(0.62);

        // Tao root container
        Group root = new Group();
        Menu.menu.createMenu(root, backgroundView);
        root.getChildren().add(canvas);
        root.getChildren().add(backgroundView);

        // Tao scene
        Scene scene = new Scene(root);
        setInput(scene);

        // Them scene vao stage
        stage.setScene(scene);
        stage.setTitle("Bomberman Made Hieu x Sang");
        stage.show();
        bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        Map.Instance.bomber = bomberman;
        Sound.sound.playSound("main");
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!soundLosePlayed && (remainTime == 0 || bomberLife == 0)) {
                    Sound.sound.playSound("lose");
                    root.getChildren().add(backgroundView);
                    soundLosePlayed = true;
                    statusGame = "lose";
                    restart();
                }
                if (statusGame.equals("win")) {
                    if (!soundCompletePlayed) {
                        Sound.sound.playSound("complete");
                        soundCompletePlayed = true;
                    }
                    root.getChildren().add(backgroundView);
                    restart();
                }
                if (statusGame != "pause" && statusGame != "new") {
                    render();
                    update();
                    timeCounter();
                }
            }
        };
        timer.start();
    }

    public void restart() {
        remainBomb = 50;
        remainTime = 180;

        bomberLife = 3;
        soundLosePlayed = false;
        soundCompletePlayed = false;

        enemies = new ArrayList<>();
        bombs = new ArrayList<>();
        tiles = new ArrayList<>();
        items = new ArrayList<>();

        Map.Instance.stillObjects = this.tiles;
        Map.Instance.enemies = this.enemies;

        bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        Map.Instance.bomber = this.bomberman;
        if (statusGame == "win" && level < 2) {
            level++;
        }
        createMap(level);
    }


    // Cài đặt input
    public void setInput(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    bomberman.setGoUp();
                    break;
                case S:
                    bomberman.setGoDown();
                    break;
                case A:
                    bomberman.setGoLeft();
                    break;
                case D:
                    bomberman.setGoRight();
                    break;
                case SPACE:
                    if (bomberman.canPlaceMultipleBomb() || !Map.Instance.hasBomb() && remainBomb > 0) {
                        tiles.add(bomberman.setBomb(gc));
                        remainBomb--;
                    }
                    break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case W:
                    bomberman.setGoUp(false);
                    break;
                case S:
                    bomberman.setGoDown(false);
                    break;
                case A:
                    bomberman.setGoLeft(false);
                    break;
                case D:
                    bomberman.setGoRight(false);
                    break;
            }
        });
    }

    public void createMap(int level) {
        try (Scanner scanner = new Scanner(new File("res/levels/Level" + level + ".txt"))) {
            currentLevel = scanner.nextInt();
            mapHeight = scanner.nextInt();
            mapWidth = scanner.nextInt();
            System.out.println("Create map size: " + mapWidth + ", " + mapHeight);

            Map.Instance.board = new char[mapHeight][mapWidth];
            scanner.nextLine();

            for (int i = 0; i < mapHeight; i++) {
                String line = scanner.nextLine();

                for (int j = 0; j < line.length(); j++) {
                    Character entityName = line.charAt(j);

                    Map.Instance.board[i][j] = entityName;
                    Entity object;
                    if (entityName == '#') {
                        object = new Wall(j, i, Sprite.wall.getFxImage());
                    } else if (entityName == '*') {
                        object = new Brick(j, i, Sprite.brick.getFxImage());
                    } else {
                        object = new Grass(j, i, Sprite.grass.getFxImage());
                    }
                    tiles.add((Tile) object);
                    if (entityName == 's') {
                        tiles.add(new Brick(j, i, Sprite.brick.getFxImage(), ItemType.SpeedItem));
                    }
                    if (entityName == 'b') {
                        tiles.add(new Brick(j, i, Sprite.brick.getFxImage(), ItemType.BombItem));

                    }
                    if (entityName == 'f') {
                        tiles.add(new Brick(j, i, Sprite.brick.getFxImage(), ItemType.FlameItem));
                    }
                    if (entityName == 'w') {
                        tiles.add(new Brick(j, i, Sprite.brick.getFxImage(), ItemType.WallPassItem));
                    }
                    if (entityName == '1') {
                        Balloon balloon = new Balloon(j, i, Sprite.balloom_left1.getFxImage());
                        enemies.add(balloon);
                    }
                    if (entityName == '2') {
                        Oneal oneal = new Oneal(j, i, Sprite.oneal_left1.getFxImage());
                        enemies.add(oneal);
                    }
                    if (entityName == '3') {
                        Doll doll = new Doll(j, i, Sprite.doll_left1.getFxImage());
                        enemies.add(doll);
                    }
                    if (entityName == '4') {
                        Ghost ghost = new Ghost(j, i, Sprite.ghost_left1.getFxImage());
                        enemies.add(ghost);
                    }
                    if (entityName == '5') {
                        Kondoria kondoria = new Kondoria(j, i, Sprite.kondoria_left1.getFxImage());
                        enemies.add(kondoria);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        Menu.menu.update();
        for (Tile tile : tiles) {
            tile.update();
        }
        for (Enemy enemy : enemies) {
            enemy.update();
            enemy.moveEnemy();
            enemy.moveHandler(enemy.getSpeed(), tiles);
        }

        bomberman.update();
        bomberman.moveHandler(bomberman.getSpeed(), tiles);
        bomberman.meetingEnemy(enemies);

        if (bomberman.isDestroyFinished()) {
//            System.out.println("Xóa bomber cũ bằng bomber mới");
            if (bomberman.getX() < Sprite.SCALED_SIZE * mapWidth / 2) {
                bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
            } else {
                bomberman = new Bomber(16, 7, Sprite.player_right.getFxImage());
            }
            bomberLife--;
            Map.Instance.bomber = bomberman;
        }
        items.remove(bomberman.checkUseItem(items));
    }

    public void render() {
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            tile.render(gc);
            if (tile instanceof Brick) {
                Brick brick = (Brick) tile;
                // Phá tường thay bằng cỏ
                if (brick.isDestroyFinished()) {
                    tiles.remove(brick);
                    i--;
                    tiles.add(new Grass(brick.getBoardPos().getY(), brick.getBoardPos().getX()
                            , Sprite.grass.getFxImage()));

                    Item item = brick.revealHiddenItem();
                    if (item != null) {
                        items.add(item);
                    }
                }
            }
            if (tile instanceof Bomb) {
                Bomb bomb = (Bomb) tile;
                if (bomb.isDestroyFinished()) {
                    tiles.remove(bomb);
                    i--;
                }
            }
        }
        items.forEach(g -> g.render(gc));


        if (!bomberman.isDestroyFinished())
            bomberman.render(gc);

        if (enemies.size() == 0) {
            Portal portal = new Portal(29, 15, Sprite.portal.getFxImage());
            tiles.add(portal);
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.render(gc);
//            if(enemy instanceof Kondoria){
//                if(enemy.isDestroy()){
//                    if(((Kondoria) enemy).isClone()) continue;
//                    BoxPos destroyPos = enemy.getBoardPos();
//                    Kondoria cloneKondoria = new Kondoria(destroyPos.y, destroyPos.x, Sprite.kondoria_left1.getFxImage(), true);
//                    enemies.add(cloneKondoria);
//                    Map.Instance.enemies.add(cloneKondoria);
//                }
//            }
            if (enemy.isDestroyFinished()) {
                enemies.remove(enemy);
                i--;
            }
        }
    }

    public void timeCounter() {
        long now = System.currentTimeMillis();
        if (now - previousTime > 1000) {
            previousTime = System.currentTimeMillis();
            remainTime--;
        }
    }
}

