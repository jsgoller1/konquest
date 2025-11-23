package display;

import input.GameKeyListener;
import javax.swing.*;
import game.*;


// NOTE (joshua): Most of this boilerplate window code is suggested from Claude
public class Window extends JFrame {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 720;

    private GameBoard board;
    private GameKeyListener keyListener;
    private Surface surface;

    private void createAWTWindow() {
        setTitle("Konquest");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centered in screen

        this.surface = new Surface();
        add(surface);
        setVisible(true);
    }

    public Window() {
        createAWTWindow();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.keyListener = new GameKeyListener();
        this.addKeyListener(this.keyListener);
        this.board = new GameBoard(20, 20, this.keyListener);
    }

    public void update(long time) {
        this.board.update(time);
        this.surface.updateDisplay(board, time);
    }

    public boolean isGameComplete() {
        return this.board.isGameComplete();
    }
}
