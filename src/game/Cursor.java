package game;

import game.ai.Behavior;

public class Cursor extends BoardPiece {
    // Cursor object actually does not "have" a position -
    // the position is determined based on whichever character
    // is currently taking their turn.


    static final String CURSOR_SPRITE_SHEET_PATH = "assets/UI/BoxSelector.png";

    @Override
    public void initializeSprites() {
        this.spriteComponent.setAnimationTimeDeltaMs(500);
        this.spriteComponent.loadSprite(0, 0);
        this.spriteComponent.loadSprite(0, 1);
    }

    public Cursor() {
        super(CURSOR_SPRITE_SHEET_PATH);
        this.initializeSprites();
    }


}
