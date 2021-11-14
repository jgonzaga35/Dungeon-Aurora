// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

/**
 * Represents wood.
 * Wood can be collected by the player and be used for building items.
 */
public class Wood extends CollectableEntity {

    public static String STRING_TYPE = "wood";

    public Wood(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public String getTypeAsString() {
        return Wood.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    @Override
    public void tick() {
    }
}
