package dungeonmania;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;

public class TestCollectables {
    /** 
     * testLoadingTreasure()
     * Test that Treasure is Collected and that it Triggers Goals  
    */
    @Test
    public void testLoadingTreasure() {
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_treasureExample", GameMode.PEACEFUL.getValue());
        Boolean found = false;
        resp = ctr.tick("", Direction.NONE);

        // Down 2 Units to the Treasure at Coord (1, 3)
        ctr.tick("", Direction.DOWN);
        resp = ctr.tick("", Direction.DOWN);

        //Checking that the Item has Been Added to Inventory
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

        //Check that the Item Was Removed from the Cell
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
        
        //Checking If Goal is Reached (No Treasure Left on Map)
        //TODO
    }
    
    /** 
     * testLoadingWood()
     * Test that Wood is Collected  
    */
    @Test
    public void testLoadingWood() {
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_woodExample", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Wood at Coord (1, 3)
        ctr.tick("", Direction.DOWN);
        resp = ctr.tick("", Direction.DOWN);
        
        //Checking If Wood was Collected
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "wood") {
                found = true;
            }
        }
        assertEquals(true, found);

        //Check that the Item Was Removed from the Cell
        int currPositionX = 0;
        int currPositionY = 0;
        boolean itemRemoved = true;
        List<EntityResponse> cellEntities = resp.getEntities();
        for (EntityResponse currEntity : cellEntities) {
            curr_type = currEntity.getType();

            Position currPosition = currEntity.getPosition();
            currPositionX = currPosition.getX();
            currPositionY = currPosition.getY();
            if (curr_type == "wood" && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);
    }
    
    /** 
     * testLoadingArrow()
     * Test that Arrow is Collected 
    */
    @Test
    public void testLoadingArrow() {
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_arrowExample", GameMode.STANDARD.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Arrow at Coord (1, 3)
        ctr.tick("", Direction.DOWN);
        resp = ctr.tick("", Direction.DOWN);
        
        //Checking If Arrow was Collected
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "arrow") {
                found = true;
            }
        }
        assertEquals(true, found);

        //Check that the Item Was Removed from the Cell
        int currPositionX = 0;
        int currPositionY = 0;
        boolean itemRemoved = true;
        List<EntityResponse> cellEntities = resp.getEntities();
        for (EntityResponse currEntity : cellEntities) {
            curr_type = currEntity.getType();

            Position currPosition = currEntity.getPosition();
            currPositionX = currPosition.getX();
            currPositionY = currPosition.getY();
            if (curr_type == "arrow" && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);
    }

    
    /** 
     * testLoadingKey()
     * Test that Key is Collected  
    */
    @Test
    public void testLoadingKey() {
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_keyExample", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Key at Coord (3, 1)
        ctr.tick("", Direction.DOWN);
        resp = ctr.tick("", Direction.DOWN);
        
        //Checking If Key was Collected
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "key") {
                found = true;
            }
        }
        assertEquals(true, found);

        //Check that the Item Was Removed from the Cell
        int currPositionX = 0;
        int currPositionY = 0;
        boolean itemRemoved = true;
        List<EntityResponse> cellEntities = resp.getEntities();
        for (EntityResponse currEntity : cellEntities) {
            curr_type = currEntity.getType();

            Position currPosition = currEntity.getPosition();
            currPositionX = currPosition.getX();
            currPositionY = currPosition.getY();
            if (curr_type == "key" && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);
        
        //Then Entering the Linked Door
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);

        //Checking that the Key has Then Been Removed by Opening Same Door Again
    }

    /** 
     * testLoadingSword()
     * Test that Sword is Collected and Impact on Battles 
    */
    @Test
    public void testLoadingSword() {
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_swordExample", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Sword at Coord (1, 3)
        ctr.tick("", Direction.DOWN);
        resp = ctr.tick("", Direction.DOWN);

        //Checking If Sword was Collected
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "sword") {
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
            if (curr_type == "sword" && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);

        //Initiating Fight with Sword
        //TODO
    }

    /** 
     * TEST: Ensure Armour is Collected  
     */
    
    @Test
    public void testLoadingArmour() {
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_armourExample", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Armour at Coord (1, 3)
        ctr.tick("", Direction.DOWN);
        resp = ctr.tick("", Direction.DOWN);

        //Checking If Armour was Collected
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

        //Check that the Item Was Removed from the Cell
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

        //Initiating Fight with Zombie
        //TODO

        //Move 10 Times
        //TODO

        //Check that Player has Lost Battle
        //TODO
    }

    /** 
     * TEST: Ensure Bomb is Collected  
     */
    
    @Test
    public void testLoadingBomb() {
        //Item Coords: Player(1,1), Bomb(1,3), Switch(7,3)
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_bombExample", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Bomb at Coord (1, 3)
        ctr.tick("", Direction.DOWN);
        resp = ctr.tick("", Direction.DOWN);

        //Checking If Bomb was Collected
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "bomb") {
                found = true;
            }
        }
        assertEquals(true, found);

        //Check that the Item Was Removed from the Cell
        int currPositionX = 0;
        int currPositionY = 0;
        boolean itemRemoved = true;
        List<EntityResponse> cellEntities = resp.getEntities();
        for (EntityResponse currEntity : cellEntities) {
            curr_type = currEntity.getType();

            Position currPosition = currEntity.getPosition();
            currPositionX = currPosition.getX();
            currPositionY = currPosition.getY();
            if (curr_type == "bomb" && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);

        //Place Bomb on Ground Cardinally Adjacent to Switch
        // Right 4 Units to the Coord (5, 3)
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);

        //Get ID for Bomb
        curr_type = "";
        String bombId = "";
        curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "bomb") {
                bombId = item.getId();
            }
        }
        //Place bomb onto (5,3)
        resp = ctr.tick(bombId, Direction.NONE);

        

        //Check Entities in Blast Radius Destroyed
        //Blast Radius is 1, therefore if bomb exploads it destroys
        //all entities in cells (6,3), (5,3), (5,4), (6,4), (7,4), (7,3), (7,2), (6,2), (5,2)
        //other than player itself

        //Make Player walk through all cells in blast radius and check no entities other
        //than player

        //Player is at Cell (5,3) check no entities here
        boolean entityFound = false;
        boolean playerFound = false;
        curr_type = "";
        List<EntityResponse> currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type == "player") {
                playerFound = true;
            }
            if ((curr_type == "wall") || (curr_type == "floor switch") || (curr_type == "bomb")) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);


        //Move Player to Cell (5,3) check no entities here
        resp = ctr.tick("", Direction.LEFT);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type == "player") {
                playerFound = true;
            }
            if ((curr_type == "wall") || (curr_type == "floor switch") || (curr_type == "bomb")) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (5,4) check no entities here
        resp = ctr.tick("", Direction.DOWN);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type == "player") {
                playerFound = true;
            }
            if ((curr_type == "wall") || (curr_type == "floor switch") || (curr_type == "bomb")) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (6,4) check no entities here
        resp = ctr.tick("", Direction.RIGHT);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type == "player") {
                playerFound = true;
            }
            if ((curr_type == "wall") || (curr_type == "floor switch") || (curr_type == "bomb")) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (7,4) check no entities here
        resp = ctr.tick("", Direction.RIGHT);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type == "player") {
                playerFound = true;
            }
            if ((curr_type == "wall") || (curr_type == "floor switch") || (curr_type == "bomb")) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (7,3) check no entities here
        resp = ctr.tick("", Direction.UP);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type == "player") {
                playerFound = true;
            }
            if ((curr_type == "wall") || (curr_type == "floor switch") || (curr_type == "bomb")) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (7,2) check no entities here
        resp = ctr.tick("", Direction.UP);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type == "player") {
                playerFound = true;
            }
            if ((curr_type == "wall") || (curr_type == "floor switch") || (curr_type == "bomb")) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (6,2) check no entities here
        resp = ctr.tick("", Direction.LEFT);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type == "player") {
                playerFound = true;
            }
            if ((curr_type == "wall") || (curr_type == "floor switch") || (curr_type == "bomb")) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (5,2) check no entities here
        resp = ctr.tick("", Direction.LEFT);

        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type == "player") {
                playerFound = true;
            }
            if ((curr_type == "wall") || (curr_type == "floor switch") || (curr_type == "bomb")) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        
        

    }
}