package dungeonmania.entities.collectables.buildables;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Pos2d;
import dungeonmania.entities.collectables.*;

public class Sceptre extends BuildableEntity {
    public static final String STRING_TYPE = "sceptre";

    public Sceptre(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }

    /**
     * Removes the items needed to craft this item from the inventory, and adds
     * itself to it.
     * 
     * If there aren't the required items present in the inventory, this does
     * nothing
     * 
     * @param inventory the inventory
     * @return true if the item was succesfully crafted
     */
    public static boolean craft(Inventory inventory) {
        List<String> materials = List.of(
            Wood.STRING_TYPE,
            Arrow.STRING_TYPE,
            Arrow.STRING_TYPE,
            Arrow.STRING_TYPE
        );
        
        if (!inventory.useItems(materials)) {
            return false;
        }
        
        inventory.add(new Bow(null, null));
        return true;
    }
}
