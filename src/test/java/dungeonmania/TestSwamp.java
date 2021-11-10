package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.Spider;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class TestSwamp {
    DungeonManiaController dc;
    Dungeon dungeon;
    Player player;

    @BeforeEach
    public void setStartingPosition() throws IOException 
    {
        String content = FileLoader.loadResourceFile("/dungeons/_test_swamp_blank.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dc = new DungeonManiaController(dungeon);
        player = TestUtils.getPlayer(dungeon);

        dungeon.getMap().flood();
    }

    @Test
    public void testMultipleEnemies() {
        for (int i = 0; i < 4; i++) dc.tick(null, Direction.DOWN); // move player horizontal to the swamp

        // spawn mercenaries staggered horizontal to the swamp.
        Mercenary merc1 = TestUtils.spawnMercenary(dungeon, 6, 4);
        Mercenary merc2 = TestUtils.spawnMercenary(dungeon, 8, 4);
        
        for (int i = 0; i < 2; i++) dc.tick(null, Direction.NONE); // merc1 gets stuck 10 ticks left

        for (int i = 0; i < 2; i++) dc.tick(null, Direction.NONE); // merc2 gets stuck merc1 8 ticks left
        assertEquals(new Pos2d(4, 4), merc1.getPosition());
        assertEquals(new Pos2d(4, 4), merc2.getPosition());
        
        for (int i = 0; i < 8; i++) dc.tick(null, Direction.NONE); // merc1 can move on the next tick merc2 2 ticks left
        assertEquals(new Pos2d(4, 4), merc1.getPosition());
        assertEquals(new Pos2d(4, 4), merc2.getPosition());
        
        for (int i = 0; i < 2; i++) dc.tick(null, Direction.NONE); // merc1 has moved merc2 can move next tick
        assertEquals(new Pos2d(2, 4), merc1.getPosition());
        assertEquals(new Pos2d(4, 4), merc2.getPosition());
        
        dc.tick(null, Direction.NONE);
        assertEquals(new Pos2d(1, 4), merc1.getPosition());
        assertEquals(new Pos2d(3, 4), merc2.getPosition());
    }
    
    @Test
    public void testSpiderPreservesCircle() {
        Spider spider = TestUtils.spawnSpider(dungeon, 3, 4);
        
        for (int i = 0; i < 3; i++) dc.tick(null, Direction.NONE); // steps in swamp after this 10 ticks to go
        assertEquals(new Pos2d(4, 4), spider.getPosition());
        
        for (int i = 0; i < 10; i++) dc.tick(null, Direction.NONE); // can move on next tick
        assertEquals(new Pos2d(4, 4), spider.getPosition());
        
        // Make sure spider still follows the circle
        dc.tick(null, Direction.NONE);
        assertEquals(new Pos2d(4, 5), spider.getPosition());
        dc.tick(null, Direction.NONE);
        assertEquals(new Pos2d(3, 5), spider.getPosition());
        dc.tick(null, Direction.NONE);
        assertEquals(new Pos2d(2, 5), spider.getPosition());
        dc.tick(null, Direction.NONE);
        assertEquals(new Pos2d(2, 4), spider.getPosition());
        dc.tick(null, Direction.NONE);
        assertEquals(new Pos2d(2, 3), spider.getPosition());
        dc.tick(null, Direction.NONE);
        assertEquals(new Pos2d(3, 3), spider.getPosition());
    }

    @Test
    public void testPlayerGetsStuck() {
        for (int i = 0; i < 4; i++) dc.tick(null, Direction.DOWN);
        for (int i = 0; i < 4; i++) dc.tick(null, Direction.RIGHT); // (4, 4) should now be stuck for 10 more ticks.
        
        for (int i = 0; i < 2; i++) dc.tick(null, Direction.NONE);
        assertEquals(new Pos2d(4, 4), player.getPosition());
        for (int i = 0; i < 2; i++) dc.tick(null, Direction.DOWN);
        assertEquals(new Pos2d(4, 4), player.getPosition());
        for (int i = 0; i < 2; i++) dc.tick(null, Direction.LEFT);
        assertEquals(new Pos2d(4, 4), player.getPosition());
        for (int i = 0; i < 2; i++) dc.tick(null, Direction.UP);
        assertEquals(new Pos2d(4, 4), player.getPosition());
        for (int i = 0; i < 2; i++) dc.tick(null, Direction.RIGHT); // should now be able to move on the next tick
        assertEquals(new Pos2d(4, 4), player.getPosition());
        
        for (int i = 0; i < 4; i++) dc.tick(null, Direction.UP);
        for (int i = 0; i < 4; i++) dc.tick(null, Direction.LEFT); 
        
        assertEquals(new Pos2d(0, 0), player.getPosition());
    }

    @Test
    public void testZombieSpawnInSwamp() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_test_swamp_zombie.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dc = new DungeonManiaController(dungeon);

        ZombieToast zom = TestUtils.getZombieToast(dungeon);

        for (int i = 0; i < 6; i++) {
            assertEquals(new Pos2d(2, 2), zom.getPosition()); // stay in the same spot for 5 ticks
            dc.tick(null, Direction.NONE);
        }
        assertNotEquals(new Pos2d(2, 2), zom.getPosition()); // move after the 6th tick
    }
    
    @Test
    public void testStuckAfterBribe() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_test_swamp.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dc = new DungeonManiaController(dungeon);
        
        Mercenary merc =  TestUtils.getMercenary(dungeon);
        
        dc.tick(null, Direction.RIGHT); // get coin 1 0
        dc.tick(null, Direction.RIGHT); // get coin 2 0
        dc.tick(null, Direction.DOWN); // get coin 2 1
        
        assertEquals(new Pos2d(2, 2), merc.getPosition()); // need to stay here for 7 more ticks
        dc.interact(merc.getId());
        
        dc.tick(null, Direction.UP); // get coin 2 0
        dc.tick(null, Direction.LEFT); // get coin 1 0
        dc.tick(null, Direction.LEFT); // get coin 0 0
        
        assertEquals(new Pos2d(2, 2), merc.getPosition()); // need to stay here for 4 more ticks
        
        for (int i = 0; i < 4; i++) dc.tick(null, Direction.NONE);
        
        assertEquals(new Pos2d(2, 2), merc.getPosition()); // can move on the next tick
        
        for (int i = 0; i < 5; i++) dc.tick(null, Direction.NONE); // merc should be next to player after this

        assertTrue(
            merc.getPosition().equals(new Pos2d(1, 0)) ||
            merc.getPosition().equals(new Pos2d(0, 1))
        );
    }
    
}
