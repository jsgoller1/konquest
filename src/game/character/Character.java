package game.character;

import game.BoardPiece;
import game.GameBoard;
import sprites.SpriteComponent;

abstract public class Character extends BoardPiece {
    protected GameBoard board;
    protected int health;
    protected int attack;
    protected int speed;

    protected Character(String spriteSheetPath, GameBoard board, int health, int attack,
            int speed) {
        super(spriteSheetPath);
        this.board = board;
        this.health = health;
        this.attack = attack;
        this.speed = speed;
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

    abstract public void onTurn();
}
