package uet.oop.bomberman.entities;

import uet.oop.bomberman.entities.mob.Bomber;
import uet.oop.bomberman.entities.mob.enemy.Balloon;
import uet.oop.bomberman.entities.mob.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Brick;
import uet.oop.bomberman.entities.tile.Tile;

import javax.swing.*;
import java.util.ArrayList;

public class Map {
    public static Map Instance = new Map();
//    public static Map getInstance() {
//        if(Instance == null) {
//            Instance = new Map();
//        }
//
//        return Instance;
//    }
    public char[][] board;
    public ArrayList<Tile> entityList;
    public ArrayList<Enemy> enemies;


    public int getWidth(){
        return board[0].length;
    }

    public int getHeight(){
        return board.length;
    }

    public void printMap(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++)
            {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public int[] getBomberPosition()
    {
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++)
            {
                if(board[i][j] == 'p'){
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0};
    }

    public Entity getEntityAt(int x, int y)
    {
//        System.out.println("Danh sách có " + entityList.size());
        for(int i = 0; i < entityList.size(); i++)
        {
            Entity entity = entityList.get(i);

            if(entity.getBoardPos().compare(x, y))
            {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Entity> getEntitiesAt(int x, int y){
        ArrayList<Entity> samePosEntities = new ArrayList<>();
        for(int i = 0; i < entityList.size(); i++)
        {
            Entity entity = entityList.get(i);
            if(entity.getBoardPos().compare(x, y))
            {
                samePosEntities.add(entity);
            }
        }

        for(int i = 0; i < enemies.size(); i++)
        {
            Entity entity = enemies.get(i);
            if(entity.getBoardPos().compare(x, y))
            {
                samePosEntities.add(entity);
            }
        }

        return samePosEntities;
    }

    public boolean hasBombAt(int x, int y)
    {
        ArrayList<Entity> foundEntities = getEntitiesAt(x, y);
        if(!foundEntities.isEmpty())
        {
            for(Entity entity : foundEntities)
            {
               if(entity instanceof Bomb){
                   return true;
               }
            }
        }
        return false;
    }

    public boolean hasBomb()
    {
        for(Entity entity : entityList)
        {
            if(entity instanceof Bomb){
                return true;
            }
        }
        return false;
    }

    public void removeAt(int x, int y)
    {
//        System.out.println("Xóa vật thể '" + board[x][y] + "' tại vi trí " + x + ", " + y);
//        if(board[x][y] != '*') return;
        ArrayList<Entity> entities = getEntitiesAt(x, y);
        for(Entity entity : entities){
            if(entity instanceof Brick)
            {
                System.out.println("Xóa brick ở vị trí " + x + ", " + y);
                ((Brick)entity).destroy();
            }
            if(entity instanceof Bomber)
            {
                System.out.println("Xóa bomber");
                ((Bomber)entity).destroy();
            }
            if(entity instanceof Enemy)
            {
                System.out.println("Xóa " + entity.toString());
                ((Enemy) entity).destroy();
            }
        }
//        if(entity != null)
//        {
//
//        }
        board[x][y] = ' ';
    }

}
