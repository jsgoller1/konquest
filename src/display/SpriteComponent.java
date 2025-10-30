package display;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import logging.Logger;

import display.Sprite;

public class SpriteComponent {
    private BufferedImage spriteSheet;
    private String spriteSheetPath;

    public void loadSpriteSheet(String spriteSheetPath) {
        try {
            this.spriteSheet = ImageIO.read(new File(spriteSheetPath));
            Logger.debug("Successfully loaded sprite sheet from " + spriteSheetPath);
            this.spriteSheetPath = spriteSheetPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SpriteComponent(String spriteSheetPath) {
        this.loadSpriteSheet(spriteSheetPath);
    }

    public Sprite loadSprite(int row, int col) {
        return this.loadSprite(row, 0, col, 0);
    }

    public Sprite loadSprite(int row, int plus_rows, int col, int plus_cols) {
        /*
         * Loads the sprite at (row, col) on the sprite sheet; each sprite is 1x1 cell on the sheet,
         * unless otherwise specified (i.e. larger multi cell sprite like a building or boat)
         */

        return new Sprite(this.spriteSheetPath, this.spriteSheet, row, plus_rows, col, plus_cols);
    }
}

