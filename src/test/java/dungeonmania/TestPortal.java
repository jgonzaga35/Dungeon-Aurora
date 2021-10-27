package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestPortal {

    @Test
    public void testPlayerMovement() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp;
        Position p;
        assertDoesNotThrow(() -> {
            ctr.newGame("_simple", GameMode.PEACEFUL.getValue());
        });

        // don't move
        resp = ctr.tick("", Direction.NONE);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(0, p.getX());
        assertEquals(0, p.getY());


        // move on a portal
        resp = ctr.tick("", Direction.RIGHT);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(4, p.getX());
        assertEquals(0, p.getY());
    }
}
