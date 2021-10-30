package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.ConsumableEntity;
import dungeonmania.entities.collectables.consumables.InvincibilityPotion;
import dungeonmania.util.FileLoader;

public class TestConsumables {
    Dungeon dungeon;
    ConsumableEntity consumable;

    @BeforeEach
    public void setStartingPostition() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_simple.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();
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
}
