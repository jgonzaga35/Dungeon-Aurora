package dungeonmania.movement;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;

public class FollowMovementBehaviour extends MovementBehaviour {
    private DungeonMap map;

    public FollowMovementBehaviour(int precedence, DungeonMap map, Cell initialCell)
    {
        super(precedence, initialCell);
        this.map = map;
    }

    public Cell move()
    {
        Cell nextCell = map.getNeighbors(getCurrentCell()).stream()
            .filter(cell -> !cell.isBlocking())
            .min(
                (c1, c2) -> 
                Integer.compare(c1.getPlayerDistance(), c2.getPlayerDistance())
            ).get();
        assert nextCell != null;

        setCurrentCell(nextCell);
        
        return nextCell;
    }
}
