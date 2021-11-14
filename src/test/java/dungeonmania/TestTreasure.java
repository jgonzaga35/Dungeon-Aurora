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

public class TestTreasure {
    /**
     * testLoadingTreasure() Test that Treasure is Collected and that it Triggers
     * Goals
     */
    @Test
    public void testLoadingTreasure() {
        // New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_treasureExample", GameMode.PEACEFUL.getValue());
        Boolean found = false;
        resp = ctr.tick(null, Direction.NONE);

        // Down 2 Units to the Treasure at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

        // Checking that the Item has Been Added to Inventory
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "treasure") {
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
            if (curr_type == "treasure" && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
                break;
            }
        }
        assertEquals(true, itemRemoved);

        // Checking If Goal is Reached (No Treasure Left on Map)
        // TODO
    }
}
