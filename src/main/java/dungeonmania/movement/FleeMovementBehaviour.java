package main.java.dungeonmania.movement;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;

public class FleeMovementBehaviour implements Movement {
    private DungeonMap map;
    private Cell currentCell;

    FleeMovementBehaviour(DungeonMap map, Cell initialCell)
    {
        this.map = map;
        this.currentCell = initialCell;
    }

    public Cell move()
    {
        return new Cell();
    }

    public Cell getCurrentPosition()
    {
        return currentCell;
    }
    
}
