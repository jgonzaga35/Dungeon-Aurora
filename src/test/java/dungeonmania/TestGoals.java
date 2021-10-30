package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestGoals {

    @Test
    public void testGetBasicGoalsAsString() {
        DungeonManiaController ctr = new DungeonManiaController();

        DungeonResponse resp = assertDoesNotThrow(() -> {
            return ctr.newGame("maze", "Peaceful");
        });

        assertEquals("get to an exit", resp.getGoals());
    }


    @Test
    public void testGetComplexGoalsAsString() {
        DungeonManiaController ctr = new DungeonManiaController();

        DungeonResponse resp = assertDoesNotThrow(() -> {
            return ctr.newGame("complex_maze", "Peaceful");
        });

        assertEquals("destroy all enemies and spawners AND get to an exit", resp.getGoals());
    }

    @Test
    public void testGetRecursiveGoalsAsString() {
        DungeonManiaController ctr = new DungeonManiaController();

        DungeonResponse resp = assertDoesNotThrow(() -> {
            return ctr.newGame("recursive_goals", GameMode.PEACEFUL.getValue());
        });

        assertEquals("(destroy all enemies and spawners AND collect all treasure) OR get to an exit", resp.getGoals());
    }


    @Test
    public void testCheckExitGoalCompletion() {
        DungeonManiaController ctr = new DungeonManiaController();
        Position p;
        
        DungeonResponse resp = ctr.newGame("exit", GameMode.PEACEFUL.getValue());
        assertEquals("get to an exit",resp.getGoals());

        // move on exit
        resp = ctr.tick("", Direction.RIGHT);
        p = TestUtils.getPlayerPosition(resp);
        assertEquals(1, p.getX());
        assertEquals(0, p.getY());
        
        // goals should now return empty string
        assertEquals("",resp.getGoals());
    }

    @Test
    public void testCheckTreasureGoalCompletion() {
        DungeonManiaController ctr = new DungeonManiaController();
        
        DungeonResponse resp = ctr.newGame("treasure_goal", GameMode.PEACEFUL.getValue());
        assertEquals("collect all treasure",resp.getGoals());

        resp = ctr.tick("", Direction.NONE);

        // Collect all treasure
        ctr.tick("", Direction.DOWN);
        resp = ctr.tick("", Direction.DOWN);


        // goals should now return empty string
        assertEquals("",resp.getGoals());
    }

    @Test
    public void testCheckOrGoalCompletion() {
        DungeonManiaController ctr = new DungeonManiaController();
        
        DungeonResponse resp = ctr.newGame("treasure_or_exit_goal", GameMode.PEACEFUL.getValue());
        assertEquals("collect all treasure OR get to an exit",resp.getGoals());

        ctr.tick("", Direction.NONE);

        // Collect all treasure
        resp = ctr.tick("", Direction.DOWN);

        // goals should now return empty string
        assertEquals("",resp.getGoals());
    }

    @Test
    public void testCheckAndGoalCompletion() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("treasure_and_exit_goal", GameMode.PEACEFUL.getValue());
        assertEquals("collect all treasure AND get to an exit",resp.getGoals());

        ctr.tick("", Direction.NONE);

        // Collect all treasure
        resp = ctr.tick("", Direction.DOWN);

        assertEquals("collect all treasure AND get to an exit",resp.getGoals());
        
        resp = ctr.tick("", Direction.DOWN);

        // goals should now return empty string
        assertEquals("",resp.getGoals());
    }

    @Test
    public void testCheckAndGoalCompletionExitFirst() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("exit_and_treasure_goal", GameMode.PEACEFUL.getValue());
        assertEquals("collect all treasure AND get to an exit",resp.getGoals());

        ctr.tick("", Direction.NONE);

        // Go to exit
        resp = ctr.tick("", Direction.DOWN);
        assertEquals("collect all treasure AND get to an exit",resp.getGoals());
        
        // Collect treasure
        resp = ctr.tick("", Direction.DOWN);

        // Goal should not be completed yet, need to go back to exit
        assertEquals("collect all treasure AND get to an exit",resp.getGoals());

        // Go back to the exit, now should expect empty string 
        resp = ctr.tick("", Direction.UP);
        assertEquals("",resp.getGoals());
    }

    @Test
    public void testCheckRecursiveGoalCompletion() {
        DungeonManiaController ctr = new DungeonManiaController();
        
        DungeonResponse resp = ctr.newGame("recursive_goals_test", GameMode.PEACEFUL.getValue());
        assertEquals("(destroy all enemies and spawners AND collect all treasure) OR get to an exit",resp.getGoals());

        ctr.tick("", Direction.NONE);

        // Collect all treasure. Since enemies and treasure goal are complete, the goal should be complete.
        resp = ctr.tick("", Direction.DOWN);
        assertEquals("",resp.getGoals());

        // Now try completing the exit goal only
        ctr = new DungeonManiaController();
        resp = ctr.newGame("recursive_goals_test_2", GameMode.PEACEFUL.getValue());
        assertEquals("(destroy all enemies and spawners AND collect all treasure) OR get to an exit",resp.getGoals());
        
        resp = ctr.tick("", Direction.DOWN);
        assertEquals("",resp.getGoals());
    }


}
