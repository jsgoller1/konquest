package behavior;

import java.util.List;
import game.Enemy;
import game.GameBoard;
import game.Character;
import game.BoardPiece;
import java.util.Random;

public class Behavior {
    private static final int chaseRange = 5; // Might delete? Each unit might have its own range
    private static final int fleeHealthThreshold = 3; // subject to change

    private EnemyState currentState;
    private Character target;
    private Enemy owner;

    public Behavior(Enemy owner) {
        this.owner = owner;
        this.currentState = EnemyState.SEARCHING;
    }

    public void updateBehavior(GameBoard board, List<Enemy> enemyUnits) {
        checkStateTransitions(board, enemyUnits);
        switch (currentState) {
            case EnemyState.SEARCHING:
                //executeSearching(board);
                break;
            case EnemyState.CHASING:
                // executeChasing(board);
                break;
            case EnemyState.FIGHTING:
                // executeFighting();
                break;
            case EnemyState.FLEEING:
                // executeFleeing(board);
                break;
            case EnemyState.DEAD:
                 executeDead(board);
                break;
        }
    }

    private void checkStateTransitions(GameBoard board, List<Enemy> enemyUnits) {
        // dead
        if (owner.getHealth() <= 0) {
            currentState = EnemyState.DEAD;
            return;
        } 
        // fleeing
        if (owner.getHealth() <= fleeHealthThreshold && currentState != EnemyState.FLEEING) {
            currentState = EnemyState.FLEEING;
            target = null;
            return;
        }

        // if dead or fleeing, no other transitiions, otherwise...
        if (currentState == EnemyState.DEAD || currentState == EnemyState.FLEEING) {
            return;
        }

        // fighting: look for adjacent player characters on the provided map
        Character adjacentPlayer = findAdjacentPlayer(board);
        if (adjacentPlayer != null) {
            currentState = EnemyState.FIGHTING;
            target = adjacentPlayer;
            return;
        }

        // chasing: look for any player within chaseRange on the map
        Character nearbyPlayer = findPlayerInRange(board, chaseRange);
        if (nearbyPlayer != null) {
            currentState = EnemyState.CHASING;
            target = nearbyPlayer;
            return;
        }

        // searching: look for any players. Otherwise walk randomly; default
        if (currentState != EnemyState.SEARCHING) { 
            currentState = EnemyState.SEARCHING;
            target = null;
        }
    }

    //private void executeSearching(GameBoard board) {}

    //private void executeChasing(GameBoard board) {}

    private void executeFighting() {
        if (target != null) {
            int damage = owner.getAttack();
            target.damage(damage);
        }
    }

    //private void executeFleeing(GameBoard board) {}

    // find owner on the provided map and remove it so it is no longer drawn
    private void executeDead(GameBoard board) {
        int ownerY = -1;
        int ownerX = -1;
        for (int y = 0; y < board.getBoardHeight(); y++) {
            for (int x = 0; x < board.getBoardWidth(); x++) {
                if (board.getCharacterPiece(y, x) == owner) {
                    ownerY = y;
                    ownerX = x;
                    break;
                }
            }
            if (ownerY != -1) break;
        }
        if (ownerY == -1) return; // owner not on map

        board.removeCharacterPiece(ownerY, ownerX);
    }

    // Find any adjacent player characters on map // May not be needed as Grant is doing pathfinding?
    private Character findAdjacentPlayer(GameBoard board) {
        int ownerY = -1;
        int ownerX = -1;
        for (int y = 0; y < board.getBoardHeight(); y++) {
            for (int x = 0; x < board.getBoardWidth(); x++) {
                if (board.getCharacterPiece(y, x) == owner) {
                    ownerY = y;
                    ownerX = x;
                    break;
                }
            }
            if (ownerY != -1) break;
        }
        if (ownerY == -1) return null; // owner not placed on map

        // Check 4 neighbors: up, down, left, right
        int[][] neigh = { {ownerY-1, ownerX}, {ownerY+1, ownerX}, {ownerY, ownerX-1}, {ownerY, ownerX+1} };
        for (int[] n : neigh) {
            int ny = n[0], nx = n[1];
            if (!board.validCell(ny, nx)) continue;
            BoardPiece p = board.getCharacterPiece(ny, nx);
            if (p instanceof Character) {
                return (Character) p;
            }
        }
        return null;
    }

    // Find any player character within given range on map // // May not be needed as Grant is doing pathfinding?
    private Character findPlayerInRange(GameBoard board, int range) {
        int ownerY = -1;
        int ownerX = -1;
        for (int y = 0; y < board.getBoardHeight(); y++) {
            for (int x = 0; x < board.getBoardWidth(); x++) {
                if (board.getCharacterPiece(y, x) == owner) {
                    ownerY = y;
                    ownerX = x;
                    break;
                }
            }
            if (ownerY != -1) break;
        }

        if (ownerY == -1) return null;

        for (int y = 0; y < board.getBoardHeight(); y++) {
            for (int x = 0; x < board.getBoardWidth(); x++) {
                BoardPiece p = board.getCharacterPiece(y, x);
                if (p instanceof Character) {
                    int dist = Math.abs(ownerY - y) + Math.abs(ownerX - x);
                    if (dist <= range) return (Character) p;
                }
            }
        }

        return null;
    }

    // getter methods
    public EnemyState getCurrentState() {
        return this.currentState;
    }

    public Character getTarget() {
        return this.target;
    }
}