package game.terrain;

import display.Sprite;

public class Grass extends Terrain {

    @Override
    public Sprite getNextSprite() {
        return this.spriteComponent.loadSprite(0, 0);
    }

}
