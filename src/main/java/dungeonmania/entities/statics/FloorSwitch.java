package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.LogicalEntity;

/**
 * Represents a floor switch.
 * Switches behave like empty squares, so other entities can appear on top of them. 
 * When a boulder is pushed onto a floor switch, it is triggered.
 *  Pushing a boulder off the floor switch untriggers it.
 */
public class FloorSwitch extends LogicalEntity {
    public static String STRING_TYPE = "switch";
    public static String ACTIVATED = "_activated";
    private boolean activated = false;
    private int tickCountActivated;

    public FloorSwitch(Dungeon dungeon, Pos2d position, String logic) {
        super(dungeon, position, logic);
    }

    public boolean isActivated() {
        return activated;
    }

    public Integer getTickCountActivated() {
        return this.tickCountActivated;
    }

    /**
     * Sets a floor switch's trigger status.
     * If a floor switch adjacent to a wire is activated,
     * activate all other interactable entities adjacent 
     * to the wire.
     * @param bool
     */
    @Override
    public void activate() {
        this.activated = true;
        this.tickCountActivated = dungeon.getTickCount();

    }

    /**
     * Deactivate
     */
    @Override
    public void deactivate() {
        this.activated = false;
    }

    /**
     * Returns the type of the entity as a string.
     */
    @Override
    public String getTypeAsString() {
        if (activated) {
            return FloorSwitch.STRING_TYPE + FloorSwitch.ACTIVATED;
        }
        return FloorSwitch.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

}
