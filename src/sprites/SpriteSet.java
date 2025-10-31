package sprites;

import logging.Logger;

import java.util.ArrayList;

public class SpriteSet {
    /*
     * A sprite set is a collection of sprites.
     */
    private ArrayList<Sprite> sprites;
    private int idx = 0;

    public SpriteSet() {
        sprites = new ArrayList<Sprite>();
    }

    public void addSprite(Sprite sprite) {
        this.sprites.add(sprite);
    }

    public Sprite nextSprite() {
        this.idx = (this.idx + 1) % sprites.size();
        return this.getSprite();
    }

    public Sprite getSprite() {
        if (this.sprites.isEmpty()) {
            Logger.debug("No sprites in set.");
            return null;
        }
        return sprites.get(idx);
    }
}
