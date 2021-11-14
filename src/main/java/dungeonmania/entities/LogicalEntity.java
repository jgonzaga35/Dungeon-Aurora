package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.entities.statics.FloorSwitch;
import dungeonmania.entities.statics.LightBulb;
import dungeonmania.entities.statics.Wire;
import dungeonmania.util.BlockingReason;

public abstract class LogicalEntity extends Entity {
    
    private List<Entity> connectedEntities = new ArrayList<Entity>();
    private List<String> connectedEntityIds = new ArrayList<String>();
    public Logic logic;

    public LogicalEntity(Dungeon dungeon, Pos2d position, String logic) {
        super(dungeon, position);
        this.logic = parseLogic(logic);
        connectedEntities.add(this);
        connectedEntityIds.add(this.getId());
    }

    /**
     * Get a list of all entities that are connected
     * on the same circuit or are adjacent.
     */
    public List<Entity> getConnectedEntities() {
        return this.connectedEntities;
    }

    /**
     * Get a list of all entity ids that are connected
     * on the same circuit or are adjacent.
     */
    public List<String> getConnectedEntityIds() {
        return this.connectedEntityIds;
    }

    /**
     * Returns true if the entity can connect to the wire.
     */
    public boolean canConnect() {
        return true;
    }

    public abstract void activate();
    public abstract void deactivate();

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
            c.getOccupants()
             .stream()
             .filter(e -> e instanceof LogicalEntity)
             .forEach(s -> {
                 if (s instanceof Wire && !connectedIds.contains(s.getId())) {
                     connectedEntities.add(s);
                     connectedEntityIds.add(s.getId());
                     addConnectedEntities(c, connectedEntityIds);
                 } else {
                     if (!connectedIds.contains(s.getId())) {
                         connectedEntities.add(s);
                         connectedEntityIds.add(s.getId());
                     }
                 }
             });
        });
    }

    /**
     * Count the number of activated adjacent switches,
     * including those connected by wires.
     * @return
     */
    public Integer countActivatedSwitches() {
        int count = (int) connectedEntities.stream()
                                           .filter(e -> 
                                           e instanceof FloorSwitch && 
                                           ((FloorSwitch) e).isActivated() &&
                                           !e.getId().equals(this.getId())
                                           )
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
    public void tick() {

    }

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.STATIC;
    }

    /**
     * Default Logical Entity is not blocking
     * @return
     */
    public BlockingReason isBlocking() {
        return BlockingReason.NOT;
    }

    /**
     * Activates Logical Entity if the co_and condition is satisfied
     */
    public void co_andActivation() {
        if (countActivatedSwitches() == countCoActivatedSwitches()) {
            activate();
        } else {
            deactivate();
        }
    }

    /**
     * Activates Logical Entity if the not condition is satisfied
     */
    public void notActivation() {
        if (countActivatedSwitches() == 0) {
            activate();
        } else {
            deactivate();
        }
    }

    /**
     * Activates Logical Entity if the xor condition is satisfied
     */
    public void xorActivation() {
        if (countActivatedSwitches() == 1) {
            activate();
        } 
        else {
            deactivate();
        }
    }

    /**
     * Activates Logical Entity if the or condition is satisfied
     */
    public void orActivation() {
        if (countActivatedSwitches() >= 1) {
            activate();
        } else {
            deactivate();
        }
    }

    /**
     * Activates Logical Entity if the and condition is satisfied
     */
    public void andActivation() {
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
