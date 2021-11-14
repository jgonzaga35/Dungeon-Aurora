package dungeonmania.entities;
import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.Entity;
import dungeonmania.Pos2d;

/**
 * Represents collecatable entities.
 * Collectable entities can be stored in the inventory of certain moving entities.
 */
public abstract class CollectableEntity extends Entity {

    public CollectableEntity(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }


    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.COLLECTABLE;
    }
}
