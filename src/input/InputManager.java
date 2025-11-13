package input;

import logging.Logger;
import game.GameBoard;

public class InputManager {
    private GameKeyListener keyListener;

    public InputManager() {
        this.keyListener = new GameKeyListener();
    }

    private void getInput() {}

    public GameKeyListener getKeyListener() {
        /*
         * Window needs this to register focus
         */
        return this.keyListener;
    }

    public void update(GameBoard board) {
        /*
         * Gathers input, and sends Commands to GameBoard
         */
        getInput();
    }
}
