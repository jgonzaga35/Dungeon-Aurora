package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.StaticEntity;
import dungeonmania.entities.statics.Portal;
import dungeonmania.util.Direction;

public class Cell {
    /**
     * all the entities on that cell
     */
    private List<Entity> occupants = new ArrayList<>();
    private Pos2d position;
    private Dungeon dungeon;

    public Cell(Dungeon dungeon, Pos2d position) {
        this.position = position;
        this.dungeon = dungeon;
    }

    public Dungeon getDungeon() {
        return this.dungeon;
    }

    public Pos2d getPosition() {
        return this.position;
    }

    public void addOccupant(Entity e) {
        this.occupants.add(e);
    }
    
    public GameMode getGameMode() {
        return this.dungeon.getGameMode();
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

    /**
     * returns the cell above, below, left or right (depending on the direction)
     * @param d direction (shouldn't be NONE)
     * @return Cell
     */
    public Cell getCellAround(Direction d) {
        return this.dungeon.getCellAround(this, d);
    }

    /**
     * Returns the portal on the cell if the cell has a portal.
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

}
