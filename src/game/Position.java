package game;

import java.util.Objects;

public class Position {
    public int y;
    public int x;

    public Position(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Position pair = (Position) o;
        return y == pair.y && x == pair.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y, x);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
