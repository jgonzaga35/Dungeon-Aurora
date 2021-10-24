package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;
import dungeonmania.util.Direction;

public class Spider extends MovingEntity {
    public static final String STRING_TYPE = "spider";
    private List<Cell> movementMap = new ArrayList();
    private int currentMovementStage = 0;
    private int direction = 1;
    private boolean hasMoved = false;

    public Spider(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        makeSpiderMap(dungeon.getMap());
    }

    private void makeSpiderMap(DungeonMap dungeonMap) {
        Cell current = dungeonMap.getCellAround(super.getCell(), Direction.UP);
        movementMap.add(current);
        current = dungeonMap.getCellAround(current, Direction.RIGHT);
        movementMap.add(current);
        for (int i = 0; i < 2; i++) {
            current = dungeonMap.getCellAround(current, Direction.DOWN);
            movementMap.add(current);
        }
        for (int i = 0; i < 2; i++) {
            current = dungeonMap.getCellAround(current, Direction.LEFT);
            movementMap.add(current);
        }
        for (int i = 0; i < 2; i++) {
            current = dungeonMap.getCellAround(current, Direction.UP);
            movementMap.add(current);
        }
    }

    private void spiderMove() {
        for (int i = 0; i < 2; i++) {
            currentMovementStage += direction;
            stayInBound();
            if (movementMap.get(currentMovementStage).hasBoulder()) {
                System.out.println("boulder, change direciton");
                direction = direction * -1;
                currentMovementStage += direction;
                stayInBound();
            } else {
                super.moveTo(movementMap.get(currentMovementStage));
                System.out.println("successfully moved");
                return;
            }
        }
        
        System.out.println("trapped between two boulders, no movement");
    }

    @Override
    public void tick() {
        // move in a random direction
        
        System.out.println("ticking the spider");

        if (hasMoved) {
            spiderMove();
        } else {
            if (!movementMap.get(0).hasBoulder()) {
                System.out.println("spider has moved up");
                super.moveTo(movementMap.get(0));
                hasMoved = true;
            }
        }
        

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
