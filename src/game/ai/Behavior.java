package game.ai;

import java.util.ArrayList;
import game.GameBoard;
import game.Position;
import game.actor.Enemy;
import game.actor.Player;
import game.pathfinding.Move;
import logging.Logger;
import game.actor.Actor;
import java.util.Random;

public class Behavior {
    private static final int CHASE_RANGE = 5;
    private static final int FLEE_HEALTH_THRESHOLD = 3;

    private GameBoard board;
    private Enemy owner;
    private EnemyState currentState;
    private Player target;
    private ArrayList<Position> path;

    public Behavior(GameBoard board, Enemy owner) {
        this.board = board;
        this.owner = owner;
        this.currentState = EnemyState.SEARCHING;
        this.target = null;
    }

    public void updateState() {
        // First, do any transitions that apply
        // regardless of what state an enemy is in
        if (this.universalStateChanges()) {
            return;
        }

        // If no universal transitions apply, then
        // try conditional transitions.
        switch (this.currentState) {
            case EnemyState.SEARCHING:
                transitionSearching();
                break;
            case EnemyState.CHASING:
                transitionChasing();
                break;
            case EnemyState.FIGHTING:
                transitionFighting();
                break;
            case EnemyState.FLEEING:
                transitionFleeing();
                break;
            case EnemyState.DEAD:
                // Dead has no transitions; a dead enemy stays dead.
                break;
        }
    }

    private boolean universalStateChanges() {
        // These are conditions that trigger transitions regardless of what state an enemy was in

        // Any enemy with 0 HP is automatically and always dead.
        if (currentState != EnemyState.DEAD && owner.getHealth() <= 0) {
            Logger.debug(String.format("%s: set state to DEAD", owner.getName()));
            currentState = EnemyState.DEAD;
            return true;
        }

        // Any live enemy who drops below fleeing threshold immediately flees
        if (currentState != EnemyState.DEAD && owner.getHealth() <= this.FLEE_HEALTH_THRESHOLD) {
            Logger.debug(String.format("%s: set state to FLEEING", owner.getName()));
            currentState = EnemyState.FLEEING;
            return true;
        }
        return false;
    }

    private void transitionSearching() {
        // If we are searching and there's an enemy in range, begin chasing.
        this.target = board.getNearestPlayer(owner, this.CHASE_RANGE);
        if (this.target != null) {
            this.currentState = EnemyState.CHASING;
            Logger.debug(String.format("%s: within range of %s (%d)", owner.getName(),
                    target.getName(), this.board.getDistance(target, owner)));
            Logger.debug(String.format("%s: set state SEARCHING->CHASING", owner.getName()));

        }
    }

    private void transitionChasing() {
        // Always chase the nearest target; if they go out of range
        // go back to searching. If a new target is closer, go for them instead.
        this.target = board.getNearestPlayer(owner, this.CHASE_RANGE);
        if (this.target == null) {
            this.currentState = EnemyState.SEARCHING;
            Logger.debug(String.format("%s: set state CHASING->SEARCHING", owner.getName()));

        } else if (this.board.getDistance(target, this.owner) == 1) {
            // If the nearest target is in attacking distance, attack them.
            this.currentState = EnemyState.FIGHTING;
            Logger.debug(String.format("%s: set state CHASING->FIGHTING", owner.getName()));
        }
    }

    private void transitionFighting() {
        // Always attack the nearest player. If they are too far,
        // begin chasing. If they disappear, begin searching.
        this.target = board.getNearestPlayer(owner, this.CHASE_RANGE);
        if (this.target == null) {
            this.currentState = EnemyState.SEARCHING;
            Logger.debug(String.format("%s: set state FIGHTING->SEARCHING", owner.getName()));

        } else if (this.board.getDistance(target, this.owner) > 1) {
            this.currentState = EnemyState.CHASING;
            Logger.debug(String.format("%s: set state FIGHTING->CHASING", owner.getName()));
        }
    }

    private void transitionFleeing() {
        // When fleeing, if an enemy reaches the edge of the board,
        // they "escape" by dying.
        if (this.board.atEdge(this.owner)) {
            this.currentState = EnemyState.DEAD;
            Logger.debug(String.format("%s: set state FLEEING->DEAD (escaped)", owner.getName()));
        }
    }

