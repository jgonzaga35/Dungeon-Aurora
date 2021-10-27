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
        startingCell = dungeon.getMap().getCell(new Pos2d(2, 2));
    }

    @Test
    public void testCircleMovement() {
        DungeonMap map = dungeon.getMap();
        Movement spider = new CircleMovementBehaviour(map, startingCell);
        
        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(2, 1));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(3, 1));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(3, 2));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(3, 3));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(2, 3));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(1, 3));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(1, 2));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(1, 1));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(2, 1));
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
    }

    @Test
    public void testRandomMovement() {
        DungeonMap map = dungeon.getMap();
        Movement zombie = new RandomMovementBehaviour(map, startingCell);
        
        // Test that the zombie doesn't go onto walls.
        for (int i = 0; i < 1000; i++)
        {
            Pos2d prevPos = zombie.getCurrentPosition().getPosition();
            
            zombie.move();
            assertFalse(prevPos.equals(zombie.getCurrentPosition().getPosition()));
            assertFalse(new Pos2d(0, 1).equals(zombie.getCurrentPosition().getPosition()));
            assertFalse(new Pos2d(1, 1).equals(zombie.getCurrentPosition().getPosition()));
            assertFalse(new Pos2d(2, 1).equals(zombie.getCurrentPosition().getPosition()));
            assertFalse(new Pos2d(3, 1).equals(zombie.getCurrentPosition().getPosition()));
        }
    }

    @Test
    public void testFleeMovement() {
        DungeonMap map = dungeon.getMap();
        Movement scaredZombie = new FleeMovementBehaviour(map, startingCell);

        scaredZombie.move();
        assertTrue(
            scaredZombie.getCurrentPosition().getPosition().equals(new Pos2d(1, 2)) |
            scaredZombie.getCurrentPosition().getPosition().equals(new Pos2d(2, 3))
        );
        
        scaredZombie.move();
        assertTrue(
            scaredZombie.getCurrentPosition().getPosition().equals(new Pos2d(0, 2)) |
            scaredZombie.getCurrentPosition().getPosition().equals(new Pos2d(1, 3)) |
            scaredZombie.getCurrentPosition().getPosition().equals(new Pos2d(2, 4))
        );
        
        scaredZombie.move();
        assertTrue(
            scaredZombie.getCurrentPosition().getPosition().equals(new Pos2d(0, 3)) |
            scaredZombie.getCurrentPosition().getPosition().equals(new Pos2d(1, 4))
        );
        
        scaredZombie.move();
        assertTrue(scaredZombie.getCurrentPosition().getPosition().equals(new Pos2d(0, 4)));
        
        scaredZombie.move();
        assertTrue(scaredZombie.getCurrentPosition().getPosition().equals(new Pos2d(0, 4)));
        
        scaredZombie.move();
        assertTrue(scaredZombie.getCurrentPosition().getPosition().equals(new Pos2d(0, 4)));
    }
}
