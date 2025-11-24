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

    @Override
    public void onTurn() {
        if (keyListener.upArrowPressed()) {
            Logger.debug("Moving player up.");
            board.moveActor(this, -1, 0);
            // this.lastUpdateTimeMs = time;
        } else if (keyListener.downArrowPressed()) {
            Logger.debug("Moving player down.");
            board.moveActor(this, 1, 0);
            // this.lastUpdateTimeMs = time;
        } else if (keyListener.leftArrowPressed()) {
            Logger.debug("Moving player left.");
            board.moveActor(this, 0, -1);
            // this.lastUpdateTimeMs = time;
        } else if (keyListener.rightArrowPressed()) {
            Logger.debug("Moving player right.");
            board.moveActor(this, 0, 1);
            // this.lastUpdateTimeMs = time;
        } else if (keyListener.spacePressed()) {
            // TODO: attack
            // this.lastUpdateTimeMs = time;
        }
    }
}