    public void executeStateBehavior() {
        switch (currentState) {
            case EnemyState.SEARCHING:
                executeSearching();
                break;
            case EnemyState.CHASING:
                executeChasing();
                break;
            case EnemyState.FIGHTING:
                executeFighting();
                break;
            case EnemyState.FLEEING:
                executeFleeing();
                break;
            case EnemyState.DEAD:
                executeDead();
                break;
        }
    }

    private void stepPath() {
        if (this.path == null || this.path.size() == 0) {
            return;
        }

        this.board.moveActorTo(this.owner, this.path.getFirst());
        this.owner.setMovesRemaining(this.owner.getMovesRemaining() - 1);
        this.path.remove(0);
    }

    private void executeSearching() {
        Logger.debug(String.format("%s: searching.", owner.getName()));
        // If we're searching, we need to pick a target and move that way.
        // this.board.getPlayerZoneTile();
        if (this.path == null) {
            this.path = new Move(this.board, this.board.getActorPosition(this.owner),
                    new Position(0, 0)).pathfind();
        }
        this.stepPath();
    }

    private void executeChasing() {
        // move towards target (nearest character)
        this.path = new Move(this.board, this.board.getActorPosition(this.owner),
                this.board.getActorPosition(this.target)).pathfind();
        for (Position pos : this.path) {
            Logger.info(String.format("(%d, %d)", pos.y, pos.x));
        }
        this.stepPath();
    }

    private void executeFighting() {
        if (target != null) {
            int damage = owner.getAttack();
            target.damage(damage);
        }
    }

    private void executeFleeing() {
        // move towards end of screen
        // depending on position, move to closest edge
        // 4 switch cases for cardinal directions?
    }

    // find owner on the provided map and remove it so it is no longer drawn // prob doesn't need to
    // be so long, but I don't know how to make the fail case cleaner
    private void executeDead() {
        // remove by reference — GameBoard will search and clear the piece
        // board.removeActor(owner);
    }

    // Find any adjacent player characters on map // May not be needed as Grant is doing
    // pathfinding?
    private Player findAdjacentPlayer() {
        // int ownerY = -1;
        // int ownerX = -1;
        // for (int y = 0; y < board.getBoardHeight(); y++) {
        // for (int x = 0; x < board.getBoardWidth(); x++) {
        // if (board.getActor(y, x) == owner) {
        // ownerY = y;
        // ownerX = x;
        // break;
        // }
        // }
        // if (ownerY != -1)
        // break;
        // }
        // if (ownerY == -1)
        // return null; // owner not placed on map

        // // Check 4 neighbors: up, down, left, right
        // int[][] neigh = {{ownerY - 1, ownerX}, {ownerY + 1, ownerX}, {ownerY, ownerX - 1},
        // {ownerY, ownerX + 1}};
        // for (int[] n : neigh) {
        // int ny = n[0], nx = n[1];
        // if (!board.validCell(ny, nx))
        // continue;
        // Actor p = board.getActor(ny, nx);
        // if (p instanceof Player) {
        // return (Player) p;
        // }
        // }
        return null;
    }

    // Find any player character within given range on map // // May not be needed as Grant is doing
    // pathfinding?
    private Player findPlayerInRange(int range) {
        // int ownerY = -1;
        // int ownerX = -1;
        // for (int y = 0; y < board.getBoardHeight(); y++) {
        // for (int x = 0; x < board.getBoardWidth(); x++) {
        // if (board.getActor(y, x) == owner) {
        // ownerY = y;
        // ownerX = x;
        // break;
        // }
        // }
        // if (ownerY != -1)
        // break;
        // }

        // if (ownerY == -1)
        // return null;

        // for (int y = 0; y < board.getBoardHeight(); y++) {
        // for (int x = 0; x < board.getBoardWidth(); x++) {
        // Actor p = board.getActor(y, x);
        // if (p instanceof Player) {
        // int dist = Math.abs(ownerY - y) + Math.abs(ownerX - x);
        // if (dist <= range)
        // return (Player) p;
        // }
        // }
        // }

        return null;
    }

    // getter methods
    public EnemyState getCurrentState() {
        return this.currentState;
    }

    public Player getTarget() {
        return this.target;
    }
}
