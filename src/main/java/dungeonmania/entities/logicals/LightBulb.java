package dungeonmania.entities.logicals;

import java.util.Objects;
import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.LogicalEntity;


public class LightBulb extends LogicalEntity {

    public static String STRING_TYPE = "light_bulb";
    public static String ON = "_on";
    public static String OFF = "_off";
    public boolean activated = false;

    public LightBulb(Dungeon dungeon, Pos2d position, String logic) {
        super(dungeon, position, logic);
    }

    /**
     * Turn the Light Bulb On 
     */
    @Override
    public void activate() {
        this.activated = true;
    }

    /**
     * Turn the Light Bulb Off
     */
    @Override
    public void deactivate() {
        this.activated = false;
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
        if (activated) {
            return LightBulb.STRING_TYPE + LightBulb.ON;
        }
        return LightBulb.STRING_TYPE + LightBulb.OFF;
    }

    @Override
    public void tick() {
        //this.getConnectedEntities().stream().filter(e -> e instanceof FloorSwitch).forEach(f -> f.tick());
        this.getConnectedEntities()
            .stream()
            .filter(e -> e instanceof FloorSwitch )
            .forEach(f -> f.tick());
            
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
}
