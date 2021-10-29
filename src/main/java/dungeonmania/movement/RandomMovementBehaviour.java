package dungeonmania.movement;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;
import dungeonmania.Utils;

public class RandomMovementBehaviour implements Movement {
    private DungeonMap map;
    private Cell currentCell;

    public RandomMovementBehaviour(DungeonMap map, Cell initialCell)
    {
        this.map = map;
        this.currentCell = initialCell;
    }

    public Cell move() {
        List<Cell> availableCells = this.map.getCellsAround(this.currentCell)
            .filter(cell -> !cell.isBlocking())
            .collect(Collectors.toList());
        this.currentCell = Utils.choose(availableCells);
        return this.currentCell;
    }

    public Cell getCurrentCell()
    {
        return currentCell;
    }
}
