package game.character;

import game.BoardPiece;
import game.GameBoard;
import sprites.SpriteComponent;

abstract public class Character extends BoardPiece {
    protected GameBoard board;
    protected int health;
    protected int attack;
    protected int speed;
    protected int movesRemaining;
    protected boolean hasAttacked;

    protected Character(String spriteSheetPath, GameBoard board, int health, int attack,
            int speed) {
        super(spriteSheetPath);
        this.board = board;
        this.health = health;
        this.attack = attack;
        this.speed = speed;
        this.movesRemaining = speed;
        this.hasAttacked = false;
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

    // What should the character do on their turn?
    abstract public void onTurn();

    // Has this character done everything they could choose to do on their turn?
    abstract public boolean isTurnCompleted();

    // Reset the things the character can do per turn in prep for next turn
    abstract public void resetTurnState();
}
