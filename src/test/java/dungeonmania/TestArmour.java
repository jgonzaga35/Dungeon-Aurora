package dungeonmania;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dungeonmania.DungeonManiaController.GameMode;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;

public class TestArmour {
    /**
     * TEST: Ensure Armour is Collected
     */

    @Test
    public void testLoadingArmour() {
        // New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_armourExample", GameMode.PEACEFUL.getValue());
        resp = ctr.tick(null, Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Armour at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

        // Checking If Armour was Collected
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "armour") {
                found = true;
            }
        }
        assertEquals(true, found);

        // Check that the Item Was Removed from the Cell
        int currPositionX = 0;
        int currPositionY = 0;
        boolean itemRemoved = true;
        List<EntityResponse> cellEntities = resp.getEntities();
        for (EntityResponse currEntity : cellEntities) {
            curr_type = currEntity.getType();

            Position currPosition = currEntity.getPosition();
            currPositionX = currPosition.getX();
            currPositionY = currPosition.getY();
            if (curr_type == "armour" && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);

        // Initiating Fight with Zombie
        // TODO

        // Move 10 Times
        // TODO

        // Check that Player has Lost Battle
        // TODO
    }

    
}
