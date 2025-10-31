package sprites;

import java.awt.image.BufferedImage;

public class Sprite {
    private String spriteSheetPath;
    private BufferedImage spriteSheet;
    private int SPRITE_HEIGHT = 16;
    private int SPRITE_WIDTH = 16;
    private int plusRows;
    private int plusCols;
    private int topLeftX;
    private int topLeftY;

    public String getSpriteSheetPath() {
        return this.spriteSheetPath;
    }

    public BufferedImage getSpriteSheet() {
        return this.spriteSheet;
    }

    public int getHeight() {
        return this.SPRITE_HEIGHT;
    }

    public int getWidth() {
        return this.SPRITE_WIDTH;
    }

    public int getTopLeftX() {
        return this.topLeftX;
    }

    public int getTopLeftY() {
        return this.topLeftY;
    }

    public int getBottomRightX() {
        return topLeftX + (this.SPRITE_WIDTH + (this.SPRITE_WIDTH * plusCols));
    }

    public int getBottomRightY() {
        return topLeftY + (this.SPRITE_HEIGHT + (this.SPRITE_HEIGHT * plusRows));
    }

    public Sprite(String spriteSheetPath, BufferedImage spriteSheet, int row, int plusRows, int col,
            int plusCols) {
        /*
         * Grabs sprite from (row, col) on the sprite sheet by internally calculating the pixels
         * that corresponds to. Some sprites (boats, castles, houses, etc) use multi-cell sprites;
         * if plusRows/plusCols are used, a larger sprite sampled from multiple adjacent cells is
         * returned.
         */
        this.spriteSheetPath = spriteSheetPath;
        this.spriteSheet = spriteSheet;
        this.plusRows = plusRows;
        this.plusCols = plusCols;
        this.topLeftX = row * this.SPRITE_WIDTH;
        this.topLeftY = row * this.SPRITE_HEIGHT;
    }
}
