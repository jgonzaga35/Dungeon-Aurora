package dungeonmania.movement;

import java.util.stream.Stream;

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

        Stream<Cell> neighbors = map.getNeighbors(currentCell).stream();
        if (neighbors.anyMatch(c -> c.getPlayerDistance() == 1)) {
            // Stay one away if possible.
            nextCell = map.getNeighbors(currentCell).stream()
                .filter(c -> c.getPlayerDistance() == 1)
                .findAny().get();
        } else {
            // else get as close as possible
            nextCell = map.getNeighbors(currentCell).stream().min(
                    (c1, c2) -> 
                    Integer.compare(c1.getPlayerDistance(), c2.getPlayerDistance())
                ).get();
        }

        setCurrentCell(nextCell);
        
        return nextCell;
    }
    
}
