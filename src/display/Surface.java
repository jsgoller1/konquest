package display;

import game.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class Surface extends JPanel {
    private GameBoard board;
    private Graphics2D g2d;

    @Override
    protected void paintComponent(Graphics g) {
        /*
         * DO NOT CALL THIS METHOD DIRECTLY. Swing triggers it when we call this.repaint(); You can
         * call methods downstream from it.
         */
        super.paintComponent(g);
        this.g2d = (Graphics2D) g;
        this.setBackground(Color.BLACK);
        this.drawGrid();
        this.drawPieces();
    }

    protected void drawGrid() {
        this.g2d.setColor(Color.LIGHT_GRAY);

        // TODO: the board should own its size;
        // hardcoding here for now.
        int SQUARE_SIZE = 40;
        int BOARD_HEIGHT_WIDTH_SQUARE_COUNT = 20;
        int BOARD_HEIGHT_WIDTH = BOARD_HEIGHT_WIDTH_SQUARE_COUNT * (SQUARE_SIZE + 1);
        int OFFSET = 100;

        // Vertical lines
        for (int x = OFFSET; x <= BOARD_HEIGHT_WIDTH; x += SQUARE_SIZE) {
            this.g2d.drawLine(x, OFFSET, x, BOARD_HEIGHT_WIDTH);
        }

        // Horizontal lines
        for (int y = OFFSET; y <= BOARD_HEIGHT_WIDTH; y += SQUARE_SIZE) {
            this.g2d.drawLine(OFFSET, y, BOARD_HEIGHT_WIDTH, y);
        }

    }


    public void updateDisplay(GameBoard board) {
        this.board = board;
        this.repaint();
    }


    private void drawPieces() {
        // TODO: probably better to have an getter / iterator / something
        for (int y = 0; y < board.getBoardHeight(); y++) {
            for (int x = 0; x < board.getBoardWidth(); x++) {
                // TODO: draw terrain and character if one exists.

            }
        }

    }
}
