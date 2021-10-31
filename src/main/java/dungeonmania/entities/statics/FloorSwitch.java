package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;
import dungeonmania.util.BlockingReason;

public class FloorSwitch extends StaticEntity{
    public static final String STRING_TYPE = "floor switch";
    private boolean triggered = false;

    public FloorSwitch(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    public void setTriggered(boolean bool) {
        this.triggered = bool;
    }

    public boolean isTriggered() {
        return this.triggered;
    }

    @Override
    public BlockingReason isBlocking() {
        return BlockingReason.NOT;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public void tick() {
        
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }


}
