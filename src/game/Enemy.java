package game;

import java.util.List;
import sprites.Sprite;
import behavior.Behavior;

public class Enemy extends BoardPiece {
    private String name;
    private int health;
    private int attack;
    private int speed;
    private Character target;
    private Behavior fsm;

    static final String ENEMY_SPRITE_SHEET_PATH = "assets/Characters/Monsters/Orcs/SpearGoblin.png";

    @Override
    public void initializeSprites() {
        this.spriteComponent.setAnimationTimeDeltaMs(500);
        this.spriteComponent.loadSprite(0, 0);
        this.spriteComponent.loadSprite(0, 1);

    }

    public Enemy(String name, int health, int attack, int speed) {
        super(ENEMY_SPRITE_SHEET_PATH);
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.speed = speed;
        this.target = null;
        this.fsm = new Behavior(this);

        this.initializeSprites();
    }

    public void updateStates(GameBoard board, List<Enemy> enemyUnits) {
        fsm.updateBehavior(board, enemyUnits);
    }

    public int getHealth() {
        return this.health;
    }

    public int getAttack() {
        return this.attack;
    }

    public void damage(int damage) {
        this.health -= damage;
    }

    // getter methods
    public Behavior getBehavior() {
        return this.fsm;
    }
}