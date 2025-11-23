package game.actor;

import java.util.List;
import sprites.Sprite;
import game.ai.Behavior;
import game.GameBoard;
import game.actor.Actor;

public class Enemy extends Actor {
    private Player target;
    private Behavior fsm;

    static final String ENEMY_SPRITE_SHEET_PATH = "assets/Characters/Monsters/Orcs/SpearGoblin.png";

    @Override
    public void initializeSprites() {
        this.spriteComponent.setAnimationTimeDeltaMs(500);
        this.spriteComponent.loadSprite(0, 0);
        this.spriteComponent.loadSprite(0, 1);
    }

    public Enemy(GameBoard board, int health, int attack, int speed) {
        super(ENEMY_SPRITE_SHEET_PATH, board, "Enemy", health, attack, speed);
        this.target = null;
        this.fsm = new Behavior(this);

        this.initializeSprites();
    }

    @Override
    public void onTurn() {
        // TODO: Remove this; this is just code allowing for automatic turn cycling.
        this.movesRemaining = 0;
        this.hasAttacked = true;

        this.fsm.updateState();
        this.fsm.executeStateBehavior();
    }

    public Player findAdjacentPlayer() {
        // If any player is one square away, returns them. Otherwise null.
        return null;
    }

    public Player findNearestPlayer() {
        // Get nearest player to this one
        return null;
    }

}
