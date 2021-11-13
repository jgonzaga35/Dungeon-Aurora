package dungeonmania.entities.statics;

import java.util.Objects;
import java.util.stream.Stream;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;

public class LightBulb extends StaticEntity {
    public static String STRING_TYPE = "light_bulb";
    public static String ON = "_on";
    public static String OFF = "_off";

    public boolean switchedOn = false;
    public Logic logic;

    public LightBulb(Dungeon dungeon, Pos2d position, String logic) {
        super(dungeon, position);
        this.logic = parseLogic(logic);
    }

    /**
     * Turn the Light Bulb On 
     */
    public void switchOn() {
        this.switchedOn = true;
    }

    public Integer countAdjacentActivations(DungeonMap map) {
        Cell base = map.getCell(this.position);
        Stream<Cell> adjacentCells = map.getCellsAround(base);
        int count = (int) adjacentCells.filter(e -> e.hasActivatedEntity()).count();
        return count;
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
        if (switchedOn) {
            return LightBulb.STRING_TYPE + LightBulb.ON;
        }
        return LightBulb.STRING_TYPE + LightBulb.OFF;
    }

    public Integer countAdjacentSwitches(DungeonMap map) { 
        return 0;
    }

    @Override
    public void tick() {
        if (Objects.equals(logic,Logic.AND)) {
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

    // TODO: fix
    public void co_andActivation() {

    }

    public void notActivation() {
        if (countAdjacentActivations(dungeon.getMap()) == 0) {
            this.switchOn();
        }
    }

    public void xorActivation() {
        if (countAdjacentActivations(dungeon.getMap()) == 1) {
            this.switchOn();
        }
    }

    public void orActivation() {
        if (countAdjacentActivations(dungeon.getMap()) >= 1) {
            this.switchOn();
        }
    }

    public void andActivation() {
        // Entity only activates if there are 2 or more adjacent activated switches
        // If there are more than two switches adjacent, all must be activated. 
        int adjacentSwitchCount = countAdjacentActivations(dungeon.getMap());
        if (adjacentSwitchCount > 2) {
            if (countAdjacentActivations(dungeon.getMap()) == adjacentSwitchCount ) { 
                this.switchOn();
            }
        } else {
            if (countAdjacentActivations(dungeon.getMap()) >= 2) { 
                this.switchOn();
            }           
        }
    }

    public enum Logic {
        AND("and"), OR("or"), XOR("xor"), NOT("not"), CO_AND("co_and");

        private final String value;
        
        Logic(final String newValue) {
            value = newValue;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Convert string mode to enum modes so we don't make typos
     * @param gameMode
     * @return
     * @throws IllegalArgumentException
     */
    private Logic parseLogic(String logic) throws IllegalArgumentException {
        if (Objects.equals(logic, "and"))
            return Logic.AND;
        if (Objects.equals(logic, "or"))
            return Logic.OR;
        if (Objects.equals(logic, "xor"))
            return Logic.XOR;
        if (Objects.equals(logic, "not"))
            return Logic.NOT;
        if (Objects.equals(logic, "co_and"))
            return Logic.CO_AND;

        throw new IllegalArgumentException(String.format("Logic %s is invalid", logic));
    }
}
