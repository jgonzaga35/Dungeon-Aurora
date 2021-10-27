package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.util.FileLoader;

public class TestMap {
    @Test
    public void testMapLoad() {

        assertDoesNotThrow(() -> {
            String content = FileLoader.loadResourceFile("/dungeons/_simple.json");
            Dungeon dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));

            System.out.println(dungeon.getMap().toString());
        });
    }

    @Test
    public void testMapFlood() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_simple.json");
        Dungeon dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));

        dungeon.getMap().flood();

        System.out.println(dungeon.getMap().toString());

        assertEquals(1, dungeon.getMap().getCell(1, 0).getPlayerDistance());
        assertEquals(2, dungeon.getMap().getCell(2, 0).getPlayerDistance());
        assertEquals(3, dungeon.getMap().getCell(3, 0).getPlayerDistance());
        assertEquals(4, dungeon.getMap().getCell(4, 0).getPlayerDistance());
    }
}
