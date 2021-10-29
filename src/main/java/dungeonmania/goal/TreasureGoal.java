package dungeonmania.goal;

import dungeonmania.Dungeon;

public class TreasureGoal extends Goal {
    
    public static String STRING_TYPE = "treasure";
    public TreasureGoal() {
        super();
    }

    public boolean isCompleted(Dungeon dungeon) {
        // Count all treasure remaining uncollected in the dungeon.
        return false;
    }


    @Override
    public String asString() {
        
        return "collect all treasure";
    }
}
