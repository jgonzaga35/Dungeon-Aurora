package dungeonmania.movement;

import java.util.List;

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
        List<Cell> path = map.findPath(getCurrentCell(), map.getPlayerCell());
        if (path != null) {
            if (path.size() == 1) next = getCurrentCell();
            else next = map.findPath(getCurrentCell(), map.getPlayerCell()).get(1);
        } else {
            next = map.getNeighbors(getCurrentCell()).stream()
                .filter(cell -> !cell.isBlocking())
                .min(
                    (c1, c2) -> 
                    Integer.compare(c1.getTravelCost(), c2.getTravelCost())
                ).get();
        }

        assert next != null;

        setCurrentCell(next);

        return next;
    }
}
