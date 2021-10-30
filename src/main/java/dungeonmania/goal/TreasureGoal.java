package dungeonmania.goal;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;

public class TreasureGoal extends Goal {
    
    public static String STRING_TYPE = "treasure";
    public TreasureGoal() {
        super();
    }

    @Override
    public boolean isCompleted(Dungeon dungeon) {
        // Count all treasure remaining uncollected in the dungeon.
        DungeonMap map = dungeon.getMap();
        if (map.countTreasure() == 0) {
            return true;
        }
        
        return false;
    }


    @Override
    public String asString() {
        
        return "collect all treasure";
    }
}
