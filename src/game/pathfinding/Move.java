package game.pathfinding;

import java.util.*;
import game.Position;
import game.GameBoard;
import game.Position;
import game.terrain.TerrainContainer;
import game.terrain.Terrain;

public class Move {
    private GameBoard board;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Move(GameBoard board, Position start, Position end) {
        this(board, start.x, start.y, end.x, end.y);
    }

    public Move(GameBoard board, int startX, int startY, int endX, int endY) {
        this.board = board;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public ArrayList<Position> pathfind() {
        ArrayList<Position> path = this.dijkstraSearch();
        return path;
    }

    private ArrayList<Position> dijkstraSearch() {
        PriorityQueue<NodeWithCost> queue =
                new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        HashMap<Position, Position> parent = new HashMap<>();
        HashMap<Position, Integer> distances = new HashMap<>();

        Position start = new Position(this.startY, this.startX);
        Position end = new Position(this.endY, this.endX);

        distances.put(start, 0);
        queue.offer(new NodeWithCost(start, 0));
        parent.put(start, null);

        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        while (!queue.isEmpty()) {
            NodeWithCost current = queue.poll();
            Position currentPair = current.node;

            if (currentPair.equals(end)) {
                return reconstructPath(parent, end);
            }

            Integer currentDistance = distances.get(currentPair);
            if (currentDistance != null && current.cost > currentDistance) {
                continue;
            }

            for (int i = 0; i < 4; i++) {
                int newX = currentPair.getX() + dx[i];
                int newY = currentPair.getY() + dy[i];
                Position neighbor = new Position(newY, newX);

                if (this.board.validCell(newX, newY)) {
                    TerrainContainer terrainContainer = this.board.getTerrainContainer(newX, newY);
                    if (terrainContainer.canBeOccupied()) {
                        int moveCost = terrainContainer.getMoveCost();
                        int newCost = current.cost + moveCost;

                        Integer neighborDistance = distances.get(neighbor);
                        if (neighborDistance == null || newCost < neighborDistance) {
                            distances.put(neighbor, newCost);
                            parent.put(neighbor, currentPair);
                            queue.offer(new NodeWithCost(neighbor, newCost));
                        }
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private class NodeWithCost {
        Position node;
        int cost;

        NodeWithCost(Position node, int cost) {
            this.node = node;
            this.cost = cost;
        }
    }

    private ArrayList<Position> reconstructPath(Map<Position, Position> parent, Position end) {
        ArrayList<Position> path = new ArrayList<>();
        Position current = end;

        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}
