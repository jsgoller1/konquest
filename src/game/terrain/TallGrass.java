package game.terrain;

import java.util.Random;
import sprites.Sprite;

public class TallGrass extends Terrain {
    static final String TALLGRASS_SPRITE_SHEET_PATH = "assets/Nature/Wheatfield.png";
    private int spriteSheetRow;
    private int spriteSheetCol;

    public TallGrass() {
        super(1, TALLGRASS_SPRITE_SHEET_PATH);
        Random rand = new Random();
        spriteSheetRow = 0; // rand.nextInt(1);
        spriteSheetCol = 0; // rand.nextInt(2, 4);

        this.initializeSprites();
    }

    @Override
    public void initializeSprites() {
        this.spriteComponent.loadSprite(spriteSheetRow, spriteSheetCol);
    }

    @Override
    public boolean canBeOccupied() {
        return true;
    }

    @Override
    public int getMoveCost() {
        return 2;
    }
}
