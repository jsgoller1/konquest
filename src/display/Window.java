package display;

import input.InputManager;
import javax.swing.*;
import game.*;


// NOTE (joshua): Most of this boilerplate window code is suggested from Claude
public class Window extends JFrame {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 720;

    private InputManager inputManager;
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

    public Window(InputManager inputManager) {
        this.inputManager = inputManager;
        createAWTWindow();
    }

    public void update(GameBoard board, long time) {
        surface.update(board, time);
    }


}
