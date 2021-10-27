package dungeonmania.entities;

import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.util.BlockingReason;
import dungeonmania.Entity;
import dungeonmania.Pos2d;

public abstract class StaticEntity extends Entity {

    public StaticEntity(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    public BlockingReason isBlocking() {
        return BlockingReason.NOT;
    }

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.STATIC;
    }
}
