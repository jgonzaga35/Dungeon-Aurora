package main.java.dungeonmania.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;
import dungeonmania.util.Direction;

public class RandomMovementBehaviour implements Movement {
    private Random rng = new Random();
    private Map<Integer, Direction> directionMap = new HashMap<Integer, Direction>();
    private DungeonMap map;
    private Cell currentCell;

    RandomMovementBehaviour(DungeonMap map, Cell initialCell)
    {
        this.map = map;
        this.currentCell = initialCell;
        this.directionMap.put(0, Direction.UP);
        this.directionMap.put(1, Direction.DOWN);
        this.directionMap.put(2, Direction.LEFT);
        this.directionMap.put(3, Direction.RIGHT);
    }

    public Cell move()
    {
        int direction = rng.nextInt(4);

        currentCell = map.getCellAround(currentCell, direction);

        return currentCell;
    }

    public Cell getCurrentPosition()
    {
        return currentCell;
    }
}
