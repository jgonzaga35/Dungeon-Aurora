package dungeonmania.entities;

import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.Entity;

public abstract class MovingEntity extends Entity {

    @Override
    public boolean isInteractable() {
        return false; // i think at least
    }

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.MOVING_ENTITY;
    }
}
