package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.Spider;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class SpiderTest {
    @Test
    public void testSpiderSpawn() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);

        assertTrue(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Spider.STRING_TYPE)));
        resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        assertTrue(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Spider.STRING_TYPE)));
    }

    @Test
    public void testSpiderMovment() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        assertTrue(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Spider.STRING_TYPE)));

        Map<String, Pos2d> positions = new HashMap<>();

        for (int i = 1; i < 200; i++) {
            resp = ctr.tick("", Direction.NONE);
            for (EntityResponse er : resp.getEntities()) {
                if (er.getType() != Spider.STRING_TYPE) {
                    continue;
                }
                
                // make sure it only moved on cell, horizontally or vertically
                Pos2d prev = positions.get(er.getId());
                Pos2d curr = Pos2d.from(er.getPosition());
                if (prev != null)
                    assertTrue(prev.squareDistance(curr) <= 1);
                positions.put(er.getId(), curr);
                
            }
        }
    }

}
