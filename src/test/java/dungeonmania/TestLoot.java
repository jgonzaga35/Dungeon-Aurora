package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Random;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.Armour;
import dungeonmania.entities.movings.Player;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

/**
 * Force battles on the player while constantly healing him up and check loot
 * distributions.
 */
public class TestLoot {
    DungeonManiaController dc;
    Dungeon dungeon;
    Player player;

    @BeforeEach
    public void setStartingPostition() throws IOException 
    {
        String content = FileLoader.loadResourceFile("/dungeons/_tiny.json");
        dungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD, new JSONObject(content));
        dc = new DungeonManiaController(dungeon);
        player = TestUtils.getPlayer(dungeon);

        dungeon.getMap().flood();
    }

    @Test
    public void testZombieDrops() {
        DungeonResponse r = null;
        for (int i = 0; i < 100; i ++) {
            TestUtils.spawnZombieToast(dungeon, 0, 0);
            r = dc.tick(null, Direction.NONE);
            player.setHealth(20);
        }

        Integer armourCount = (int) TestUtils.countInventoryOfType(r, Armour.STRING_TYPE);

        // Probability of failing on a valid solution = 0.00763 
        assertTrue(armourCount > 5 && armourCount < 25);
    }

    @Test
    public void testMercenaryDrops() {
        DungeonResponse r = null;
        for (int i = 0; i < 100; i ++) {
            TestUtils.spawnMercenary(dungeon, 0, 0);
            r = dc.tick(null, Direction.NONE);
            player.setHealth(20);
        }

        Integer armourCount = (int) TestUtils.countInventoryOfType(r, Armour.STRING_TYPE);

        // Probability of failing on a valid solution = 0.00149 
        assertTrue(armourCount > 15 && armourCount < 45);
    }

    @Test
    public void testAssassinDrops() {
        DungeonResponse r = null;
        for (int i = 0; i < 100; i ++) {
            TestUtils.spawnAssassin(dungeon, 0, 0);
            r = dc.tick(null, Direction.NONE);
            player.setHealth(20);
        }

        Integer armourCount = (int) TestUtils.countInventoryOfType(r, Armour.STRING_TYPE);

        // Probability of failing on a valid solution = 0.00149 
        assertTrue(armourCount > 15 && armourCount < 45);
    }
    
}
