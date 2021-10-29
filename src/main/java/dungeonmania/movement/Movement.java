package dungeonmania.movement;

import dungeonmania.Cell;

public interface Movement {
    /**
     * Returns the cell that the entity should move to next based on it's
     * current movement strategy.
     * 
     * @return the next Cell that the entity should move to.
     */
    public Cell move();
    public Cell getCurrentCell();
}
