package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TestSaveLoadGame {
    @Test
    public void testSaveGame() {
        DungeonManiaController ctr = new DungeonManiaController();
        ctr.newGame("_boulder_simple", GameMode.PEACEFUL.getValue());
        DungeonResponse resp = ctr.saveGame("g1");
        assertTrue(ctr.allGames().contains("g1"));
        assertThrows(IllegalArgumentException.class, () -> ctr.saveGame("g1"));
    }

    @Test
    public void testLoadGame() {
        DungeonManiaController ctr = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> ctr.loadGame("NonExistent"));
    }

    @Test
    public void testAllGames() {
        DungeonManiaController ctr = new DungeonManiaController();
        ctr.newGame("_boulder_simple", GameMode.PEACEFUL.getValue());
        ctr.saveGame("g1");
        ctr.saveGame("g3");
        ctr.saveGame("g4");
        ctr.saveGame("g5");

        assertEquals(Arrays.asList("g1","g3","g4","g5"), ctr.allGames());
    }

    @Test
    public void testSavedContent() {
        DungeonManiaController ctr = new DungeonManiaController();
        ctr.newGame("_boulder_simple", GameMode.PEACEFUL.getValue());
        ctr.tick("", Direction.NONE);
        ctr.tick("", Direction.UP);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.LEFT);
        DungeonResponse resp1 = ctr.saveGame("g1");
        DungeonResponse resp2 = ctr.loadGame("g1");

        assertEquals(resp1, resp2);
    }
}
