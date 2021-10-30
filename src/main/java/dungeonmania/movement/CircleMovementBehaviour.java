package dungeonmania.movement;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;

public class CircleMovementBehaviour extends MovementBehaviour {
    private DungeonMap map;

    public DungeonMap getMap() {
        return this.map;
    }

    private Cell currentCell;

    public CircleMovementBehaviour(int precedence, DungeonMap map, Cell initialCell)
    {
        super(precedence);
        this.map = map;
        this.currentCell = initialCell;
    }

    public Cell move()
    {
        return new Cell(new Pos2d(0, 0));
    }

    public Cell getCurrentCell()
    {
        return currentCell;
    }
    
}
