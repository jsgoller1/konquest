package behavior;

import java.util.List;
import game.Enemy;
import game.GameBoard;

public class Behavior {
    private static final int chaseRange = 5;
    private static final int fleeHealthThreshold = 3;

    private EnemyState currentState;
    private Character target;
    private Enemy owner;

    public Behavior(Enemy owner) {
        this.owner = owner;
        this.currentState = EnemyState.SEARCHING;
    }

    public void updateBehavior(GameBoard map, List<Enemy> enemyUnits) {
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
        // PlayerUnit nearbyPlayerUnit = findPlayerInRange(playerUnits, chaseRange);
        if (owner.getHealth() <= 0) {
            currentState = EnemyState.DEAD;
            return;
        } else if (owner.getHealth() <= fleeHealthThreshold) {
            currentState = EnemyState.FLEEING;
        } else if (target != null && map.distanceToTarget(target) <= 1) {
            currentState = EnemyState.FIGHTING;
        } else if (target != null && map.distanceToTarget(target) <= chaseRange) {
            currentState = EnemyState.CHASING;
            // target = nearbyPlayerUnit;
            return;
        }
        if (currentState != EnemyState.SEARCHING) {
            currentState = EnemyState.SEARCHING;
            target = null;
        }
    }

    // private PlayerUnit findPlayerInRange(List<character> playerUnits, int range) {}

}


