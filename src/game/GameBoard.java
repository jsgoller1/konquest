package game;

import logging.Logger;

public class GameBoard {
    private BoardPiece boardData[][];

    public GameBoard(int height, int width) {
        if (height < 5 || width < 5) {
            Logger.error("Board must be at least 5x5; cannot create smaller board.");
            return;
        }
        boardData = new BoardPiece[height][width];
    }

    public BoardPiece getBoardPiece(int y, int x) {
        if (!(0 <= y && y < this.getBoardHeight() && 0 <= x && x < this.getBoardWidth())) {
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
