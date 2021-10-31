package dungeonmania.goal;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;

public class ExitGoal extends Goal {
   
    public static String STRING_TYPE = "exit";
    public ExitGoal() {
        super();
    }

    @Override
    public boolean isCompleted(Dungeon dungeon) {

        // Check if exit condition has been reached.
        DungeonMap map = dungeon.getMap();

        Cell playerCell = map.getPlayerCell();
        if (playerCell != null && playerCell.hasExit()) {
            return true;
        }

        return false;
    }

    @Override
    public String asString() {
        return "get to an exit";
    }
    
}
