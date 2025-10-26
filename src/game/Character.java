package game;

import display.Sprite;


public class Character extends BoardPiece {
    private String name;
    private int health;
    private int attack;
    private int speed;

    static final String CHARACTER_SPRITE_SHEET_PATH =
            "assets/Characters/Soldiers/Melee/RedMelee/SwordsmanRed.png";

    @Override
    public Sprite getNextSprite() {
        return this.spriteComponent.loadSprite(0, 0);
    }

    public Character(String name, int health, int attack, int speed) {
        super(CHARACTER_SPRITE_SHEET_PATH);
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.speed = speed;
    }
}
