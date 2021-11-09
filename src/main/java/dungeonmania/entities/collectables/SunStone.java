package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

public class SunStone extends CollectableEntity {

    public static final String STRING_TYPE = "sun_stone";

    public SunStone(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public void tick() {
    }

    @Override
    public String getTypeAsString() {
        return this.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; 
    }
    
}
