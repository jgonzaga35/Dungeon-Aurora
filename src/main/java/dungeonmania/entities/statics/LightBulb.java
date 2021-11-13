package dungeonmania.entities.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;


public class LightBulb extends StaticEntity {

    public static String STRING_TYPE = "light_bulb";
    public static String ON = "_on";
    public static String OFF = "_off";

    private List<Entity> connectedEntities = new ArrayList<Entity>();
    public boolean activated = false;
    public Logic logic;

    public LightBulb(Dungeon dungeon, Pos2d position, String logic) {
        super(dungeon, position);
        this.logic = parseLogic(logic);
        addConnectedEntities(dungeon.getMap().getCell(position));
    }

    /**
     * Turn the Light Bulb On 
     */
    public void activate() {
        this.activated = true;
    }

    /**
     * Turn the Light Bulb Off
     */
    public void deactivate() {
        this.activated = false;
    }

    /**
     * Count the number of activated adjacent switches,
     * including those connected by wires.
     * @return
     */
    public Integer countActivatedSwitches() {
        int count = (int) connectedEntities.stream()
                                           .filter(e -> e instanceof FloorSwitch && ((FloorSwitch) e).isActivated())
                                           .count();
        
        return count;

    }

    /**
     * Count the number of activated adjacent switches
     * that were activated at the same tick count.
     */
    public Integer countCoActivatedSwitches() {
        FloorSwitch s = (FloorSwitch) connectedEntities.stream()
                         .filter(e -> e instanceof FloorSwitch && ((FloorSwitch) e).isActivated())
                         .findFirst().orElse(null);
        
        if (s != null) {
            int tickActivationCount = s.getTickCountActivated();
            int count = (int) connectedEntities.stream()
                                               .filter(e -> e instanceof FloorSwitch 
                                                        && ((FloorSwitch) e).isActivated()
                                                        && ((FloorSwitch) e).getTickCountActivated() == tickActivationCount)
                                               .count();
            return count;
        } else {
            return -1;
        }
    }

    /**
     * Count the number of adjacent switches,
     * including those connected by wires.
     */
    public Integer countAdjacentSwitches() {
        int count = (int) connectedEntities.stream()
                                           .filter(e -> e instanceof FloorSwitch)
                                           .count();
        
        return count;

    }

    @Override 
    public boolean canConnect() {
        return true;
    }
    /**
     * Add all entities that are either switches or interact via switches
     * Every Wire should have a list of entities that are connected
     * to the circuit and not just the individual wire.
     */
    public void addConnectedEntities(Cell cell) {
        
        // Get cardinally adjacent cells
        Stream<Cell> adjacentCells = this.dungeon.getMap().getCellsAround(cell);
        
        // Add connected entities from adjacent cells 
        adjacentCells.forEach(c -> {
            c.getOccupants().stream()
                            .filter(e -> e.canConnect())
                            .forEach(s -> {
                                System.out.println(s.getTypeAsString());
                                if (s instanceof Wire) {
                                    addConnectedEntities(c);
                                } else {
                                    if (!connectedEntities.contains(s)) {
                                            connectedEntities.add(s);
                                    }
                                }
                            });
        });
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

    // TODO: fix
    public void co_andActivation() {
        if (countActivatedSwitches() == countCoActivatedSwitches()) {
            this.activate();
        }
    }

    public void notActivation() {
        if (countActivatedSwitches() == 0) {
            this.activate();
        }
    }

    public void xorActivation() {
        if (countActivatedSwitches() == 1) {
            this.activate();
        }
    }

    public void orActivation() {
        if (countActivatedSwitches() >= 1) {
            this.activate();
        }
    }

    public void andActivation() {
        // Entity only activates if there are 2 or more adjacent activated switches
        // If there are more than two switches adjacent, all must be activated. 
        int activatedSwitchCount = countActivatedSwitches();
        int adjacentSwitchCount = countAdjacentSwitches();

        if (activatedSwitchCount > 2 && (activatedSwitchCount == adjacentSwitchCount)) {
            this.activate();
            
        } else if (activatedSwitchCount >= 2) { 
            this.activate();
        } else {
            this.deactivate();
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
