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

    public void updateStates(GameBoard board, List<Enemy> enemyUnits) {
        fsm.updateBehavior(board, enemyUnits);
    }

    // getter methods
    public Behavior getBehavior() {
        return this.fsm;
    }

    @Override
    public void onTurn() {}
}
