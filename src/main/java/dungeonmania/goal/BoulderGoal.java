package dungeonmania.goal;

import dungeonmania.Dungeon;

public class BoulderGoal extends Goal {

    public static String STRING_TYPE = "boulder";
    public BoulderGoal() {
        super();
    }

    public boolean isCompleted(Dungeon dungeon) {
        // Count all boulders on floor switches
        return false;
    }

    @Override
    public String asString() {
        return "have a boulder on all floor switches";
    }
    
}
