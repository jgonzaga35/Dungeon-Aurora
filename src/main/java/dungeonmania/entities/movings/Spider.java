package dungeonmania.entities.movings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;
import dungeonmania.entities.MovingEntity;
import dungeonmania.util.Direction;

public class Spider extends MovingEntity {
    public static final String STRING_TYPE = "spider";
    private List<Cell> movementMap = new ArrayList();
    private int currentMovementStage = 0;
    private int direction = 1;
    private boolean hasMoved = false;
    
    public static final int MAX_SPIDERS = 5;

    /**
     * A Factory method that makes a new instance of Spider 
     * at a random coordinate.
     * @param dungeon
     * @return
     */
    public static Spider spawnSpider(Dungeon dungeon) {
        Cell cell = randomPosition(dungeon);
        if (cell != null) {
            Spider spider = new Spider(dungeon, cell.getPosition());
            cell.addOccupant(spider);
            return spider;
        }
        return null;
    }

    /**
     * Generates a random location for the Spider to spawn.
     * @param dungeon
     * @return
     */
    private static Cell randomPosition(Dungeon dungeon) {
        DungeonMap dungeonMap = dungeon.getMap();
        int width = dungeonMap.getWidth();
        int height = dungeonMap.getHeight();

        for (int i = 0; i < width * height; i++) {
            // For a map with i cells, loop i times
            Random random = new Random();
            int x = random.nextInt(width - 2) + 1;
            int y = random.nextInt(height - 2) + 1;
            Pos2d spawn = new Pos2d(x, y);

            if (!dungeonMap.getCell(spawn).hasBoulder()) {
                return dungeonMap.getCell(spawn);
            }
        }

        // Return null if no suitable location is found.
        return null;
    }

    public Spider(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        makeSpiderMap(dungeon.getMap());
    }

    /**
     * Stores the cells around a spider's spawn in an array
     * @param dungeonMap
     */
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

    /**
     * The spider moves in the circular pattern
     */
    private void spiderMove() {
        for (int i = 0; i < 2; i++) {
            // A spider should only be able to change direction twice
            // per move/tick. Eg. if a spider is caught between two boulders
            // it would not infinitely loop.
            currentMovementStage += direction;
            stayInBound();
            if (movementMap.get(currentMovementStage).hasBoulder()) {
                // Boulder in path, change direction
                direction = direction * -1;
                currentMovementStage += direction;
                stayInBound();
            } else {
                super.moveTo(movementMap.get(currentMovementStage));
                return;
            }
        }
    }

    /**
     * Helper method, ensures that currtentMovementStage is in s
     * the range [0,7].
     */
    private void stayInBound() {
        if (currentMovementStage > 7) currentMovementStage = 0;
        if (currentMovementStage < 0) currentMovementStage = 7;
    }

    @Override
    public void tick() {
        if (hasMoved) {
            spiderMove();
        } else {
            if (!movementMap.get(0).hasBoulder()) {
                super.moveTo(movementMap.get(0));
                hasMoved = true;
            }
        }
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }
}
