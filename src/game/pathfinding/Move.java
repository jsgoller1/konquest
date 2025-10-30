public class Move {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private int gridWidth;
    private int gridHeight;

    public Move(int startX, int startY int endX, int endY){
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    // Simple method that checks whether we are searching within boundaries
    private boolean isValid(int x, int y) {
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight;
    }



    public List<Pair> breadthFirstSearch() {
        Queue<Pair> queue = new LinkedList<>();
        Map<Pair, Pair> parent = new HashMap<>();
        Set<Pair> visited = new HashSet<>();

        Pair start = new Pair(startY, startX);
        Pair end = new Pair(endY, endX);

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

                if (isValid(newX, newY) && !visited.contains(neighbor)) {
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


    public static void main(String[] args) {
        Move move3 = new Move(0, 0, 2, 3);
        System.out.println("Path from (0,0) to (2,3):");
        for (Position pos : move3.getPath()) {
            System.out.println(pos);
        }
    }
}
