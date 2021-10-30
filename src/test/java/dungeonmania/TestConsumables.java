package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.ConsumableEntity;
import dungeonmania.entities.collectables.consumables.InvincibilityPotion;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class TestConsumables {
    DungeonManiaController dc;
    Dungeon dungeon;

    @BeforeEach
    public void setStartingPostition() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_test_potions.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();
        dc = new DungeonManiaController(dungeon);
    }

    @Test
    public void basicConsumeableTest() {
        InvincibilityPotion potion = new InvincibilityPotion(dungeon, new Pos2d(3, 2));
        List<ConsumableEntity> testList = new ArrayList<>();
        potion.onItemUse(n -> testList.add(n));
        
        potion.use();
        potion.use();
        
        assertEquals(2, testList.size());
    }
    
    @Test
    public void testInvincibilityFleeBehaviour() {
        Cell zomCell = dungeon.getMap().getCell(5, 9);
        ZombieToast zombie = new ZombieToast(dungeon, zomCell.getPosition());
        zomCell.addOccupant(zombie);

        dc.tick(null, Direction.UP);
        dc.tick(null, Direction.DOWN);
        

        Integer zomDist = zombie.getCell().getPlayerDistance();
        dc.tick("invincibility_potion", Direction.NONE);
        for (int i = 0; i < 5; i++) {
            assertTrue(zomDist < zombie.getCell().getPlayerDistance());
            zomDist = zombie.getCell().getPlayerDistance();
            dc.tick(null, Direction.NONE);
        }
        

    }
}
