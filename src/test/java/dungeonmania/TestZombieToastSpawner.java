package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.entities.statics.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestZombieToastSpawner {
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
            resp = ctr.tick(null, Direction.NONE);
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
            resp = ctr.tick(null, Direction.NONE);
            // there isn't anywhere for zombies to spawn
            assertEquals(0, resp.getEntities().stream()
                .filter(e -> Objects.equals(ZombieToast.STRING_TYPE, e.getType())).count());
        }
    }

    @Test
    public void testDestroyZombieToastSpawnerSuccess() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_destroy_zombie_toast_spawner", GameMode.STANDARD.getValue());

        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getType().equals(Sword.STRING_TYPE)));
        resp = ctr.tick(null, Direction.RIGHT);
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals(Sword.STRING_TYPE)));

        String zombieToastSpawnerId = resp.getEntities().stream()
            .filter(e -> e.getType().equals(ZombieToastSpawner.STRING_TYPE))
            .findFirst().get().getId();

        resp = ctr.interact(zombieToastSpawnerId); // destroys the zombie toast spawner
        assertFalse(resp.getEntities().stream().anyMatch(e -> e.getType().equals(ZombieToastSpawner.STRING_TYPE)));

        Position p = TestUtils.getPlayerPosition(resp);
        assertEquals(2, p.getX());
        assertEquals(0, p.getY());
    }


    @Test
    public void testDestroyZombieToastSpawnerTooFar() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_destroy_zombie_toast_spawner", GameMode.STANDARD.getValue());

        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getType().equals(Sword.STRING_TYPE)));
        resp = ctr.tick(null, Direction.RIGHT);
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals(Sword.STRING_TYPE)));

        String zombieToastSpawnerId = resp.getEntities().stream()
            .filter(e -> e.getType().equals(ZombieToastSpawner.STRING_TYPE))
            .findFirst().get().getId();

        resp = ctr.tick(null, Direction.DOWN); // move too far away from spawner

        // java's annoying with setting variable outside of scope
        ArrayList<DungeonResponse> arr = new ArrayList<>();
        assertThrows(InvalidActionException.class, () -> {
            arr.set(0, ctr.interact(zombieToastSpawnerId));
        });
        resp = arr.get(0);
        assertTrue(resp.getEntities().stream().anyMatch(e -> e.getType().equals(ZombieToastSpawner.STRING_TYPE)));

        Position p = TestUtils.getPlayerPosition(resp);
        assertEquals(2, p.getX());
        assertEquals(0, p.getY());
    }

}
