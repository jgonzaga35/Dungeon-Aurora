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
        if (this.switchedOn) {
            return LightBulb.STRING_TYPE + LightBulb.ON;
        }
        return LightBulb.STRING_TYPE + LightBulb.OFF;
    }

    @Override
    public void tick() {
         
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
