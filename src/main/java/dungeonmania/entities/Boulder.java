package dungeonmania.entities;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.util.BlockingReason;

public class Boulder extends StaticEntity {
    public static final String STRING_TYPE = "boulder";

    public Boulder(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }



    @Override
    public BlockingReason isBlocking() {
        // TODO Auto-generated method stub
        return BlockingReason.BOULDER;
    }

    @Override
    public boolean isInteractable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }
    
}
