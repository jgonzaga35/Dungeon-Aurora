package dungeonmania.movement;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;

public class FollowMovementBehaviour extends MovementBehaviour {
    private DungeonMap map;
    private Cell currentCell;

    public FollowMovementBehaviour(int precedence, DungeonMap map, Cell initialCell)
    {
        super(precedence);
        this.map = map;
        this.currentCell = initialCell;
    }

    public Cell move()
    {
        currentCell = map.getNeighbors(currentCell).stream()
            .min(
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
