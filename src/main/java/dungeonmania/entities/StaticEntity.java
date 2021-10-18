package dungeonmania.entities;

import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.Entity;

public abstract class StaticEntity extends Entity {

    public abstract boolean isBlocking();

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.STATIC;
    }
}
