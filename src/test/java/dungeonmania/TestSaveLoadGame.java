package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.DungeonResponse;

public class TestSaveLoadGame {
    @Test
    public void saveGame() {
        DungeonManiaController ctr = new DungeonManiaController();
        ctr.newGame("_boulder_simple", GameMode.PEACEFUL.getValue());
        DungeonResponse resp = ctr.saveGame("g1");
        assertTrue(ctr.allGames().contains("g1"));
    }

    @Test
    public void loadGame() {
        DungeonManiaController ctr = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> ctr.loadGame("NonExistent"));
    }
}
