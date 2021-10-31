package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class TestPotions {
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
