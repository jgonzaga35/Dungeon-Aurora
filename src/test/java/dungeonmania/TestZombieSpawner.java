package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TestZombieSpawner {
    @BeforeEach
    public void setSeed() {
        Utils.setSeed(1);
    }

    /**
     * Makes sure that zombies are indeed spawned
     */
    @Test
    public void testSpawnZombies() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_zombies_park", GameMode.PEACEFUL.getValue());
        
        for (int i = 1; i < 101; i++) {
            resp = ctr.tick("", Direction.NONE);
            // a zombie should spawn every 20 ticks, and there is already one
            // zombie on the map
            assertEquals(1+Math.floorDiv(i, 20), resp.getEntities().stream()
                .filter(e -> Objects.equals(ZombieToast.STRING_TYPE, e.getType())).count());
        }
    }

    /**
     * makes sure that if there is no unblocked cell around the spawner, no
     * zombies are spawned
     */
    @Test
    public void testSpawnZombiesBlocked() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_zombies_spawner_blocked", GameMode.STANDARD.getValue());

        for (int i = 1; i < 101; i++) {
            resp = ctr.tick("", Direction.NONE);
            // there isn't anywhere for zombies to spawn
            assertEquals(0, resp.getEntities().stream()
                .filter(e -> Objects.equals(ZombieToast.STRING_TYPE, e.getType())).count());
        }
    }
}
