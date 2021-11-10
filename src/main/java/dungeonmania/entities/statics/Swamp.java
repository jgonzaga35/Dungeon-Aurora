package dungeonmania.entities.statics;


import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;

public class Swamp extends StaticEntity {

    public static String STRING_TYPE = "swamp_tile";
    
    private Integer movement_factor;

    public Swamp(Dungeon dungeon, Pos2d position, Integer movement_factor) {
        super(dungeon, position);
        this.movement_factor = movement_factor;
    }

    public Integer getMovementFactor() {
        return this.movement_factor;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public String getTypeAsString() {
        return Swamp.STRING_TYPE;
    }

    @Override
    public void tick() {}
}
