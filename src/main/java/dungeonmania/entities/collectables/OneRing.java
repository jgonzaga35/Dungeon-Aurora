package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

public class OneRing extends CollectableEntity {
    public static final String STRING_TYPE = "one_ring";

    public OneRing(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public String getTypeAsString() {
        return OneRing.STRING_TYPE;
    }

    @Override
    public void tick() {
    }
    
}
