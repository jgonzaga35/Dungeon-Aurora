package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.StaticEntity;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.statics.Boulder;
import dungeonmania.entities.statics.Door;
import dungeonmania.entities.statics.Exit;
import dungeonmania.entities.statics.FloorSwitch;
import dungeonmania.util.BlockingReason;
import dungeonmania.entities.statics.Portal;
import dungeonmania.util.Direction;

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

    public Integer getPlayerDistance() {
        return this.playerDistance;
    }

    public void setPlayerDistance(Integer playerDistance) {
        this.playerDistance = playerDistance;
    }

    public Pos2d getPosition() {
        return this.position;
    }

    public void addOccupant(Entity e) {
        this.occupants.add(e);
    }

    public boolean hasBoulder() {
        return occupants.stream().anyMatch(occupant -> occupant instanceof Boulder);
    }

    /**
     * 
     * @param d
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
        for (Entity occupant: this.occupants) {
            if (occupant instanceof Portal) {
                return (Portal) occupant;
            }
        }
        return null;
    }

    public FloorSwitch getFloorSwitch() {
        for (Entity occupant: this.occupants) {
            if (occupant instanceof FloorSwitch) {
                return (FloorSwitch) occupant;
            }
        }
        return null;
    }
    
    public boolean hasPlayer() {
        return this.occupants.stream().anyMatch(e -> e instanceof Player);
    }

    public boolean hasExit() {
        return this.occupants.stream().anyMatch(e -> e instanceof Exit);
    }
    
    /**
     * @return the entities on that cell
     */
    public List<Entity> getOccupants() {
        return occupants;
    }

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

    public void onWalked(Pos2d from, Pos2d to) {
        // for (Entity e: this.occupants) {
        //     // e.onWalked(from, to);
        // }
    }

    public boolean hasUntriggeredFloorSwitch() {
        for (Entity occupant: this.occupants) {
            if (occupant instanceof FloorSwitch && !((FloorSwitch) occupant).isTriggered()) {
                return true;
            }
        }
        return false;
    }
}
