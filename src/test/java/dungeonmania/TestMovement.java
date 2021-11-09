package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.goal.ExitGoal;
import dungeonmania.movement.CircleMovementBehaviour;
import dungeonmania.movement.FleeMovementBehaviour;
import dungeonmania.movement.FollowMovementBehaviour;
import dungeonmania.movement.FriendlyMovementBehaviour;
import dungeonmania.movement.MovementBehaviour;
import dungeonmania.movement.RandomMovementBehaviour;
import dungeonmania.util.Counter;
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
    public void testCircleMovementAgainstBoundary() 
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
    public void testCircleMovementAgainstBoundaryTop() 
    {
        DungeonMap map = dungeon.getMap();
        startingCell = map.getCell(new Pos2d(2, 0));
        MovementBehaviour spider = new CircleMovementBehaviour(1, map, startingCell);
        
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
        assertEquals(new Pos2d(1, 0), spider.getCurrentCell().getPosition());
        
        spider.move();
        assertEquals(new Pos2d(2, 0), spider.getCurrentCell().getPosition());
        
        spider.move();
        assertEquals(new Pos2d(3, 0), spider.getCurrentCell().getPosition());
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

    @Test
    public void testBlockedCircleMovementBehaviour() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon d = new Dungeon("manual", GameMode.STANDARD, map, new ExitGoal());
        
        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new CircleMovementBehaviour(0, map, cell);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        for (int i = 0; i < 10; i++)
            assertEquals(cell.getPosition(), movement.move().getPosition());
    }

    @Test
    public void testBlockedFleeMovementBehaviour() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon d = new Dungeon("manual", GameMode.STANDARD, map, new ExitGoal());
        
        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new FleeMovementBehaviour(0, map, cell);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        for (int i = 0; i < 10; i++)
            assertEquals(cell.getPosition(), movement.move().getPosition());
    }

    @Test
    public void testBlockedFriendlyMovementBehaviour() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon d = new Dungeon("manual", GameMode.STANDARD, map, new ExitGoal());
        
        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new FriendlyMovementBehaviour(0, map, cell);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        for (int i = 0; i < 10; i++)
            assertEquals(cell.getPosition(), movement.move().getPosition());
    }

    @Test
    public void testBlockedRandomMovementBehaviour() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon d = new Dungeon("manual", GameMode.STANDARD, map, new ExitGoal());
        
        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new RandomMovementBehaviour(0, map, cell);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        for (int i = 0; i < 10; i++)
            assertEquals(cell.getPosition(), movement.move().getPosition());
    }

    @Test
    public void testBlockedFollowMovementBehaviour() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon d = new Dungeon("manual", GameMode.STANDARD, map, new ExitGoal());
        
        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new FollowMovementBehaviour(0, map, cell);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        for (int i = 0; i < 10; i++)
            assertEquals(cell.getPosition(), movement.move().getPosition());
    }

    @Test
    public void testRandomMovementDistribution() {
        DungeonMap map = new DungeonMap(10, 10);

        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new RandomMovementBehaviour(0, map, cell);

        // Each shift (up, down, right, left) is a key in that counter
        Counter<Pos2d> counter = new Counter<>();
        Pos2d prev = cell.getPosition();

        int numMoves = 1000;
        for (int i = 0; i < numMoves; i++) {
            Pos2d pos = movement.move().getPosition();
            Pos2d shift = pos.minus(prev);
            counter.add(shift, 1);
            prev = pos;
        }

        assertEquals(4, counter.values().count(), "should get 4 shifts: up, down, right and left");

        // i'm not going to do the maths, but that should be good enough
        assertTrue(counter.values().allMatch(n -> Math.abs(numMoves / 4 - n) < 10));
    }
}
