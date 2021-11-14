package dungeonmania.movement;

import dungeonmania.Cell;

/**
 * Represents a waiting behaviour.
 * Entities that implement this strategy will remain at the same spot for a set duration.
 */
public class WaitMovementBehaviour extends MovementBehaviour {

    Integer duration;

    public WaitMovementBehaviour(int precedence, Cell initialCell, int waitDuration) {
        super(precedence, initialCell);
        this.duration = waitDuration;
    }

    public boolean isActive() {
        return this.duration > 0;
    }

    @Override
    public Cell move() {
        this.duration--;

        return this.getCurrentCell();
    }
    
}
