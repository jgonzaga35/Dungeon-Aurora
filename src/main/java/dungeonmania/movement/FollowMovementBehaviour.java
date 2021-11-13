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
        // Get second cell of the path
        Cell next;
        if (map.findPath(getCurrentCell(), map.getPlayerCell()) != null) 
            next = map.findPath(getCurrentCell(), map.getPlayerCell()).get(1);
        else 
            next = map.getNeighbors(getCurrentCell()).stream()
                .filter(cell -> !cell.isBlocking())
                .min(
                    (c1, c2) -> 
                    Integer.compare(c1.getTravelCost(), c2.getTravelCost())
                ).get();

        assert next != null;

        setCurrentCell(next);

        return next;
    }
}
