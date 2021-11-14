package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;
import dungeonmania.util.BlockingReason;

/**
 * Represents a floor switch.
 * Switches behave like empty squares, so other entities can appear on top of them. 
 * When a boulder is pushed onto a floor switch, it is triggered.
 *  Pushing a boulder off the floor switch untriggers it.
 */
public class FloorSwitch extends StaticEntity {
    public static String STRING_TYPE = "switch";
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
        return FloorSwitch.STRING_TYPE;
    }


}
