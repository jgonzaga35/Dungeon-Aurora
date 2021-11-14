package dungeonmania;

import java.util.List;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
// import dungeonmania.exceptions.InvalidActionException;
// import dungeonmania.util.FileLoader;
// import org.json.JSONObject;
// import java.util.Random;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.statics.FloorSwitch;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.statics.Wall;
import dungeonmania.entities.collectables.Bomb;
// import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.collectables.SunStone;

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

     /** 
     * TEST: Ensure Sun Stone is Collected & Opens Doors & Is Not Removed  
     */
    
    @Test
    public void testSunStoneOpeningDoor() {
        //Item Coords: Player(1,1), SunStone(1,3), Door 1 (2,4), Door 2 (3,3), Door 3 (6, 3)
        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_door_maze_sun_stone", GameMode.PEACEFUL.getValue());
        resp = ctr.tick(null, Direction.NONE);

        // Down 2 Units to the SunStone at Coord (1, 3)
        ctr.tick(null, Direction.DOWN);
        resp = ctr.tick(null, Direction.DOWN);

        // check that the Sun Stone is in player inventory
        // Checking that the Item has Been Added to Inventory
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals(SunStone.STRING_TYPE)));

        //Check that the Item Was Removed from the Cell
        int currPositionX = 0;
        int currPositionY = 0;
        boolean itemRemoved = true;
        String curr_type = "";
        List<EntityResponse> cellEntities = resp.getEntities();
        for (EntityResponse currEntity : cellEntities) {
            curr_type = currEntity.getType();

            Position currPosition = currEntity.getPosition();
            currPositionX = currPosition.getX();
            currPositionY = currPosition.getY();
            if (curr_type.equals(SunStone.STRING_TYPE) && currPositionX == 1 && currPositionY == 3) {
                itemRemoved = false;
            }
        }
        assertEquals(true, itemRemoved);

        // using Sun Stone to open Door 2 
        resp = ctr.tick(null, Direction.RIGHT);
        Position p1 = TestUtils.getPlayerPosition(resp);

        resp = ctr.tick(null, Direction.DOWN);
        Position p2 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p1, p2);

        // using Sun Stone to open Door 1 
        ctr.tick(null, Direction.UP);
        resp = ctr.tick(null, Direction.RIGHT);
        Position p3 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p3, p2);

        // moving right
        resp = ctr.tick(null, Direction.RIGHT);

        // using Sun Stone to open Door 3
        Position p4 = TestUtils.getPlayerPosition(resp);
        
        resp = ctr.tick(null, Direction.RIGHT);
        Position p5 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p4, p5);

        // walking back through Door 1
        resp = ctr.tick(null, Direction.LEFT);
        Position p6 = TestUtils.getPlayerPosition(resp);
        
        resp = ctr.tick(null, Direction.LEFT);
        Position p7 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p6, p7);

        // using Sun Stone to open Door 2
        resp = ctr.tick(null, Direction.LEFT);
        Position p8 = TestUtils.getPlayerPosition(resp);
        resp = ctr.tick(null, Direction.DOWN);
        Position p9 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p8, p9);

        // using Sun Stone to open Door 3 
        resp = ctr.tick(null, Direction.UP);
        resp = ctr.tick(null, Direction.RIGHT);
        resp = ctr.tick(null, Direction.RIGHT);
        resp = ctr.tick(null, Direction.RIGHT);

        // using Sun Stone to open Door 3 
        Position p10 = TestUtils.getPlayerPosition(resp);
        resp = ctr.tick(null, Direction.RIGHT);
        Position p11 = TestUtils.getPlayerPosition(resp);
        assertNotEquals(p10, p11);

        // Check the Sun Stone is Still in Inventory (Should Not Be Discarded as Per Spec)
        assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals("sun_stone")));
    }

    /**
     * Test Bribing Mercenary with Sun Stone
     */
    @Test
    public void testSunStoneMercenaryBribe() throws IOException {
        // DungeonManiaController dc;
        // Dungeon dungeon;
        // DungeonResponse resp;
        // Mercenary merc;

        // String content = FileLoader.loadResourceFile("/dungeons/_sun_stone_merc_test.json");
        // dungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD, new JSONObject(content));
        // dc = new DungeonManiaController(dungeon);
        // Player player = (Player) dungeon.getMap().allEntities().stream()
        //     .filter(e -> e instanceof Player)
        //     .findFirst().get();
        // merc = (Mercenary) dungeon.getMap().allEntities().stream()
        //     .filter(e -> e instanceof Mercenary)
        //     .findFirst().get();

        // dungeon.getMap().flood();
        // Integer dist = merc.getCell().getPlayerDistance();

        // // Try bribing invalid id
        // assertThrows(IllegalArgumentException.class, () -> {
        //     dc.interact("invalid");
        // });
        
        
        // // Try bribing with no money
        // assertThrows(InvalidActionException.class, () -> {
        //     dc.interact(merc.getId());
        // });
        
        // for (int i = 0; i < 2; i++) {
        //     dc.tick(null, Direction.RIGHT);
            
        //     assertTrue(merc.getCell().getPlayerDistance() == dist);
        //     dist = merc.getCell().getPlayerDistance();
        // }
        // // player pos (7, 5)
        // // Try bribing outside of range
        // assertThrows(InvalidActionException.class, () -> {
        //     dc.interact(merc.getId());
        // });
        
        // for (int i = 0; i < 5; i++) {
        //     dc.tick(null, Direction.UP);
        //     assertTrue(merc.getCell().getPlayerDistance() <= dist);
        //     dist = merc.getCell().getPlayerDistance();
        // }
        // // player pos (7, 0)
        // // merc pos (5, 0)
        // // two cardinal squares away, bribe possible
        
        // assertDoesNotThrow(() -> dc.interact(merc.getId()));    
        
        // // Try bribing friendly
        // assertThrows(IllegalArgumentException.class, () -> {
        //     dc.interact(merc.getId());
        // });
    

        // resp = dc.tick(null, Direction.NONE);

        // // check the Sun Stone is still in inventory after bribe
        // assertTrue(resp.getInventory().stream().anyMatch(item -> item.getType().equals("sun_stone")));
    }
}