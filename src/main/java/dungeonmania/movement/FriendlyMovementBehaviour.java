package dungeonmania.movement;

import java.util.stream.Stream;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;

public class FriendlyMovementBehaviour extends MovementBehaviour {
    private DungeonMap map;
    private Cell currentCell;

    public FriendlyMovementBehaviour(int precedence, DungeonMap map, Cell initialCell) {
        super(precedence);
        this.map = map;
        this.currentCell = initialCell;
    }

    @Override
    public Cell move() {
        // remain at the players side if possible.
        if (currentCell.getPlayerDistance() == 1) return currentCell;

        Stream<Cell> neighbors = map.getNeighbors(currentCell).stream();
        if (neighbors.anyMatch(c -> c.getPlayerDistance() == 1)) {
            // Stay one away if possible.
            currentCell = map.getNeighbors(currentCell).stream()
                .filter(c -> c.getPlayerDistance() == 1)
                .findAny().get();
        } else {
            // else get as close as possible
            currentCell = map.getNeighbors(currentCell).stream().min(
                    (c1, c2) -> 
                    Integer.compare(c1.getPlayerDistance(), c2.getPlayerDistance())
                ).get();
        }
        
        return currentCell;
    }

    @Override
    public Cell getCurrentCell() {
        return currentCell;
    }
    
}
