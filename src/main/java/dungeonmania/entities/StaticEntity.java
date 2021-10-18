package dungeonmania.entities;

import dungeonmania.Cell;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.Entity;

public abstract class StaticEntity extends Entity {

    public StaticEntity(Cell cell) {
        super(cell);
    }

    public abstract boolean isBlocking();

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.STATIC;
    }
}
