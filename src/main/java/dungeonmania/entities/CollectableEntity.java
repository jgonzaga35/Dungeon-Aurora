package dungeonmania.entities;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.util.Direction;
import dungeonmania.Entity;
import dungeonmania.Pos2d;

public abstract class CollectableEntity extends Entity {

    public CollectableEntity(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.MOVING_ENTITY;
    }
}
