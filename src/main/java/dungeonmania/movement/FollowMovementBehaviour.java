package dungeonmania.movement;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;

public class FollowMovementBehaviour implements Movement {
    private DungeonMap map;
    private Cell currentCell;

    public FollowMovementBehaviour(DungeonMap map, Cell initialCell)
    {
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

    public Cell getCurrentPosition()
    {
        return currentCell;
    }
    
}
