package game;

import java.util.PriorityQueue;



public class TurnOrder (Unit[] Units) { // Takes in an array of units to initialize the turn order
    PriorityQueue<Unit> PriorityQueue = new PriorityQueue<Unit>();
    for (Unit unit : Units) {
        PriorityQueue.add(unit);
    
    }


    public void addUnit(Unit unit) { // Adds a unit to the turn order
        PriorityQueue.add(unit);
    }





    public void removeUnit(Unit unit) { // removed a unit from the turn order (does it also "kill" the unit / clean up?)
        PriorityQueue.remove(unit);
    }






    public Unit getNextUnit() { // returns the next unit in the turn
        //return extractMax(PriorityQueue);

        Unit nextUnit = PriorityQueue.poll();
        PriorityQueue.add(nextUnit);


        return nextUnit;

    }
}


