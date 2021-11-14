package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.LogicalEntity;
import dungeonmania.entities.StaticEntity;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.statics.Boulder;
import dungeonmania.entities.statics.Door;
import dungeonmania.entities.statics.Exit;
import dungeonmania.entities.statics.FloorSwitch;
import dungeonmania.entities.statics.Portal;
import dungeonmania.entities.statics.Swamp;
import dungeonmania.entities.statics.Wire;
import dungeonmania.util.BlockingReason;
import dungeonmania.util.Direction;

/**
 * Represents a single cell or square of the dungeon map.
 * Entities occupying a cell are stored inside this class.
 */
public class Cell {
    /**
     * all the entities on that cell
     */
    private List<Entity> occupants = new ArrayList<>();
    private Pos2d position;
    private Integer playerDistance;

    public Cell(Pos2d position) {
        this.position = position;
    }

    /**
     * Calculates the distance between the player and this cell.
     * @return Integer representing the distance 
     */
    public Integer getPlayerDistance() {
        return this.playerDistance;
    }

    /**
     * Calculates the travel cost to traverse the current cell
     * @return Integer representing the coast 
     */
    public Integer getTravelCost() {
        if (getSwamp() != null) return getSwamp().getMovementFactor();
        return 1;
    }

    public void setPlayerDistance(Integer playerDistance) {
        this.playerDistance = playerDistance;
    }

    public Pos2d getPosition() {
        return this.position;
    }

    /**
     * Add an occupent to the current list of occupants in the cell
     * @param e, an entity
     */
    public void addOccupant(Entity e) {
        this.occupants.add(e);
    }

    /**
     * Is there a boulder occupying this cell?
     * @return boolean, true if there is boulder and false if not
     */
    public boolean hasBoulder() {
        return occupants.stream().anyMatch(occupant -> occupant instanceof Boulder);
    }

    /**
     * Tries to push the boulder to another cell.
     * @param d, direction of the push
     * @return true if boulder is successfully pushed
     */
    public boolean pushBoulder(Direction d) {
        Boulder boulder = null;
        for (Entity e : occupants) {
            if (e instanceof Boulder) {
                boulder = (Boulder) e;
            }
        }

        if (boulder == null) return false;
        else return boulder.roll(d);
    }

    /**
     * Attempts to unlock the door on this cell
     * @return boolean representing success
     */
    public boolean unlockDoor() {
        Door door = null;
        for (Entity e : occupants) {
            if (e instanceof Door) {
                door = (Door) e;
            }
        }

        if (door == null) return false;
        else return door.open();
    }

    /**
     * Return portal if cell has a portal
     * @return
     */
    public Portal hasPortal() {
        return (Portal) occupants.stream().filter(o -> o instanceof Portal).findFirst().orElse(null);
    }

    /**
     * Return swamp_tile if cell has a swamp_tile
     * @return
     */
    public Swamp getSwamp() {
        return (Swamp) occupants.stream().filter(o -> o instanceof Swamp).findFirst().orElse(null);
    }

    /**
     * Return floor switch if cell has a floor switch
     * @return
     */
    public FloorSwitch getFloorSwitch() {
        for (Entity occupant: this.occupants) {
            if (occupant instanceof FloorSwitch) {
                return (FloorSwitch) occupant;
            }
        }
        return null;
    }
    
    /**
     * @return boolean, true if the player is on this cell, false otherwise
     */
    public boolean hasPlayer() {
        return this.occupants.stream().anyMatch(e -> e instanceof Player);
    }

    /**
     * @return boolean, true if the exit is on this cell, false otherwise
     */
    public boolean hasExit() {
        return this.occupants.stream().anyMatch(e -> e instanceof Exit);
    }
    
    /**
     * @return the entities on that cell
     */
    public List<Entity> getOccupants() {
        return occupants;
    }

    /**
     * Removes an occupant from this cell
     * @param e, the entity being removed
     * @return boolean representing success of the removal
     */
    public boolean removeOccupant(Entity e) {
        return this.occupants.remove(e);
    }

    /**
     * @return the blocking reason of this cell
     */
    public BlockingReason getBlocking() {
        for (Entity e: this.occupants) {
            if (e instanceof StaticEntity && 
                !((StaticEntity) e).isBlocking().equals(BlockingReason.NOT)) {
                    return ((StaticEntity)e).isBlocking();
                }

            if (e instanceof LogicalEntity && 
            !((LogicalEntity) e).isBlocking().equals(BlockingReason.NOT)) {
                return ((LogicalEntity)e).isBlocking();
            }            
        }
        return BlockingReason.NOT;
    }

    /**
     * @return true if there is a static element on the cell that is blocking
     */
    public boolean isBlocking() {
        if (this.getBlocking().equals(BlockingReason.NOT)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return true if the floor switch on this cell has been triggered
     */
    public boolean hasDeactivatedFloorSwitch() {
        for (Entity occupant: this.occupants) {
            if (occupant instanceof FloorSwitch && !((FloorSwitch) occupant).isActivated()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActivatedFloorSwitch() {
        for (Entity occupant: this.occupants) {
            if (occupant instanceof FloorSwitch && ((FloorSwitch) occupant).isActivated()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasActivatedEntity() {
        for (Entity occupant: this.occupants) {
            if (occupant instanceof FloorSwitch && ((FloorSwitch) occupant).isActivated()) {
                return true;
            }
            if (occupant instanceof Wire && ((Wire) occupant).isActivated()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String str = "[pos: " + position.toString() + ", Tcost: " + getTravelCost() + "]";
        return str;
    }
}
