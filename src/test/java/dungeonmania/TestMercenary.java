package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.movings.Player;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class TestMercenary {
    DungeonManiaController dc;
    Dungeon dungeon;
    Player player;
    Mercenary merc;

    @BeforeEach
    public void setStartingPostition() throws IOException 
    {
        String content = FileLoader.loadResourceFile("/dungeons/_merc_test.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dc = new DungeonManiaController(dungeon);
        player = (Player) dungeon.getMap().allEntities().stream()
            .filter(e -> e instanceof Player)
            .findFirst().get();
        merc = (Mercenary) dungeon.getMap().allEntities().stream()
            .filter(e -> e instanceof Mercenary)
            .findFirst().get();
    }

    @Test
    public void testHostileMovement() {
        DungeonResponse resp;
        resp = dc.tick(null, Direction.RIGHT);
        assertEquals(new Pos2d(1, 0), merc.getPosition());
        // player pos (6, 5)
        
        resp = dc.tick(null, Direction.RIGHT);
        assertEquals(new Pos2d(2, 0), merc.getPosition());
        // player pos (7, 5)
        
        resp = dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(3, 0), merc.getPosition());
        // player pos (7, 4)
        
        resp = dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(4, 0), merc.getPosition());
        // player pos (7, 3)
        
        resp = dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(5, 0), merc.getPosition());
        // player pos (7, 2)
        
        resp = dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(6, 0), merc.getPosition());
        // player pos (7, 1)
        
        resp = dc.tick(null, Direction.UP);
        // player pos (7, 0)
        // battle should happen
        assertEquals(0, TestUtils.countEntitiesOfType(resp, Mercenary.STRING_TYPE));
        assertEquals(new Pos2d(7, 0), player.getPosition());
    }

    @Test
    public void testFriendlyMovement() {
        dc.tick(null, Direction.RIGHT);
        assertEquals(new Pos2d(1, 0), merc.getPosition());
        // player pos (6, 5)
        
        dc.tick(null, Direction.RIGHT);
        assertEquals(new Pos2d(2, 0), merc.getPosition());
        // player pos (7, 5)
        
        dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(3, 0), merc.getPosition());
        // player pos (7, 4)
        
        dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(4, 0), merc.getPosition());
        // player pos (7, 3)
        
        dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(5, 0), merc.getPosition());
        // player pos (7, 2)
        
        dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(6, 0), merc.getPosition());
        // player pos (7, 1)
        
        dc.interact("mercenary");
        dc.tick(null, Direction.UP);
        // player pos (7, 0)
        // battle should happen
        assertEquals(new Pos2d(6, 0), merc.getPosition());
        assertEquals(new Pos2d(7, 0), player.getPosition());
        
        dc.tick(null, Direction.LEFT);
        assertEquals(new Pos2d(5, 0), merc.getPosition());
        // player pos (6, 0)
        
        dc.tick(null, Direction.LEFT);
        assertEquals(new Pos2d(4, 0), merc.getPosition());
        // player pos (5, 0)
        
        dc.tick(null, Direction.RIGHT);
        assertEquals(new Pos2d(5, 0), merc.getPosition());
        // player pos (6, 0)
        
        dc.tick(null, Direction.DOWN);
        assertEquals(new Pos2d(5, 1), merc.getPosition());
        // player pos (6, 1)
        
        dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(5, 0), merc.getPosition());
        // player pos (6, 0)
    }

    @Test
    public void testBribe() {
        DungeonResponse resp;
        
        // Try bribing with no money
        assertThrows(InvalidActionException.class, () -> {
            dc.interact("mercenary");
        });
        
        resp = dc.tick(null, Direction.RIGHT);
        assertEquals(new Pos2d(1, 0), merc.getPosition());
        // player pos (6, 5)

        // Try bribing outside of range
        assertThrows(InvalidActionException.class, () -> {
            dc.interact("mercenary");
        });
        
        resp = dc.tick(null, Direction.RIGHT);
        assertEquals(new Pos2d(2, 0), merc.getPosition());
        // player pos (7, 5)
        
        resp = dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(3, 0), merc.getPosition());
        // player pos (7, 4)
        
        resp = dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(4, 0), merc.getPosition());
        // player pos (7, 3)
        
        resp = dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(5, 0), merc.getPosition());
        // player pos (7, 2)
        
        resp = dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(6, 0), merc.getPosition());
        // player pos (7, 1)
        // two cardinal squares away, bribe possible

        assertDoesNotThrow(() -> dc.interact("mercenary"));
        
    }

    @Test
    public void testSpawn() {
        
    }
}
