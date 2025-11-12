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
        board = new GameBoard(20, 20);
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

        Enemy dummyEnemy = new Enemy("Dummy", 100, 10, 5); // to delete
        // Add a dummy enemy here; not actually on game board,
            // so won't show up on screent
        while (GAME_RUNNING) {
            long time = System.currentTimeMillis();
            inputManager.update(board);
            window.update(board, time);

            // To test your FSM, try mutating the state of the enemy directly 
            // here, and then print what state they're in after.
            dummyEnemy.setHp = 0; // Example: set health to 0 to test "dead" state
            // Print the enemy's current state, should be "dead"
        }
    }
}
