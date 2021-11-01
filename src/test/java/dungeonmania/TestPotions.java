package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.consumables.HealthPotion;
import dungeonmania.entities.collectables.consumables.InvincibilityPotion;
import dungeonmania.entities.collectables.consumables.InvisibilityPotion;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.Spider;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class TestPotions {
    DungeonManiaController dc;
    Dungeon dungeon;
    InvincibilityPotion invincibilityPot;
    InvisibilityPotion invisPot;
    HealthPotion healthPot;

    @BeforeEach
    public void setStartingPostition() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_test_potions.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();

        Cell invincibleCell = dungeon.getMap().getCell(5, 4);
        invincibilityPot= new InvincibilityPotion(dungeon, invincibleCell.getPosition());
        invincibleCell.addOccupant(invincibilityPot);
        
        Cell invisibleCell = dungeon.getMap().getCell(5, 6);
        invisPot= new InvisibilityPotion(dungeon, invisibleCell.getPosition());
        invisibleCell.addOccupant(invisPot);
        Cell healthCell = dungeon.getMap().getCell(4, 5);
        healthPot = new HealthPotion(dungeon, healthCell.getPosition());
        healthCell.addOccupant(healthPot);
        
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
    public void testInvincibilityBattles() {        
        dc.tick(null, Direction.UP);
        
        // use pot
        dc.tick(invincibilityPot.getId(), Direction.NONE);
        
        // player at 5, 4
        Cell mercCell = dungeon.getMap().getCell(5, 5);
        Mercenary merc = new Mercenary(dungeon, mercCell.getPosition());
        mercCell.addOccupant(merc);
        
        Cell mercCell2 = dungeon.getMap().getCell(5, 3);
        Mercenary merc2 = new Mercenary(dungeon, mercCell2.getPosition());
        mercCell2.addOccupant(merc2);
        
        Cell mercCell3 = dungeon.getMap().getCell(4, 4);
        Mercenary merc3 = new Mercenary(dungeon, mercCell3.getPosition());
        mercCell3.addOccupant(merc3);
        
        Cell mercCell4 = dungeon.getMap().getCell(6, 4);
        Mercenary merc4 = new Mercenary(dungeon, mercCell4.getPosition());
        mercCell4.addOccupant(merc4);
        
        // battle where player would have died.
        DungeonResponse dr = dc.tick(null, Direction.NONE);
        
        assertEquals(1, TestUtils.countEntitiesOfType(dr, "player"));
        
        // remove Potion effect
        for (int i = 0; i < 7; i++) dr = dc.tick(null, Direction.NONE);
        
        // now player should die
        
        Cell mercCell5 = dungeon.getMap().getCell(5, 5);
        Mercenary merc5 = new Mercenary(dungeon, mercCell5.getPosition());
        mercCell5.addOccupant(merc5);
        
        Cell mercCell6 = dungeon.getMap().getCell(5, 3);
        Mercenary merc6 = new Mercenary(dungeon, mercCell6.getPosition());
        mercCell6.addOccupant(merc6);
        
        Cell mercCell7 = dungeon.getMap().getCell(4, 4);
        Mercenary merc7 = new Mercenary(dungeon, mercCell7.getPosition());
        mercCell7.addOccupant(merc7);
        
        Cell mercCell8 = dungeon.getMap().getCell(6, 4);
        Mercenary merc8 = new Mercenary(dungeon, mercCell8.getPosition());
        mercCell8.addOccupant(merc8);
        
        // battle where player should died.
        dr = dc.tick(null, Direction.NONE);
        
        assertEquals(0, TestUtils.countEntitiesOfType(dr, "player"));
        
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
    
    @Test
    public void testInvisibilityMovement() {
        
        dc.tick(null, Direction.DOWN);

        Cell mercCell = dungeon.getMap().getCell(2, 2);
        Mercenary merc = new Mercenary(dungeon, mercCell.getPosition());
        mercCell.addOccupant(merc);
        
        Integer mercDist = merc.getCell().getPlayerDistance();
        
        // use pot
        dc.tick(invisPot.getId(), Direction.NONE);
        
        // Highly unlikely that the merc always moves closer when moving randomly.
        boolean hasNotMovedCloser = false;
        for (int i = 0; i < 9; i++) {
            if (mercDist < merc.getCell().getPlayerDistance()) hasNotMovedCloser = true;
            mercDist = merc.getCell().getPlayerDistance();
            dc.tick(null, Direction.NONE);
        }

        assertTrue(hasNotMovedCloser);
    }
    
    @Test
    public void testInvisibilityNoBattles() throws IOException{
        String content = FileLoader.loadResourceFile("/dungeons/_force_battle.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();
        
        Cell invisibleCell = dungeon.getMap().getCell(0, 1);
        invisPot= new InvisibilityPotion(dungeon, invisibleCell.getPosition());
        invisibleCell.addOccupant(invisPot);
        
        dc = new DungeonManiaController(dungeon);
        // player in (0, 0) with no inventory
        
        dc.tick(null, Direction.DOWN);
        
        Cell mercCell = dungeon.getMap().getCell(0, 0);
        Mercenary merc = new Mercenary(dungeon, mercCell.getPosition());
        mercCell.addOccupant(merc);
        
        // use pot merc is forced to move down.
        DungeonResponse dr = dc.tick(invisPot.getId(), Direction.NONE);

        // battle should have been avoided

        assertEquals(1, TestUtils.countEntitiesOfType(dr, "mercenary"));
            
    @Test
    public void testHealthPotionRefill() {
        Cell mercCell = dungeon.getMap().getCell(5, 7);
        Mercenary merc = new Mercenary(dungeon, mercCell.getPosition());
        mercCell.addOccupant(merc);

        Player player = (Player) dungeon.getMap().allEntities().stream().filter(e -> e instanceof Player).findFirst().get();
        
        dc.tick(null, Direction.LEFT);
        
        // battle should damage player;
        for (int i = 0; i < 5; i++) dc.tick(null, Direction.NONE);

        assertTrue(player.getHealth() < 10);

        // use pot
        dc.tick(healthPot.getId(), Direction.NONE);

        assertTrue(player.getHealth() - 10 < 0.01);
        
    }
}
        