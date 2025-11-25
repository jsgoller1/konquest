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
        if (currentState != EnemyState.DEAD && currentState != EnemyState.FLEEING
                && owner.getHealth() <= this.FLEE_HEALTH_THRESHOLD) {
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

        Position pos = this.board.getActorPosition(this.owner);
        Logger.debug(String.format("Stepping actor %s from (%d, %d) to (%d, %d)",
                this.owner.getName(), path.getFirst().y, path.getFirst().x, pos.y, pos.x));

        // If the path begins with the current position, just remove it
        if (this.path.getFirst().equals(this.board.getActorPosition(this.owner))) {
            Logger.info("First path step is current position; skipping.");
            this.path.remove(0);
            if (this.path.size() == 0) {
                return;
            }
        }

        this.board.moveActorTo(this.owner, this.path.getFirst());
        this.owner.setMovesRemaining(this.owner.getMovesRemaining() - 1);
        this.path.remove(0);
    }

    private void executeSearching() {
        Logger.debug(String.format("%s: searching.", owner.getName()));
        // If we're searching, we need to pick a target and move that way.
        if (this.path == null || this.path.size() == 0) {
            this.path = new Move(this.board, this.board.getActorPosition(this.owner),
                    this.board.getPlayerZoneTile()).pathfind();
        }
        Logger.info(String.format("Search path for %s:", this.owner.getName()));
        for (Position pos : this.path) {
            Logger.info(String.format("(%d, %d)", pos.y, pos.x));
        }
        this.stepPath();
    }

    private void executeChasing() {
        // move towards target (nearest character)
        this.path = new Move(this.board, this.board.getActorPosition(this.owner),
                this.board.getActorPosition(this.target)).pathfind();
        Logger.info(String.format("Chase path for %s:", this.owner.getName()));
        for (Position pos : this.path) {
            Logger.info(String.format("(%d, %d)", pos.y, pos.x));
        }
        this.stepPath();
    }

    private void executeFighting() {
        if (target != null) {
            int damage = this.owner.getAttack();
            Logger.info(
                    String.format("%s attacks %s!", this.owner.getName(), this.target.getName()));
            this.target.damage(damage);
            this.owner.setHasAttacked(true);
        }
    }

    private void executeFleeing() {
        if (path == null || path.size() == 0) {
            this.path = new Move(this.board, this.board.getActorPosition(this.owner),
                    this.board.getNearestCorner(this.owner)).pathfind();
        }
        Logger.info(String.format("Flee path for %s:", this.owner.getName()));
        for (Position pos : this.path) {
            Logger.info(String.format("(%d, %d)", pos.y, pos.x));
        }
        this.stepPath();
    }

    private void executeDead() {
        // remove by reference — GameBoard will search and clear the piece
        this.board.removeActor(this.owner);
    }

    // getter methods
    public EnemyState getCurrentState() {
        return this.currentState;
    }

    public Player getTarget() {
        return this.target;
    }
}
