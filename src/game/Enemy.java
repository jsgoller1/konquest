package game;

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

    public Enemy(String name, int health, int attack, int speed, Character target) {
        super(ENEMY_SPRITE_SHEET_PATH);
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.speed = speed;
        this.target = target;
        this.fsm = new Behavior(this);

        this.initializeSprites();
    }

    public int getHealth() {
        return this.health;
    }

    public void damage(int damage) {
        this.health -= damage;
    }
}
