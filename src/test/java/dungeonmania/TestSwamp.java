package dungeonmania;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.Player;
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
    
}
