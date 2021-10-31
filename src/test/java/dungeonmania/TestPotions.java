package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.consumables.InvincibilityPotion;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.movings.Spider;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class TestPotions {
    DungeonManiaController dc;
    Dungeon dungeon;
    InvincibilityPotion invincibilityPot;

    @BeforeEach
    public void setStartingPostition() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_test_potions.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();

        
        Cell invincibleCell = dungeon.getMap().getCell(5, 4);
        invincibilityPot= new InvincibilityPotion(dungeon, invincibleCell.getPosition());
        invincibleCell.addOccupant(invincibilityPot);
        
        dc = new DungeonManiaController(dungeon);
        // player in (5, 5) with no inventory
    }
    
    @Test
    public void testInvincibilityFleeBehaviour() {
        Cell mercCell = dungeon.getMap().getCell(5, 7);
        Mercenary merc = new Mercenary(dungeon, mercCell.getPosition());
        mercCell.addOccupant(merc);
        
        dc.tick(null, Direction.UP);
        
        Integer mercDist = merc.getCell().getPlayerDistance();
        
        // use pot
        dc.tick(invincibilityPot.getId(), Direction.NONE);
        
        for (int i = 0; i < 5; i++) {
            assertTrue(mercDist < merc.getCell().getPlayerDistance());
            mercDist = merc.getCell().getPlayerDistance();
            dc.tick(null, Direction.NONE);
        }
    }
    
    @Test
    public void testInvincibilityAffectsNewSpawns() {        
        dc.tick(null, Direction.UP);
        
        // use pot
        dc.tick(invincibilityPot.getId(), Direction.NONE);
        
        Cell mercCell = dungeon.getMap().getCell(5, 7);
        Mercenary merc = new Mercenary(dungeon, mercCell.getPosition());
        mercCell.addOccupant(merc);
        
        Integer mercDist = merc.getCell().getPlayerDistance();
        // 4 ticks left
        for (int i = 0; i < 4; i++) {
            dc.tick(null, Direction.NONE);
            assertTrue(mercDist < merc.getCell().getPlayerDistance());
            mercDist = merc.getCell().getPlayerDistance();
        }
    }
    
    @Test
    public void testInvincibilityDuration() {        
        dc.tick(null, Direction.UP);
        
        // use pot
        dc.tick(invincibilityPot.getId(), Direction.NONE);
        
        Cell mercCell = dungeon.getMap().getCell(5, 7);
        Mercenary merc = new Mercenary(dungeon, mercCell.getPosition());
        mercCell.addOccupant(merc);
        
        Integer mercDist = merc.getCell().getPlayerDistance();
        for (int i = 0; i < 4; i++) {
            dc.tick(null, Direction.NONE);
            assertTrue(mercDist < merc.getCell().getPlayerDistance());
            mercDist = merc.getCell().getPlayerDistance();
        }

        for (int i = 0; i < 4; i++) {
            dc.tick(null, Direction.NONE);
            assertTrue(mercDist > merc.getCell().getPlayerDistance());
            mercDist = merc.getCell().getPlayerDistance();
        }
    }
    
    @Test
    public void testNotUsingPotion() {        
        dc.tick(null, Direction.UP);
        
        Cell mercCell = dungeon.getMap().getCell(8, 8);
        Mercenary merc = new Mercenary(dungeon, mercCell.getPosition());
        mercCell.addOccupant(merc);
        
        Integer mercDist = merc.getCell().getPlayerDistance();
        for (int i = 0; i < 4; i++) {
            dc.tick(null, Direction.NONE);
            assertTrue(mercDist > merc.getCell().getPlayerDistance());
            mercDist = merc.getCell().getPlayerDistance();
        }
    }

    @Test
    public void testInvincibilityOnSpiders() {
        Cell spiderCell = dungeon.getMap().getCell(5, 6);
        Spider spider = new Spider(dungeon, spiderCell.getPosition());
        spiderCell.addOccupant(spider);
        
        dc.tick(null, Direction.UP);
        // Spider moves up into loop, next step would have been to go right.
        
        Integer spiderDist = spider.getCell().getPlayerDistance();
        
        // use pot
        dc.tick(invincibilityPot.getId(), Direction.NONE);
        
        for (int i = 0; i < 4; i++) {
            assertTrue(spiderDist < spider.getCell().getPlayerDistance());
            spiderDist = spider.getCell().getPlayerDistance();
            dc.tick(null, Direction.NONE);
        }
        assertTrue(
            spider.getCell().getPosition().equals(new Pos2d(8, 7)) ||
            spider.getCell().getPosition().equals(new Pos2d(7, 8)) ||
            spider.getCell().getPosition().equals(new Pos2d(0, 7)) ||
            spider.getCell().getPosition().equals(new Pos2d(3, 8))
        );
        
        // spider should now continue into the loop by moving right or bouncing back.
        dc.tick(null, Direction.NONE);
        assertTrue(
            spider.getCell().getPosition().equals(new Pos2d(8, 7)) || // bounce
            spider.getCell().getPosition().equals(new Pos2d(8, 8)) || // right
            spider.getCell().getPosition().equals(new Pos2d(1, 7)) || // right
            spider.getCell().getPosition().equals(new Pos2d(4, 8))    // right
            );
            
        dc.tick(null, Direction.NONE);
        // then down or left
        assertTrue(
            spider.getCell().getPosition().equals(new Pos2d(7, 7)) || // left
            spider.getCell().getPosition().equals(new Pos2d(8, 8)) || // bounce
            spider.getCell().getPosition().equals(new Pos2d(1, 8)) || // down
            spider.getCell().getPosition().equals(new Pos2d(4, 8))    // bounce
            );
                
                
        dc.tick(null, Direction.NONE);
        assertTrue(
            spider.getCell().getPosition().equals(new Pos2d(6, 7)) || // left
            spider.getCell().getPosition().equals(new Pos2d(7, 8)) || // left
            spider.getCell().getPosition().equals(new Pos2d(1, 8)) || // bounce
            spider.getCell().getPosition().equals(new Pos2d(3, 8))    // left
        );
    }
}
