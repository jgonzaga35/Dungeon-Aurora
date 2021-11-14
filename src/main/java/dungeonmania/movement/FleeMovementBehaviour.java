package dungeonmania.movement;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;

/**
 * Represents the flee movement behaviour of enemies when player is invicible.
 * The flee movement will cause the entity to move away from the player.s
 */
public class FleeMovementBehaviour extends MovementBehaviour {
    private DungeonMap map;

    public FleeMovementBehaviour(int precedence, DungeonMap map, Cell initialCell)
    {
        super(precedence, initialCell);
        this.map = map;
    }

    public Cell move()
    {
        Cell nextCell = map.getNeighbors(getCurrentCell()).stream()
            .filter(c -> !c.isBlocking())
            .max(
                (c1, c2) -> 
                Integer.compare(c1.getPlayerDistance(), c2.getPlayerDistance())
            ).get();
        assert nextCell != null;
        
        setCurrentCell(nextCell);

        return nextCell;
    }   
}
