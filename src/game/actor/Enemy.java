package game.actor;

import java.util.List;
import sprites.Sprite;
import game.ai.Behavior;
import game.ai.EnemyState;
import game.GameBoard;
import game.actor.Actor;

public class Enemy extends Actor {
    private Player target;
    private Behavior fsm;

    static final String ENEMY_SPRITE_SHEET_PATH = "assets/Characters/Monsters/Orcs/SpearGoblin.png";
    static final int HEALTH_POINTS = 10;
    static final int ATTACK_POWER = 2;
    static final int SPEED_RATING = 4;

    @Override
    public void initializeSprites() {
        this.spriteComponent.setAnimationTimeDeltaMs(500);
        this.spriteComponent.loadSprite(0, 0);
        this.spriteComponent.loadSprite(0, 1);
    }

    public Enemy(GameBoard board, int id) {
        super(ENEMY_SPRITE_SHEET_PATH, board, id, "Enemy", HEALTH_POINTS, ATTACK_POWER,
                SPEED_RATING);
        this.target = null;
        this.fsm = new Behavior(this.board, this);

        this.initializeSprites();
    }

    @Override
    public void onTurn() {
        this.fsm.updateState();
        this.fsm.executeStateBehavior();
    }

    @Override
    public boolean isTurnCompleted() {
        switch (this.fsm.getCurrentState()) {
            case EnemyState.DEAD:
                return true;
            case EnemyState.CHASING:
            case EnemyState.SEARCHING:
            case EnemyState.FLEEING:
                return this.movesRemaining == 0;
            case EnemyState.FIGHTING:
                return this.hasAttacked;
            default:
                return true;
        }
    }

    public String getState() {
        switch (this.fsm.getCurrentState()) {
            case EnemyState.DEAD:
                return "DEAD";
            case EnemyState.CHASING:
                return "CHASING";
            case EnemyState.SEARCHING:
                return "SEARCHING";
            case EnemyState.FLEEING:
                return "FLEEING";
            case EnemyState.FIGHTING:
                return "FIGHTING";
            default:
                return "";
        }
    }
}
