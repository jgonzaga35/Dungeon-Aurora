package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.util.Direction;



public class Spider extends MovingEntity {
    public static final String STRING_TYPE = "spider";
    private List<Cell> movementMap = new ArrayList();
    private int currentMovementStage = 0;
    private int direction = 1;

    public Spider(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        moveUp(dungeon);

    }

    private void moveUp(Dungeon dungeon) {
        Cell up = dungeon.getCellAround(super.getCell(), Direction.UP);
        super.moveTo(up);
    }

    private void makeSpiderMap() {
        Cell current = super.getCell();
        movementMap.add(current);
        current = dungeon.getCellAround(current, Direction.RIGHT);
        movementMap.add(current);
        for (int i = 0; i < 2; i++) {
            current = dungeon.getCellAround(current, Direction.DOWN);
            movementMap.add(current);
        }
        for (int i = 0; i < 2; i++) {
            current = dungeon.getCellAround(current, Direction.LEFT);
            movementMap.add(current);
        }
        for (int i = 0; i < 2; i++) {
            current = dungeon.getCellAround(current, Direction.UP);
            movementMap.add(current);
        }
    }

    @Override
    public void tick() {
        // move in a random direction
        currentMovementStage += direction;
        stayInBound();
        super.moveTo(movementMap.get(currentMovementStage));
    }

    private void stayInBound() {
        if (currentMovementStage > 7) {
            currentMovementStage = 0;
        }
        if (currentMovementStage < 0) {
            currentMovementStage = 7;
        }
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }
}
