package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.Hydra;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TestHydra {

    @Test
    public void testHydraSpawn() {
        // Testing Hydra Spawn on HARD MODE
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.HARD.getValue());

        resp = ctr.tick(null, Direction.NONE);

        for (int i = 1; i < 50; i++) {
            resp = ctr.tick(null, Direction.NONE);
        }

        assertTrue(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Hydra.STRING_TYPE)));

        // Testing Hydra DOES NOT Spawn on PEACEFUL MODE
        ctr = new DungeonManiaController();
        resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());

        resp = ctr.tick(null, Direction.NONE);

        for (int i = 1; i < 50; i++) {
            resp = ctr.tick(null, Direction.NONE);
        }

        assertFalse(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Hydra.STRING_TYPE)));

        // Testing Hydra DOES NOT Spawn on STANDARD MODE
        ctr = new DungeonManiaController();
        resp = ctr.newGame("maze", GameMode.STANDARD.getValue());

        resp = ctr.tick(null, Direction.NONE);

        for (int i = 1; i < 50; i++) {
            resp = ctr.tick(null, Direction.NONE);
        }

        assertFalse(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Hydra.STRING_TYPE)));
    }




}
