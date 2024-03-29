package uet.oop.bomberman;

import uet.oop.bomberman.entities.Map;

import java.util.LinkedList;

public class BFS {
    private static class Cell {
        int x;
        int y;
        int dist;    //distance
        Cell prev;  //parent cell in the path

        Cell(int x, int y, int dist, Cell prev) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.prev = prev;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    public static int[][] convertMap(char[][] matrix) {
        int[][] result = new int[Map.Instance.getHeight()][Map.Instance.getWidth()];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == '#' || matrix[i][j] == '*') {
                    result[i][j] = 0;
                } else {
                    result[i][j] = 1;
                }
            }
        }
        return result;
    }

    //BFS, Time O(n^2), Space O(n^2)
    public static int[] shortestPath(char[][] matrixChar, int[] start, int[] end) {
        int[][] matrix = convertMap(matrixChar);
        int sx = start[0], sy = start[1];
        int dx = end[0], dy = end[1];
        //if start or end value is 0, return
        if (matrix[sx][sy] == 0 || matrix[dx][dy] == 0) {
            return start;
        }
        //initialize the cells
        int m = matrix.length;
        int n = matrix[0].length;
        Cell[][] cells = new Cell[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != 0) {
                    cells[i][j] = new Cell(i, j, Integer.MAX_VALUE, null);
                }
            }
        }
        //breadth first search
        LinkedList<Cell> queue = new LinkedList<>();
        Cell src = cells[sx][sy];
        src.dist = 0;
        queue.add(src);
        Cell dest = null;
        Cell p;
        while ((p = queue.poll()) != null) {
            //find destination
            if (p.x == dx && p.y == dy) {
                dest = p;
                break;
            }
            // moving up
            visit(cells, queue, p.x - 1, p.y, p);
            // moving down
            visit(cells, queue, p.x + 1, p.y, p);
            // moving left
            visit(cells, queue, p.x, p.y - 1, p);
            //moving right
            visit(cells, queue, p.x, p.y + 1, p);
        }

        //compose the path if path exists
        if (dest == null) {
            return start;
        } else {
            LinkedList<Cell> path = new LinkedList<>();
            p = dest;
            do {
                path.addFirst(p);
            } while ((p = p.prev) != null);
            if (path.size() > 10) {
                return start;
            }
            if (path.size() > 1) {
                int[] result = new int[]{path.get(1).y, path.get(1).x};
                return result;
            } else if (path.size() == 1) {
                return start;
            }
        }
        return start;
    }

    //function to update cell visiting status, Time O(1), Space O(1)
    private static void visit(Cell[][] cells, LinkedList<Cell> queue, int x, int y, Cell parent) {
        //out of boundary
        if (x < 0 || x >= cells.length || y < 0 || y >= cells[0].length || cells[x][y] == null) {
            return;
        }
        //update distance, and previous node
        int dist = parent.dist + 1;
        Cell p = cells[x][y];
        if (dist < p.dist) {
            p.dist = dist;
            p.prev = parent;
            queue.add(p);
        }
    }
}
