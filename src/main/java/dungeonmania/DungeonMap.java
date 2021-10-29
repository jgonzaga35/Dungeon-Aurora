package dungeonmania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.StaticEntity;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.statics.Wall;
import dungeonmania.util.Direction;

import java.lang.System;

public class DungeonMap {

    final private String PLAYER = " P ";
    final private String WALL = " # ";
    final private String STATIC = " S ";
    final private String ENEMY = " E ";

    private List<List<Cell>> dungeonMap = new ArrayList<>();
    private int width;
    private int height;
    
    DungeonMap(JSONObject json) {
        this.width = json.getInt("width");
        this.height = json.getInt("height");
        
        // a grid of empty cells
        for (int y = 0; y < height; y++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                row.add(new Cell(new Pos2d(x, y)));
            }
            dungeonMap.add(row);
        }
        resetDistances();
    }

    /**
     * Sets all player distances to the maximum and sets the cell with the player to 0.
     */
    private void resetDistances() {
        for (List<Cell> row : dungeonMap) {
            for (Cell cell : row) {
                if (cell.getOccupants().stream().anyMatch(e -> e instanceof Player)) {
                    cell.setPlayerDistance(0);
                } else {
                    cell.setPlayerDistance(width * height);
                }
            }
        }
    }
    
    public Cell getCell(Pos2d pos) {
        return dungeonMap.get(pos.getY()).get(pos.getX());
    }

    public List<Entity> allEntities() {
        List<Entity> all = new ArrayList<Entity>();
        for (List<Cell> row : dungeonMap) {
            for (Cell cell : row) {
                all.addAll(cell.getOccupants());
            }
        }
        return all;
    }

    public Cell getCell(int x, int y) {
        return dungeonMap.get(y).get(x);
    }

    /**
     * Retreives Cell Player is Currently In
     * @return Cell
     */
    public Cell getPlayerCell() {
        for (List<Cell> row : dungeonMap) {
            System.out.println("Check that rows working");
            for (Cell cell : row) {
                System.out.println("Check that cells working");
                //Checks Where Cell is 0 blocks from player
                System.out.println(cell.getPlayerDistance());
                if (cell.getPlayerDistance() == 0) {
                    return cell;
                }
            }
        }
        return null;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
    
    /**
     * Fills in all the distances from the player for all the cells. Calls 
     * setDistances() initially to reset values.
     */
    public void flood() {
        resetDistances();

        int explorationLevel = 0;
        int valuesChanged = 1;

        while (valuesChanged != 0) {
            valuesChanged = 0;

            // Look for cells with the current explorationLevel
            for (List<Cell> row : dungeonMap) {
                for (Cell cell : row) {
                    if (cell.getPlayerDistance() == explorationLevel) {
                        valuesChanged += this.propagateFrom(cell);
                    }
                }
            }

            explorationLevel++;
        }
    }

    /**
     * Direction.NONE returns the given cell
     * 
     * @param cell
     * @param d
     * @return the cell above, below, left or right of cell, depending on direction
     */
    public Cell getCellAround(Cell cell, Direction d) {
        Pos2d pos = cell.getPosition();
        if (d == Direction.UP) {
            if (pos.getY() == 0) {
                return null;
            }
            return getCell(pos.getX(), pos.getY() - 1);
        } else if (d == Direction.DOWN) {
            if (pos.getY() == getHeight() - 1) {
                return null;
            }
            return getCell(pos.getX(), pos.getY() + 1);
        } else if (d == Direction.LEFT) {
            if (pos.getX() == 0) {
                return null;
            }
            return getCell(pos.getX() - 1, pos.getY());
        } else if (d == Direction.RIGHT) {
            if (pos.getX() == getWidth() - 1) {
                return null;
            }
            return getCell(pos.getX() + 1, pos.getY());
        } else {
            return cell;
        }
    }

    public Cell getCellAround(Pos2d position, Direction d) {
        Cell cell = getCell(position);
        return getCellAround(cell, d);
    }

    private int propagateFrom(Cell cell) {
        AtomicInteger changesMade = new AtomicInteger(0);

        Arrays.stream(Direction.values()).forEach(d -> {
            Cell neighbor = getCellAround(cell, d);
            if (neighbor != null) {
                if (!neighbor.isBlocking() && neighbor.getPlayerDistance() == width * height) {
                    changesMade.incrementAndGet();
                    neighbor.setPlayerDistance(cell.getPlayerDistance() + 1);
                }
            }
        });

        return changesMade.get();
    }

    @Override
    public String toString() {
        String result = "";
        for (List<Cell> row : dungeonMap) {
            for (Cell cell : row) {
                if (cell.getOccupants().stream().anyMatch(e -> e instanceof Player)) {
                    result += PLAYER;
                } else if (cell.getOccupants().stream().anyMatch(e -> e instanceof Wall)) {
                    result += WALL;
                } else if (cell.getOccupants().stream().anyMatch(e -> e instanceof MovingEntity)) {
                    result += ENEMY;
                } else if (cell.getOccupants().stream().anyMatch(e -> e instanceof StaticEntity)) {
                    result += STATIC;
                } else {
                    result += " " + cell.getPlayerDistance() + " ";
                }
            }
            result += "\n";
        }

        return result;
    }
}
