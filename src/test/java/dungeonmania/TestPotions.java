package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.consumables.InvincibilityPotion;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class TestPotions {
    DungeonManiaController dc;
    Dungeon dungeon;
    InvincibilityPotion invinsiblePot;

    @BeforeEach
    public void setStartingPostition() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_test_potions.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();

        
        Cell invinsibleCell = dungeon.getMap().getCell(5, 4);
        invinsiblePot= new InvincibilityPotion(dungeon, invinsibleCell.getPosition());
        invinsibleCell.addOccupant(invinsiblePot);
        
        dc = new DungeonManiaController(dungeon);
        // player in (5, 5) with no inventory
    }
    
    @Test
    public void testInvincibilityFleeBehaviour() {
        Cell zomCell = dungeon.getMap().getCell(5, 7);
        ZombieToast zombie = new ZombieToast(dungeon, zomCell.getPosition());
        zomCell.addOccupant(zombie);
        
        dc.tick(null, Direction.UP);
        
        Integer zomDist = zombie.getCell().getPlayerDistance();

        // use pot
        dc.tick(invinsiblePot.getId(), Direction.NONE);

        for (int i = 0; i < 5; i++) {
            assertTrue(zomDist < zombie.getCell().getPlayerDistance());
            zomDist = zombie.getCell().getPlayerDistance();
            dc.tick(null, Direction.NONE);
        }
    }
}
