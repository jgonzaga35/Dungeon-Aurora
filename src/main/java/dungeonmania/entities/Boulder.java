package dungeonmania.entities;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;

public class Boulder extends StaticEntity {
    public static String STRING_TYPE = "boulder";

    public Boulder(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public boolean isBlocking() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isInteractable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getTypeAsString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
        
    }
    
}
