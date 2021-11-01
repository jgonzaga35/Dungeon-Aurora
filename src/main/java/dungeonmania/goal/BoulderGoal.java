package dungeonmania.goal;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;

public class BoulderGoal extends Goal {

    public static String STRING_TYPE = "boulder";
    public BoulderGoal() {
        super();
    }

    @Override
    public boolean isCompleted(Dungeon dungeon) {
        DungeonMap map = dungeon.getMap();
        return map.allFloorSwitchesTriggered();
    }

    @Override
    public String asString() {
        return "have a boulder on all floor switches";
    }
    
}
