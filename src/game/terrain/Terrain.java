package game.terrain;

import game.BoardPiece;

public abstract class Terrain extends BoardPiece {
    private int moveCost;

    public Terrain(int moveCost, String spriteSheetPath) {
        super(spriteSheetPath);
        this.moveCost = moveCost;
    }

    public int getMoveCost() {
        return this.moveCost;
    }
}
