package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;
import dungeonmania.util.BlockingReason;

public class FloorSwitch extends StaticEntity {
    public static String STRING_TYPE = "switch";
    private boolean triggered = false;

    public FloorSwitch(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    /**
     * Sets a floor switch's trigger status.
     * If a floor switch adjacent to a wire is triggered,
     * activate all other interactable entities adjacent 
     * to the wire.
     * @param bool
     */
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
        return FloorSwitch.STRING_TYPE;
    }


}
