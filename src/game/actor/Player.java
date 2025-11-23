package game.actor;

import logging.Logger;
import game.GameBoard;
import game.actor.Actor;
import sprites.Sprite;
import input.GameKeyListener;


public class Player extends Actor {
    private GameKeyListener keyListener;

    static final String PLAYER_SPRITE_SHEET_PATH =
            "assets/Characters/Soldiers/Melee/RedMelee/SwordsmanRed.png";

    @Override
    public void initializeSprites() {
        this.spriteComponent.setAnimationTimeDeltaMs(500);
        this.spriteComponent.loadSprite(0, 0);
        this.spriteComponent.loadSprite(0, 1);
    }

    public Player(GameBoard board, int health, int attack, int speed, GameKeyListener keyListener) {
        super(PLAYER_SPRITE_SHEET_PATH, board, "Player", health, attack, speed);
        this.keyListener = keyListener;
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

    private void move(int dy, int dx) {
        Logger.debug(String.format("Moving player dy=%d, dx=%d", dy, dx));
        if (movesRemaining > 0 && board.moveActor(this, dy, dx)) {
            this.movesRemaining--;
        }
    }

    @Override
    public void onTurn() {
        if (keyListener.upArrowPressed()) {
            this.move(-1, 0);
        } else if (keyListener.downArrowPressed()) {
            this.move(1, 0);
        } else if (keyListener.leftArrowPressed()) {
            this.move(0, -1);
        } else if (keyListener.rightArrowPressed()) {
            this.move(0, 1);
        } else if (keyListener.spacePressed()) {
            Logger.debug("Player attacking...");
            if (!this.hasAttacked) {
                Logger.debug("Player attacks!");
                // TODO: attack
                this.hasAttacked = true;
            }
        } else if (keyListener.backspacePressed()) {
            Logger.debug("Ending player's turn.");
            this.movesRemaining = 0;
            this.hasAttacked = true;
        }
    }

    @Override
    public boolean isTurnCompleted() {
        return this.movesRemaining == 0 && this.hasAttacked;
    }
}
