package dungeonmania;

import java.util.List;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.FileLoader;
import org.json.JSONObject;
import java.util.Random;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.collectables.SunStone;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.response.models.EntityResponse;

public class TestSunStone {
    /**
     * TEST: Ensure Sun Stone is Collected & Opens Doors & Is Not Removed
     */

    @Test
    public void testSunStoneOpeningDoor() {
        // Item Coords: Player(1,1), SunStone(1,3), Door 1 (2,4), Door 2 (3,3), Door 3
        // (6, 3)
        // New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_door_maze_sun_stone", GameMode.PEACEFUL.getValue());
        resp = ctr.tick(null, Direction.NONE);

        // Down 2 Units to the SunStone at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

        // check that the Sun Stone is in player inventory
        // Checking that the Item has Been Added to Inventory
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals(SunStone.STRING_TYPE)));

        // Check that the Item Was Removed from the Cell
        int currPositionX = 0;
        int currPositionY = 0;
        boolean itemRemoved = true;
        String curr_type = "";
        List<EntityResponse> cellEntities = resp.getEntities();
        for (EntityResponse currEntity : cellEntities) {
            curr_type = currEntity.getType();

            Position currPosition = currEntity.getPosition();
            currPositionX = currPosition.getX();
            currPositionY = currPosition.getY();
            if (curr_type.equals(SunStone.STRING_TYPE) && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);

        // using Sun Stone to open Door 2
        resp = ctr.tick(null, Direction.RIGHT);
        Position p1 = TestUtils.getPlayerPosition(resp);

        resp = ctr.tick(null, Direction.DOWN);
        Position p2 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p1, p2);

        // using Sun Stone to open Door 1
        ctr.tick(null, Direction.UP);
        resp = ctr.tick(null, Direction.RIGHT);
        Position p3 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p3, p2);

        // moving right
        resp = ctr.tick(null, Direction.RIGHT);

        // using Sun Stone to open Door 3
        Position p4 = TestUtils.getPlayerPosition(resp);

        resp = ctr.tick(null, Direction.RIGHT);
        Position p5 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p4, p5);

        // walking back through Door 1
        resp = ctr.tick(null, Direction.LEFT);
        Position p6 = TestUtils.getPlayerPosition(resp);

        resp = ctr.tick(null, Direction.LEFT);
        Position p7 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p6, p7);

        // using Sun Stone to open Door 2
        resp = ctr.tick(null, Direction.LEFT);
        Position p8 = TestUtils.getPlayerPosition(resp);
        resp = ctr.tick(null, Direction.DOWN);
        Position p9 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p8, p9);

        // using Sun Stone to open Door 3
        resp = ctr.tick(null, Direction.UP);
        resp = ctr.tick(null, Direction.RIGHT);
        resp = ctr.tick(null, Direction.RIGHT);
        resp = ctr.tick(null, Direction.RIGHT);

        // using Sun Stone to open Door 3
        Position p10 = TestUtils.getPlayerPosition(resp);
        resp = ctr.tick(null, Direction.RIGHT);
        Position p11 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p10, p11);

        // Check the Sun Stone is Still in Inventory (Should Not Be Discarded as Per
        // Spec)
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals("sun_stone")));
    }

    
    
   

    
    /**
     * Test Bribing Mercenary with Sun Stone
     */
    @Test
    public void testSunStoneMercenaryBribe() throws IOException {
        DungeonManiaController dc;
        Dungeon dungeon;
        DungeonResponse resp;
        Mercenary merc;

        String content = FileLoader.loadResourceFile("/dungeons/_sun_stone_merc_test.json");
        dungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD, new JSONObject(content));
        dc = new DungeonManiaController(dungeon);
        merc = (Mercenary) dungeon.getMap().allEntities().stream().filter(e -> e instanceof Mercenary).findFirst()
                .get();

        dungeon.getMap().flood();
        Integer dist = merc.getCell().getPlayerDistance();

        // Try bribing invalid id
        assertThrows(IllegalArgumentException.class, () -> {
            dc.interact("invalid");
        });

        // Try bribing with no money
        assertThrows(InvalidActionException.class, () -> {
            dc.interact(merc.getId());
        });

        for (int i = 0; i < 2; i++) {
            dc.tick(null, Direction.RIGHT);

            assertTrue(merc.getCell().getPlayerDistance() == dist);
            dist = merc.getCell().getPlayerDistance();
        }
        // player pos (7, 5)
        // Try bribing outside of range
        assertThrows(InvalidActionException.class, () -> {
            dc.interact(merc.getId());
        });

        for (int i = 0; i < 5; i++) {
            dc.tick(null, Direction.UP);
            assertTrue(merc.getCell().getPlayerDistance() <= dist);
            dist = merc.getCell().getPlayerDistance();
        }
        // player pos (7, 0)
        // merc pos (5, 0)
        // two cardinal squares away, bribe possible

        assertDoesNotThrow(() -> dc.interact(merc.getId()));

        // Try bribing friendly
        assertThrows(IllegalArgumentException.class, () -> {
            dc.interact(merc.getId());
        });

        resp = dc.tick(null, Direction.NONE);

        // check the Sun Stone is still in inventory after bribe
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals("sun_stone")));
    }
    
}
