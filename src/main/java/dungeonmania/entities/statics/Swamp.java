package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;

public class Swamp extends StaticEntity {

    public static String STRING_TYPE = "swamp_tile";

    public Swamp(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
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
