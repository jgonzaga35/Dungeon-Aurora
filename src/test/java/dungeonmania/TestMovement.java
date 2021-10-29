package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.movement.FleeMovementBehaviour;
import dungeonmania.movement.MovementBehaviour;
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
    public void testFleeMovement() {
        DungeonMap map = dungeon.getMap();
        MovementBehaviour scaredZombie = new FleeMovementBehaviour(0, map, startingCell);

        scaredZombie.move();
        assertTrue(
            scaredZombie.getCurrentCell().getPosition().equals(new Pos2d(1, 2)) |
            scaredZombie.getCurrentCell().getPosition().equals(new Pos2d(2, 3))
        );
        
        scaredZombie.move();
        assertTrue(
            scaredZombie.getCurrentCell().getPosition().equals(new Pos2d(0, 2)) |
            scaredZombie.getCurrentCell().getPosition().equals(new Pos2d(1, 3)) |
            scaredZombie.getCurrentCell().getPosition().equals(new Pos2d(2, 4))
        );
        
        scaredZombie.move();
        assertTrue(
            scaredZombie.getCurrentCell().getPosition().equals(new Pos2d(0, 3)) |
            scaredZombie.getCurrentCell().getPosition().equals(new Pos2d(1, 4))
        );
        
        scaredZombie.move();
        assertTrue(scaredZombie.getCurrentCell().getPosition().equals(new Pos2d(0, 4)));
        
        scaredZombie.move();
        assertTrue(scaredZombie.getCurrentCell().getPosition().equals(new Pos2d(0, 4)));
        
        scaredZombie.move();
        assertTrue(scaredZombie.getCurrentCell().getPosition().equals(new Pos2d(0, 4)));
    }
}
