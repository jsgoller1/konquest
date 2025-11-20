package game.pathfinding;

import java.util.*;
import game.GameBoard;
import game.terrain.Terrain;

public class Move {
    private GameBoard board;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Move(int startX, int startY, int endX, int endY, GameBoard board) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.board = board;
    }

    public List<Pair> dijkstraSearch() {
        PriorityQueue<NodeWithCost> queue =
                new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        HashMap<Pair, Pair> parent = new HashMap<>();
        HashMap<Pair, Integer> distances = new HashMap<>();

        Pair start = new Pair(this.startY, this.startX);
        Pair end = new Pair(this.endY, this.endX);

        distances.put(start, 0);
        queue.offer(new NodeWithCost(start, 0));
        parent.put(start, null);

        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        while (!queue.isEmpty()) {
            NodeWithCost current = queue.poll();
            Pair currentPair = current.node;

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
                Pair neighbor = new Pair(newY, newX);

                if (this.board.validCell(newX, newY)) {
                    Terrain terrain = this.board.getTerrain(newX, newY);
                    if (terrain.canBeOccupied()) {
                        int moveCost = terrain.getMoveCost();
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
        Pair node;
        int cost;

        NodeWithCost(Pair node, int cost) {
            this.node = node;
            this.cost = cost;
        }
    }

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
