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
    public void setStartingPostition() throws IOException 
    {
        String content = FileLoader.loadResourceFile("/dungeons/_simple.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        startingCell = dungeon.getMap().getCell(new Pos2d(2, 2));
    }

    @Test
    public void testCircleMovement() 
    {
        DungeonMap map = dungeon.getMap();
        Movement spider = new CircleMovementBehaviour(map, startingCell);
        
        spider.move();
        assertEquals(new Pos2d(2, 1), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 1), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 2), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 3), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 3), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 3), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 2), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 1), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 1), spider.getCurrentPosition().getPosition());
    }

    @Test
    public void testCircleBoulderInteraction() throws IOException
    {
        String content = FileLoader.loadResourceFile("/dungeons/_simple_with_boulder.json");
        Dungeon boulderDungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        Cell spiderStart = boulderDungeon.getMap().getCell(new Pos2d(2, 2));

        DungeonMap map = boulderDungeon.getMap();
        Movement spider = new CircleMovementBehaviour(map, spiderStart);

        System.out.println(map.toString());
        System.out.println(map.getCell(3, 3).getOccupants().toString());

        spider.move();
        assertEquals(new Pos2d(2, 1), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 1), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 2), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 1), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 1), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 1), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 2), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 3), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 3), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 3), spider.getCurrentPosition().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 2), spider.getCurrentPosition().getPosition());
    }
}
