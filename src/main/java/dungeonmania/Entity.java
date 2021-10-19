package dungeonmania;

import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.util.Direction;

public abstract class Entity {
    private String id;

    private static int nextEntityId = 1;

    private Cell cell;

    public Entity(Cell cell) {
        this.id = "Entity-" + Entity.nextEntityId;
        this.cell = cell;

        Entity.nextEntityId++;
    }

    public String getId() {
        return this.id;
    }

    /**
     * Moves an entity from the current cell to the target cell
     * @param target
     */
    public void moveTo(Cell target) {
        Cell from = this.cell;
        from.removeOccupant(this);
        target.addOccupant(this);

        this.cell = target;
        this.cell.onWalked(from.getPosition(), this.cell.getPosition());
    }

    /**
     * @return the cell this entity is on
     */
    public Cell getCell() {
        return this.cell;
    }

    /**
     * returns the cell above, below, left or right (depending on the direction)
     * @param d the direction (shouldn't be NONE)
     * @return Cell
     */
    public Cell getCellAround(Direction d) {
        return this.cell.getCellAround(d);
    }

    public abstract boolean isInteractable();

    public abstract LayerLevel getLayerLevel();

    public abstract String getTypeAsString();

    public abstract void tick();
}