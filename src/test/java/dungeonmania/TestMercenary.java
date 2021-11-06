package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        dungeon.getMap().flood();
    }

    @Test
    public void testHostileMovement() {
        DungeonResponse resp;
        Integer dist = merc.getCell().getPlayerDistance();

        for (int i = 0; i < 2; i++) {
            resp = dc.tick(null, Direction.RIGHT);

            assertTrue(merc.getCell().getPlayerDistance() == dist);
            dist = merc.getCell().getPlayerDistance();
        }
        // player pos (7, 5)
        
        for (int i = 0; i < 5; i++) {
            resp = dc.tick(null, Direction.UP);
            assertTrue(merc.getCell().getPlayerDistance() <= dist);
            dist = merc.getCell().getPlayerDistance();
        }
        // player pos (7, 0)
        
        for (int i = 0; i < 5; i++) {
            resp = dc.tick(null, Direction.NONE);
            assertTrue(merc.getCell().getPlayerDistance() < dist);
            dist = merc.getCell().getPlayerDistance();
        }
        // player pos (7, 0)
        // merc pos (6, 0)
        
        resp = dc.tick(null, Direction.UP);
        // player pos (7, 0)
        // merc pos (7, 0)
        // battle should happen
        assertEquals(0, TestUtils.countEntitiesOfType(resp, Mercenary.STRING_TYPE));
        assertEquals(new Pos2d(7, 0), player.getPosition());
    }

    @Test
    public void testFriendlyMovement() {
        Integer dist = merc.getCell().getPlayerDistance();

        for (int i = 0; i < 2; i++) {
            dc.tick(null, Direction.RIGHT);

            assertTrue(merc.getCell().getPlayerDistance() == dist);
            dist = merc.getCell().getPlayerDistance();
        }
        // player pos (7, 5)
        
        for (int i = 0; i < 5; i++) {
            dc.tick(null, Direction.UP);
            assertTrue(merc.getCell().getPlayerDistance() <= dist);
            dist = merc.getCell().getPlayerDistance();
        }
        // player pos (7, 0)
        
        for (int i = 0; i < 5; i++) {
            dc.tick(null, Direction.NONE);
            assertTrue(merc.getCell().getPlayerDistance() < dist);
            dist = merc.getCell().getPlayerDistance();
        }
        // player pos (7, 0)
        // merc pos (6, 0)
        
        dc.interact(merc.getId());
        dc.tick(null, Direction.UP);
        // player pos (7, 0)
        assertEquals(new Pos2d(6, 0), merc.getPosition());
        assertEquals(new Pos2d(7, 0), player.getPosition());
        
        dc.tick(null, Direction.LEFT);
        assertTrue(
            merc.getPosition().equals(new Pos2d(5, 0)) || 
            merc.getPosition().equals(new Pos2d(6, 1)) ||
            merc.getPosition().equals(new Pos2d(7, 0))
        );
        // player pos (6, 0)
        
        dc.tick(null, Direction.LEFT);
        
        dc.tick(null, Direction.LEFT);
        assertTrue(merc.getCell().getPlayerDistance() == 1);
        // player pos (5, 0)
        
        dc.tick(null, Direction.RIGHT);
        assertTrue(merc.getCell().getPlayerDistance() == 1);
        // player pos (6, 0)
        
        dc.tick(null, Direction.DOWN);
        assertTrue(merc.getCell().getPlayerDistance() == 1);
        // player pos (6, 1)
        
        dc.tick(null, Direction.UP);
        assertTrue(merc.getCell().getPlayerDistance() == 1);
        // player pos (6, 0)
    }

    @Test
    public void testBribe() {
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
        
        for (int i = 0; i < 4; i++) {
            dc.tick(null, Direction.NONE);
            System.out.println(i + " old= " + dist + " new= " + merc.getCell().getPlayerDistance());
            assertTrue(merc.getCell().getPlayerDistance() < dist);
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
    }
    
    @Test
    public void testSpawn() {
        // black box test because we don't need too much control

        DungeonManiaController ctr = new DungeonManiaController();
        // map in which the player can lock himself in so that we can spawn a
        // lot of mercenaries and test probability distributions (for assassins)
        DungeonResponse resp = ctr.newGame("_mercenary_zoo", GameMode.STANDARD.getValue());
        ctr.setSeed(1);
        
        // player locks goes and locks himself with the boulder
        for (int i = 0; i < 3; i++) ctr.tick(null, Direction.RIGHT);
        for (int i = 0; i < 2; i++) ctr.tick(null, Direction.DOWN);
        ctr.tick(null, Direction.LEFT);
        ctr.tick(null, Direction.UP);

        int ticks_used = 3 + 2 + 1 + 1;
        assert ticks_used <= Mercenary.SPAWN_EVERY_N_TICKS; 

        // tick until a mercenary spawns, so that we line up
        for (int i = ticks_used; i < Mercenary.SPAWN_EVERY_N_TICKS; i++) ctr.tick(null, Direction.NONE);

        // player is now safe (apart from spiders)
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < Mercenary.SPAWN_EVERY_N_TICKS; j++) {
                resp = ctr.tick(null, Direction.NONE);
            }
            assertEquals(1 + i, TestUtils.countEntitiesOfType(resp, Mercenary.STRING_TYPE), "on " + i + "th");
        }

    }
}
