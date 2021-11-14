package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class TestZombieToastMovement {
    
    /**
     * makes sure that if there is no unblocked cell around the spawner, no
     * zombies are spawned
     */
    @Test
    public void testZombiesDontTeleport() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_zombies_maze", GameMode.PEACEFUL.getValue());
        ctr.setSeed(1);

        // zombie id -> position
        Map<String, Pos2d> positions = new HashMap<>();

        // store cells that are blocking to look them up
        Set<Pos2d> blocking = new HashSet<>();
        for (EntityResponse er2 : resp.getEntities()) {
            if (TestUtils.isBlocking(er2)) {
                int x = er2.getPosition().getX();
                int y = er2.getPosition().getY();
                blocking.add(new Pos2d(x, y));
            }
        }

        for (int i = 1; i < 200; i++) {
            resp = ctr.tick(null, Direction.NONE);
            for (EntityResponse er : resp.getEntities()) {
                if (er.getType() != ZombieToast.STRING_TYPE) 
                    continue;
                
                // make sure it only moved on cell, horizontally or vertically
                Pos2d prev = positions.get(er.getId());
                Pos2d curr = Pos2d.from(er.getPosition());
                if (prev != null) {
                    System.out.println(i + ", " + prev.squareDistance(curr));
                    assertTrue(prev.squareDistance(curr) <= 1);
                }
                positions.put(er.getId(), curr);

                // make sure it's not on a blocking cell
                assertFalse(blocking.contains(curr));
            }
        }
    }
}
