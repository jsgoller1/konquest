package game.pathfinding;

import java.util.*;
import game.GameBoard;
import pathfinding.Pair;

public class Move {
    private GameBoard board;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private int gridWidth;
    private int gridHeight;

    public Move(int startX, int startY, int endX, int endY, GameBoard board) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.board = board;
        this.gridWidth = board.getBoardWidth();
        this.gridHeight = board.getBoardHeight();

    }

    // Simple method that checks whether we are searching within boundaries



    public List<Pair> breadthFirstSearch() {
        LinkedList<Pair> queue = new LinkedList<>();
        HashMap<Pair, Pair> parent = new HashMap<>();
        HashSet<Pair> visited = new HashSet<>();

        Pair start = new Pair(this.startY, this.startX);
        Pair end = new Pair(this.endY, this.endX);

        queue.offer(start);
        visited.add(start);
        parent.put(start, null);

        // Four directions: up, down, left, right
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        while (!queue.isEmpty()) {
            Pair current = queue.poll();

            // Found the destination. Calls reconstructPath which puts all pairs into list and
            // reverses them.
            if (current.equals(end)) {
                return reconstructPath(parent, end);
            }

            // adds dx and dy to current pair to find neighbors and checks whether they are valid
            // and !visited
            for (int i = 0; i < 4; i++) {
                int newX = current.getX() + dx[i];
                int newY = current.getY() + dy[i];
                Pair neighbor = new Pair(newY, newX);

                if (this.board.validCell(newX, newY) && !visited.contains(neighbor)) {
                    queue.offer(neighbor);
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }

        // No path found. not in Grid
        return new ArrayList<>();
    }

    // reverses the pairs from end to start so we are left with the correct path: Start -> End
    private List<Pair> reconstructPath(Map<Pair, Pair> parent, Pair end) {
        List<Pair> path = new ArrayList<>();
        Pair current = end;

        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        Collections.reverse(path);
        return path;
    }



}
