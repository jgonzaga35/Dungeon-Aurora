package dungeonmania;

import java.io.IOException;
import java.util.*;
import java.util.stream.*;

import org.json.JSONObject;

import dungeonmania.entities.movings.Mercenary;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class DungeonManiaController {
    private Dungeon dungeon;
    private Map<String, Dungeon> savedGames = new HashMap<>();

    /**
     * Standard z values. To get the integer value, call Layers.STATIC.getValue()
     * for example
     * 
     * https://stackoverflow.com/a/3990421/6164984
     */
    public enum LayerLevel {
        STATIC(1), COLLECTABLE(25), MOVING_ENTITY(50), PLAYER(100);

        private final int value;

        LayerLevel(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    public enum GameMode {
        STANDARD("standard"), PEACEFUL("peaceful"), HARD("hard");

        private final String value;

        GameMode(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    public DungeonManiaController() {
        
    }

    public DungeonManiaController(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Stream.of(GameMode.values()).map(mode -> mode.getValue()).collect(Collectors.toList());
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

    /**
     * Creates a new game
     * @param dungeonName
     * @param gameModeString
     * @return
     * @throws IllegalArgumentException
     */
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

    /**
     * Convert string mode to enum modes so we don't make typos
     * @param gameMode
     * @return
     * @throws IllegalArgumentException
     */
    private GameMode parseGameMode(String gameMode) throws IllegalArgumentException {
        if (Objects.equals(gameMode, GameMode.STANDARD.getValue()))
            return GameMode.STANDARD;
        if (Objects.equals(gameMode, GameMode.HARD.getValue()))
            return GameMode.HARD;
        if (Objects.equals(gameMode, GameMode.PEACEFUL.getValue()))
            return GameMode.PEACEFUL;
        throw new IllegalArgumentException(String.format("Game mode %s is invalid", gameMode));
    }

    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        if (savedGames.containsKey(name)) throw new IllegalArgumentException();
        savedGames.put(name, this.dungeon);
        return this.makeDungeonResponse();
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        if (!savedGames.containsKey(name)) throw new IllegalArgumentException();
        this.dungeon = savedGames.get(name);
        return this.makeDungeonResponse();
    }

    public List<String> allGames() {
        return savedGames.keySet().stream().collect(Collectors.toList());
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {
        this.dungeon.tick(itemUsed, movementDirection);
        return this.makeDungeonResponse();
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity interactEntity = dungeon.getMap()
            .allEntities().stream().filter(e -> e.getId().equals(entityId))
            .findFirst().orElse(null);

        if (interactEntity == null) throw new IllegalArgumentException("entityId does not exist");
        if (!interactEntity.isInteractable()) throw new IllegalArgumentException("Entity is not interactable");
        if (interactEntity instanceof Mercenary) dungeon.bribeMercenary((Mercenary)interactEntity);
        
        return this.makeDungeonResponse();
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        this.dungeon.build(buildable);
        return this.makeDungeonResponse();
    }

    /**
     * Every endpoint has to return a DungeonResponse, so we just use this
     * helper function
     * @return
     */
    private DungeonResponse makeDungeonResponse() {
        return new DungeonResponse(
            this.dungeon.getId(),
            this.dungeon.getName(),
            this.dungeon.getEntitiesResponse(),
            this.dungeon.getInventoryAsItemResponse(),
            new ArrayList<String>(),
            this.dungeon.getGoalAsString(),
            new ArrayList<AnimationQueue>()
        );
    }
}