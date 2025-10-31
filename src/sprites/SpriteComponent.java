package sprites;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import logging.Logger;

public class SpriteComponent {
    private BufferedImage spriteSheet;
    private String spriteSheetPath;
    private SpriteSet spriteSet;
    private long animationTimeDeltaMs = 0;
    private long lastUpdateTimeMs = 0;

    public SpriteComponent(String spriteSheetPath, int animationTimeDeltaMs) {
        this.loadSpriteSheet(spriteSheetPath);
        this.spriteSet = new SpriteSet();
        this.animationTimeDeltaMs = animationTimeDeltaMs;
    }

    public SpriteComponent(String spriteSheetPath) {
        this(spriteSheetPath, Integer.MAX_VALUE);
    }

    public void setAnimationTimeDeltaMs(long animationTimeDeltaMs) {
        this.animationTimeDeltaMs = animationTimeDeltaMs;
    }

    public void loadSpriteSheet(String spriteSheetPath) {
        try {
            this.spriteSheet = ImageIO.read(new File(spriteSheetPath));
            Logger.debug("Successfully loaded sprite sheet from " + spriteSheetPath);
            this.spriteSheetPath = spriteSheetPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAnimation(long updateTime) {
        long deltaTime = Math.abs(this.lastUpdateTimeMs - updateTime);

        if (deltaTime > this.animationTimeDeltaMs) {
            Sprite sprite = this.spriteSet.nextSprite();
            this.lastUpdateTimeMs = updateTime;
        }
    }

    public Sprite getNextSprite() {
        return this.spriteSet.getSprite();
    }

    public void loadSprite(int row, int col) {
        this.loadSprite(row, 0, col, 0);
    }

    public void loadSprite(int row, int plus_rows, int col, int plus_cols) {
        /*
         * Loads the sprite at (row, col) on the sprite sheet; each sprite is 1x1 cell on the sheet,
         * unless otherwise specified (i.e. larger multi cell sprite like a building or boat)
         */

        this.spriteSet.addSprite(
                new Sprite(this.spriteSheetPath, this.spriteSheet, row, plus_rows, col, plus_cols));
    }
}

