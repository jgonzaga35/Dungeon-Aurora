package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;
import dungeonmania.util.BlockingReason;

public class Door extends StaticEntity {

    public static String STRING_TYPE = "door";

    public int doorId;

    public Door(Dungeon dungeon, Pos2d position, int doorId) {
        super(dungeon, position);
        this.doorId = doorId;
    }

    @Override
    public BlockingReason isBlocking() {
        return BlockingReason.DOOR;
    }

    @Override
    public String getTypeAsString() {
        return Exit.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    @Override
    public void tick() {
    }

    
}
