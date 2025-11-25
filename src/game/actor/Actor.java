package game.actor;

import game.BoardPiece;
import game.GameBoard;
import sprites.SpriteComponent;

abstract public class Actor extends BoardPiece {
    protected String name;
    protected GameBoard board;
    protected int health;
    protected int attack;
    protected int speed;

    protected Actor(String spriteSheetPath, GameBoard board, String name, int health, int attack,
            int speed) {
        super(spriteSheetPath);
        this.board = board;
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.speed = speed;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
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
