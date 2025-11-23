package game.character;

import java.util.List;
import sprites.Sprite;
import game.ai.Behavior;
import game.character.Character;
import game.GameBoard;

public class Enemy extends Character {
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
        super(ENEMY_SPRITE_SHEET_PATH, board, health, attack, speed);
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

    @Override
    public boolean isTurnCompleted() {
        return false;
    }

    @Override
    public void resetTurnState() {}
}
