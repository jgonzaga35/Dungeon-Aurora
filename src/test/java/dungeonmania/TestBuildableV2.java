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
    public void testEmptyBuildablesResponseList() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_buildable_sceptre", GameMode.PEACEFUL.getValue());
        assertEquals(List.of(), resp.getBuildables());
    }

    @Test
    public void testBuildSceptre1() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_buildable_sceptre", GameMode.PEACEFUL.getValue());

        // pick up the materials - Wood, Key, Sun Stone
        resp = ctr.tick(null, Direction.LEFT); 
        resp = ctr.tick(null, Direction.UP); 
        resp = ctr.tick(null, Direction.DOWN); 
        resp = ctr.tick(null, Direction.RIGHT); 
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(Sceptre.STRING_TYPE);
        });
        resp = ctr.tick(null, Direction.DOWN); 

        List<String> actual = resp.getInventory().stream().map(item -> item.getType()).collect(Collectors.toList());
        List<String> expected = Arrays.asList(Wood.STRING_TYPE, Key.STRING_TYPE, SunStone.STRING_TYPE);
        Collections.sort(actual);
        Collections.sort(expected);
        assertEquals(expected, actual);
        actual.clear();

        assertEquals(List.of(Sceptre.STRING_TYPE), resp.getBuildables());

        assertDoesNotThrow(() -> {
            DungeonResponse r = ctr.build(Sceptre.STRING_TYPE);
            actual.addAll(r.getInventory().stream().map(ir -> ir.getType()).collect(Collectors.toList()));
        });
        assertEquals(List.of(Sceptre.STRING_TYPE), actual);
    }

    @Test
    public void testBuildSceptre2() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_buildable_sceptre", GameMode.PEACEFUL.getValue());

        // pick up the materials - Wood, Treasure, Sun Stone
        resp = ctr.tick(null, Direction.UP); 
        resp = ctr.tick(null, Direction.LEFT); 
        resp = ctr.tick(null, Direction.RIGHT); 
        resp = ctr.tick(null, Direction.DOWN); 
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(Sceptre.STRING_TYPE);
        });
        resp = ctr.tick(null, Direction.DOWN); 

        List<String> actual = resp.getInventory().stream().map(item -> item.getType()).collect(Collectors.toList());
        List<String> expected = Arrays.asList(Wood.STRING_TYPE, Treasure.STRING_TYPE, SunStone.STRING_TYPE);
        Collections.sort(actual);
        Collections.sort(expected);
        assertEquals(expected, actual);
        actual.clear();

        assertEquals(List.of(Sceptre.STRING_TYPE), resp.getBuildables());

        assertDoesNotThrow(() -> {
            DungeonResponse r = ctr.build(Sceptre.STRING_TYPE);
            actual.addAll(r.getInventory().stream().map(ir -> ir.getType()).collect(Collectors.toList()));
        });
        assertEquals(List.of(Sceptre.STRING_TYPE), actual);
    }

    @Test
    public void testBuildSceptre3() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_buildable_sceptre", GameMode.PEACEFUL.getValue());

        // pick up the materials - Arrow * 2, Treasure, Sun Stone
        resp = ctr.tick(null, Direction.UP); 
        resp = ctr.tick(null, Direction.LEFT); 
        resp = ctr.tick(null, Direction.RIGHT); 
        resp = ctr.tick(null, Direction.DOWN); 
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(Sceptre.STRING_TYPE);
        });
        resp = ctr.tick(null, Direction.RIGHT); 
        resp = ctr.tick(null, Direction.RIGHT); 

        List<String> actual = resp.getInventory().stream().map(item -> item.getType()).collect(Collectors.toList());
        List<String> expected = Arrays.asList(Arrow.STRING_TYPE, Arrow.STRING_TYPE, Treasure.STRING_TYPE, SunStone.STRING_TYPE);
        Collections.sort(actual);
        Collections.sort(expected);
        assertEquals(expected, actual);
        actual.clear();

        assertEquals(List.of(Sceptre.STRING_TYPE), resp.getBuildables());

        assertDoesNotThrow(() -> {
            DungeonResponse r = ctr.build(Sceptre.STRING_TYPE);
            actual.addAll(r.getInventory().stream().map(ir -> ir.getType()).collect(Collectors.toList()));
        });
        assertEquals(List.of(Sceptre.STRING_TYPE), actual);
    }

    @Test
    public void testBuildSceptre4() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_buildable_sceptre", GameMode.PEACEFUL.getValue());

        // pick up the materials - Arrow * 2, Key, Sun Stone
        resp = ctr.tick(null, Direction.LEFT); 
        resp = ctr.tick(null, Direction.UP); 
        resp = ctr.tick(null, Direction.DOWN); 
        resp = ctr.tick(null, Direction.RIGHT); 
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(Sceptre.STRING_TYPE);
        });
        resp = ctr.tick(null, Direction.RIGHT); 
        resp = ctr.tick(null, Direction.RIGHT); 

        List<String> actual = resp.getInventory().stream().map(item -> item.getType()).collect(Collectors.toList());
        List<String> expected = Arrays.asList(Arrow.STRING_TYPE, Arrow.STRING_TYPE, Key.STRING_TYPE, SunStone.STRING_TYPE);
        Collections.sort(actual);
        Collections.sort(expected);
        assertEquals(expected, actual);
        actual.clear();

        assertEquals(List.of(Sceptre.STRING_TYPE), resp.getBuildables());

        assertDoesNotThrow(() -> {
            DungeonResponse r = ctr.build(Sceptre.STRING_TYPE);
            actual.addAll(r.getInventory().stream().map(ir -> ir.getType()).collect(Collectors.toList()));
        });
        assertEquals(List.of(Sceptre.STRING_TYPE), actual);
    }

    @Test
    public void testBuildMidnightArmour() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_buildable_midnight", GameMode.STANDARD.getValue());
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(MidnightArmour.STRING_TYPE);
        });

        // pick up the materials - Sun Stone, armour
        resp = ctr.tick(null, Direction.LEFT); 
        resp = ctr.tick(null, Direction.LEFT); 

        // invalid when there is a zombie on the map
        assertThrows(InvalidActionException.class, () -> {
            ctr.build(MidnightArmour.STRING_TYPE);
        });

        // ensure the zombie is killed
        resp = ctr.tick(null, Direction.RIGHT); 
        resp = ctr.tick(null, Direction.RIGHT); 
        resp = ctr.tick(null, Direction.RIGHT); 
        resp = ctr.tick(null, Direction.RIGHT);
        
        List<String> actual = resp.getInventory().stream().map(item -> item.getType()).collect(Collectors.toList());
        List<String> expected = Arrays.asList(Armour.STRING_TYPE, SunStone.STRING_TYPE);
        Collections.sort(actual);
        Collections.sort(expected);
        assertEquals(expected, actual);
        actual.clear();

        assertEquals(List.of(MidnightArmour.STRING_TYPE), resp.getBuildables());

        assertDoesNotThrow(() -> {
            DungeonResponse r = ctr.build(MidnightArmour.STRING_TYPE);
            actual.addAll(r.getInventory().stream().map(ir -> ir.getType()).collect(Collectors.toList()));
        });

        assertEquals(List.of(MidnightArmour.STRING_TYPE), actual);
    }

    @Test
    public void testBuildableDisplayAll() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_buildable_all", GameMode.STANDARD.getValue());

        assertThrows(InvalidActionException.class, () -> ctr.build(MidnightArmour.STRING_TYPE));
        assertThrows(InvalidActionException.class, () -> ctr.build(Bow.STRING_TYPE));
        assertThrows(InvalidActionException.class, () -> ctr.build(Sceptre.STRING_TYPE));
        assertThrows(InvalidActionException.class, () -> ctr.build(Shield.STRING_TYPE));

        // pick up the materials for ALL buildable entities
        for (int i = 0; i < 12; i++) {
            resp = ctr.tick(null, Direction.RIGHT); 
        }

        List<String> actual = resp.getBuildables();
        List<String> expected = Arrays.asList(
            MidnightArmour.STRING_TYPE,
            Shield.STRING_TYPE,
            Sceptre.STRING_TYPE,
            Bow.STRING_TYPE
        );

        Collections.sort(actual);
        Collections.sort(expected);
        
        assertEquals(expected, actual);

        assertDoesNotThrow(() -> {
            ctr.build(MidnightArmour.STRING_TYPE);
            ctr.build(Bow.STRING_TYPE);
            ctr.build(Sceptre.STRING_TYPE);
            ctr.build(Shield.STRING_TYPE);
        });

    }
}
