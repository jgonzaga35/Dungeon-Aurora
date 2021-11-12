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

        List<Cell> neighbours = map.getNeighbors(currentCell).stream()
            .filter(c -> !c.isBlocking()).collect(Collectors.toList());

        if (neighbours.stream().anyMatch(c -> c.getPlayerDistance() == 1)) {
            // Stay one away if possible.
            nextCell = neighbours.stream()
                .filter(c -> c.getPlayerDistance() == 1)
                .findAny().get();
        } else {
            // else get as close as possible
            nextCell = neighbours.stream().min(
                    (c1, c2) -> 
                    Integer.compare(c1.getPlayerDistance(), c2.getPlayerDistance())
                ).get();
        }

        setCurrentCell(nextCell);
        
        return nextCell;
    }
    
}
