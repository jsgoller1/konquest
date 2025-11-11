package game;

import logging.Logger;
import java.util.Random;
import game.enemy.Enemy;
import game.terrain.Grass;
import game.terrain.Mountain;
import game.terrain.Terrain;


public class GameBoard {
    private Terrain terrainPieces[][];
    private BoardPiece characterPieces[][];

    private Cursor cursor;
    private int height;
    private int width;

    public GameBoard(int height, int width) {
        if (height < 5 || width < 5) {
            Logger.error("Board must be at least 5x5; cannot create smaller board.");
            return;
        }
        cursor = new Cursor();

        this.height = height;
        this.width = width;
        terrainPieces = new Terrain[height][width];
        characterPieces = new BoardPiece[height][width];
        initializeTerrain();
        initializeCharacters();
    }

    public void update() {
        for 

    }

    private void initializeTerrain() {
        // Randomly add board pieces; this is just a proof of concept to ensure
        /// texture loading works correctly and will be scrapped
        Random rand = new Random();
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int pick = rand.nextInt(100);
                if (pick < 85) {
                    terrainPieces[y][x] = new Grass();
                } else {
                    terrainPieces[y][x] = new Mountain();
                }
            }
        }
    }

    private void initializeCharacters() {
        // Randomly add board pieces; this is just a proof of concept to ensure
        /// texture loading works correctly and will be scrapped
        Random rand = new Random();

        // Place player
        boolean placingPlayer = true;
        while (placingPlayer) {
            int row = rand.nextInt(this.height);
            int col = rand.nextInt(this.width);
            Terrain terrain = terrainPieces[row][col];
            if (terrain.canBeOccupied()) {
                characterPieces[row][col] = new Character("Knight", 10, 10, 10);
                placingPlayer = false;
            }
        }

        // Place enemies
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int pick = rand.nextInt(100);
                if (terrainPieces[y][x].canBeOccupied() && pick < 8) {
                    characterPieces[y][x] = new Enemy("Enemy", 10, 10, 10);
                }
            }
        }
    }

    public boolean validCell(int y, int x) {
        return (0 <= y && y < this.getBoardHeight() && 0 <= x && x < this.getBoardWidth());
    }

    public boolean cellEmpty(int y, int x) {
        return validCell(y, x) && (characterPieces[y][x] == null)
                && (terrainPieces[y][x].canBeOccupied());
    }

    public BoardPiece getTerrainPiece(int y, int x) {
        if (!validCell(y, x)) {
            Logger.error(String.format("Cannot get character at (%d, %d)", x, y));
            return null;
        }
        return this.terrainPieces[y][x];
    }

    public BoardPiece getCharacterPiece(int y, int x) {
        if (!validCell(y, x)) {
            Logger.error(String.format("Cannot get character piece at (%d, %d)", x, y));
            return null;
        }
        return this.characterPieces[y][x];
    }

    public int getBoardHeight() {
        return this.height;
    }

    public int getBoardWidth() {
        return this.width;
    }
}
