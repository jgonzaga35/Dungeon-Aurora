package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.Zombie;
import dungeonmania.entities.statics.Wall;
import dungeonmania.entities.statics.ZombieToastSpawner;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Counter;
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
        DungeonResponse resp = ctr.newGame("_zombies_park", GameMode.STANDARD.getValue());
        // 3 walls + 1 player + 1 spawner 
        assertEquals(5, resp.getEntities().size());

        for (int i = 1; i < 101; i++) {
            resp = ctr.tick("", Direction.NONE);
            // a zombie should spawn every 20 seconds
            assertEquals(5 + Math.floorDiv(i, 20), resp.getEntities().size());
        }
        // make sure we have the right count of zombies, wall and so on
        Counter<String> typeCounts = new Counter<>();
        typeCounts.add(Player.STRING_TYPE, 1);
        typeCounts.add(ZombieToastSpawner.STRING_TYPE, 1);
        typeCounts.add(Wall.STRING_TYPE, 3);
        typeCounts.add(Zombie.STRING_TYPE, 5);

        assertEquals(typeCounts,
            Counter.from(resp.getEntities().stream()
                .map(e -> e.getType()).iterator())
        );
    }

    /**
     * makes sure that if there is no unblocked cell around the spawner, no
     * zombies are spawned
     */
    @Test
    public void testSpawnZombiesBlocked() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_zombies_spawner_blocked", GameMode.STANDARD.getValue());
        // 2 walls + 1 player + 1 spawner 
        assertEquals(4, resp.getEntities().size());

        for (int i = 1; i < 101; i++) {
            resp = ctr.tick("", Direction.NONE);
            assertEquals(4, resp.getEntities().size()); // there isn't anywhere for zombies to spawn
        }

        Counter<String> typeCounts = new Counter<>();
        typeCounts.add(Player.STRING_TYPE, 1);
        typeCounts.add(ZombieToastSpawner.STRING_TYPE, 1);
        typeCounts.add(Wall.STRING_TYPE, 2);

        assertEquals(typeCounts,
            Counter.from(resp.getEntities().stream()
                .map(e -> e.getType()).iterator())
        );
    }
}
