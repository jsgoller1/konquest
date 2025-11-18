package behavior;

import java.util.List;
import game.Enemy;
import game.GameBoard;

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

    public void updateBehavior(GameBoard map, List<Enemy> enemyUnits) {
        checkStateTransitions(map, enemyUnits);
        switch (currentState) {
            case EnemyState.SEARCHING:
                // executeSearching(map);
                break;
            case EnemyState.CHASING:
                // executeChasing(map);
                break;
            case EnemyState.FIGHTING:
                // executeFighting();
                break;
            case EnemyState.FLEEING:
                // executeFleeing(map);
                break;
            case EnemyState.DEAD:
                // executeDead();
                break;
        }
    }

    private void checkStateTransitions(GameBoard map, List<Enemy> enemyUnits) {
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

        // fighting
        Character adjacentPlayer = findAdjacentPlayer(playerUnits);         // Will i need to pass playerUnits as a parameter too?
        if(adjacentPlayer != null) {
            currentState = EnemyState.FIGHTING;
            target = adjacentPlayer;
            return;
        }

        // chasing
        Character nearbyPlayer = findPlayerInRange(playerUnits, chaseRange); // same question as above
        if (nearbyPlayer != null) {
            currentState = EnemyState.CHASING;
            target = nearbyPlayer;
            return;

        }

        // searching; default
        if (currentState != EnemyState.SEARCHING) { 
            currentState = EnemyState.SEARCHING;
            target = null;
        }
    }

    //private void executeSearching(Gameboard map) {}
    //private void executeChasing(Gameboard map) {}
    //private void executeFighting() {}
    //private void executeFleeing(Gameboard map) {}
    //private void executeDead() {}

    private Character findAdjacentPlayer(List<Character> playerUnits) {
        for (Character player : playerUnits) {
            //
            //
            //
        }
        return null;
    }

    private Character findPlayerInRange(List<Character> playerUnits, int range) {
        for (Character player : playerUnits) {
            //
            //
            //
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