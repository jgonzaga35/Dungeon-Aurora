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
import dungeonmania.entities.movings.Spider;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class TestSpider {
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
        DungeonResponse resp = ctr.newGame("_simple", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        assertTrue(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Spider.STRING_TYPE)));

        Map<String, Pos2d> positions = new HashMap<>();

        for (int i = 1; i < 50; i++) {
            resp = ctr.tick("", Direction.NONE);
            for (EntityResponse spider : resp.getEntities()) {
                if (spider.getType() != Spider.STRING_TYPE) {
                    continue;
                }
                // make sure it only moved one cell, horizontally or vertically
                Pos2d prev = positions.get(spider.getId());
                Pos2d curr = Pos2d.from(spider.getPosition());
                if (prev != null) {
                    int distance = prev.squareDistance(curr);
                    assertTrue(distance == 1);
                }
                    
                positions.put(spider.getId(), curr);
                
            }
        }
    }

    @Test
    public void testSpiderId() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.STANDARD.getValue());

        List<String> idList = resp.getEntities().stream().map(e -> e.getId()).collect(Collectors.toList());
        Set<String> idSet = new HashSet<String>(idList);

        assertTrue(idList.size() == idSet.size());
        
        resp = ctr.tick("", Direction.DOWN);

        idList = resp.getEntities().stream().map(e -> e.getId()).collect(Collectors.toList());
        idSet = new HashSet<String>(idList);

        assertTrue(idList.size() == idSet.size());

        assertTrue(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Spider.STRING_TYPE)));

        resp = ctr.tick("", Direction.DOWN);
        assertTrue(resp.getEntities().stream().anyMatch(x -> x.getType().equals(Spider.STRING_TYPE)));
        idList = resp.getEntities().stream().map(e -> e.getId()).collect(Collectors.toList());
        idSet = new HashSet<String>(idList);

        assertTrue(idList.size() == idSet.size());
    }


    @Test
    public void testNullPointerError() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
    
        for (int i = 0; i < 50; i++) {
            resp = ctr.tick("", Direction.NONE);
        }
    }

    @Test
    public void testSpiderSpawnLimit() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());

        for (int i = 0; i < 10; i ++) {
            resp = ctr.tick("", Direction.NONE);
        }
        long numSpiders = resp.getEntities().stream().filter(x -> x.getType().equals(Spider.STRING_TYPE)).count();
        assertTrue(numSpiders <= 5);
    }

}
