package game;

public class Cursor {
    private int y;
    private int x;

    Cursor() {
        x = y = 0;
    }

    public void move(int dy, int dx) {
        y += dy;
        x += dx;
    }
}
