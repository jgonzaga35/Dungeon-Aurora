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
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Dungeon {
    private String id;
    private DungeonMap dungeonMap;
    private GameMode mode;
    private Goals goals;
    private Player player;
    private String name;

    public static int nextDungeonId = 1;

    public Dungeon(String name, GameMode mode, DungeonMap dungeonMap, Goals goals) {
        this.name = name;
        this.mode = mode;
        this.dungeonMap = dungeonMap;
        this.goals = goals;
        this.id = "dungeon-" + Dungeon.nextDungeonId;
        this.player = null;

        Dungeon.nextDungeonId++;
    }

    /**
     * Creates a Dungeon instance from the JSON file's content
     */
    public static Dungeon fromJSONObject(String name, GameMode mode, JSONObject obj) {
        Goals goals = Goals.fromJSONObject(obj);

        DungeonMap map = new DungeonMap(obj);

        List<List<Cell>> dungeonMap = new ArrayList<>(); // a list of rows
        Dungeon dungeon = new Dungeon(name, mode, map, goals);

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
                cell.addOccupant(new Wall(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Exit.STRING_TYPE)) {
                cell.addOccupant(new Exit(dungeon, cell.getPosition()));
            } else if (Objects.equals(type, Player.STRING_TYPE)) {
                player = new Player(dungeon, cell.getPosition());
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

    public String getGoalsAsString() {
        return this.goals.asString();
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

        for (int y = 0; y < this.dungeonMap.getHeight(); y++) {
            for (int x = 0; x < this.dungeonMap.getWidth(); x++) {
                Cell cell = this.dungeonMap.getCell(x, y);
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
            return this.dungeonMap.getCell(pos.getX(), pos.getY() - 1);
        } else if (d == Direction.DOWN) {
            if (pos.getY() == this.dungeonMap.getHeight() - 1) {
                return null;
            }
            return this.dungeonMap.getCell(pos.getX(), pos.getY() + 1);
        } else if (d == Direction.LEFT) {
            if (pos.getX() == 0) {
                return null;
            }
            return this.dungeonMap.getCell(pos.getX() - 1, pos.getY());
        } else if (d == Direction.RIGHT) {
            if (pos.getX() == this.dungeonMap.getWidth() - 1) {
                return null;
            }
            return this.dungeonMap.getCell(pos.getX() + 1, pos.getY());
        } else {
            throw new Error("unexpected direction: " + d);
        }
    }

    public DungeonMap getMap() {
        return dungeonMap;
    }
}
