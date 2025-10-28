package game;

import display.SpriteComponent;
import display.Sprite;

import java.awt.image.BufferedImage;


public abstract class BoardPiece {
    protected SpriteComponent spriteComponent;

    public abstract Sprite getNextSprite();

    protected BoardPiece(String spriteSheetPath) {
        this.spriteComponent = new SpriteComponent(spriteSheetPath);
    }


}
