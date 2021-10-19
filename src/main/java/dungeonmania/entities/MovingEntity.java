package dungeonmania.entities;

import dungeonmania.Cell;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.Entity;

public abstract class MovingEntity extends Entity {

    public MovingEntity(Cell cell) {
        super(cell);
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
