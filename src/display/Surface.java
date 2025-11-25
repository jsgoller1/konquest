package display;

import game.*;
import game.terrain.TerrainContainer;
import game.terrain.Terrain;
import game.actor.Actor;
import game.actor.Player;
import game.actor.Enemy;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import logging.Logger;
import sprites.Sprite;

public class Surface extends JPanel {
    String fonts[];
    int font;


    public Surface() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.fonts = ge.getAvailableFontFamilyNames();
        this.font = 0;
    }

    private GameBoard board;

    // TODO: the board should own its size;
    // hardcoding here for now.
    int SQUARE_SIZE = 32;
    int BOARD_HEIGHT_WIDTH_SQUARE_COUNT = 20;
    int BOARD_HEIGHT_WIDTH = BOARD_HEIGHT_WIDTH_SQUARE_COUNT * (SQUARE_SIZE);
    int OFFSET = SQUARE_SIZE;
    long currUpdateTime = 0;


    public void updateDisplay(GameBoard board, long time) {
        // Surface.paintComponent() drives actual drawing, but we cannot
        // call it directly. The workflow here is:
        // update board -> call repaint() -> ??? swing magic ??? -> paintComponent() called
        this.board = board;
        this.repaint();
        this.currUpdateTime = time;
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
        this.drawUI(g2d);
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

    private void drawUI(Graphics2D g2d) {
        g2d.setFont(new Font("Bodoni 72 Smallcaps", Font.BOLD, 24));

        ArrayList<Player> players = this.board.getPlayers();
        int y = 35;
        g2d.setColor(Color.BLUE);
        g2d.drawString("Your Forces", 700, y);
        y += 25;
        g2d.drawString("----------------------------", 700, y);
        y += 25;
        for (Player player : players) {
            g2d.drawString(String.format("%s | HP: %d/%d | MP: %d | AP: %d", player.getName(),
                    player.getHealth(), player.getMaxHealth(), player.getMovesRemaining(),
                    player.getHasAttacked() ? 0 : 1), 700, y);
            y += 30;
        }

        ArrayList<Enemy> enemies = this.board.getEnemies();
        y += 50;
        g2d.setColor(Color.RED);
        g2d.drawString("Enemy Troops", 700, y);
        y += 25;
        g2d.drawString("----------------------------", 700, y);
        y += 25;
        for (Enemy enemy : enemies) {
            g2d.drawString(String.format("%s: %d/%d HP", enemy.getName(), enemy.getHealth(),
                    enemy.getMaxHealth()), 700, y);
            y += 30;
        }

        y += 50;
        g2d.setColor(Color.WHITE);
        g2d.drawString("Controls:", 700, y);
        y += 25;
        g2d.drawString("----------------------------", 700, y);
        y += 25;
        g2d.drawString("Arrows: Move selected troop", 700, y);
        y += 30;
        g2d.drawString("Space: Attack", 700, y);
        y += 30;
        g2d.drawString("ESC: quit", 700, y);
        y += 30;
        g2d.drawString("Backspace: End turn", 700, y);

    }

    private int gridXToWindowX(int gridX) {
        return this.OFFSET + gridX * this.SQUARE_SIZE;
    }

    private int gridYToWindowY(int gridY) {
        return this.OFFSET + gridY * this.SQUARE_SIZE;
    }

    private void drawPiece(Graphics2D g2d, BoardPiece piece, int row, int col, long deltaTime) {
        if (piece == null) {
            // Logger.warn("No piece at Y: " + row + ", X: " + col);
            return;
        }
        if (!(board.validCell(row, col))) {
            Logger.error("Cell invalid, cannot draw. Y: " + row + ", X: " + col);
            return;
        }

        piece.updateAnimation(deltaTime);
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
        if (this.board == null) {
            Logger.warn("Board is null; skipping drawing.");
            return;
        }

        Position cursorPostion = board.getCursorPosition();

        for (int row = 0; row < board.getBoardHeight(); row++) {
            for (int col = 0; col < board.getBoardWidth(); col++) {
                TerrainContainer container = board.getTerrainContainer(row, col);
                Actor actor = board.getActor(row, col);

                for (Terrain piece : container.getBackgroundPieces()) {
                    drawPiece(g2d, piece, row, col, this.currUpdateTime);
                }

                if (cursorPostion.y == row && cursorPostion.x == col) {
                    drawPiece(g2d, board.getCursor(), row, col, this.currUpdateTime);
                }

                drawPiece(g2d, actor, row, col, this.currUpdateTime);

                for (Terrain piece : container.getForegroundPieces()) {
                    drawPiece(g2d, piece, row, col, this.currUpdateTime);
                }

            }
        }
    }
}


