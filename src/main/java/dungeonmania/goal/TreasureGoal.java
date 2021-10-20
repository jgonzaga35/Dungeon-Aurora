package dungeonmania.goal;

public class TreasureGoal extends Goal {
    public TreasureGoal() {
        super();
    }


    @Override
    public void addSubGoal(Goal goal) {
    }

    @Override
    public String asString() {
        
        return "collect all treasure";
    }
}
