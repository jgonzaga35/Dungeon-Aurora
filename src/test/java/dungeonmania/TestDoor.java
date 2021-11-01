package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestDoor {
    @Test
    public void rightKeyRightDoor() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp;
        Position p;
        ctr.newGame("_door_maze", GameMode.PEACEFUL.getValue());

        // move the player to pick up key 1
        resp = ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

        // check that the key is in player inventory
        // Checking that the Item has Been Added to Inventory
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));

        // attempt to use key 1 to open door 2 (should fail)
        resp = ctr.tick(null, Direction.RIGHT);
        Position p1 = TestUtils.getPlayerPosition(resp);
        
        resp = ctr.tick(null, Direction.DOWN);
        Position p2 = TestUtils.getPlayerPosition(resp);
        assertEquals(p1, p2);

        // attempt to use key 1 to open door 1 (should succeed)
        resp = ctr.tick(null, Direction.RIGHT);
        Position p3 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p3, p2);
        // check there key is used and there is no key
        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));

        // player moves to pickup key 2
        resp = ctr.tick(null, Direction.RIGHT);
        // check there is key
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));

        // player moves to where key 3 is
        // player shouldn't pick up the key as player cant only hold one key
        resp = ctr.tick(null, Direction.RIGHT);
        assertTrue(1 == resp.getInventory().stream().filter(item -> item.getType().equals("key")).count());

        // attempt to use key 2 to open door 3 (should fail)
        Position p4 = TestUtils.getPlayerPosition(resp);
        
        resp = ctr.tick(null, Direction.RIGHT);
        Position p5 = TestUtils.getPlayerPosition(resp);
        assertEquals(p4, p5);

        // attempt to walk through door 1, which is already opened
        resp = ctr.tick(null, Direction.LEFT);
        Position p6 = TestUtils.getPlayerPosition(resp);
        
        resp = ctr.tick(null, Direction.LEFT);
        Position p7 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p6, p7);

        // attempt to use key 2 to open door 2 (should succeed)
        resp = ctr.tick(null, Direction.LEFT);
        Position p8 = TestUtils.getPlayerPosition(resp);
        resp = ctr.tick(null, Direction.DOWN);
        Position p9 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p8, p9);
        // check there key is used and there is no key
        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));

        // now pick up key 3 and open door 3 
        resp = ctr.tick(null, Direction.UP);
        resp = ctr.tick(null, Direction.RIGHT);
        resp = ctr.tick(null, Direction.RIGHT);
        resp = ctr.tick(null, Direction.RIGHT);
        // check there is key
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));

        // attempt to use key 3 to open door 3 (should succeed)
        Position p10 = TestUtils.getPlayerPosition(resp);
        resp = ctr.tick(null, Direction.RIGHT);
        Position p11 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p10, p11);
        // check there key is used and there is no key
        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));
    }
}
