package dungeonmania.movement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;
import dungeonmania.util.Direction;

public class RandomMovementBehaviour implements Movement {
    private Random rng = new Random();
    private List<Direction> directions = new ArrayList<Direction>(Arrays.asList(
        Direction.LEFT,
        Direction.RIGHT,
        Direction.UP,
        Direction.DOWN
    ));
    private Map<Integer, Direction> directionMap = new HashMap<Integer, Direction>();
    private DungeonMap map;
    private Cell currentCell;

    public RandomMovementBehaviour(DungeonMap map, Cell initialCell)
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
        List<Direction> modifiedDirections = new ArrayList<Direction>(directions);
        
        // Remove invalid movement directions
        if (currentCell.getPosition().getX() == 0)
        {
            modifiedDirections.remove(Direction.LEFT);
        }
        else if (map.getCellAround(currentCell, Direction.LEFT).isBlocking())
        {
            modifiedDirections.remove(Direction.LEFT);
        }

        if (currentCell.getPosition().getX() == map.getWidth() - 1)
        {
            modifiedDirections.remove(Direction.RIGHT);
        }
        else if (map.getCellAround(currentCell, Direction.RIGHT).isBlocking())
        {
            modifiedDirections.remove(Direction.RIGHT);
        }

        if (currentCell.getPosition().getY() == 0)
        {
            modifiedDirections.remove(Direction.UP);
        }
        else if (map.getCellAround(currentCell, Direction.UP).isBlocking())
        {
            modifiedDirections.remove(Direction.UP);
        }

        if (currentCell.getPosition().getY() == map.getHeight() - 1)
        {
            modifiedDirections.remove(Direction.DOWN);
        }
        else if (map.getCellAround(currentCell, Direction.DOWN).isBlocking())
        {
            modifiedDirections.remove(Direction.DOWN);
        }

        int direction = rng.nextInt(modifiedDirections.size());
        
        currentCell = map.getCellAround(currentCell, modifiedDirections.get(direction));

        return currentCell;
    }

    public Cell getCurrentPosition()
    {
        return currentCell;
    }
}
