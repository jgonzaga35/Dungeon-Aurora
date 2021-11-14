package dungeonmania.entities.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.util.BlockingReason;

public class SwitchDoor extends Door {
    
    public static String STRING_TYPE = "switch_door";
    public static String UNLOCKED = "_unlocked";
    private List<Entity> connectedEntities = new ArrayList<Entity>();
    public Logic logic;
    private boolean locked = true;
    private List<String> connectedId = new ArrayList<String>();

    public SwitchDoor(Dungeon dungeon, Pos2d position, int doorId, String logic) {
        super(dungeon, position, doorId);
        this.logic = parseLogic(logic);
        addConnectedEntities(dungeon.getMap().getCell(position), new ArrayList<String>());
    }

    @Override
    public boolean canConnect() {
        return true;
    }    

    public boolean isLocked() {
        return this.locked;
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
                                    if (!connectedEntities.contains(s)) {
                                            connectedEntities.add(s);
                                            connectedId.add(s.getId());
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
        if (locked) return SwitchDoor.STRING_TYPE;
        else return SwitchDoor.STRING_TYPE + SwitchDoor.UNLOCKED;
    }

    @Override
    public void tick() {
        connectedEntities.stream().filter(e -> e instanceof FloorSwitch).forEach(f -> f.tick());
        if (Objects.isNull(logic)) {
            this.open();
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

    public boolean activate() {
        locked = false;
        return true;
    }

    public boolean deactivate() {
        locked = true;
        return false;
    }

    public void co_andActivation() {
        if (countActivatedSwitches() == countCoActivatedSwitches()) {
            activate();
        } else {
            deactivate();
        }
    }

    public void notActivation() {
        if (countActivatedSwitches() == 0) {
            activate();
        } else {
            deactivate();
        }
    }

    public void xorActivation() {
        if (countActivatedSwitches() == 1) {
            this.activate();
        } else {
            deactivate();
        }
    }

    public void orActivation() {
        if (countActivatedSwitches() >= 1) {
            activate();
        } else {
            deactivate();
        }
    }

    public void andActivation() {
        // Entity only activates if there are 2 or more adjacent activated switches
        // If there are more than two switches adjacent, all must be activated. 
        int activatedSwitchCount = countActivatedSwitches();
        int adjacentSwitchCount = countAdjacentSwitches();

        if (activatedSwitchCount > 2 && (activatedSwitchCount == adjacentSwitchCount)) {
            activate();
            
        } else if (activatedSwitchCount >= 2) { 
            activate();
        } else {
            deactivate();
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
