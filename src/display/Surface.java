package display;

import game.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import logging.Logger;

public class Surface extends JPanel {
    private GameBoard board;

    // TODO: the board should own its size;
    // hardcoding here for now.
    int SQUARE_SIZE = 32;
    int BOARD_HEIGHT_WIDTH_SQUARE_COUNT = 20;
    int BOARD_HEIGHT_WIDTH = BOARD_HEIGHT_WIDTH_SQUARE_COUNT * (SQUARE_SIZE);
    int OFFSET = SQUARE_SIZE;


    public void updateDisplay(GameBoard board) {
        // Surface.paintComponent() drives actual drawing, but we cannot
        // call it directly. The workflow here is:
        // update board -> call repaint() -> ??? swing magic ??? -> paintComponent() called
        this.board = board;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        /*
         * DO NOT CALL THIS METHOD DIRECTLY. Swing triggers it when we call this.repaint(); You can
         * call methods downstream from it.
         */
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        this.setBackground(Color.BLACK);
        this.drawGrid(g2d);
        this.drawPieces(g2d);
    }

    protected void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY);

        // Vertical lines
        for (int x = 0; x <= this.BOARD_HEIGHT_WIDTH_SQUARE_COUNT; x += 1) {
            int lineStartX = this.OFFSET + (this.SQUARE_SIZE * x);
            int lineStartY = this.OFFSET;
            int lineEndX = lineStartX;
            int lineEndY = lineStartY + BOARD_HEIGHT_WIDTH;

            g2d.drawLine(lineStartX, lineStartY, lineEndX, lineEndY);
        }

        // Horizontal lines
        for (int y = 0; y <= this.BOARD_HEIGHT_WIDTH_SQUARE_COUNT; y += 1) {
            int lineStartX = this.OFFSET;
            int lineStartY = this.OFFSET + (this.SQUARE_SIZE * y);
            int lineEndX = lineStartX + BOARD_HEIGHT_WIDTH;
            int lineEndY = lineStartY;

            g2d.drawLine(lineStartX, lineStartY, lineEndX, lineEndY);
        }

    }

    private int gridXToWindowX(int gridX) {
        return this.OFFSET + gridX * this.SQUARE_SIZE;
    }

    private int gridYToWindowY(int gridY) {
        return this.OFFSET + gridY * this.SQUARE_SIZE;
    }

    private void drawPiece(Graphics2D g2d, BoardPiece piece, int row, int col) {
        if (piece == null) {
            Logger.warn("No piece at Y: " + row + ", X: " + col);
            return;
        }
        if (!(board.validCell(row, col))) {
            Logger.error("Cell invalid, cannot draw. Y: " + row + ", X: " + col);
            return;
        }

        Sprite sprite = piece.getNextSprite();
        if (sprite.getSpriteSheet() == null) {
            Logger.warn("Sprite sheet is null for " + piece.toString());
        }
        int gx = gridXToWindowX(col);
        int gy = gridYToWindowY(row);

        g2d.drawImage(sprite.getSpriteSheet(), gx, gy, gx + sprite.getWidth() * 2,
                gy + sprite.getHeight() * 2, sprite.getTopLeftX(), sprite.getTopLeftY(),
                sprite.getBottomRightX(), sprite.getBottomRightY(), null);
    }

    private void drawPieces(Graphics2D g2d) {
        for (int row = 0; row < board.getBoardHeight(); row++) {
            for (int col = 0; col < board.getBoardWidth(); col++) {
                drawPiece(g2d, board.getTerrainPiece(row, col), row, col);
                drawPiece(g2d, board.getCharacterPiece(row, col), row, col);
            }
        }
    }
}


