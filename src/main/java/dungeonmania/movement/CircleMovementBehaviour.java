package main.java.dungeonmania.movement;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;

public class CircleMovementBehaviour implements Movement {
    private DungeonMap map;
    private Cell currentCell;

    CircleMovementBehaviour(DungeonMap map, Cell initialCell)
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
