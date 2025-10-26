package game;

import display.Sprite;


public class Enemy extends BoardPiece {
    private String name;
    private int health;
    private int attack;
    private int speed;

    static final String ENEMY_SPRITE_SHEET_PATH = "assets/Characters/Monsters/Orcs/SpearGoblin.png";

    @Override
    public Sprite getNextSprite() {
        return this.spriteComponent.loadSprite(0, 0);
    }

    public Enemy(String name, int health, int attack, int speed) {
        super(ENEMY_SPRITE_SHEET_PATH);
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.speed = speed;
    }
}
