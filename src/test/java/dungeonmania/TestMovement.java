package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.goal.ExitGoal;
import dungeonmania.movement.CircleMovementBehaviour;
import dungeonmania.movement.FleeMovementBehaviour;
import dungeonmania.movement.FollowMovementBehaviour;
import dungeonmania.movement.FriendlyMovementBehaviour;
import dungeonmania.movement.MovementBehaviour;
import dungeonmania.movement.RandomMovementBehaviour;
import dungeonmania.util.Counter;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class TestMovement {
    Dungeon dungeon;
    Cell startingCell;

    @BeforeEach
    public void setStartingPostition() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_simple.json");
        dungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD, new JSONObject(content));
        dungeon.getMap().flood();
        startingCell = dungeon.getMap().getCell(new Pos2d(2, 2));
    }

    @Test
    public void testPathFinding1() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_pathfinding1.json");
        dungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD, new JSONObject(content));
        DungeonManiaController dc = new DungeonManiaController(dungeon);
        Mercenary assassin = TestUtils.getMercenary(dungeon);

        List<Pos2d> followedPath = new ArrayList<>();

        // let the assassin find it's path
        while (!assassin.getPosition().equals(new Pos2d(0, 0))) {
            dc.tick(null, Direction.NONE);
            if (!followedPath.stream().anyMatch(p -> p.equals(assassin.getPosition())))
                followedPath.add(assassin.getPosition());
        }

        // define expected path
        List<Pos2d> expectedPath = Arrays.asList(new Pos2d(7, 8), new Pos2d(6, 8), new Pos2d(5, 8), new Pos2d(4, 8),
                new Pos2d(3, 8), new Pos2d(2, 8), new Pos2d(1, 8), new Pos2d(0, 8), new Pos2d(0, 7), new Pos2d(0, 6),
                new Pos2d(0, 5), new Pos2d(0, 4), new Pos2d(0, 3), new Pos2d(0, 2), new Pos2d(0, 1), new Pos2d(0, 0));

        assertEquals(expectedPath.size(), followedPath.size());
        for (int i = 0; i < followedPath.size(); i++) {
            assertEquals(expectedPath.get(i), followedPath.get(i), "at " + i);
        }
    }

    @Test
    public void testPathFinding2() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_pathfinding2.json");
        dungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD, new JSONObject(content));
        DungeonManiaController dc = new DungeonManiaController(dungeon);
        Mercenary assassin = TestUtils.getMercenary(dungeon);

        List<Pos2d> followedPath = new ArrayList<>();

        // let the assassin find it's path
        while (!assassin.getPosition().equals(new Pos2d(0, 0))) {
            TestUtils.getPlayer(dungeon).setHealth(40);
            dc.tick(null, Direction.NONE);
            if (!followedPath.stream().anyMatch(p -> p.equals(assassin.getPosition())))
                followedPath.add(assassin.getPosition());
        }

        // define expected path
        List<Pos2d> expectedPath = Arrays.asList(new Pos2d(7, 8), new Pos2d(6, 8), new Pos2d(5, 8), new Pos2d(4, 8),
                new Pos2d(3, 8), new Pos2d(2, 8), new Pos2d(1, 8), new Pos2d(0, 8), new Pos2d(0, 7), new Pos2d(0, 6),
                new Pos2d(1, 6), new Pos2d(2, 6), new Pos2d(3, 6), new Pos2d(4, 6), new Pos2d(5, 6), new Pos2d(6, 6),
                new Pos2d(7, 6), new Pos2d(8, 6), new Pos2d(8, 5), new Pos2d(8, 4), new Pos2d(7, 4), new Pos2d(6, 4),
                new Pos2d(5, 4), new Pos2d(4, 4), new Pos2d(3, 4), new Pos2d(2, 4), new Pos2d(1, 4), new Pos2d(0, 4),
                new Pos2d(0, 3), new Pos2d(0, 2), new Pos2d(1, 2), new Pos2d(2, 2), new Pos2d(3, 2), new Pos2d(4, 2),
                new Pos2d(5, 2), new Pos2d(6, 2), new Pos2d(7, 2), new Pos2d(8, 2), new Pos2d(8, 1), new Pos2d(8, 0),
                new Pos2d(7, 0), new Pos2d(6, 0), new Pos2d(5, 0), new Pos2d(4, 0), new Pos2d(3, 0), new Pos2d(2, 0),
                new Pos2d(1, 0), new Pos2d(0, 0));

        assertEquals(expectedPath.size(), followedPath.size());
        for (int i = 0; i < followedPath.size(); i++) {
            assertEquals(expectedPath.get(i), followedPath.get(i), "at " + i);
        }
    }

    @Test
    public void testPathFinding3() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_pathfinding3.json");
        dungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD, new JSONObject(content));
        DungeonManiaController dc = new DungeonManiaController(dungeon);
        Mercenary assassin = TestUtils.getMercenary(dungeon);

        List<Pos2d> followedPath = new ArrayList<>();

        // let the assassin find it's path
        while (!assassin.getPosition().equals(new Pos2d(0, 0))) {
            TestUtils.getPlayer(dungeon).setHealth(40);
            dc.tick(null, Direction.NONE);
            if (!followedPath.stream().anyMatch(p -> p.equals(assassin.getPosition())))
                followedPath.add(assassin.getPosition());
        }

        // define expected path
        List<Pos2d> expectedPath = Arrays.asList(new Pos2d(7, 8), new Pos2d(6, 8), new Pos2d(5, 8), new Pos2d(4, 8),
                new Pos2d(4, 7), new Pos2d(4, 6), new Pos2d(5, 6), new Pos2d(6, 6), new Pos2d(7, 6), new Pos2d(8, 6),
                new Pos2d(8, 5), new Pos2d(8, 4), new Pos2d(7, 4), new Pos2d(6, 4), new Pos2d(5, 4), new Pos2d(4, 4),
                new Pos2d(3, 4), new Pos2d(2, 4), new Pos2d(2, 3), new Pos2d(2, 2), new Pos2d(3, 2), new Pos2d(4, 2),
                new Pos2d(5, 2), new Pos2d(6, 2), new Pos2d(7, 2), new Pos2d(8, 2), new Pos2d(8, 1), new Pos2d(8, 0),
                new Pos2d(7, 0), new Pos2d(6, 0), new Pos2d(5, 0), new Pos2d(4, 0), new Pos2d(3, 0), new Pos2d(2, 0),
                new Pos2d(1, 0), new Pos2d(0, 0));

        assertEquals(expectedPath.size(), followedPath.size());
        for (int i = 0; i < followedPath.size(); i++) {
            assertEquals(expectedPath.get(i), followedPath.get(i), "at " + i);
        }
    }

    @Test
    public void testPathFinding4() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_pathfinding4.json");
        dungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD, new JSONObject(content));
        DungeonManiaController dc = new DungeonManiaController(dungeon);
        Mercenary assassin = TestUtils.getMercenary(dungeon);
        assassin.clearInventory();

        List<Pos2d> followedPath = new ArrayList<>();

        // let the assassin find it's path
        while (!assassin.getPosition().equals(new Pos2d(0, 0))) {
            // make sure the player does not die
            TestUtils.getPlayer(dungeon).setHealth(40);
            dc.tick(null, Direction.NONE);
            if (!followedPath.stream().anyMatch(p -> p.equals(assassin.getPosition())))
                followedPath.add(assassin.getPosition());
        }

        // define expected path
        List<Pos2d> expectedPath = Arrays.asList(new Pos2d(7, 8), new Pos2d(6, 8), new Pos2d(5, 8), new Pos2d(4, 8),
                new Pos2d(4, 7), new Pos2d(4, 6), new Pos2d(4, 5), new Pos2d(4, 4), new Pos2d(5, 4), new Pos2d(5, 3),
                new Pos2d(5, 2), new Pos2d(6, 2), new Pos2d(7, 2), new Pos2d(7, 1), new Pos2d(7, 0), new Pos2d(6, 0),
                new Pos2d(5, 0), new Pos2d(4, 0), new Pos2d(3, 0), new Pos2d(2, 0), new Pos2d(1, 0), new Pos2d(0, 0));

        assertEquals(expectedPath.size(), followedPath.size());
        for (int i = 0; i < followedPath.size(); i++) {
            assertEquals(expectedPath.get(i), followedPath.get(i), "at " + i);
        }
    }

    @Test
    public void testCircleMovement() {
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
    public void testCircleMovementAgainstBoundary() {
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
    public void testCircleMovementAgainstBoundaryTop() {
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
    public void testFriendlyMovementBasic() {
        DungeonMap map = dungeon.getMap();
        startingCell = dungeon.getMap().getCell(new Pos2d(1, 0));
        MovementBehaviour friend = new FriendlyMovementBehaviour(1, map, startingCell);

        for (int i = 0; i < 5; i++) {
            friend.move();

            assertEquals(new Pos2d(1, 0), friend.getCurrentCell().getPosition());
        }
    }

    @Test
    public void testCircleBoulderInteraction() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_simple_with_boulder.json");
        Dungeon boulderDungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD,
                new JSONObject(content));
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
    public void testBoulderEdgeSeven() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_simple_with_boulder2.json");
        Dungeon boulderDungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD,
                new JSONObject(content));
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
    public void testBoulderEdgeZero() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_simple_with_boulder2.json");
        Dungeon boulderDungeon = Dungeon.fromJSONObject(new Random(1), "name", GameMode.STANDARD,
                new JSONObject(content));
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
        Dungeon d = new Dungeon(new Random(1), "manual", GameMode.STANDARD, map, new ExitGoal());

        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new CircleMovementBehaviour(0, map, cell);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        for (int i = 0; i < 10; i++)
            assertEquals(cell.getPosition(), movement.move().getPosition());
    }

    @Test
    public void testBlockedFleeMovementBehaviour() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon d = new Dungeon(new Random(1), "manual", GameMode.STANDARD, map, new ExitGoal());

        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new FleeMovementBehaviour(0, map, cell);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        for (int i = 0; i < 10; i++)
            assertEquals(cell.getPosition(), movement.move().getPosition());
    }

    @Test
    public void testBlockedFriendlyMovementBehaviour() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon d = new Dungeon(new Random(1), "manual", GameMode.STANDARD, map, new ExitGoal());

        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new FriendlyMovementBehaviour(0, map, cell);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        for (int i = 0; i < 10; i++)
            assertEquals(cell.getPosition(), movement.move().getPosition());
    }

    @Test
    public void testBlockedRandomMovementBehaviour() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon d = new Dungeon(new Random(1), "manual", GameMode.STANDARD, map, new ExitGoal());

        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new RandomMovementBehaviour(0, d, cell);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        for (int i = 0; i < 10; i++)
            assertEquals(cell.getPosition(), movement.move().getPosition());
    }

    @Test
    public void testBlockedFollowMovementBehaviour() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon d = new Dungeon(new Random(1), "manual", GameMode.STANDARD, map, new ExitGoal());

        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new FollowMovementBehaviour(0, map, cell);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        for (int i = 0; i < 10; i++)
            assertEquals(cell.getPosition(), movement.move().getPosition());
    }

    @Test
    public void testRandomMovementDistribution() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon dungeon = new Dungeon(new Random(1), "manual", GameMode.STANDARD, map, new ExitGoal());

        Cell cell = map.getCell(4, 4);
        MovementBehaviour movement = new RandomMovementBehaviour(0, dungeon, cell);

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
