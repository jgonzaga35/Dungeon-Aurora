package dungeonmania.goal;

import dungeonmania.Dungeon;

public class EnemiesGoal extends Goal {

    public static String STRING_TYPE = "ENEMIES";
    public EnemiesGoal() {
        super();
    }

    public boolean isCompleted(Dungeon dungeon) {
        // Count all survivng enemies
        return false;
    }

    @Override
    public String asString() {
        return "destroy all enemies and spawners";
    }
    
}
