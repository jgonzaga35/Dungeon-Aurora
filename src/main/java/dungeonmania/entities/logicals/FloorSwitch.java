package dungeonmania.entities.logicals;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.LogicalEntity;

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
        if (!activated) {
            this.activated = true;
            this.tickCountActivated = dungeon.getTickCount();
        }
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
