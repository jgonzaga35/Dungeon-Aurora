package dungeonmania.entities;

import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.Entity;
import dungeonmania.Pos2d;

public abstract class StaticEntity extends Entity {

    public StaticEntity(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    public abstract boolean isBlocking();

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.STATIC;
    }
}
