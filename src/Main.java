import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import logging.Logger;
import display.Window;
import game.GameBoard;
import game.pathfinding.*;
import java.util.*;
import game.Enemy;

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

        // to delete; test for Jonathan's behavior system
        List<Enemy> enemies = new ArrayList<>();
        Enemy dummy = new Enemy("Dummy", 100, 10, 5);
        enemies.add(dummy);
        Logger.info("Before damage: health=" + dummy.getHealth() + " state="
                + dummy.getBehavior().getCurrentState());
        dummy.damage(90);
        dummy.updateStates(board, enemies);
        Logger.info("After damage: health=" + dummy.getHealth() + " state="
                + dummy.getBehavior().getCurrentState());
        dummy.damage(10);
        dummy.updateStates(board, enemies);
        Logger.info("After damage: health=" + dummy.getHealth() + " state="
                + dummy.getBehavior().getCurrentState());
        Logger.info("Enemy State: " + dummy.getBehavior().getCurrentState());


        while (GAME_RUNNING) {
            long time = System.currentTimeMillis();
            window.update(board, time);
            for (Enemy enemy : enemies) {
                enemy.updateStates(board, enemies);
            }
        }
    }
}
