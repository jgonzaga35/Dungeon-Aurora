package dungeonmania.entities.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.entities.Connectable;
import dungeonmania.entities.StaticEntity;
import dungeonmania.util.BlockingReason;

public class FloorSwitch extends StaticEntity implements Connectable {
    public static String STRING_TYPE = "switch";
    public static String ACTIVATED = "_activated";
    private boolean activated = false;
    private int tickCountActivated;
    private List<Entity> connectedEntities = new ArrayList<Entity>();
    private List<String> connectedId = new ArrayList<String>();
    public Logic logic;

    public FloorSwitch(Dungeon dungeon, Pos2d position, String logic) {
        super(dungeon, position);
        this.logic = parseLogic(logic);
    }

    @Override
    public void connectEntities() {
        addConnectedEntities(this.getCell(), this.connectedId);
    }

    /**
     * Sets a floor switch's trigger status.
     * If a floor switch adjacent to a wire is activated,
     * activate all other interactable entities adjacent 
     * to the wire.
     * @param bool
     */
    public void activate(boolean bool) {
        this.activated = bool;
        this.tickCountActivated = dungeon.getTickCount();

    }

    public boolean isActivated() {
        return activated;
    }

    public Integer getTickCountActivated() {
        return this.tickCountActivated;
    }

    @Override
    public BlockingReason isBlocking() {
        return BlockingReason.NOT;
    }

    /**
     * Turn the Light Bulb On 
     */
    public void activate() {
        this.activated = true;
        connectedEntities.stream().filter(e -> e instanceof FloorSwitch).forEach(f -> f.tick());
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
    public void addConnectedEntities(Cell cell, List<String> connectedIds) {
        
        // Get cardinally adjacent cells
        Stream<Cell> adjacentCells = this.dungeon.getMap().getCellsAround(cell);
        
        // Add connected entities from adjacent cells 
        adjacentCells.forEach(c -> {
            c.getOccupants().stream()
                            .filter(e -> e.canConnect())
                            .forEach(s -> {
                                System.out.println(s.getTypeAsString());
                                if (s instanceof Wire && !connectedIds.contains(s.getId())) {
                                    connectedId.add(s.getId());
                                    addConnectedEntities(c, connectedId);
                                } else {
                                    if (!connectedEntities.contains(s) && !connectedIds.contains(s.getId())) {
                                            connectedEntities.add(s);
                                            connectedId.add(s.getId());
                                    }
                                }
                            });
        });
    }

    @Override
    public boolean isInteractable() {
        return false;
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

    public void co_andActivation() {
        if (countActivatedSwitches() == countCoActivatedSwitches()) {
            this.activate(); 
        } else {
            this.deactivate();
        }
    }

    public void notActivation() {
        if (countActivatedSwitches() == 0) {
            this.activate();
        } else {
            this.deactivate();
        }
    }

    public void xorActivation() {
        System.out.println("THERE ARE" + countActivatedSwitches());
        if (countActivatedSwitches() == 1) {
            System.out.println("ACTIVATING");
            this.activate();
        } else {
            this.deactivate();
        }
    }

    public void orActivation() {
        if (countActivatedSwitches() >= 1) {
            this.activate();
        } else {
            this.deactivate();
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
        if (Objects.isNull(logic)) 
            return null;
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
