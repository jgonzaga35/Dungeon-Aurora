package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestWalk {

    @Test
    public void testPlayerMovement() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp;
        Position p;
        assertDoesNotThrow(() -> {
            ctr.newGame("_simple", "Peaceful");
        });

        // move on a free spot
        resp = ctr.tick("", Direction.RIGHT);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(1, p.getX());
        assertEquals(0, p.getY());

        // try to move against a wall
        resp  = ctr.tick("", Direction.DOWN);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(1, p.getX());
        assertEquals(0, p.getY());
    }
}
