package dungeonmania;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.Hydra;
import dungeonmania.entities.movings.Player;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;

public class TestAnduril {

    @Test
    public void testLoadingAnduril() {
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_andurilExample", GameMode.PEACEFUL.getValue());
        resp = ctr.tick(null, Direction.NONE);
        Boolean found = false;

        // Down 2 Units to Anduril at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

        //Checking If Anduril was Collected
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "anduril") {
                found = true;
            }
        }
        assertEquals(true, found);

        //Check that the Item Was Removed from the Cell
        boolean itemRemoved = true;
        int currPositionX = 0;
        int currPositionY = 0;
        List<EntityResponse> cellEntities = resp.getEntities();
        for (EntityResponse currEntity : cellEntities) {
            curr_type = currEntity.getType();

            Position currPosition = currEntity.getPosition();
            currPositionX = currPosition.getX();
            currPositionY = currPosition.getY();
            if (curr_type == "anduril" && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);
    }

    @Test
    public void testFightHydra() {
        
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();

        DungeonResponse resp = ctr.newGame("_andurilHydra", GameMode.HARD.getValue());
        resp = ctr.tick(null, Direction.NONE);

        // Down 2 Units to Anduril at Coord (1, 2)
        resp = ctr.tick(null, Direction.DOWN);
        
        // Down to Hydra
        resp = ctr.tick(null, Direction.DOWN);

        // If they fight, Anduril should do 21 damage instead of 7 and so player should one shot
        assertEquals(0, TestUtils.countEntitiesOfType(resp, Hydra.STRING_TYPE));
        assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
    
    }
}
