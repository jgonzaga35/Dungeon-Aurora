package dungeonmania.movement;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;

public class FleeMovementBehaviour extends MovementBehaviour {
    private DungeonMap map;
    private Cell currentCell;

    public FleeMovementBehaviour(int precedence, DungeonMap map, Cell initialCell)
    {
        super(precedence);
        this.map = map;
        this.currentCell = initialCell;
    }

    public Cell move()
    {
        currentCell = map.getNeighbors(currentCell).stream()
            .filter(c -> !c.isBlocking())
            .max(
                (c1, c2) -> 
                Integer.compare(c1.getPlayerDistance(), c2.getPlayerDistance())
            ).get();
        
        return currentCell;
    }

    public Cell getCurrentCell()
    {
        return currentCell;
    }
    
}
