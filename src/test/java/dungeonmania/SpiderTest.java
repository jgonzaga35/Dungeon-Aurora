package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
                // make sure it only moved one cell, horizontally or vertically
                Pos2d prev = positions.get(er.getId());
                Pos2d curr = Pos2d.from(er.getPosition());
                if (prev != null)
                    assertTrue(prev.squareDistance(curr) == 1);
                positions.put(er.getId(), curr);
                
            }
        }
    }

    @Test
    public void testSpiderId() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        List<String> idList = resp.getEntities().stream().map(e -> e.getId()).collect(Collectors.toList());
        Set<String> idSet = new HashSet<String>(idList);

        assertTrue(idList.size() == idSet.size());
        

        resp = ctr.tick("", Direction.NONE);

        idList = resp.getEntities().stream().map(e -> e.getId()).collect(Collectors.toList());
        idSet = new HashSet<String>(idList);

        assertTrue(idList.size() == idSet.size());

        assertTrue(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Spider.STRING_TYPE)));
        resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        assertTrue(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Spider.STRING_TYPE)));
    }


    @Test
    public void testNull() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
    
        for (int i = 0; i < 5000; i++) {
            resp = ctr.tick("", Direction.NONE);

        }
    }

}
