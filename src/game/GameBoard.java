package game;

import logging.Logger;
import java.util.Random;
import game.terrain.Grass;
import game.terrain.Mountain;
import game.terrain.Terrain;
import input.GameKeyListener;

public class GameBoard {
    private Terrain terrainPieces[][];
    private BoardPiece characterPieces[][];

    private long lastUpdateTimeMs;
    private long delayMs = 100;

    private Cursor cursor;
    private int height;
    private int width;

    // TODO: hate this, need one source of truth on positions
    private int playerY;
    private int playerX;

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

    public void update(GameKeyListener keyListener, long time) {
        long timeDelta = time - this.lastUpdateTimeMs;
        if (timeDelta < delayMs) {
            Logger.debug(String.format("Update is too soon: %d", timeDelta));
            return;
        }

        if (keyListener.upArrowPressed()) {
            Logger.debug("Moving player up.");
            this.movePlayer(-1, 0);
            this.lastUpdateTimeMs = time;
        } else if (keyListener.downArrowPressed()) {
            Logger.debug("Moving player down.");
            this.movePlayer(1, 0);
            this.lastUpdateTimeMs = time;
        } else if (keyListener.leftArrowPressed()) {
            Logger.debug("Moving player left.");
            this.movePlayer(0, -1);
            this.lastUpdateTimeMs = time;
        } else if (keyListener.rightArrowPressed()) {
            Logger.debug("Moving player right.");
            this.movePlayer(0, 1);
            this.lastUpdateTimeMs = time;
        } else if (keyListener.spacePressed()) {
            // TODO: attack
            this.lastUpdateTimeMs = time;
        }
    }

    private boolean movePlayer(int dy, int dx) {
        if (this.moveCharacter(this.playerY, this.playerX, dy, dx)) {
            this.playerX += dx;
            this.playerY += dy;
            return true;
        }
        return false;
    }

    private boolean moveCharacter(int y, int x, int dy, int dx) {
        /*
         * This is a generic method for moving any characters - player, orcs, etc.
         */
        int ny = y + dy;
        int nx = x + dx;
        if (!(this.validCell(y, x) && this.validCell(ny, nx)
                && this.terrainPieces[ny][nx].canBeOccupied() && this.characterPieces[y][x] != null
                && this.characterPieces[ny][nx] == null)) {
            Logger.debug(
                    String.format("Not moving character from (%d, %d) to (%d, %d)", y, x, ny, nx));
            return false;
        }
        BoardPiece piece = characterPieces[y][x];
        characterPieces[y][x] = null;
        characterPieces[y + dy][x + dx] = piece;
        return true;
    }

    private void initializeTerrain() {
        // Randomly add board pieces; this is just a proof of concept to ensure
        /// texture loading works correctly and will be scrapped
        Random rand = new Random();
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int pick = rand.nextInt(100);
                if (pick < 90) {
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
                this.playerY = row;
                this.playerX = col;
                characterPieces[row][col] = new Character("Knight", 10, 10, 10);
                placingPlayer = false;
            }
        }

        // Place enemies
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int pick = rand.nextInt(100);
                if (terrainPieces[y][x].canBeOccupied() && this.characterPieces[y][x] == null
                        && pick < 2) {
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

    public Terrain getTerrainPiece(int y, int x) {
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

    // removes piece // added by Jonathan
    public void removeCharacterPiece(int y, int x) {
        if (!validCell(y, x)) {
            Logger.error(String.format("Cannot remove character piece at (%d, %d)", x, y));
            return;
        }
        this.characterPieces[y][x] = null;
    }

    /**
     * Remove the given piece from the board by reference. Returns true if
     * the piece was found and removed, false otherwise.
     */
    public boolean removeCharacterPiece(BoardPiece piece) {
        if (piece == null) return false;
        for (int y = 0; y < this.getBoardHeight(); y++) {
            for (int x = 0; x < this.getBoardWidth(); x++) {
                if (this.characterPieces[y][x] == piece) {
                    this.characterPieces[y][x] = null;
                    return true;
                }
            }
        }
        return false;
    }

    public int getBoardHeight() {
        return this.height;
    }

    public int getBoardWidth() {
        return this.width;
    }
}
