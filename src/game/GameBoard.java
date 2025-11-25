package game;

import logging.Logger;
import java.io.Console;
import java.util.ArrayList;
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
    private Actor actors[][];
    private Cursor cursor;

    private TurnManager turnManager;

    private long lastUpdateTimeMs;
    private long delayMs = 100;
    private boolean quitNow = false;

    private int height;
    private int width;

    private HashMap<Actor, Position> positionCache;
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
        this.actors = new Actor[height][width];
        this.positionCache = new HashMap<Actor, Position>();
        this.turnManager = new TurnManager();
        this.cursor = new Cursor();

        initializeTerrain(true);
        initializeActors();
        // setupTestActors();

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

    public Cursor getCursor() {
        return this.cursor;
    }

    public Position getCursorPosition() {
        return this.positionCache.get(this.turnManager.getCurrentTurnActor());
    }

    public Position getActorPosition(Actor actor) {
        return this.positionCache.get(actor);
    }

    public boolean moveActorTo(Actor actor, Position newPosition) {
        Position current = this.positionCache.get(actor);
        assert current != null;
        assert this.actors[current.y][current.x] == actor;

        if (!(this.canBeOccupied(newPosition.y, newPosition.x))) {
            Logger.debug(String.format("Not moving actor from (%d, %d) to (%d, %d)", current.y,
                    current.x, newPosition.y, newPosition.x));
            return false;
        }
        this.actors[current.y][current.x] = null;
        this.actors[newPosition.y][newPosition.x] = actor;
        this.positionCache.put(actor, newPosition);
        Logger.info(String.format("Moved %s to (%d, %d)", actor.getName(), newPosition.y,
                newPosition.x));

        return true;
    }

    public boolean moveActor(Actor actor, int dy, int dx) {
        /*
         * This is a generic method for moving any actors - player, orcs, etc.
         */
        if (actor == null) {
            Logger.warn("Attempting to move a null actor; skipping.");
            return false;
        }

        Position position = this.positionCache.get(actor);
        assert position != null;
        assert this.actors[position.y][position.x] == actor;

        Position newPosition = new Position(position.y + dy, position.x + dx);
        return this.moveActorTo(actor, newPosition);
    }

    private void initializeTerrain(boolean spawnMountains) {
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
                } else if (pick > 80 && spawnMountains) {
                    container.addBackgroundPiece(new Mountain());
                }
            }
        }
        Logger.info("Initialized terrain.");
    }

    private void setupTestActors() {
        this.createPlayer(0, 2, 5);
        this.createPlayer(0, 2, 19);
        this.createEnemy(0, 3, 7);
        this.createEnemy(0, 3, 5);
        this.createEnemy(0, 4, 5);
    }

    private boolean createPlayer(int id, int y, int x) {
        if (!this.canBeOccupied(y, x)) {
            return false;
        }
        Player player = new Player(this, this.keyListener);
        player.setName(String.format("Soldier %d", id));
        actors[y][x] = player;
        this.positionCache.put(player, new Position(y, x));
        this.turnManager.register(player);
        ++this.playerCount;
        Logger.info(String.format("Created %s", player.getName()));
        return true;
    }

    private boolean createEnemy(int id, int y, int x) {
        if (!this.canBeOccupied(y, x)) {
            return false;
        }
        Enemy enemy = new Enemy(this);
        enemy.setName(String.format("Orc %d", id));
        actors[y][x] = enemy;
        this.positionCache.put(enemy, new Position(y, x));
        this.turnManager.register(enemy);
        ++this.enemyCount;
        Logger.info(String.format("Created %s", enemy.getName()));
        return true;
    }


    private void initializeActors() {
        Logger.info("Initializing actors.");
        Random rand = new Random();
        int playersRemaining = 4;
        int enemiesRemaining = 4;

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int pick = rand.nextInt(100);
                if (this.canBeOccupied(y, x) && pick < 2) {
                    // Place enemies on the top half of the board
                    if (y > this.height / 2) {
                        if (enemiesRemaining == 0) {
                            continue;
                        }
                        this.createEnemy(this.enemyCount, y, x);
                        enemiesRemaining--;
                    } else {
                        // Place players on the bottom half
                        if (playersRemaining == 0) {
                            continue;
                        }
                        this.createPlayer(this.playerCount, y, x);
                        playersRemaining--;
                    }
                }
            }
        }
        Logger.info("Initialized characters.");
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
                && this.actors[y][x] == null;
    }

    public boolean cellEmpty(int y, int x) {
        return validCell(y, x) && (actors[y][x] == null)
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
        return this.actors[y][x];
    }

    public boolean removeActor(int y, int x) {
        if (this.cellEmpty(y, x)) {
            Logger.error(String.format("Cannot remove actor at (%d, %d)", x, y));
            return false;
        }
        Actor actor = this.actors[y][x];
        this.positionCache.remove(actor);
        this.turnManager.deregister(actor);
        this.actors[y][x] = null;
        if (actor instanceof Player) {
            playerCount--;
        } else {
            enemyCount--;
        }
        return true;
    }

    public boolean removeActor(Actor actor) {
        Position position = this.positionCache.get(actor);
        if (position == null) {
            Logger.info("Cannot remove actor " + actor.getName());
            return false;
        }
        return this.removeActor(position.y, position.x);
    }

    public int getDistance(Position position1, Position position2) {
        int xDistance = (position1.x - position2.x);
        int yDistance = (position1.y - position2.y);
        return (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }

    public int getDistance(Actor actor1, Actor actor2) {
        Position position1 = this.positionCache.get(actor1);
        Position position2 = this.positionCache.get(actor2);
        return getDistance(position1, position2);
    }

    public boolean atEdge(Actor actor) {
        // Returns true if an actor is at the edge of the board; useful for fleeing behavior.
        Position position = this.positionCache.get(actor);
        return position.x == 0 || position.y == 0 || position.x == this.width
                || position.y == this.height;
    }

    public Player getNearestPlayer(Enemy source, int distanceLimit) {
        // Get closest player to an enemy based on Euclidean distance, within distance limit.
        // If distanceLimit == -1, will be ignored.

        // TODO: this can and probably should be made generic, but
        // for now we only need it for finding players
        double best = Integer.MAX_VALUE;
        Player bestPlayer = null;
        for (Actor actor : this.positionCache.keySet()) {
            if (actor instanceof Player) {
                int distance = this.getDistance(source, actor);
                if (distance <= best) {
                    bestPlayer = (Player) actor;
                    best = distance;
                }
            }
        }
        if (distanceLimit == -1 || best <= distanceLimit) {
            return bestPlayer;
        }
        return null;
    }

    public ArrayList<Position> getSurroundingSpaces(Actor actor) {
        if (actor == null || this.positionCache.get(actor) == null) {
            Logger.warn("Cannot get surrounding tiles for actor.");
            return null;
        }
        Position pos = this.positionCache.get(actor);
        ArrayList surrounding = new ArrayList();
        for (int dy : new int[] {-1, 0, 1}) {
            for (int dx : new int[] {-1, 0, 1}) {
                if (this.validCell(pos.y + dy, pos.x + dx)) {
                    surrounding.add(new Position(pos.y + dy, pos.x + dx));
                }
            }
        }
        return surrounding;
    }

    public Position getNearestCorner(Actor actor) {
        Position position = this.positionCache.get(actor);
        Position[] corners = new Position[] {new Position(0, 0), new Position(0, this.width - 1),
                new Position(this.width - 1, 0), new Position(this.height - 1, this.width - 1)};
        Position best = null;
        int bestDistance = Integer.MAX_VALUE;
        for (Position corner : corners) {
            if (this.getDistance(position, corner) <= bestDistance) {
                bestDistance = this.getDistance(position, corner);
                best = corner;
            }
        }
        return best;
    }

    public Position getPlayerZoneTile() {
        boolean searching = true;
        Random rand = new Random();
        int y = 0, x = 0;
        while (searching) {
            x = rand.nextInt(this.getBoardWidth());
            y = rand.nextInt(this.getBoardHeight() / 2);
            if (this.canBeOccupied(y, x)) {
                searching = false;
            }
        }
        return new Position(y, x);
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                Actor actor = this.getActor(y, x);
                if (actor != null && actor instanceof Player) {
                    players.add((Player) actor);
                }
            }
        }
        return players;
    }

    public ArrayList<Enemy> getEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                Actor actor = this.getActor(y, x);
                if (actor != null && actor instanceof Enemy) {
                    enemies.add((Enemy) actor);
                }
            }
        }
        return enemies;

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
