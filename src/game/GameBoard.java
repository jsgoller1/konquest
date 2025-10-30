package game;

import logging.Logger;
import java.util.Random;
import game.terrain.Grass;
import game.terrain.Mountain;


public class GameBoard {
    private BoardPiece boardData[][];
    private Cursor cursor;

    public GameBoard(int height, int width) {
        if (height < 5 || width < 5) {
            Logger.error("Board must be at least 5x5; cannot create smaller board.");
            return;
        }
        boardData = new BoardPiece[height][width];
        cursor = new Cursor();

        // Randomly add board pieces; this is just a proof of concept to ensure
        /// texture loading works correctly and will be scrapped
        Random rand = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pick = rand.nextInt(100);
                if (pick < 65) {
                    boardData[y][x] = new Grass();
                } else if (pick < 75) {
                    boardData[y][x] = new Mountain();
                } else if (pick < 87) {
                    boardData[y][x] = new Character("Knight", 10, 10, 10);
                } else {
                    boardData[y][x] = new Enemy("Enemy", 10, 10, 10);
                }
            }
        }
    }

    public boolean validCell(int y, int x) {
        return (0 <= y && y < this.getBoardHeight() && 0 <= x && x < this.getBoardWidth());
    }

    public boolean cellEmpty(int y, int x) {
        return validCell(y, x) && (boardData[y][x] == null);

    }

    public BoardPiece getBoardPiece(int y, int x) {
        if (!validCell(y, x)) {
            Logger.error(String.format("Cannot get piece at (%d, %d)", x, y));
            return null;
        }
        return this.boardData[y][x];
    }

    public int getBoardHeight() {
        return boardData.length;
    }

    public int getBoardWidth() {
        return boardData[0].length;
    }

}
