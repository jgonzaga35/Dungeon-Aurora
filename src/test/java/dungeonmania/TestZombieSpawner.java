package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TestZombieSpawner {
    @BeforeEach
    public void setSeed() {
        Utils.setSeed(1);
    }

    @Test
    public void spawnerZombie() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_zombies_park", GameMode.STANDARD.getValue());
        // 3 walls + 1 player + 1 spawner 
        assertEquals(5, resp.getEntities().size());

        for (int i = 0; i < 100; i++) {
            resp = ctr.tick("", Direction.NONE);
            // a zombie should spawn every 20 seconds
            assertEquals(5 + Math.floorDiv(i, 20), resp.getEntities().size());
        }
        
    }
}
