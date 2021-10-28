package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.StaticEntity;
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
        //System.out.println(position + " adding occupant " + e.getId() + " " + e.getTypeAsString());
        this.occupants.add(e);
    }

    public boolean hasBoulder() {
        return occupants.stream().anyMatch(occupant -> occupant instanceof Boulder);
    }


    public Portal hasPortal() {
        for (Entity occupant: this.occupants) {
            if (occupant instanceof Portal) {
                return (Portal) occupant;
            }
        }
        return null;
    }
    
    /**
     * @return the entities on that cell
     */
    public List<Entity> getOccupants() {
        return occupants;
    }

    public boolean removeOccupant(Entity e) {
        //System.out.println(position + " removing occupant " + e.getId() + " " + e.getTypeAsString());
        return this.occupants.remove(e);
    }

    /**
     * @return true if there is a static element on the cell that is blocking
     */
    public boolean isBlocking() {
        for (Entity e: this.occupants) {
            if (e instanceof StaticEntity && ((StaticEntity) e).isBlocking())
                return true;
        }
        return false;
    }

    public void onWalked(Pos2d from, Pos2d to) {
        for (Entity e: this.occupants) {
            // e.onWalked(from, to);
        }
    }
}
