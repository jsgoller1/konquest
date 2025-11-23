package game.ai;

import java.util.ArrayDeque;
import game.character.Character;

public class TurnOrder {
    private ArrayDeque<Character> queue;

    public TurnOrder() {
        this.queue = new ArrayDeque<Character>();
    }

    public void addUnit(Character piece) {
        this.queue.add(piece);
    }

    public void removeUnit(Character piece) {
        this.queue.remove(piece);
    }

    public Character peek() {
        return this.queue.peek();
    }

}
