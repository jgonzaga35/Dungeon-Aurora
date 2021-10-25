package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

public class TestCollectables {
    /** 
     * TEST: Ensure Treasure is Collected by 
     */
    
    @Test
    public void testLoadingTreasure() {
        //Loading New Map (Treasure is at Coords (7, 10))
        String content = FileLoader.loadResourceFile("/dungeons/advanced.json");
        Dungeon dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));

        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);

        //Checking if Character Has Any Treasure (Should be None)


        //Moving to the Treasure

        // TODO
        resp  = ctr.tick("", Direction.DOWN);
        
        //Checking If Goal is Reached (No Treasure Left on Map)

        //TODO
    }

}