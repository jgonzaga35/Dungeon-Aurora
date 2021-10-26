package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        startingCell = map.getCell(new Pos2d(3, 6));
    }

    @Test
    public void testCircleMovement() {
        Movement spider = new CircleMovementBehaviour(map, startingCell);
        
        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(3, 5));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(4, 5));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(4, 6));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(4, 7));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(3, 7));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(2, 7));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(2, 6));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(2, 5));

        spider.move();
        assertEquals(spider.getCurrentPosition().getPosition(), new Pos2d(3, 5));
    }

    @Test
    public void testFollowMovement() {
        Movement merc = new FollowMovementBehaviour(map, startingCell);
    }

    @Test
    public void testRandomMovement() {
        Movement zombie = new RandomMovementBehaviour(map, startingCell);
    }

    @Test
    public void testFleeMovement() {
        Movement scaredZombie = new FleeMovementBehaviour(map, startingCell);
    }
}
