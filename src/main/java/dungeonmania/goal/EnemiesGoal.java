package dungeonmania.goal;

import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;

public class EnemiesGoal extends Goal {

    public static String STRING_TYPE = "enemies";
    public EnemiesGoal() {
        super();
    }

    /**
     * I don't include spiders at the moment for the sake of testing, I will add later - justin
     */
    @Override
    public boolean isCompleted(Dungeon dungeon) {
        // Count all enemies and spawners
        DungeonMap map = dungeon.getMap();
        if (map.countCellsWithZombieToast() == 0 && map.countCellsWithSpawners() == 0) {
            return true;
        }

        return false;
    }

    @Override
    public String asString() {
        return "destroy all enemies and spawners";
    }
    
}
