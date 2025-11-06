package display;

import input.GameKeyListener;
import javax.swing.*;
import game.*;


// NOTE (joshua): Most of this boilerplate window code is suggested from Claude
public class Window extends JFrame {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 720;

    private GameKeyListener keyListener;
    private Surface surface;

    private void createAWTWindow() {
        setTitle("Konquest");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centered in screen

        surface = new Surface();
        add(surface);
        setVisible(true);
    }

    public Window() {
        createAWTWindow();
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.keyListener = new GameKeyListener();
        this.addKeyListener(this.keyListener);
    }

    public void update(GameBoard board, long time) {
        board.update(keyListener, time);
        surface.updateDisplay(board, time);
    }
}
