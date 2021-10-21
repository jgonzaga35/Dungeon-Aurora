package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
}
