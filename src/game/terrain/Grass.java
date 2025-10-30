package game.terrain;

import java.util.Random;
import display.Sprite;

public class Grass extends Terrain {
    static final String GRASS_SPRITE_SHEET_PATH = "assets/Ground/TexturedGrass.png";
    private int spriteSheetRow;
    private int spriteSheetCol;

    public Grass() {
        super(1, GRASS_SPRITE_SHEET_PATH);
        Random rand = new Random();
        spriteSheetRow = rand.nextInt(2);
        spriteSheetCol = rand.nextInt(3);
    }

    @Override
    public Sprite getNextSprite() {
        return this.spriteComponent.loadSprite(spriteSheetRow, spriteSheetCol);
    }

    @Override
    public boolean canBeOccupied() {
        return true;
    }
}
