package display;

import javax.swing.*;
import game.*;


// NOTE (joshua): Most of this boilerplate window code is suggested from Claude
public class Window extends JFrame {
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 1200;

    private static Surface surface;

    public Window() {
        setTitle("Untitled Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centered in screen

        surface = new Surface();
        add(surface);
        setVisible(true);
    }

    public void updateDisplay(GameBoard board) {
        surface.updateDisplay(board);
    }


}
