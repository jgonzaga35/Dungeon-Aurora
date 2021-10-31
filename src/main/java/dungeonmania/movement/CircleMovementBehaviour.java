package dungeonmania.movement;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Cell;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;

public class CircleMovementBehaviour extends MovementBehaviour {
    private DungeonMap map;

    public DungeonMap getMap() {
        return this.map;
    }
    private List<Pos2d> movementCycle = new ArrayList<>();
    private int step = 0;
    private int direction = 1;

    public CircleMovementBehaviour(int precedence, DungeonMap map, Cell initialCell)
    {
        super(precedence, initialCell);
        this.map = map;
        int initialX = initialCell.getPosition().getX();
        int initialY = initialCell.getPosition().getY();

        this.movementCycle.add(new Pos2d(
            initialX, 
            initialY - 1
        ));
        this.movementCycle.add(new Pos2d(
            initialX + 1, 
            initialY - 1
        ));
        this.movementCycle.add(new Pos2d(
            initialX + 1, 
            initialY
        ));
        this.movementCycle.add(new Pos2d(
            initialX + 1, 
            initialY + 1
        ));
        this.movementCycle.add(new Pos2d(
            initialX, 
            initialY + 1
        ));
        this.movementCycle.add(new Pos2d(
            initialX - 1, 
            initialY + 1
        ));
        this.movementCycle.add(new Pos2d(
            initialX - 1, 
            initialY
        ));
        this.movementCycle.add(new Pos2d(
            initialX - 1, 
            initialY - 1
        ));
    }

    public Cell move()
    {
        System.out.println("Step: " + step);
        System.out.println("Direction: " + direction);
        
        Cell nextCell = map.getCell(movementCycle.get(step));
        if (nextCell.hasBoulder())
        {
            direction *= -1;
            step += direction;

            // Checking this here covers edge cases.
            if (step > 7) step = 0;
            if (step < 0) step = 7;

            System.out.println("Step2: " + step);
            System.out.println("Direction2: " + direction);
            nextCell = map.getCell(movementCycle.get(step));
        }
        step += direction;

        if (step > 7) step = 0; 
        if (step < 0) step = 7; 

        setCurrentCell(nextCell);

        return nextCell;
    }
    
}
