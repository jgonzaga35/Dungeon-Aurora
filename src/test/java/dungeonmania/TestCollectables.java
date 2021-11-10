package dungeonmania;

import java.util.List;

import org.json.JSONObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.statics.FloorSwitch;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.statics.Wall;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.rare.OneRing;
import dungeonmania.entities.movings.Mercenary;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.FileLoader;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;



public class TestCollectables {
    String content = "";
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
        resp = ctr.tick(null, Direction.NONE);

        // Down 2 Units to the Treasure at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

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
        resp = ctr.tick(null, Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Wood at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);
        
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
        resp = ctr.tick(null, Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Arrow at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);
        
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
        resp = ctr.tick(null, Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Key at Coord (3, 1)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);
        
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
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);

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
        resp = ctr.tick(null, Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Sword at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

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
        resp = ctr.tick(null, Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Armour at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

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
     * TEST: Ensure The One Ring is Collected  
     */
    
    @Test
    public void testCollectingTheOneRing() {
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_theOneRingExample", GameMode.PEACEFUL.getValue());
        resp = ctr.tick(null, Direction.NONE);
        Boolean found = false;

        // Up 1 Unit to The One Ring at Coord (5, 4)
        resp = ctr.tick(null, Direction.UP);

        //Checking If The One Ring was Collected
        found = false;
        String curr_type = "";
        String ringId = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == OneRing.STRING_TYPE) {
                ringId = item.getId(); 
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
            if (curr_type.equals(OneRing.STRING_TYPE) && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);
    }

    @Test
    public void testTheOneRingEffect() {
        DungeonManiaController dc;
        Dungeon dungeon;
        OneRing oneRing;
        

        assertDoesNotThrow(() -> {
            content = FileLoader.loadResourceFile("/dungeons/_theOneRingExample.json");
        });
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();

        //Placing One Ring in Cell (5, 4)
        Cell oneRingCell = dungeon.getMap().getCell(5, 4);
        oneRing= new OneRing(dungeon, oneRingCell.getPosition());
        oneRingCell.addOccupant(oneRing);
        
        
        dc = new DungeonManiaController(dungeon);
        // player in (5, 5) with no inventory

        //Enter Into Battle and Ensure Player Does Not Die
        dc.tick(null, Direction.UP);
    
        
        // player at 5, 4
        Cell mercCell = dungeon.getMap().getCell(5, 5);
        Mercenary merc = new Mercenary(dungeon, mercCell.getPosition());
        mercCell.addOccupant(merc);
        
        Cell mercCell2 = dungeon.getMap().getCell(5, 3);
        Mercenary merc2 = new Mercenary(dungeon, mercCell2.getPosition());
        mercCell2.addOccupant(merc2);
        
        Cell mercCell3 = dungeon.getMap().getCell(4, 4);
        Mercenary merc3 = new Mercenary(dungeon, mercCell3.getPosition());
        mercCell3.addOccupant(merc3);
        
        Cell mercCell4 = dungeon.getMap().getCell(6, 4);
        Mercenary merc4 = new Mercenary(dungeon, mercCell4.getPosition());
        mercCell4.addOccupant(merc4);
        
        // battle where player would have died.
        DungeonResponse dr = dc.tick(null, Direction.NONE);
        
        assertEquals(1, TestUtils.countEntitiesOfType(dr, "player"));
        
        //Ensure The One Ring Now Removed From Inventory as it Has Been
        //Used
        Boolean found = false;
        String curr_type = "";
        String ringId = "";
        List<ItemResponse> curr_inventory = dr.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == OneRing.STRING_TYPE) {
                ringId = item.getId(); 
                found = true;
            }
        }
        assertEquals(false, found);
        
        // now player should die
        
        Cell mercCell5 = dungeon.getMap().getCell(5, 5);
        Mercenary merc5 = new Mercenary(dungeon, mercCell5.getPosition());
        mercCell5.addOccupant(merc5);
        
        Cell mercCell6 = dungeon.getMap().getCell(5, 3);
        Mercenary merc6 = new Mercenary(dungeon, mercCell6.getPosition());
        mercCell6.addOccupant(merc6);
        
        Cell mercCell7 = dungeon.getMap().getCell(4, 4);
        Mercenary merc7 = new Mercenary(dungeon, mercCell7.getPosition());
        mercCell7.addOccupant(merc7);
        
        Cell mercCell8 = dungeon.getMap().getCell(6, 4);
        Mercenary merc8 = new Mercenary(dungeon, mercCell8.getPosition());
        mercCell8.addOccupant(merc8);
        
        // battle where player should died.
        dr = dc.tick(null, Direction.NONE);
        
        assertEquals(0, TestUtils.countEntitiesOfType(dr, "player"));

    }

    /**
     * TEST: Ensure Bomb is Collected  
     */
    
    @Test
    public void testLoadingBomb() {
        //Item Coords: Player(1,1), Bomb(1,3), Boulder (7,2), Switch(7,3)
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_bombExample", GameMode.PEACEFUL.getValue());
        resp = ctr.tick(null, Direction.NONE);
        Boolean found = false;

        // Down 2 Units to the Bomb at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

        //Checking If Bomb was Collected
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
            if (curr_type.equals(Bomb.STRING_TYPE) && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);

        //Place Bomb on Ground Cardinally Adjacent to Switch
        // Right 4 Units to the Coord (5, 3)
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);

        //Get ID for Bomb
        curr_type = "";
        String bombId = "";
        curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type.equals("bomb")) {
                bombId = item.getId();
            }
        }
        //Place bomb onto (5,3)
        resp = ctr.tick(bombId, Direction.NONE);

        //Go Up and Right to Move to (6,1)
        ctr.tick(null, Direction.UP);
        ctr.tick(null,Direction.UP);
        ctr.tick(null, Direction.RIGHT);

        //Move Down to (6,2) to Push Boulder Onto Switch & Explode Bomb
        ctr.tick(null, Direction.DOWN);

        //Move to (6,3) where Switch Was & Ensure There are No Entities Here
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
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE)) || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Check Entities in Blast Radius Destroyed
        //Blast Radius is 1, therefore if bomb exploads it destroys
        //all entities in cells (6,3), (5,3), (5,4), (6,4), (7,4), (7,3), (7,2), (6,2), (5,2)
        //other than player itself

        //Make Player walk through all cells in blast radius and check no entities other
        //than player

        //Player is at Cell (5,3) check no entities here
        entityFound = false;
        playerFound = false;
        curr_type = "";
        currEntities = resp.getEntities();
        for (EntityResponse item : currEntities) {
            curr_type = item.getType();
            if (curr_type.equals(Player.STRING_TYPE)) {
                playerFound = true;
            }
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE)) || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);


        //Move Player to Cell (5,3) check no entities here
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
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE)) || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (5,4) check no entities here
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
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE)) || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (6,4) check no entities here
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
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE)) || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (7,4) check no entities here
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
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE)) || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (7,3) check no entities here
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
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE)) || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (7,2) check no entities here
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
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE)) || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (6,2) check no entities here
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
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE)) || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        //Move Player to Cell (5,2) check no entities here
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
            if ((curr_type.equals(Wall.STRING_TYPE)) || (curr_type.equals(FloorSwitch.STRING_TYPE)) || (curr_type.equals(Bomb.STRING_TYPE))) {
                entityFound = true;
            }
        }
        assertEquals(true, playerFound);
        assertEquals(false, entityFound);

        
        

    }
}