package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

public class OneRing extends CollectableEntity {

    ///////////////////////////////////////////////////////////////
    // TODO: Delete this class. only created for testing assassins.
    ///////////////////////////////////////////////////////////////

    public static final String STRING_TYPE = "one_ring";

    public OneRing(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean isInteractable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getTypeAsString() {
        // TODO Auto-generated method stub
        return STRING_TYPE;
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
        
    }
    
}
