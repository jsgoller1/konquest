package game;

import java.awt.image.BufferedImage;

public abstract class BoardPiece {
    // TODO: BoardPiece shouldn't know anything about Swing, but for
    // now this will do (e.g. if we do terminal style display, we'll
    // want this to be an ASCII character)
    protected BufferedImage texture;

    public BufferedImage getDrawData() {
        return texture;
    }
}
