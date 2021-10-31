package dungeonmania.entities;
import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.Entity;
import dungeonmania.Pos2d;

public abstract class CollectableEntity extends Entity {

    public CollectableEntity(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.COLLECTABLE;
    }
}
