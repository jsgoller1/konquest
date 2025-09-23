package display;

import javax.swing.*;
import game.*;
import java.awt.*;
import java.awt.image.BufferedImage;


// NOTE (joshua): Most of this boilerplate window code is suggested from Claude
public class Window extends JFrame {
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 1200;

    public Window() {
        setTitle("Untitled Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centered in screen

        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(Color.BLACK);
        add(gamePanel);

        setVisible(true);
    }

    public void updateDisplay(GameBoard board) {
        this.drawGrid(board);
        this.drawPieces(board);
    }

    private void drawGrid(GameBoard board) {

    }

    private void drawPieces(GameBoard board) {
        // TODO: probably better to have an getter / iterator / something
        for (int y = 0; y < board.getBoardHeight(); y++) {
            for (int x = 0; x < board.getBoardWidth(); x++) {
                BoardPiece piece = board.getBoardPiece(y, x);
                if (piece != null) {
                    BufferedImage image = piece.getDrawData();
                }
                // TODO: draw image
            }
        }

    }
}
