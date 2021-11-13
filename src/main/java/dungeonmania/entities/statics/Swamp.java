package dungeonmania.entities.statics;


import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;

public class Swamp extends StaticEntity {

    public static String STRING_TYPE = "swamp_tile";
    
    private Integer movementFactor;

    public Swamp(Dungeon dungeon, Pos2d position, Integer movementFactor) {
        super(dungeon, position);
        this.movementFactor = movementFactor;
    }

    public Integer getMovementFactor() {
        return this.movementFactor;
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
