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
    public void testBuildSceptre1() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_buildable", GameMode.PEACEFUL.getValue());
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(Shield.STRING_TYPE);
        });
        resp = ctr.tick(null, Direction.RIGHT); // pick up the wood
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(Shield.STRING_TYPE);
        });
        resp = ctr.tick(null, Direction.RIGHT); // pick up the second wood
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(Shield.STRING_TYPE);
        });
        resp = ctr.tick(null, Direction.RIGHT); // pick up the treasure
        List<String> actual = resp.getInventory().stream().map(ir -> ir.getType()).collect(Collectors.toList());
        List<String> expected = Arrays.asList(Wood.STRING_TYPE, Wood.STRING_TYPE, Treasure.STRING_TYPE);
        Collections.sort(actual);
        Collections.sort(expected);
        assertEquals(expected, actual);

        actual.clear();
        assertDoesNotThrow(() -> {
            DungeonResponse r = ctr.build(Shield.STRING_TYPE);
            actual.addAll(r.getInventory().stream().map(ir -> ir.getType()).collect(Collectors.toList()));
        });
        assertEquals(List.of(Shield.STRING_TYPE), actual);
    }
}
