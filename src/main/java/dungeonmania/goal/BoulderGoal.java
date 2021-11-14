package dungeonmania.goal;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;

/**
 * Represents boulder goal.
 * A game is won when all floor swithces have boulders on them.
 */
public class BoulderGoal extends Goal {

    public static String STRING_TYPE = "boulders";
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
