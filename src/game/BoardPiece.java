package game;

import sprites.Sprite;
import sprites.SpriteComponent;
import java.awt.image.BufferedImage;


public abstract class BoardPiece {
    protected SpriteComponent spriteComponent;

    protected BoardPiece(String spriteSheetPath) {
        this.spriteComponent = new SpriteComponent(spriteSheetPath);
    }

    abstract protected void initializeSprites();

    public Sprite getNextSprite() {
        return this.spriteComponent.getNextSprite();
    }


}
