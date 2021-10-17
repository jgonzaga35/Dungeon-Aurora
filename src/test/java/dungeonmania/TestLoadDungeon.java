package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

public class TestLoadDungeon {

    @ParameterizedTest
    @ValueSource(strings={"Peaceful", "Standard", "Hard"}) // test for all modes
    public void testLoadingMaze(String mode) {
        DungeonManiaController ctr = new DungeonManiaController();

        DungeonResponse resp = assertDoesNotThrow(() -> {
            return ctr.newGame("maze", mode);
        });

        assertEquals("maze", resp.getDungeonName());
        assertEquals(0, resp.getInventory().size());
        int numPlayers = 0;
        int numExit = 0;
        int numWalls = 0;

        for (EntityResponse er : resp.getEntities()) {
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
