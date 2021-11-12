package dungeonmania.movement;

import dungeonmania.Cell;

/**
 * Movement strategies have a precedence to handle multiple potions being taken
 * at once. Here's an example.
 * 
 * For the argument, assume that both potion last the same amount of time.
 * Strategies are listed ordered by time (last was set last)
 * Active strategy is highlighted with underscores.
 * Mode is standard.
 * 
 * <pre>
 * ---
 * | Event                         | Enemy Mercenary Movement     |
 * | ----------------------------- | ---------------------------- |
 * | -                             | _FollowPlayer_               |
 * | Invisibility Potion           | FollowPlayer, _Random_       | // mercenary doesn't know where the player is
 * | Invincibility Potion          | FollowPlayer, _Random_, Flee | // eventhough the invincibility potion was taken *later*
 * |                               |                              | //   the mercenary can't see where the player is, so it
 * |                               |                              | //   can't run away
 * | Invincibility Potion runs out | FollowPlayer, _Flee_         | // now, mercenary can see, so it runs away
 * | Invisibility Potion runs out  | _FollowPlayer_               |
 * </pre>
 * 
 * So, how do we know which strategy to use? Precendence.
 * 
 * Can this be tested? Nope, because the spec doesn't specify the duration
 * of the potions. So it's a pure waste of time mark-wise. But it's
 * intersting :) Mathieu
 */
public abstract class MovementBehaviour {

    private int precedence;
    private Cell currentCell;
    public MovementBehaviour(int precedence, Cell initialCell) {
        this.precedence = precedence;
        this.currentCell = initialCell;
    }

    /**
     * Moves to the next position.
     * 
     * @return the next Cell that the entity has moved to.
     */
    public abstract Cell move();

    public Cell getCurrentCell() {
        return this.currentCell;
    }

    public void setCurrentCell(Cell cell) {
        this.currentCell = cell;
    }

    public int getPrecendence() {
        return this.precedence;
    }
}
