package dungeonmania.goal;

import java.util.List;

import dungeonmania.Dungeon;

public class ExitGoal extends Goal {
   
    public static String STRING_TYPE = "exit";
    public ExitGoal() {
        super();
    }

    public boolean isCompleted(Dungeon dungeon) {
        Goal goal = dungeon.getGoal();
        if (goal instanceof AndGoal || goal instanceof OrGoal) {
            List<Goal> subgoals = goal.getSubgoals();
            
            // Check that every other subgoal has been completed before checking exit condition
            for (Goal subgoal: subgoals) {
                if (!subgoal.isCompleted(dungeon)) {
                    return false;
                }  
            }
        }

        // Check if exit condition has been reached.

        return false;
    }

    @Override
    public String asString() {
        return "get to an exit";
    }
    
}
