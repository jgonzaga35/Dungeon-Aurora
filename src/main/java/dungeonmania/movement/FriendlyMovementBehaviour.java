package dungeonmania.movement;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;

public class FriendlyMovementBehaviour extends MovementBehaviour {
    private DungeonMap map;

    public FriendlyMovementBehaviour(int precedence, DungeonMap map, Cell initialCell) {
        super(precedence, initialCell);
        this.map = map;
    }

    @Override
    public Cell move() {
        // remain at the players side if possible.
        Cell currentCell = getCurrentCell();
        Cell nextCell;
        if (currentCell.getPlayerDistance() == 1) return currentCell;
        if (currentCell.getPlayerDistance() == 0) {
            // move one cell away.
            return map.getNeighbors(currentCell).stream()
                .filter(c -> !c.isBlocking() && c.getPlayerDistance() == 1)
                .findAny().get();
        }

        // Get second cell of the path
        if (map.findPath(getCurrentCell(), map.getPlayerCell()) != null) 
            nextCell = map.findPath(getCurrentCell(), map.getPlayerCell()).get(1);
        else 
            nextCell = map.getNeighbors(getCurrentCell()).stream()
                .filter(cell -> !cell.isBlocking())
                .min(
                    (c1, c2) -> 
                    Integer.compare(c1.getTravelCost(), c2.getTravelCost())
                ).get();

        assert nextCell != null;

        setCurrentCell(nextCell);

        return nextCell;
    }
    
}
