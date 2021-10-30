package dungeonmania.goal;

import java.util.ArrayList; 
import java.util.List;

import org.json.JSONObject;

import dungeonmania.Dungeon;

import org.json.JSONArray;
import org.json.JSONException;

public class AndGoal extends Goal {

    public static String STRING_TYPE = "AND";
    private List<Goal> subgoals = new ArrayList<Goal>();
    private boolean child = false;

    public AndGoal() {
        super();
    }

    @Override 
    public boolean isCompleted(Dungeon dungeon) {
        return subgoals.stream().allMatch(subgoal -> subgoal.isCompleted(dungeon));
    }

    /**
     * Set the goal as a child goal 
     */
    @Override
    public void setChild(boolean bool) {
        this.child = bool;
    }

    /**
     * Check if the goal as a child goal 
     */
    @Override
    public boolean isChild() {
        return this.child;
    }

    /**
     * Given a JSONObject, add the subgoals to the goal
     */
    @Override
    public void addSubGoalFromJSON(JSONObject goalsJSON) {
        if (goalsJSON == null) return;
        String type = goalsJSON.getString("goal");
        Goal subgoal = Goal.getGoalFromGoalType(type);


        if (subgoal instanceof AndGoal || subgoal instanceof OrGoal) {
            subgoal.setHasSubGoal(true);
            JSONArray subgoalsJSON = goalsJSON.getJSONArray("subgoals");
   
            for (int i = 0; i < subgoalsJSON.length(); i++) {
                JSONObject obj = null;
                try {
                    obj = subgoalsJSON.getJSONObject(i);
                } catch (JSONException e) {
                    String subgoalType = (String) subgoalsJSON.get(i);
                    subgoal.addToSubGoals(Goal.getGoalFromGoalType(subgoalType));
                }
                addSubGoalFromJSON(obj);
            }
            subgoals.add(subgoal);
            return;
        }

        subgoals.add(subgoal);
    }

    @Override
    public void addToSubGoals(Goal subgoal) {
        subgoals.add(subgoal);
    }
    
    @Override
    public List<Goal> getSubgoals() {
        return this.subgoals;
    }

    @Override
    public String asString() {
        String goals;
        if (isChild()) {
            goals = "(";
        } else {
            goals = "";
        }
        
        int i = 0;
        for (Goal subgoal: subgoals) {
            goals = goals + subgoal.asString();
            
            if (i < subgoals.size() - 1) {
                goals = goals + " AND ";
            }
            i++;
        }

        if (isChild()) {
            goals = goals + ")";
        }
        return goals;
    }
}
