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
    private List<List<Cell>> dungeonMap;
    private GameMode mode;
    private Goals goals;
    private Player player;
    private String name;

    public Dungeon(String name, GameMode mode, List<List<Cell>> dungeonMap, Goals goals, Player player) {
        this.name = name;
        this.mode = mode;
        this.dungeonMap = dungeonMap;
        this.goals = goals;
        this.player = player;
    }

    /**
     * Returns a Dungeon instance from a file the json files in resources
     */
    public static Dungeon fromJSONObject(String name, GameMode mode, JSONObject obj) {
        Goals goals = Goals.fromJSONObject(obj);

        List<List<Cell>> dungeonMap = new ArrayList<>(); // a list of rows

        int width = obj.getInt("width");
        int height = obj.getInt("height");

        // a grid of empty cells
        for (int y = 0; y < height; y++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                row.add(new Cell());
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
            if (Objects.equals(type, "wall")) {
                dungeonMap.get(y).get(x).addOccupant(new Wall());
            } else if (Objects.equals(type, "exit")) {
                dungeonMap.get(y).get(x).addOccupant(new Exit());
            } else if (Objects.equals(type, "player")) {
                player = new Player(new Pos2d(x, y));
                dungeonMap.get(y).get(x).addOccupant(player);
            } else {
                throw new Error("unhandled entity type: " + type);
            }
        }

        if (player == null) {
            throw new Error("the player's position wasn't specified");
        }

        return new Dungeon(name, mode, dungeonMap, goals, player);
    }

    public void tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {
        // for now, ignore item used
        // update every entity

        for (int y = 0; y < this.dungeonMap.size(); y++) {
            for (int x = 0; x < this.dungeonMap.get(0).size(); x++) {
                Cell cell = this.dungeonMap.get(y).get(x);
                for (Entity e: cell.getOccupants()) {
                    // e.tick();
                }
            }
        }

        this.player.move(this.dungeonMap, movementDirection);
    }

    public String getId() {
        return "not implemented";
    }

    public String getName() {
        return this.name;
    }

    public String getGoalsAsString() {
        return this.goals.asString();
    }

    public List<EntityResponse> getEntitiesResponse() {
        ArrayList<EntityResponse> entities = new ArrayList<>();

        int y = 0;
        for (List<Cell> row : this.dungeonMap) {
            int x = 0;
            for (Cell cell: row) {
                for (Entity entity : cell.getOccupants()) {
                    entities.add(new EntityResponse(
                        entity.getId(),
                        entity.getTypeAsString(),
                        new Position(x, y, entity.getLayerLevel().getValue()),
                        entity.isInteractable()
                    ));
                }
                x++;
            }
            y++;
        }
        return entities;
    }
}
