package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestBoulder {

    @Test
    public void pushBoulder() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp;
        Position p;
        ctr.newGame("_boulder_simple", GameMode.PEACEFUL.getValue());

        // move the boulder to a free spot
        resp = ctr.tick("", Direction.RIGHT);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(1, p.getX());
        assertEquals(0, p.getY());

        // try to push the boulder further
        // no movement
        resp  = ctr.tick("", Direction.RIGHT);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(1, p.getX());
        assertEquals(0, p.getY());
    }
    
}
