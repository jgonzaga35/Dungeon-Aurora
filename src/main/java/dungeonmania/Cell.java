package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.StaticEntity;

public class Cell {
    /**
     * all the entities on that cell
     */
    private List<Entity> occupants = new ArrayList<>();
    private Pos2d position;
    private Integer playerDistance;

    
    public Cell(Dungeon dungeon, Pos2d position) {
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
