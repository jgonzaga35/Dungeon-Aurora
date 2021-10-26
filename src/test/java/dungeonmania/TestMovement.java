package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.movement.CircleMovementBehaviour;
import dungeonmania.movement.FleeMovementBehaviour;
import dungeonmania.movement.FollowMovementBehaviour;
import dungeonmania.movement.Movement;
import dungeonmania.movement.RandomMovementBehaviour;
import dungeonmania.util.FileLoader;

public class TestMovement {
    DungeonMap map;
    Cell startingCell;

    @BeforeEach
    public void setStartingPostition() throws IOException {
        String content = FileLoader.loadResourceFile("/dungeons/_simple.json");
        map = new DungeonMap(new JSONObject(content));
        startingCell = map.getCell(new Pos2d(2, 2));
    }

    @Test
    public void testCircleMovement() {
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
        Movement zombie = new RandomMovementBehaviour(map, startingCell);

        for (int i = 0; i < 1000; i++)
        {
            Pos2d prevPos = zombie.getCurrentPosition().getPosition();

            zombie.move();
            assertNotEquals(prevPos, zombie.getCurrentPosition().getPosition());
            assertNotEquals(new Pos2d(0, 1), zombie.getCurrentPosition().getPosition());
            assertNotEquals(new Pos2d(1, 1), zombie.getCurrentPosition().getPosition());
            assertNotEquals(new Pos2d(2, 1), zombie.getCurrentPosition().getPosition());
            assertNotEquals(new Pos2d(3, 1), zombie.getCurrentPosition().getPosition());
        }
    }

    @Test
    public void testFleeMovement() {
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
