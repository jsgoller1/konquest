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
    protected int movesRemaining;
    protected boolean hasAttacked;

    protected Actor(String spriteSheetPath, GameBoard board, String name, int health, int attack,
            int speed) {
        super(spriteSheetPath);
        this.board = board;
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.speed = speed;
        this.movesRemaining = speed;
        this.hasAttacked = false;
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

    public int getMovesRemaining() {
        return this.movesRemaining;
    }

    public void setMovesRemaining(int newVal) {
        this.movesRemaining = newVal;
    }

    // What should the character do on their turn?
    abstract public void onTurn();

    // Has this character done everything they could choose to do on their turn?
    // By default, that's "moved as much as possible and attacked"
    public boolean isTurnCompleted() {
        return this.movesRemaining == 0 && this.hasAttacked;
    }

    // Reset the things the character can do per turn in prep for next turn
    public void resetTurnState() {
        this.movesRemaining = this.speed;
        this.hasAttacked = false;
    }
}
