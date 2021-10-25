package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.json.JSONObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class TestCollectables {
    /** 
     * TEST: Ensure Treasure is Collected by 
     */
    
    @Test
    public void testLoadingTreasure() {
        assertDoesNotThrow(() -> {
            //Loading New Map (Treasure is at Coords (7, 10) and Player is at Coords (1, 1))
            String content = FileLoader.loadResourceFile("/dungeons/treasureExample.json");
            Dungeon dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        });

        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);

        //Moving to the Treasure

        // Down 6 Units to Coord (7, 1)
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);

        // Right 9 Units to Coord (7, 1)
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        
        //Checking If Goal is Reached (No Treasure Left on Map)
        //TODO
    }

}