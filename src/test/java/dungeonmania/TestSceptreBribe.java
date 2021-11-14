package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.buildables.*;
import dungeonmania.response.models.*;
import dungeonmania.util.*;

public class TestSceptreBribe {
    @Test
    public void testSceptreBribe() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_sceptre_bribe", GameMode.PEACEFUL.getValue());

        // pick up the materials - Wood, Key, Sun Stone
        resp = ctr.tick(null, Direction.LEFT); 
        resp = ctr.tick(null, Direction.LEFT); 
        resp = ctr.tick(null, Direction.LEFT);

        assertDoesNotThrow(() -> {
            DungeonResponse r = ctr.build(Sceptre.STRING_TYPE);
            ctr.interact("Entity-1"); // test bribe mercenary
        });



    }

}
