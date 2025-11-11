import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import logging.Logger;
import display.Window;
import game.GameBoard;
<<<<<<< HEAD
=======
import input.InputManager;
import game.pathfinding;
>>>>>>> 9c3fe43 (new changes to .pair and .move)

class Main {
    private static Window window;
    private static GameBoard board;

    public static void main(String[] args) {
        // TODO: set logging levels and game board size via CLI args
        Logger.info("Starting game....");
        boolean GAME_RUNNING = true;
        board = new GameBoard(20, 20);

        try {
            SwingUtilities.invokeAndWait(() -> {
                window = new Window();
                Logger.info("Window created!");
            });
        } catch (InterruptedException | InvocationTargetException e) {
            Logger.error(String.format("Couldn't create window: %s", e.getMessage()));
            GAME_RUNNING = false;
        }
<<<<<<< HEAD

        while (GAME_RUNNING) {
            long time = System.currentTimeMillis();
            window.update(board, time);
        }
=======
        /*
         * Move move = new Move(2, 2, 5, 4, board); Logger.info("Move Successful! Currently at: ");
         * while (GAME_RUNNING) { long time = System.currentTimeMillis();
         * inputManager.update(board); window.update(board, time);
         * 
         * }
         */
>>>>>>> 9c3fe43 (new changes to .pair and .move)
    }
}
