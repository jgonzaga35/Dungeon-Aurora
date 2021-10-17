package dungeonmania;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.json.JSONObject;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class DungeonManiaController {
    private Dungeon dungeon;

    public enum GameMode {
        STANDARD, PEACEFUL, HARD
    }

    /**
     * Standard z values. To get the integer value, call Layers.STATIC.getValue()
     * for example
     * 
     * https://stackoverflow.com/a/3990421/6164984
     */
    public enum Layers {
        STATIC(1), PLAYER(100);

        private final int value;

        Layers(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    public DungeonManiaController() {
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("Standard", "Peaceful", "Hard");
    }

    /**
     * /dungeons
     * 
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DungeonResponse newGame(String dungeonName, String gameModeString) throws IllegalArgumentException {
        GameMode gameMode = this.parseGameMode(gameModeString);
        String content;

        try {
            content = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not load dungeon map: " + e.getMessage());
        }

        this.dungeon = Dungeon.fromJSONObject(dungeonName, gameMode, new JSONObject(content));

        return this.makeDungeonResponse();
    }

    private GameMode parseGameMode(String gameMode) throws IllegalArgumentException {
        if (Objects.equals(gameMode, "Standard"))
            return GameMode.STANDARD;
        if (Objects.equals(gameMode, "Hard"))
            return GameMode.HARD;
        if (Objects.equals(gameMode, "Peaceful"))
            return GameMode.PEACEFUL;
        throw new IllegalArgumentException(String.format("Game mode %s is invalid", gameMode));
    }

    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        return null;
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        return null;
    }

    public List<String> allGames() {
        return new ArrayList<>();
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    private DungeonResponse makeDungeonResponse() {
        return new DungeonResponse(
            this.dungeon.getId(),
            this.dungeon.getName(),
            this.dungeon.getEntitiesResponse(),
            new ArrayList<ItemResponse>(),
            new ArrayList<String>(),
            this.dungeon.getGoalsAsString(),
            new ArrayList<AnimationQueue>()
        );
    }
}