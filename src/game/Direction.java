package game;

public enum Direction {
    /*
     * Credit: Claude
     */
    NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> NORTH;
        };
    }
}
