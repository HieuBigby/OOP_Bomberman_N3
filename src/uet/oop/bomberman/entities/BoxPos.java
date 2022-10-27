package uet.oop.bomberman.entities;

import uet.oop.bomberman.entities.mob.AdjacentPos;

public class BoxPos {
    public int x, y;

    public BoxPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean compare(int x, int y) {
        return this.x == x && this.y == y;
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    public String toString() {
        return "BoxPos{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Copy vị trí, có thể lấy điểm lân cận của vị trí cần copy.
     */
    public BoxPos clone(AdjacentPos adjacentPos) {
        int x = this.x;
        int y = this.y;

        switch (adjacentPos) {
            case UP:
                x = clamp(x - 1, 0, Map.Instance.getHeight());
                break;
            case DOWN:
                x = clamp(x + 1, 0, Map.Instance.getHeight());
                break;
            case LEFT:
                y = clamp(y - 1, 0, Map.Instance.getWidth());
                break;
            case RIGHT:
                y = clamp(y + 1, 0, Map.Instance.getWidth());
                break;
        }
        return new BoxPos(x, y);
    }
}
