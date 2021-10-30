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
        resp = ctr.tick("", Direction.DOWN);
        resp = ctr.tick("", Direction.DOWN);

        // check that the key is in player inventory
        // Checking that the Item has Been Added to Inventory
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));

        // attempt to use key 1 to open door 2 (should fail)
        resp = ctr.tick("", Direction.RIGHT);
        Position p1 = TestUtils.getPlayerPosition(resp);
        
        resp = ctr.tick("", Direction.DOWN);
        Position p2 = TestUtils.getPlayerPosition(resp);
        assertEquals(p1.getX(), p2.getX());
        assertEquals(p1.getY(), p2.getY());

        // attempt to use key 1 to open door 1 (should succeed)
        resp = ctr.tick("", Direction.RIGHT);
        Position p3 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p3.getX(), p2.getX());
        assertNotEquals(p3.getY(), p2.getY());
        // check there key is used and there is no key
        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));

        // player moves to pickup key 2
        resp = ctr.tick("", Direction.RIGHT);
        // check there is key
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));

        // player moves to where key 3 is
        // player shouldn't pick up the key as player cant only hold one key
        resp = ctr.tick("", Direction.RIGHT);
        assertTrue(1 == resp.getInventory().stream().filter(item -> item.getType().equals("key")).count());

        // attempt to use key 2 to open door 3 (should fail)
        Position p4 = TestUtils.getPlayerPosition(resp);
        
        resp = ctr.tick("", Direction.RIGHT);
        Position p5 = TestUtils.getPlayerPosition(resp);
        assertEquals(p4.getX(), p5.getX());
        assertEquals(p4.getY(), p5.getY());

        // attempt to walk through door 1, which is already opened
        resp = ctr.tick("", Direction.LEFT);
        Position p6 = TestUtils.getPlayerPosition(resp);
        
        resp = ctr.tick("", Direction.LEFT);
        Position p7 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p6.getX(), p7.getX());
        assertNotEquals(p6.getY(), p7.getY());

        // attempt to use key 2 to open door 2 (should succeed)
        resp = ctr.tick("", Direction.LEFT);
        Position p8 = TestUtils.getPlayerPosition(resp);
        resp = ctr.tick("", Direction.DOWN);
        Position p9 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p8.getX(), p9.getX());
        assertNotEquals(p8.getY(), p9.getY());
        // check there key is used and there is no key
        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));

        // now pick up key 3 and open door 3 
        resp = ctr.tick("", Direction.UP);
        resp = ctr.tick("", Direction.RIGHT);
        resp = ctr.tick("", Direction.RIGHT);
        resp = ctr.tick("", Direction.RIGHT);
        // check there key is used and there is no key
        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));
        // check there is key
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));

        // attempt to use key 2 to open door 2 (should succeed)
        Position p10 = TestUtils.getPlayerPosition(resp);
        resp = ctr.tick("", Direction.RIGHT);
        Position p11 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p10.getX(), p11.getX());
        assertNotEquals(p10.getY(), p11.getY());
        // check there key is used and there is no key
        assertFalse(resp.getInventory().stream().anyMatch(item -> item.getType().equals("key")));
    }
}
