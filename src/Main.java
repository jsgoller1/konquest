import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import logging.Logger;
import display.Window;
import game.GameBoard;
import game.pathfinding.*;
import java.util.ArrayList;


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

        while (GAME_RUNNING) {
            long time = System.currentTimeMillis();
            window.update(board, time);
        }
    }
}
