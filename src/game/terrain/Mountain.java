package game.terrain;

import java.util.Random;
import display.Sprite;

public class Mountain extends Terrain {
    static final String MOUNTAIN_SPRITE_SHEET_PATH = "assets/Ground/Cliff.png";
    private int spriteSheetRow;
    private int spriteSheetCol;


    @Override
    public Sprite getNextSprite() {
        // NOTE: Sampling is weird here; looks ok for now but need to probably slightly tweak
        // and look at the sprite sheet dimensions more carefully
        return this.spriteComponent.loadSprite(0, 2, 0, 2);
    }

    public Mountain() {
        super(99, MOUNTAIN_SPRITE_SHEET_PATH);
        this.spriteSheetRow = 0;
        this.spriteSheetCol = 5;
    }

}
