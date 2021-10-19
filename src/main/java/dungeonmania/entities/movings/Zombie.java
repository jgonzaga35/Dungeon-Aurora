package dungeonmania.entities.movings;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Cell;
import dungeonmania.Utils;
import dungeonmania.entities.MovingEntity;

public class Zombie extends MovingEntity {

    public static final String STRING_TYPE = "zombie";

    public Zombie(Cell cell) {
        super(cell);
    }

    @Override
    public String getTypeAsString() {
        return Zombie.STRING_TYPE;
    }

    @Override
    public void tick() {
        // move in a random direction
        List<Cell> availableCells = this.getCellsAround()
            .filter(cell -> !cell.isBlocking())
            .collect(Collectors.toList());

        if (availableCells.size() == 0)
            return; // don't move anywhere

        Cell cell = Utils.choose(availableCells);
        this.moveTo(cell);
    }
}
