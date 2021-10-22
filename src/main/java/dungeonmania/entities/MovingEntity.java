package dungeonmania.entities;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.util.Direction;

public abstract class MovingEntity extends Entity {

    public MovingEntity(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    /**
     * Moves an entity from the current cell to the target cell
     * @param target
     */
    public void moveTo(Cell target) {
        Cell from = this.getCell();
        from.removeOccupant(this);
        target.addOccupant(this);

        this.position = target.getPosition();
        // this.getCell().onWalked(from.getPosition(), this.position);
    }

    public Cell inspectCell(Direction d) {
        return dungeon.getMap().getCellAround(dungeon.getMap().getCell(position), d);
    }

    @Override
    public boolean isInteractable() {
        return false; // i think at least
    }

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.MOVING_ENTITY;
    }
}
