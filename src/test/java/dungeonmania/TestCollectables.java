package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

/*
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;*/

import org.json.JSONObject;

import org.junit.jupiter.api.Test;



/*
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;*/

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;

public class TestCollectables {
    /** 
     * TEST: Ensure Treasure is Collected by 
     */
    
    @Test
    public void testLoadingTreasure() {
        assertDoesNotThrow(() -> {
            //Loading New Map (Treasure is at Coords (7, 10) and Player is at Coords (1, 1))
            String content = FileLoader.loadResourceFile("/dungeons/treasureExample.json");
            Dungeon dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        });

        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        Boolean found = false;
        resp = ctr.tick("", Direction.NONE);

        //Moving to the Treasure

        // Down 6 Units to Coord (7, 1)
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);

        // Right 9 Units to Coord (7, 10)
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        resp = ctr.tick("", Direction.RIGHT);

        //Checking that the Item has Been Collected
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "treasure") {
                found = true;
            }
        }
        assertEquals(found, true);
        
        //Checking If Goal is Reached (No Treasure Left on Map)
        //TODO
    }

    /** 
     * TEST: Ensure Wood is Collected  
     */
    
    @Test
    public void testLoadingWood() {
        assertDoesNotThrow(() -> {
            //Loading New Map (Wood is at Coords (7, 10) and Player is at Coords (1, 1))
            String content = FileLoader.loadResourceFile("/dungeons/woodExample.json");
            Dungeon dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        });

        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        //Moving to the Wood

        // Down 6 Units to Coord (7, 1)
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);

        // Right 9 Units to Coord (7, 1)
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        
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
        assertEquals(found, true);
    }

    /** 
     * TEST: Ensure Arrow is Collected  
     */
    
    @Test
    public void testLoadingArrow() {
        assertDoesNotThrow(() -> {
            //Loading New Map (Arrow is at Coords (7, 10) and Player is at Coords (1, 1))
            String content = FileLoader.loadResourceFile("/dungeons/arrowExample.json");
            Dungeon dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        });

        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        //Moving to the Arrow

        // Down 6 Units to Coord (7, 1)
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);

        // Right 9 Units to Coord (7, 1)
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        
        //Checking If Arrow was Collected
        found = false;
        String curr_type = "";
        List<ItemResponse> curr_inventory = resp.getInventory();
        for (ItemResponse item : curr_inventory) {
            curr_type = item.getType();
            if (curr_type == "wood") {
                found = true;
            }
        }
        assertEquals(found, true);
        
    }


    /** 
     * TEST: Ensure Key is Collected  
     */
    
    @Test
    public void testLoadingKey() {
        assertDoesNotThrow(() -> {
            //Loading New Map (Key is at Coords (7, 10) and Player is at Coords (1, 1))
            String content = FileLoader.loadResourceFile("/dungeons/keyExample.json");
            Dungeon dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        });

        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        //Moving to the Key

        // Down 6 Units to Coord (7, 1)
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);

        // Right 9 Units to Coord (7, 1)
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);

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
        assertEquals(found, true);
        
        //Then Entering the Linked Door
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);

        //Checking that the Key has Then Been Removed by Opening Same Door Again



        
    }
    /** 
     * TEST: Ensure Sword is Collected  
     */
    
    @Test
    public void testLoadingSword() {
        assertDoesNotThrow(() -> {
            //Loading New Map (Key is at Coords (7, 10) and Player is at Coords (1, 1))
            String content = FileLoader.loadResourceFile("/dungeons/swordExample.json");
            Dungeon dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        });

        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        //Moving to the Sword

        // Down 6 Units to Coord (7, 1)
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);

        // Right 9 Units to Coord (7, 1)
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        
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
        assertEquals(found, true);

        //Then Entering the Linked Door
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);

        //Initiating Fight with Sword

        

        
    }

    /** 
     * TEST: Ensure Armour is Collected  
     */
    
    @Test
    public void testLoadingArmour() {
        assertDoesNotThrow(() -> {
            //Loading New Map (Armour is at Coords (7, 10) and Player is at Coords (1, 1))
            String content = FileLoader.loadResourceFile("/dungeons/armourExample.json");
            Dungeon dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        });

        //New Game
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("maze", GameMode.PEACEFUL.getValue());
        resp = ctr.tick("", Direction.NONE);
        Boolean found = false;

        //Moving to the Armour

        // Down 6 Units to Coord (7, 1)
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);
        ctr.tick("", Direction.DOWN);

        // Right 9 Units to Coord (7, 1)
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);

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
        assertEquals(found, true);

        //Initiating Fight with Zombie

        //Move 10 Times

        //Check that Player has Lost Battle

        

        
    }
}