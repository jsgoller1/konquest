package behavior;

////////////////////////////////////////////

public enum EnemyState {
    SEARCHING, // Move randomly looking for players
    CHASING, // Move towards nearby player units
    FIGHTING, // Attack adjacent player units
    FLEEING, // Run to map edge when low health
    DEAD // No actions when dead
}



/// variables
// distanceToPlayer
// distanceToEdge
// health

// work on FSM
// get pieces together
