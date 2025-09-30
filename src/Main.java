import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import logging.Logger;
import display.Window;
import game.GameBoard;
import input.InputManager;

class Main {
    private static Window window;
    private static GameBoard board;

    public static void main(String[] args) {
        // TODO: set logging levels and game board size via CLI args
        Logger.info("Starting game....");
        InputManager inputManager = new InputManager();
        board = new GameBoard(25, 25);
        boolean GAME_RUNNING = true;

        try {
            SwingUtilities.invokeAndWait(() -> {
                window = new Window(inputManager);
                Logger.info("Window created!");
            });
        } catch (InterruptedException | InvocationTargetException e) {
            Logger.error(String.format("Couldn't create window: %s", e.getMessage()));
            GAME_RUNNING = false;
        }

        while (GAME_RUNNING) {
            inputManager.update(board);
            window.update(board);

        }
    }
}
