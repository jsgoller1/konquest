package game.character;

import game.character.Character;
import sprites.Sprite;


public class Player extends Character {


    static final String CHARACTER_SPRITE_SHEET_PATH =
            "assets/Characters/Soldiers/Melee/RedMelee/SwordsmanRed.png";

    @Override
    public void initializeSprites() {
        this.spriteComponent.setAnimationTimeDeltaMs(500);
        this.spriteComponent.loadSprite(0, 0);
        this.spriteComponent.loadSprite(0, 1);
    }

    public Player(int health, int attack, int speed) {
        super(CHARACTER_SPRITE_SHEET_PATH);
        this.health = health;
        this.attack = attack;
        this.speed = speed;

        this.initializeSprites();
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
}
