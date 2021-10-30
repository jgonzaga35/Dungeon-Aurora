package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


import dungeonmania.response.models.DungeonResponse;

public class TestGoals {

    @Test
    public void testGetBasicGoalsAsString() {
        DungeonManiaController ctr = new DungeonManiaController();

        DungeonResponse resp = assertDoesNotThrow(() -> {
            return ctr.newGame("maze", "Peaceful");
        });

        assertEquals("get to an exit", resp.getGoals());
    }


    @Test
    public void testGetComplexGoalsAsString() {
        DungeonManiaController ctr = new DungeonManiaController();

        DungeonResponse resp = assertDoesNotThrow(() -> {
            return ctr.newGame("complex_maze", "Peaceful");
        });

        assertEquals("destroy all enemies and spawners AND collect all treasure", resp.getGoals());
    }

}
