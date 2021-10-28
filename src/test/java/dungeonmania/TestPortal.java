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
    public void testPlayerEnterPortal() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp;
        Position p;
        assertDoesNotThrow(() -> {
            ctr.newGame("portals", GameMode.PEACEFUL.getValue());
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

    @Test
    public void testOnlyOnePortal() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp;
        Position p;
        assertDoesNotThrow(() -> {
            ctr.newGame("one_portal", GameMode.PEACEFUL.getValue());
        });

        // don't move
        resp = ctr.tick("", Direction.NONE);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(0, p.getX());
        assertEquals(0, p.getY());


        // make sure portal does not teleport the player
        resp = ctr.tick("", Direction.RIGHT);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(1, p.getX());
        assertEquals(0, p.getY());
    }

    @Test
    public void testOnlyPortalDifferentColours() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp;
        Position p;
        assertDoesNotThrow(() -> {
            ctr.newGame("colour_portal", GameMode.PEACEFUL.getValue());
        });

        // don't move
        resp = ctr.tick("", Direction.NONE);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(0, p.getX());
        assertEquals(0, p.getY());


        // make sure portal does not teleport the player
        resp = ctr.tick("", Direction.RIGHT);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(1, p.getX());
        assertEquals(0, p.getY());
    }

}
