package dungeonmania.movement;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Utils;

/**
 * Represents a random movement behaviour that follows no pattern.
 */
public class RandomMovementBehaviour extends MovementBehaviour {
    private Dungeon dungeon;

    public RandomMovementBehaviour(int precedence, Dungeon dungeon, Cell initialCell)
    {
        super(precedence, initialCell);
        this.dungeon = dungeon;
    }

    public Cell move() {
        Cell nextCell;
        List<Cell> availableCells = this.dungeon.getMap().getCellsAround(getCurrentCell())
            .filter(cell -> !cell.isBlocking())
            .collect(Collectors.toList());

        if (availableCells.size() == 0) {
            return getCurrentCell();
        }

        nextCell = Utils.choose(availableCells, this.dungeon.getRandom());
        setCurrentCell(nextCell);

        return nextCell;
    }
}
