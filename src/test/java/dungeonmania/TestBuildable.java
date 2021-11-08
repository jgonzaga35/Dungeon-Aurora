package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.collectables.buildables.Shield;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TestBuildable {
    @Test
    public void testBuildShieldTreasure() {
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

    @Test
    public void testBuildShieldKey() {
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
        resp = ctr.tick(null, Direction.DOWN); // pick up the key
        List<String> actual = resp.getInventory().stream().map(ir -> ir.getType()).collect(Collectors.toList());
        List<String> expected = Arrays.asList(Wood.STRING_TYPE, Wood.STRING_TYPE, Key.STRING_TYPE);
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

    @Test
    public void testUnknownBuildable() {
        DungeonManiaController ctr = new DungeonManiaController();
        ctr.newGame("_buildable", GameMode.PEACEFUL.getValue());
        assertThrows(IllegalArgumentException.class, () -> {
            ctr.build("foobar");
        });
    }
}
