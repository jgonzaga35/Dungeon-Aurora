package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.collectables.buildables.*;
import dungeonmania.exceptions.*;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.*;

public class TestBuildableV2 {
    @Test
    public void testBuildSceptreInvalid() {
        DungeonManiaController ctr = new DungeonManiaController();
        ctr.newGame("_buildable", GameMode.PEACEFUL.getValue());
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(Sceptre.STRING_TYPE);
        });
    }

    @Test
    public void testBuildSceptre1() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_buildable_sceptre", GameMode.PEACEFUL.getValue());
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(Sceptre.STRING_TYPE);
        });

        // pick up the materials
        resp = ctr.tick(null, Direction.LEFT); 
        resp = ctr.tick(null, Direction.UP); 
        resp = ctr.tick(null, Direction.DOWN); 
        resp = ctr.tick(null, Direction.RIGHT); 
        resp = ctr.tick(null, Direction.DOWN); 

        List<String> actual = resp.getInventory().stream().map(item -> item.getType()).collect(Collectors.toList());
        List<String> expected = Arrays.asList(Wood.STRING_TYPE, Key.STRING_TYPE, SunStone.STRING_TYPE);
        Collections.sort(actual);
        Collections.sort(expected);
        assertEquals(expected, actual);
        actual.clear();

        assertDoesNotThrow(() -> {
            DungeonResponse r = ctr.build(Sceptre.STRING_TYPE);
            actual.addAll(r.getInventory().stream().map(ir -> ir.getType()).collect(Collectors.toList()));
        });
        assertEquals(List.of(Sceptre.STRING_TYPE), actual);
    }
}
