package game.ai;

import java.util.ArrayDeque;
import game.actor.Actor;

public class TurnOrder {
    private ArrayDeque<Actor> queue;

    public TurnOrder() {
        this.queue = new ArrayDeque<Actor>();
    }

    public void addUnit(Actor piece) {
        this.queue.add(piece);
    }

    public void removeUnit(Actor piece) {
        this.queue.remove(piece);
    }

    public Actor peek() {
        return this.queue.peek();
    }

}
