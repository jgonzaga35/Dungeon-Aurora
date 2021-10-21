package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.statics.Exit;
import dungeonmania.entities.statics.Wall;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goal.Goal;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Dungeon {
    private String id;
    private List<List<Cell>> dungeonMap;
    private GameMode mode;
    private Goal goal;
    private Player player;
    private String name;

    public static int nextDungeonId = 1;

    public Dungeon(String name, GameMode mode, List<List<Cell>> dungeonMap, Goal goal) {
        this.name = name;
        this.mode = mode;
        this.dungeonMap = dungeonMap;
        this.goal = goal;
        this.id = "dungeon-" + Dungeon.nextDungeonId;
        this.player = null;

        Dungeon.nextDungeonId++;
    }

    /**
     * Creates a Dungeon instance from the JSON file's content
     */
    public static Dungeon fromJSONObject(String name, GameMode mode, JSONObject obj) {
        
        Goal goal = Goal.fromJSONObject(obj);

        List<List<Cell>> dungeonMap = new ArrayList<>(); // a list of rows
        Dungeon dungeon = new Dungeon(name, mode, dungeonMap, goal);

        int width = obj.getInt("width");
        int height = obj.getInt("height");

        // a grid of empty cells
        for (int y = 0; y < height; y++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                row.add(new Cell(dungeon, new Pos2d(x, y)));
            }
            dungeonMap.add(row);
        }

        JSONArray entities = obj.getJSONArray("entities");
        Player player = null;

        for (int i = 0; i < entities.length(); i++) {
            JSONObject entity = entities.getJSONObject(i);
            int x = entity.getInt("x");
            int y = entity.getInt("y");
            String type = entity.getString("type");

            // TODO: probably need a builder pattern here
            // for now, i just handle walls and player
            Cell cell = dungeonMap.get(y).get(x);
            if (Objects.equals(type, Wall.STRING_TYPE)) {
                cell.addOccupant(new Wall(cell));
            } else if (Objects.equals(type, Exit.STRING_TYPE)) {
                cell.addOccupant(new Exit(cell));
            } else if (Objects.equals(type, Player.STRING_TYPE)) {
                player = new Player(cell);
                cell.addOccupant(player);
            } else {
                throw new Error("unhandled entity type: " + type);
            }
        }

        if (player == null) {
            throw new Error("the player's position wasn't specified");
        }

        dungeon.setPlayer(player);
        return dungeon;
    }

    public void tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {
        // for now, ignore item used
        // update every entity

        this.player.handleMoveOrder(movementDirection);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Goal getGoal() {
        return this.goal;
    }

    public String getGoalAsString() {
        return this.goal.asString();
    }

    /**
     * this should only be called exactly once, after the dungeon has been
     * constructed.
     * 
     * This method is just there so the Dungeon constructor doesn't have to look
     * for the player in the entire map
     * @param player
     */
    private void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Create an entity response object for each entity in the dungeon
     * @return list of entity responses
     */
    public List<EntityResponse> getEntitiesResponse() {
        ArrayList<EntityResponse> entities = new ArrayList<>();

        for (int y = 0; y < this.dungeonMap.size(); y++) {
            for (int x = 0; x < this.dungeonMap.get(0).size(); x++) {
                Cell cell = this.dungeonMap.get(y).get(x);
                for (Entity entity : cell.getOccupants()) {
                    entities.add(new EntityResponse(
                        entity.getId(),
                        entity.getTypeAsString(),
                        new Position(x, y, entity.getLayerLevel().getValue()),
                        entity.isInteractable()
                    ));
                }
            }
        }
        return entities;
    }

    public GameMode getGameMode() {
        return this.mode;
    }

    /**
     * Direction shouldn't Direction.NONE
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
            return this.dungeonMap.get(pos.getY() - 1).get(pos.getX());
        } else if (d == Direction.DOWN) {
            if (pos.getY() == this.dungeonMap.size() - 1) {
                return null;
            }
            return this.dungeonMap.get(pos.getY() + 1).get(pos.getX());
        } else if (d == Direction.LEFT) {
            if (pos.getX() == 0) {
                return null;
            }
            return this.dungeonMap.get(pos.getY()).get(pos.getX() - 1);
        } else if (d == Direction.RIGHT) {
            if (pos.getX() == this.dungeonMap.get(0).size() - 1) {
                return null;
            }
            return this.dungeonMap.get(pos.getY()).get(pos.getX() + 1);
        } else {
            throw new Error("unexpected direction: " + d);
        }
    }
}
