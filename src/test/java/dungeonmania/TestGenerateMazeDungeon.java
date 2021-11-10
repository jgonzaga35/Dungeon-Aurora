package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.statics.Exit;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Position;

public class TestGenerateMazeDungeon {
    @Test
    public void testRightStartAndEnd() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.generateDungeon(1, 3, 47, 45, GameMode.PEACEFUL.getValue());

        Position p = TestUtils.getPlayerPosition(resp);
        assertEquals(1, p.getX());
        assertEquals(3, p.getY());

        assertEquals(1, resp.getEntities().stream().filter(e -> e.getType().equals(Exit.STRING_TYPE)).count());
        Position exitPosition = resp.getEntities().stream()
            .filter(e -> e.getType().equals(Exit.STRING_TYPE)).findFirst().get().getPosition();
        assertEquals(47, exitPosition.getX());
        assertEquals(45, exitPosition.getY());
    }
    
}
