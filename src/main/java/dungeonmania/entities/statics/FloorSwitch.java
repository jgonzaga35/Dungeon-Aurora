package dungeonmania.entities.statics;


import java.util.Objects;
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
        this.activated = true;
        this.tickCountActivated = dungeon.getTickCount();
        this.getConnectedEntities().stream().filter(e -> e instanceof FloorSwitch).forEach(f -> f.tick());

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
        if (activated) 
            return FloorSwitch.STRING_TYPE + FloorSwitch.ACTIVATED;
        return FloorSwitch.STRING_TYPE;
    }

    @Override
    public void tick() {
        if (Objects.isNull(logic)) {

        } else if (Objects.equals(logic, Logic.AND)) {
            andActivation();
        } else if (Objects.equals(logic, Logic.OR)) {
            orActivation();
        } else if (Objects.equals(logic, Logic.XOR)) {
            xorActivation();
        } else if (Objects.equals(logic, Logic.NOT)) {
            notActivation();
        } else if (Objects.equals(logic, Logic.CO_AND)) {
            co_andActivation();
        }
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

}
