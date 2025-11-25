package game.ai;

import game.actor.Actor;

public class TurnManager {
    private TurnOrder turnOrder;

    public TurnManager() {
        this.turnOrder = new TurnOrder();
    }

    public void register(Actor character) {
        this.turnOrder.addUnit(character);
    }

    public void deregister(Actor character) {}

    public void takeTurn() {
        // TODO: This doesn't actually cycle the turn order, just confirms logic works for now.
        Actor current = turnOrder.peek();
        current.onTurn();
    }
}
