package dungeonmania.movement;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;
import dungeonmania.Utils;

public class RandomMovementBehaviour extends MovementBehaviour {
    private DungeonMap map;

    public RandomMovementBehaviour(int precedence, DungeonMap map, Cell initialCell)
    {
        super(precedence, initialCell);
        this.map = map;
    }

    public Cell move() {
        Cell nextCell;
        List<Cell> availableCells = this.map.getCellsAround(getCurrentCell())
            .filter(cell -> !cell.isBlocking())
            .collect(Collectors.toList());
            nextCell = Utils.choose(availableCells);
            setCurrentCell(nextCell);

        return nextCell;
    }
}
