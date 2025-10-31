package game.terrain;

import game.BoardPiece;

public abstract class Terrain extends BoardPiece {
    private int moveCost;

    public Terrain(int moveCost, String spriteSheetPath) {
        super(spriteSheetPath);
        this.moveCost = moveCost;
    }

    // How much does it cost to move through this tile?
    public int getMoveCost() {
        return this.moveCost;
    }

    // Can a character occupy a tile with this type of terrain?
    public abstract boolean canBeOccupied();
}
