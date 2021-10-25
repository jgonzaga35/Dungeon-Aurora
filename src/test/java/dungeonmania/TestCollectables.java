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

public class TestLoadCollectables {
    /** 
     * Testing Making the Treasure Entity
     */
    @Test
    public void testLoadingTreasure() {
        //Creating Treasure
        Treasure treasure = new Treasure();

        //
        DungeonResponse resp = assertDoesNotThrow(() -> {
            return ctr.newGame("maze", mode);
        });

        assertEquals("maze", resp.getDungeonName());
        assertEquals(0, resp.getInventory().size());
        int numPlayers = 0;
        int numExit = 0;
        int numWalls = 0;

        Set<String> ids = new HashSet<>();
        for (EntityResponse er : resp.getEntities()) {

            // make sure we don't have duplicate entities ids
            assertTrue(!ids.contains(er.getId()));
            ids.add(er.getId());

            if (Objects.equals(er.getType(), "wall")) {
                numWalls++;
            } else if (Objects.equals(er.getType(), "player")) {
                numPlayers++;
            } else if (Objects.equals(er.getType(), "exit")) {
                numExit++;
            }
        }

        assertEquals(1, numExit);
        assertEquals(1, numPlayers);
        // cat src/test/resources/dungeons/maze.json | grep wall | wc -l
        assertEquals(204, numWalls);
    }
}