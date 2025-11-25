package game;

import logging.Logger;
import java.io.Console;
import java.util.HashMap;
import java.util.Random;
import game.actor.Actor;
import game.actor.Enemy;
import game.actor.Player;
import game.ai.TurnManager;
import game.terrain.Grass;
import game.terrain.Mountain;
import game.terrain.TallGrass;
import game.terrain.Terrain;
import game.terrain.TerrainContainer;
import input.GameKeyListener;


public class GameBoard {
    private GameKeyListener keyListener;
    private TerrainContainer terrainContainers[][];
    private Actor actorPieces[][];

    private TurnManager turnManager;

    private long lastUpdateTimeMs;
    private long delayMs = 100;
    private boolean quitNow = false;

    private int height;
    private int width;

    private HashMap<Actor, Position> positionCache;
    private Actor player;
    private int playerCount;
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
        this.actorPieces = new Actor[height][width];
        this.positionCache = new HashMap<Actor, Position>();
        this.turnManager = new TurnManager();
        initializeTerrain();
        initializeActors();
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
        if (this.moveActor(this.player, dy, dx)) {
            return true;
        }
        return false;
    }

    public Position getActorPosition(Actor actor) {
        return this.positionCache.get(actor);
    }

    public boolean moveActor(Actor actor, int dy, int dx) {
        /*
         * This is a generic method for moving any actors - player, orcs, etc.
         */
        Position position = this.positionCache.get(actor);
        assert this.actorPieces[position.y][position.x] == actor;

        int ny = position.y + dy;
        int nx = position.x + dx;
        if (!(this.canBeOccupied(ny, nx))) {
            Logger.debug(String.format("Not moving actor from (%d, %d) to (%d, %d)", position.y,
                    position.x, ny, nx));
            return false;
        }
        this.actorPieces[position.y][position.x] = null;
        this.actorPieces[ny][nx] = actor;
        this.positionCache.put(actor, new Position(ny, nx));
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

    private void initializeActors() {
        // Randomly add board pieces; this is just a proof of concept to ensure
        /// texture loading works correctly and will be scrapped
        Logger.info("Initializing actors...");

        Random rand = new Random();

        // Place player
        boolean placingPlayer = true;
        while (placingPlayer) {
            int y = rand.nextInt(this.height);
            int x = rand.nextInt(this.width);
            if (this.canBeOccupied(y, x)) {
                this.player = new Player(this, 10, 10, 10, this.keyListener);
                this.actorPieces[y][x] = player;
                this.positionCache.put(player, new Position(y, x));
                this.turnManager.register(player);
                placingPlayer = false;
            }
        }
        this.playerCount++;

        // Place enemies
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int pick = rand.nextInt(100);
                if (this.canBeOccupied(y, x) && pick < 2) {
                    Enemy enemy = new Enemy(this, 10, 10, 10);
                    enemy.setName(String.format("%s-%d", enemy.getName(), ++this.enemyCount));
                    this.actorPieces[y][x] = enemy;
                    this.positionCache.put(enemy, new Position(y, x));
                    this.turnManager.register(enemy);
                }
            }
        }
        Logger.info("Initialized actors.");
    }

    public boolean validCell(int y, int x) {
        // Simple check against whether the y and x value even exist on the board; do not use
        // for putting board pieces on the board
        return (0 <= y && y < this.getBoardHeight() && 0 <= x && x < this.getBoardWidth());
    }

    public boolean canBeOccupied(int y, int x) {
        /*
         * Checks if a actor can occupy a cell. Reasons it can't: Cell is invalid / outside board,
         * cell contains terrain that can't be occupied (mountain), another actor is already in the
         * cell
         */
        return this.validCell(y, x) && this.terrainContainers[y][x].canBeOccupied()
                && this.actorPieces[y][x] == null;
    }

    public boolean cellEmpty(int y, int x) {
        return validCell(y, x) && (actorPieces[y][x] == null)
                && (this.terrainContainers[y][x].canBeOccupied());
    }

    public TerrainContainer getTerrainContainer(int y, int x) {
        if (!validCell(y, x)) {
            Logger.error(String.format("Cannot get actor at (%d, %d)", x, y));
            return null;
        }
        return this.terrainContainers[y][x];
    }

    public Actor getActor(int y, int x) {
        if (!validCell(y, x)) {
            Logger.error(String.format("Cannot get actor at (%d, %d)", x, y));
            return null;
        }
        return this.actorPieces[y][x];
    }

    public boolean removeActor(int y, int x) {
        if (!this.cellEmpty(y, x)) {
            Logger.error(String.format("Cannot remove actor at (%d, %d)", x, y));
            return false;
        }
        Actor actor = this.actorPieces[y][x];
        this.positionCache.remove(actor);
        this.actorPieces[y][x] = null;
        return true;
    }

    public boolean removeActor(Actor actor) {
        Position position = this.positionCache.get(actor);
        if (position == null) {
            return false;
        }
        return this.removeActor(position.y, position.x);
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
