package dungeonmania.entities.statics;


import java.util.Objects;
import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.LogicalEntity;
import dungeonmania.util.BlockingReason;

public class SwitchDoor extends LogicalEntity {
    
    public static String STRING_TYPE = "switch_door";
    public static String UNLOCKED = "_unlocked";
    private boolean locked = true;

    public SwitchDoor(Dungeon dungeon, Pos2d position, String logic) {
        super(dungeon, position, logic);
    }

    public boolean isLocked() {
        return this.locked;
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    /**
     * Returns the type of the entity as a string.
     */
    @Override
    public String getTypeAsString() {
        if (locked) return SwitchDoor.STRING_TYPE;
        else return SwitchDoor.STRING_TYPE + SwitchDoor.UNLOCKED;
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
    public BlockingReason isBlocking() {
        if (locked) return BlockingReason.DOOR;
        else return BlockingReason.NOT;
    }

    @Override
    public void activate() {
        this.locked = false;
    }

    @Override
    public void deactivate() {
        locked = true;
    }

}
