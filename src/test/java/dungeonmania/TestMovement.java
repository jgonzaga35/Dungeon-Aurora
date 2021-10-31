package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.movement.CircleMovementBehaviour;
import dungeonmania.movement.FriendlyMovementBehaviour;
import dungeonmania.movement.MovementBehaviour;
import dungeonmania.util.FileLoader;

public class TestMovement {
    Dungeon dungeon;
    Cell startingCell;

    @BeforeEach
    public void setStartingPostition() throws IOException 
    {
        String content = FileLoader.loadResourceFile("/dungeons/_simple.json");
        dungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();
        startingCell = dungeon.getMap().getCell(new Pos2d(2, 2));
    }

    @Test
    public void testCircleMovement() 
    {
        DungeonMap map = dungeon.getMap();
        MovementBehaviour spider = new CircleMovementBehaviour(1, map, startingCell);
        
        spider.move();
        assertEquals(new Pos2d(2, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 3), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 3), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 3), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 1), spider.getCurrentCell().getPosition());
    }

    @Test
    public void testCircleMovementAgainstBoundry() 
    {
        DungeonMap map = dungeon.getMap();
        startingCell = map.getCell(new Pos2d(4, 2));
        MovementBehaviour spider = new CircleMovementBehaviour(1, map, startingCell);
        
        spider.move();
        assertEquals(new Pos2d(4, 1), spider.getCurrentCell().getPosition());
        
        spider.move();
        assertEquals(new Pos2d(4, 1), spider.getCurrentCell().getPosition());
        
        spider.move();
        assertEquals(new Pos2d(3, 1), spider.getCurrentCell().getPosition());
        
        spider.move();
        assertEquals(new Pos2d(3, 2), spider.getCurrentCell().getPosition());
        
        spider.move();
        assertEquals(new Pos2d(3, 3), spider.getCurrentCell().getPosition());
        
        spider.move();
        assertEquals(new Pos2d(4, 3), spider.getCurrentCell().getPosition());
        
        spider.move();
        assertEquals(new Pos2d(4, 3), spider.getCurrentCell().getPosition());
        
        spider.move();
        assertEquals(new Pos2d(3, 3), spider.getCurrentCell().getPosition());

    }

    @Test
    public void testFriendlyMovementBasic() 
    {
        DungeonMap map = dungeon.getMap();
        startingCell = dungeon.getMap().getCell(new Pos2d(1, 0));
        MovementBehaviour friend = new FriendlyMovementBehaviour(1, map, startingCell);

        for (int i = 0; i < 5; i++) {
            friend.move();

            assertEquals(new Pos2d(1, 0), friend.getCurrentCell().getPosition());
        }
    }

    @Test
    public void testCircleBoulderInteraction() throws IOException
    {
        String content = FileLoader.loadResourceFile("/dungeons/_simple_with_boulder.json");
        Dungeon boulderDungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        Cell spiderStart = boulderDungeon.getMap().getCell(new Pos2d(2, 2));

        DungeonMap map = boulderDungeon.getMap();
        MovementBehaviour spider = new CircleMovementBehaviour(1, map, spiderStart);

        spider.move();
        assertEquals(new Pos2d(2, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 3), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 3), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 3), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 3), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 2), spider.getCurrentCell().getPosition());
    }

    @Test
    public void testBoulderEdgeSeven() throws IOException
    {
        String content = FileLoader.loadResourceFile("/dungeons/_simple_with_boulder2.json");
        Dungeon boulderDungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        Cell spiderStart = boulderDungeon.getMap().getCell(new Pos2d(2, 1));

        DungeonMap map = boulderDungeon.getMap();
        MovementBehaviour spider = new CircleMovementBehaviour(1, map, spiderStart);

        spider.move();
        assertEquals(new Pos2d(2, 0), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 0), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 0), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 0), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 0), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(3, 0), spider.getCurrentCell().getPosition());
    }

    @Test
    public void testBoulderEdgeZero() throws IOException
    {
        String content = FileLoader.loadResourceFile("/dungeons/_simple_with_boulder2.json");
        Dungeon boulderDungeon = Dungeon.fromJSONObject("name", GameMode.STANDARD, new JSONObject(content));
        Cell spiderStart = boulderDungeon.getMap().getCell(new Pos2d(1, 1));

        DungeonMap map = boulderDungeon.getMap();
        MovementBehaviour spider = new CircleMovementBehaviour(1, map, spiderStart);

        spider.move();
        assertEquals(new Pos2d(0, 0), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(0, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(0, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 0), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 0), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(2, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(1, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(0, 2), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(0, 1), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(0, 0), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(0, 0), spider.getCurrentCell().getPosition());

        spider.move();
        assertEquals(new Pos2d(0, 1), spider.getCurrentCell().getPosition());
    }
}
