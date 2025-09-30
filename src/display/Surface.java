package display;

import game.*;
import javax.swing.*;
import game.BoardPiece;
import game.GameBoard;
import java.awt.image.BufferedImage;
import java.awt.Color;


public class Surface extends JPanel {

    public Surface() {
        this.setBackground(Color.BLACK);
    }

    public void updateDisplay(GameBoard board) {
        this.drawGrid(board);
        this.drawPieces(board);
    }

    private void drawGrid(GameBoard board) {
        // TODO: move to Grid Panel
        // See: https://claude.ai/share/915f8e39-ba9e-45c3-a673-27f28a94a2b0
        // Each cell takes up (HEIGHT / board.height pixels) x (WIDTH / board.width) pixels
        int cellHeight = this.HEIGHT / board.getBoardHeight();
        int cellWidth = this.WIDTH / board.getBoardWidth();
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
