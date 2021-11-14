package dungeonmania;

import java.util.List;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.logicals.*;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.statics.Wall;
import dungeonmania.entities.logicals.*;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;

public class TestBomb {
     
    /**
     * TEST: Ensure Bomb is Collected
     */

    @Test
    public void testLoadingBomb() {
        // Item Coords: Player(1,1), Bomb(1,3), Boulder (7,2), Switch(7,3)
        // New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_bombExample", GameMode.PEACEFUL.getValue());
        resp = ctr.tick(null, Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Bomb at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

        // Checking If Bomb was Collected
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type.equals(Bomb.STRING_TYPE)) {
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
            if (curr_type.equals(Bomb.STRING_TYPE) && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);

        // Place Bomb on Ground Cardinally Adjacent to Switch
        // Right 4 Units to the Coord (5, 3)
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);

        // Get ID for Bomb
        curr_type = "";
        String bombId = "";
        curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type.equals("bomb")) {
                bombId = item.getId();
            }
        }
        // Place bomb onto (5,3)
        resp = ctr.tick(bombId, Direction.NONE);

        // Go Up and Right to Move to (6,1)
        ctr.tick(null, Direction.UP);
        ctr.tick(null, Direction.UP);
        ctr.tick(null, Direction.RIGHT);

        // Move Down to (6,2) to Push Boulder Onto Switch & Explode Bomb
        ctr.tick(null, Direction.DOWN);

        // Move to (6,3) where Switch Was & Ensure There are No Entities Here
        resp = ctr.tick(null, Direction.DOWN);

        boolean entityFound = false;
        boolean playerFound = false;
        curr_type = "";
        List<EntityResponse> currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE))
                    || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        // Check Entities in Blast Radius Destroyed
        // Blast Radius is 1, therefore if bomb exploads it destroys
        // all entities in cells (6,3), (5,3), (5,4), (6,4), (7,4), (7,3), (7,2), (6,2),
        // (5,2)
        // other than player itself

        // Make Player walk through all cells in blast radius and check no entities
        // other
        // than player

        // Player is at Cell (5,3) check no entities here
        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE))
                    || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        // Move Player to Cell (5,3) check no entities here
        resp = ctr.tick(null, Direction.LEFT);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE))
                    || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        // Move Player to Cell (5,4) check no entities here
        resp = ctr.tick(null, Direction.DOWN);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE))
                    || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        // Move Player to Cell (6,4) check no entities here
        resp = ctr.tick(null, Direction.RIGHT);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE))
                    || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        // Move Player to Cell (7,4) check no entities here
        resp = ctr.tick(null, Direction.RIGHT);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE))
                    || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        // Move Player to Cell (7,3) check no entities here
        resp = ctr.tick(null, Direction.UP);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE))
                    || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        // Move Player to Cell (7,2) check no entities here
        resp = ctr.tick(null, Direction.UP);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE))
                    || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        // Move Player to Cell (6,2) check no entities here
        resp = ctr.tick(null, Direction.LEFT);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE))
                    || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        // Move Player to Cell (5,2) check no entities here
        resp = ctr.tick(null, Direction.LEFT);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE))
                    || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

    }
    
}
