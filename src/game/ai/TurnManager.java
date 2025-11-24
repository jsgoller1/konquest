package game.ai;

import game.actor.Actor;
import logging.Logger;

public class TurnManager {
    private TurnOrder turnOrder;

    public TurnManager() {
        this.turnOrder = new TurnOrder();
    }

    public void register(Actor actor) {
        if (actor == null) {
            Logger.warn("TurnManager: Trying to register null actor, skipping.");
            return;
        }
        Logger.debug(String.format("TurnManager registered %s", actor.getName()));
        this.turnOrder.addUnit(actor);
    }

    public void deregister(Actor character) {}

    public void takeTurn() {
        // TODO: This doesn't actually cycle the turn order, just confirms logic works for now.
        Actor current = turnOrder.peek();
        if (current == null) {
            Logger.warn("TurnManager: Null actor attempting to take turn. Skipping.");
        }
        // NOTE: This runs on every loop; be careful about overwhelming output.
        // Logger.debug(String.format("%s taking turn.", current.getName()));
        current.onTurn();
    }
}
