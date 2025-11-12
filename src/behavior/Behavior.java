import java.util.List;

public class Behavior{
    private static final int chaseRange = 5;
    private static final int fleeHealthThreshold = 3;

    private EnemyState currentState;
    private PlayerUnit target;
    private Enemy enemy;

    public Behavior(Enemy enemy){
        this.enemy = enemy;
        this.currentState = EnemyState.SEARCHING;
        this.target = null;
    }

    public void updateBehavior(GameBoard map, List<Enemy> enemyUnits){
        switch(currentState){
            case SEARCHING:
                executeSearching(map);
                break;
            case CHASING:
                executeChasing(map);
                break;
            case FIGHTING:
                executeFighting();
                break;
            case FLEEING:
                executeFleeing(map);
                break;
            case DEAD:
                executeDead();
                break;
        }
    }

    private void checkStateTransitions(GameBoard map, List<Enemy> enemyUnits){
        PlayerUnit nearbyPlayerUnit = findPlayerInRange(playerUnits, chaseRange);
        if (enemy.getHealth() <= 0) {
            currentState = EnemyState.DEAD;
            return;
        } else if (enemy.getHealth() <= fleeHealthThreshold) {
            currentState = EnemyState.FLEEING;
        } else if (target != null && distanceToTarget(target) <= 1) {
            currentState = EnemyState.FIGHTING;
        } else if (target != null && distanceToTarget(target) <= chaseRange) {
            currentState = EnemyState.CHASING;
            target = nearbyPlayerUnit;
            return;
        } 
        if (currentState != EnemyState.SEARCHING) {
            currentState = EnemyState.SEARCHING;
            target = null;
        }
    }
    // private PlayerUnit findPlayerInRange(List<character> playerUnits, int range)

}

////////////////////////////////////////////

public enum EnemyState {
    SEARCHING,   // Move randomly looking for players
    CHASING,     // Move towards nearby player units
    FIGHTING,    // Attack adjacent player units
    FLEEING,     // Run to map edge when low health
    DEAD         // No actions when dead
}



/// variables
// distanceToPlayer
// distanceToEdge
// health

// work on FSM
// get pieces together