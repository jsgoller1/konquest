package game;

import logging.Logger;
import java.io.Console;
import java.util.Random;
import game.ai.TurnManager;
import game.character.Enemy;
import game.character.Player;
import game.terrain.Grass;
import game.terrain.Mountain;
import game.terrain.TallGrass;
import game.terrain.Terrain;
import game.terrain.TerrainContainer;
import input.GameKeyListener;


public class GameBoard {
    private GameKeyListener keyListener;
    private TerrainContainer terrainContainers[][];
    private BoardPiece characterPieces[][];
    private TurnManager turnManager;

    private long lastUpdateTimeMs;
    private long delayMs = 100;
    private boolean quitNow = false;

    private int height;
    private int width;

    // TODO: hate this, need one source of truth on positions
    private Player player;
    private int playerCount;
    private int playerY;
    private int playerX;
    private int enemyCount;

    public GameBoard(int height, int width, GameKeyListener keyListener) {
        Logger.info("Creating board...");

        if (height < 5 || width < 5) {
            Logger.error("Board must be at least 5x5; cannot create smaller board.");
            return;
        }

        this.height = height;
        this.width = width;
        this.keyListener = keyListener;
        this.terrainContainers = new TerrainContainer[height][width];
        this.characterPieces = new BoardPiece[height][width];
        this.turnManager = new TurnManager();
        initializeTerrain();
        initializeCharacters();
        Logger.info("Board created.");
    }

    public void update(long time) {
        long timeDelta = time - this.lastUpdateTimeMs;
        if (timeDelta < delayMs) {
            // Logger.debug(String.format("Update is too soon: %d", timeDelta));
            return;
        }
        if (this.keyListener.escapePressed()) {
            Logger.debug("Escape pressed - quitting!");
            this.quitNow = true;
        }

        this.lastUpdateTimeMs = time;
        this.turnManager.takeTurn();
    }

    public boolean movePlayer(int dy, int dx) {
        if (this.moveCharacter(this.playerY, this.playerX, dy, dx)) {
            this.playerX += dx;
            this.playerY += dy;
            return true;
        }
        return false;
    }

    public boolean moveCharacter(int y, int x, int dy, int dx) {
        /*
         * This is a generic method for moving any characters - player, orcs, etc.
         */
        int ny = y + dy;
        int nx = x + dx;
        if (!(this.validCell(y, x) && this.validCell(ny, nx)
                && this.terrainContainers[ny][nx].canBeOccupied()
                && this.characterPieces[y][x] != null && this.characterPieces[ny][nx] == null)) {
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
        Logger.info("Initializing terrain...");
        Random rand = new Random();
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.terrainContainers[y][x] = new TerrainContainer();
                TerrainContainer container = this.terrainContainers[y][x];
                container.addBackgroundPiece(new Grass());

                int pick = rand.nextInt(100);
                if (pick > 90) {
                    container.addForegroundPiece(new TallGrass());
                } else if (pick > 80) {
                    container.addBackgroundPiece(new Mountain());
                }
            }
        }
        Logger.info("Initialized terrain.");
    }

    private void initializeCharacters() {
        // Randomly add board pieces; this is just a proof of concept to ensure
        /// texture loading works correctly and will be scrapped
        Logger.info("Initializing characters...");

        Random rand = new Random();

        // Place player
        boolean placingPlayer = true;
        while (placingPlayer) {
            int row = rand.nextInt(this.height);
            int col = rand.nextInt(this.width);
            TerrainContainer terrainContainer = this.terrainContainers[row][col];
            if (terrainContainer.canBeOccupied()) {
                this.playerY = row;
                this.playerX = col;
                this.player = new Player(this, 10, 10, 10, this.keyListener);
                characterPieces[row][col] = player;
                this.turnManager.register(player);
                placingPlayer = false;
            }
        }
        this.playerCount++;

        // Place enemies
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int pick = rand.nextInt(100);
                if (this.terrainContainers[y][x].canBeOccupied()
                        && this.characterPieces[y][x] == null && pick < 2) {
                    Enemy enemy = new Enemy(this, 10, 10, 10);
                    enemy.setName(String.format("%s-%d", enemy.getName(), ++this.enemyCount));
                    characterPieces[y][x] = enemy;
                    this.turnManager.register(enemy);

                }
            }
        }
        Logger.info("Initialized characters.");
    }

    public boolean validCell(int y, int x) {
        return (0 <= y && y < this.getBoardHeight() && 0 <= x && x < this.getBoardWidth());
    }

    public boolean cellEmpty(int y, int x) {
        return validCell(y, x) && (characterPieces[y][x] == null)
                && (this.terrainContainers[y][x].canBeOccupied());
    }

    public TerrainContainer getTerrainContainer(int y, int x) {
        if (!validCell(y, x)) {
            Logger.error(String.format("Cannot get character at (%d, %d)", x, y));
            return null;
        }
        return this.terrainContainers[y][x];
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
     * Remove the given piece from the board by reference. Returns true if the piece was found and
     * removed, false otherwise.
     */
    public boolean removeCharacterPiece(BoardPiece piece) {
        if (piece == null)
            return false;
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

    public boolean isGameComplete() {
        return this.quitNow || this.playerCount == 0 || this.enemyCount == 0;
    }
}
