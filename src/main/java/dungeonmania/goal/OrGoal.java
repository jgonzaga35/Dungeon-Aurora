package dungeonmania.goal;

import java.util.ArrayList; 
import java.util.List;


public class OrGoal extends Goal {

    public static String STRING_TYPE = "OR";
    private List<Goal> subgoals = new ArrayList<Goal>();
    
    public OrGoal() {
        super();
    }

    @Override
    public void addSubGoal(Goal goal) {
        subgoals.add(goal);
    }

    @Override
    public String asString() {
        String goals = "";
        
        int i = 0;
        for (Goal subgoal: subgoals) {
            goals = goals + subgoal.asString();
            
            if (i < subgoals.size() - 1) {
                goals = goals + " OR ";
            }
            i++;
        }
        return goals;
    }
    
}
