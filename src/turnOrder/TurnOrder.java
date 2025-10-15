package game;

import java.util.PriorityQueue;



public class TurnOrder { // Takes in an array of units to initialize the turn order
    private PriorityQueue<BoardPiece> priorityQueue;

    public TurnOrder(BoardPiece[] boardPieces) {
        priorityQueue = new PriorityQueue<BoardPiece>();
        for (BoardPiece boardPiece : boardPieces) {
            priorityQueue.add(boardPiece);
        }
    }


    public void addUnit(BoardPiece piece) { // Adds a unit to the turn order
        priorityQueue.add(piece);
    }





    public void removeUnit(BoardPiece piece) { // removed a unit from the turn order (does it also "kill" the unit / clean up?)
        priorityQueue.remove(piece);
    }






    public BoardPiece getNextUnit() { // returns the next unit in the turn order
        BoardPiece nextUnit = priorityQueue.poll();
        priorityQueue.add(nextUnit);
        return nextUnit;
    }
}


