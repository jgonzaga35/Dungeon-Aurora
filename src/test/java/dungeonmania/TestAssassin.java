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
import dungeonmania.entities.movings.Assassin;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.movings.Player;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class TestAssassin {
    DungeonManiaController dc;
    Dungeon dungeon;
    Player player;
    Assassin assassin;

    @BeforeEach
    public void setStartingPostition() throws IOException 
    {
        String content = FileLoader.loadResourceFile("/dungeons/_assassin_test.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dc = new DungeonManiaController(dungeon);
        player = (Player) dungeon.getMap().allEntities().stream()
            .filter(e -> e instanceof Player)
            .findFirst().get();
        assassin = (Assassin) dungeon.getMap().allEntities().stream()
            .filter(e -> e instanceof Assassin)
            .findFirst().get();

        dungeon.getMap().flood();
    }

    /**
     * Testing that the assassin move correctly and follows the player 
     * They fight and the assassin should kill the player.
     */
    @Test
    public void testHostileMovement() {
        DungeonResponse resp;
        Integer dist = assassin.getCell().getPlayerDistance();

        for (int i = 0; i < 2; i++) {
            resp = dc.tick(null, Direction.RIGHT);

            assertTrue(assassin.getCell().getPlayerDistance() == dist);
            dist = assassin.getCell().getPlayerDistance();
        }
        // player pos (7, 5)
        
        for (int i = 0; i < 5; i++) {
            resp = dc.tick(null, Direction.UP);
            assertTrue(assassin.getCell().getPlayerDistance() <= dist);
            dist = assassin.getCell().getPlayerDistance();
        }
        // player pos (7, 0)
        
        for (int i = 0; i < 5; i++) {
            resp = dc.tick(null, Direction.NONE);
            assertTrue(assassin.getCell().getPlayerDistance() < dist);
            dist = assassin.getCell().getPlayerDistance();
        }
        // player pos (7, 0)
        // assassin pos (6, 0)
        
        resp = dc.tick(null, Direction.UP);
        // player pos (7, 0)
        // assassin pos (7, 0)
        // battle should happen
        // Assassin should kill the player
        assertEquals(1, TestUtils.countEntitiesOfType(resp, Assassin.STRING_TYPE));
        assertEquals(0, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
        assertEquals(new Pos2d(7, 0), assassin.getPosition());
    }

    /**
     * Test that the assassin stays one square away from the player when it is 
     * friendly.
     */
    @Test
    public void testFriendlyMovement() {
        Integer dist = assassin.getCell().getPlayerDistance();

        // pick up treasure.
        for (int i = 0; i < 2; i++) {
            dc.tick(null, Direction.RIGHT);

            assertTrue(assassin.getCell().getPlayerDistance() == dist);
            dist = assassin.getCell().getPlayerDistance();
        }
        // player pos (7, 5)
        
        for (int i = 0; i < 5; i++) {
            dc.tick(null, Direction.UP);
            assertTrue(assassin.getCell().getPlayerDistance() <= dist);
            dist = assassin.getCell().getPlayerDistance();
        }
        // player pos (7, 0) pick up ring
        
        for (int i = 0; i < 5; i++) {
            dc.tick(null, Direction.NONE);
            assertTrue(assassin.getCell().getPlayerDistance() < dist);
            dist = assassin.getCell().getPlayerDistance();
        }
        // player pos (7, 0)
        // assassin pos (6, 0)
        
        dc.interact(assassin.getId());
        dc.tick(null, Direction.UP);
        // player pos (7, 0)
        assertEquals(new Pos2d(6, 0), assassin.getPosition());
        assertEquals(new Pos2d(7, 0), player.getPosition());
        
        dc.tick(null, Direction.LEFT);
        assertTrue(
            assassin.getPosition().equals(new Pos2d(5, 0)) || 
            assassin.getPosition().equals(new Pos2d(6, 1)) ||
            assassin.getPosition().equals(new Pos2d(7, 0))
        );
        // player pos (6, 0)
        
        dc.tick(null, Direction.LEFT);
        
        dc.tick(null, Direction.LEFT);
        assertTrue(assassin.getCell().getPlayerDistance() == 1);
        // player pos (5, 0)
        
        dc.tick(null, Direction.RIGHT);
        assertTrue(assassin.getCell().getPlayerDistance() == 1);
        // player pos (6, 0)
        
        dc.tick(null, Direction.DOWN);
        assertTrue(assassin.getCell().getPlayerDistance() == 1);
        // player pos (6, 1)
        
        dc.tick(null, Direction.UP);
        assertTrue(assassin.getCell().getPlayerDistance() == 1);
        // player pos (6, 0)
    }

    /**
     * Test various edge cases for bribing
     * - out of range
     * - no gold
     * - no ring
     * - successful bribe 
     * - and trying to bribe a friendly assassin.
     */
    @Test
    public void testBribe() {
        Integer dist = assassin.getCell().getPlayerDistance();

        // Try bribing invalid id
        assertThrows(IllegalArgumentException.class, () -> {
            dc.interact("invalid");
        });
        
        
        // Try bribing with no money
        assertThrows(InvalidActionException.class, () -> {
            dc.interact(assassin.getId());
        });
        
        for (int i = 0; i < 2; i++) {
            dc.tick(null, Direction.RIGHT);
            
            assertTrue(assassin.getCell().getPlayerDistance() == dist);
            dist = assassin.getCell().getPlayerDistance();
        }
        // player pos (7, 5)
        // Try bribing outside of range
        assertThrows(InvalidActionException.class, () -> {
            dc.interact(assassin.getId());
        });
        
        for (int i = 0; i < 4; i++) {
            dc.tick(null, Direction.UP);
            assertTrue(assassin.getCell().getPlayerDistance() <= dist);
            dist = assassin.getCell().getPlayerDistance();
        }
        // player pos (7, 1)
        
        for (int i = 0; i < 4; i++) {
            dc.tick(null, Direction.NONE);
            assertTrue(assassin.getCell().getPlayerDistance() < dist);
            dist = assassin.getCell().getPlayerDistance();
        }
        // player pos (7, 1)
        // assassin pos (5, 1)
        // two cardinal squares away, bribe possible but no ring
        
        // Try bribing with no ring
        assertThrows(InvalidActionException.class, () -> {
            dc.interact(assassin.getId());
        });
        
        // pick up the one ring
        dc.tick(null, Direction.UP);
        // player pos (7, 0)
        // assassin pos (5, 0)
        // two cardinal squares away, bribe possible but no ring
        
        assertDoesNotThrow(() -> dc.interact(assassin.getId()));    
        
        // Try bribing friendly
        assertThrows(IllegalArgumentException.class, () -> {
            dc.interact(assassin.getId());
        });
    }
    
    /**
     * Makes sure assassins spawn at the right frequencies.
     */
    @Test
    public void testSpawn() {
        // black box test because we don't need too much control

        DungeonManiaController ctr = new DungeonManiaController();
        // map in which the player can lock himself in so that we can spawn a
        // lot of mercenaries and test probability distributions (for assassins)
        DungeonResponse resp = ctr.newGame("_mercenary_zoo", GameMode.STANDARD.getValue());
        ctr.setSeed(1);
        assertEquals(0, TestUtils.countEntitiesOfType(resp, Mercenary.STRING_TYPE));
        
        // player locks goes and locks himself with the boulder
        for (int i = 0; i < 3; i++) ctr.tick(null, Direction.RIGHT);
        for (int i = 0; i < 2; i++) ctr.tick(null, Direction.DOWN);
        ctr.tick(null, Direction.LEFT);
        ctr.tick(null, Direction.UP);
        // player is now safe (apart from spiders)

        // tick until a mercenary spawns, so that we line up with the tick count
        int ticks_used = 3 + 2 + 1 + 1;
        assert ticks_used <= Mercenary.SPAWN_EVERY_N_TICKS; 
        for (int i = ticks_used; i < Mercenary.SPAWN_EVERY_N_TICKS; i++) resp = ctr.tick(null, Direction.NONE);
        assertTrue(
            TestUtils.countEntitiesOfType(resp, Mercenary.STRING_TYPE) == 1 ||
            TestUtils.countEntitiesOfType(resp, Assassin.STRING_TYPE) == 1
        );

        // The chance of no assassins spawning in 50 spawns is 5e-7
        for (int i = 0; i < 50; i++) { // every loop, we spawn a new merc
            for (int j = 0; j < Mercenary.SPAWN_EVERY_N_TICKS; j++) {
                resp = ctr.tick(null, Direction.NONE);
            }
        }

        // Probability of this failing should be 0.033
        assertTrue(
            6 <= TestUtils.countEntitiesOfType(resp, Assassin.STRING_TYPE) &&
            TestUtils.countEntitiesOfType(resp, Assassin.STRING_TYPE) <= 20
        );
    }

    /**
     * Test that assassins don't spawn on empty maps.
     */
    @Test
    public void testNoSpawn() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_mercenary_no_spawn", GameMode.STANDARD.getValue());
        ctr.setSeed(1);
        for (int i = 0; i < 1000; i++) {
            // move down as far as we can so that the Assassin doesn't spawn
            // onto the player (and gets killed)
            resp = ctr.tick(null, Direction.DOWN);
            assertEquals(0, TestUtils.countEntitiesOfType(resp, Assassin.STRING_TYPE), "i=" + i);
        }
    }
}
