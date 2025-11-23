package game.ai;

import game.character.Character;

public class TurnManager {
    private TurnOrder turnOrder;

    public TurnManager() {
        this.turnOrder = new TurnOrder();
    }

    public void register(Character character) {
        this.turnOrder.addUnit(character);
    }

    public void deregister(Character character) {}

    public void takeTurn() {
        // TODO: This doesn't actually cycle the turn order, just confirms logic works for now.
        Character current = turnOrder.peek();
        current.onTurn();
    }
}
