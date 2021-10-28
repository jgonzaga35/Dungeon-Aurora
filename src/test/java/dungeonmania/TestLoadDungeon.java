package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
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

public class TestLoadDungeon {

    private static Stream<Arguments> testLoadingMaze() {
        // returns every available mode
        return Stream.of(GameMode.values())
            .map(mode -> mode.getValue())
            .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
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

    @Test
    public void testUniqueDungeonId() {
        DungeonManiaController ctr = new DungeonManiaController();

        DungeonResponse resp;
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            resp = assertDoesNotThrow(() -> {
                return ctr.newGame("maze", GameMode.PEACEFUL.getValue());
            });
            assertTrue(!ids.contains(resp.getDungeonId()), "duplicate dungeon id: " + resp.getDungeonId());
            ids.add(resp.getDungeonId());
        }
    }

    @Test
    public void testDungeonModes() {
        DungeonManiaController ctr = new DungeonManiaController();
        TestUtils.assertEqualsUnordered(Arrays.asList(
            GameMode.HARD.getValue(),
            GameMode.STANDARD.getValue(),
            GameMode.PEACEFUL.getValue()
        ), ctr.getGameModes());
    }

}