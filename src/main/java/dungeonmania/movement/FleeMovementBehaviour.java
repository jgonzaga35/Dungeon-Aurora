package dungeonmania.movement;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;

public class FleeMovementBehaviour implements Movement {
    private DungeonMap map;
    private Cell currentCell;

    public FleeMovementBehaviour(DungeonMap map, Cell initialCell)
    {
        this.map = map;
        this.currentCell = initialCell;
    }

    public Cell move()
    {
        return new Cell(new Pos2d(0, 0));
    }

    public Cell getCurrentPosition()
    {
        return currentCell;
    }
    
}
