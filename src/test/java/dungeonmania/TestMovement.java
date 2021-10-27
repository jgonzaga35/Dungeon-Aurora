package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.EnumSource.Mode;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.movement.CircleMovementBehaviour;
import dungeonmania.movement.FleeMovementBehaviour;
import dungeonmania.movement.FollowMovementBehaviour;
import dungeonmania.movement.Movement;
import dungeonmania.movement.RandomMovementBehaviour;
import dungeonmania.util.FileLoader;

public class TestMovement {
    Dungeon dungeon;
    Cell startingCell;

    @BeforeEach
    public void setStartingPostition() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_simple.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();
        startingCell = dungeon.getMap().getCell(new Pos2d(2, 2));
    }

    @Test
    public void testFollowMovement() {
        DungeonMap map = dungeon.getMap();
        Movement merc = new FollowMovementBehaviour(map, startingCell);

        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(3, 2));
        
        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(4, 2));
        
        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(4, 1));
        
        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(4, 0));

        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(3, 0));

        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(2, 0));

        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(1, 0));

        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(0, 0));
    }

    @Test
    public void testFollowMovementInMaze() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/maze.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();
        startingCell = dungeon.getMap().getCell(new Pos2d(8, 15));

        DungeonMap map = dungeon.getMap();
        Movement merc = new FollowMovementBehaviour(map, startingCell);

        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(7, 15));
        
        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(6, 15));
        
        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(5, 15));
        
        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(5, 14));

        for (int i = 0; i < 12; i++)
        {
            merc.move();
            assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(4, 14 - i));
        }
        
        for (int i = 0; i < 3; i++)
        {
            merc.move();
            assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(3 - i, 3));
        }

        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(1, 2));

        merc.move();
        assertEquals(merc.getCurrentPosition().getPosition(), new Pos2d(1, 1));
    }
}
