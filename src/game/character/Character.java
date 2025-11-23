package game.character;

import game.BoardPiece;
import sprites.SpriteComponent;

abstract public class Character extends BoardPiece {
    protected String name;
    protected int health;
    protected int attack;
    protected int speed;

    protected Character(String spriteSheetPath) {
        super(spriteSheetPath);
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
