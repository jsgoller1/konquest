package game;

import java.awt.image.BufferedImage;

public abstract class BoardPiece {
    // TODO: BoardPiece shouldn't know anything about Swing, but for
    // now this will do (e.g. if we do terminal style display, we'll
    // want this to be an ASCII character)
    String name;
    int health;
    int attack;
    int speed;

    public BoardPiece(String name, int health, int attack, int speed) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.speed = speed;
    }



    protected BufferedImage texture;

    public BufferedImage getDrawData() {
        return texture;
    }
}
