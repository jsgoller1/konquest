package game.terrain;

import java.util.ArrayList;
import java.lang.Math;

public class TerrainContainer {
    private ArrayList<Terrain> backgroundPieces;
    private ArrayList<Terrain> foregroundPieces;

    public TerrainContainer() {
        this.backgroundPieces = new ArrayList<Terrain>();
        this.foregroundPieces = new ArrayList<Terrain>();
    }

    public void addBackgroundPiece(Terrain piece) {
        this.backgroundPieces.add(piece);
    }

    public ArrayList<Terrain> getBackgroundPieces() {
        return this.backgroundPieces;
    }

    public void addForegroundPiece(Terrain piece) {
        this.foregroundPieces.add(piece);
    }

    public ArrayList<Terrain> getForegroundPieces() {
        return this.foregroundPieces;
    }

    public boolean canBeOccupied() {
        boolean result = true;
        for (Terrain piece : this.backgroundPieces) {
            result &= piece.canBeOccupied();
        }
        for (Terrain piece : this.foregroundPieces) {
            result &= piece.canBeOccupied();
            return result;
        }
        return result;
    }

    public int getMoveCost() {
        int cost = 0;
        for (Terrain piece : this.backgroundPieces) {
            cost = Math.max(cost, piece.getMoveCost());
        }
        for (Terrain piece : this.foregroundPieces) {
            cost = Math.max(cost, piece.getMoveCost());
        }
        return cost;
    }
}
