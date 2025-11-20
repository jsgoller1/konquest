package game.terrain;

import java.util.Random;
import sprites.Sprite;

public class Mountain extends Terrain {
    static final String MOUNTAIN_SPRITE_SHEET_PATH = "assets/Ground/Cliff.png";
    private int spriteSheetRow;
    private int spriteSheetCol;

    public Mountain() {
        super(99, MOUNTAIN_SPRITE_SHEET_PATH);
        this.spriteSheetRow = 0;
        this.spriteSheetCol = 5;

        this.initializeSprites();
    }

    @Override
    public void initializeSprites() {
        // NOTE: Sampling is weird here; looks ok for now but need to probably slightly tweak
        // and look at the sprite sheet dimensions more carefully
        this.spriteComponent.loadSprite(0, 2, 0, 2);
    }

    @Override
    public boolean canBeOccupied() {
        return false;
    }

    @Override
    public int getMoveCost() {
        return 99;
    }

}
