package dungeonmania.goal;


import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Dungeon;

public abstract class Goal {

    private Boolean hasSubGoal;
    
    public Goal() {
        this.hasSubGoal = false;
    }

    public void addSubGoal(Goal goal) {
        
    }

    public List<Goal> getSubgoals() {
        return null;
    }

    public boolean isCompleted(Dungeon dungeon) {
        return false;
    }

    /**
     * Creates a Goals instance from the JSON file's content
     * @param obj
     * @return
     */
    public static Goal fromJSONObject(JSONObject obj) {

        JSONObject goalConditions = obj.getJSONObject("goal-condition");
        String goalType = goalConditions.getString("goal");
        Goal goal = Goal.getGoalFromGoalType(goalType);

        // Special case for AND goal and OR goals: contains subgoals!
        if (goalType.equals("AND") || goalType.equals("OR")) {
            goal.setHasSubGoal(true);

            // For every subgoal in the JSON file, add them to the goal object.
            JSONArray subGoals = goalConditions.getJSONArray("subgoals");
            for (int i = 0; i < subGoals.length(); i++) {
                JSONObject json = subGoals.getJSONObject(i);
                String type = json.getString("goal");
                Goal subgoal = Goal.getGoalFromGoalType(type);
                goal.addSubGoal(subgoal);
            }
        }

        return goal;
    }

    /**
     * Given the goalType, return the corresponding Goal object
     * @param goalType
     * @return
     */
    public static Goal getGoalFromGoalType(String goalType) {

        // Cannot instantiate an abstract class, so default create AndGoal
        Goal goal = null;

        if (goalType.equals(AndGoal.STRING_TYPE)) {
            goal = new AndGoal();
        } else if (goalType.equals(OrGoal.STRING_TYPE)) {
            goal = new OrGoal();
        } else if (goalType.equals(ExitGoal.STRING_TYPE)) {
            goal = new ExitGoal();
        } else if (goalType.equals(TreasureGoal.STRING_TYPE)) {
            goal = new TreasureGoal();
        } else if (goalType.equals(BoulderGoal.STRING_TYPE)) {
            goal = new BoulderGoal();
        } else if (goalType.equals(EnemiesGoal.STRING_TYPE)) {
            goal = new EnemiesGoal();
        }

        return goal;
    }

    void setHasSubGoal(boolean bool) {
        this.hasSubGoal = bool;
    }

    public boolean hasSubGoal() {
        return hasSubGoal;
    }

    public abstract String asString();
}
