package dungeonmania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.EntityResponse;

public class Dungeon {
    private List<List<Cell>> dungeonMap;
    private GameMode mode;
    private Goals goals;
    private Pos2d playerPosition;
    private String name;

    public Dungeon(String name, GameMode mode, List<List<Cell>> dungeonMap, Goals goals, Pos2d playerPosition) {
        this.name = name;
        this.mode = mode;
        this.dungeonMap = dungeonMap;
        this.goals = goals;
        this.playerPosition = playerPosition;
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
        Pos2d playerPosition = null;

        for (int i = 0; i < entities.length(); i++) {
            JSONObject entity = entities.getJSONObject(i);
            int x = entity.getInt("x");
            int y = entity.getInt("y");
            String type = entity.getString("type");

            // TODO: probably need a builder pattern here
            // for now, i just handle walls and player
            if (Arrays.asList("wall", "exit").contains(type)) {
                dungeonMap.get(y).get(x).setStaticEntityType(type);
            } else if (Objects.equals(type, "player")) {
                playerPosition = new Pos2d(x, y);
            } else {
                throw new Error("unhandled entity type: " + type);
            }
        }

        if (playerPosition == null) {
            throw new Error("the player's position wasn't specified");
        }

        return new Dungeon(name, mode, dungeonMap, goals, playerPosition);
    }

    public String getId() {
        return "not specified";
    }

    public String getName() {
        return this.name;
    }

    public String getGoalsAsString() {
        return this.goals.asString();
    }

    public List<EntityResponse> getEntitiesResponse() {
        ArrayList<EntityResponse> entities = new ArrayList<>();
        return entities;
    }

}
