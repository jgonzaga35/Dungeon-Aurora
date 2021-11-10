package dungeonmania.entities.collectables.buildables;

import java.util.ArrayList;
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
        List<String> materials = new ArrayList<>();

        // Search for Sun Stone
        List<String> sunstone = List.of(
            SunStone.STRING_TYPE
        );

        if (inventory.findItems(sunstone) != null) {
            materials.addAll(sunstone);
        } else return false; // no Sun Stone

        // Search for Wood/Arrows
        List<String> wood = List.of(
            Wood.STRING_TYPE
        );

        List<String> arrows = List.of(
            Arrow.STRING_TYPE,
            Arrow.STRING_TYPE
        );
        
        if (inventory.findItems(wood) != null) {
            materials.addAll(wood);
        } else if (inventory.findItems(arrows) != null){
            materials.addAll(arrows);
        } else return false; // no wood or arrows

        // Search for Key/Treasure
        List<String> key = List.of(
            Key.STRING_TYPE
        );

        List<String> treature = List.of(
            Treasure.STRING_TYPE
        );

        if (inventory.findItems(key) != null) {
            materials.addAll(key);
        } else if (inventory.findItems(treature) != null){
            materials.addAll(treature);
        } else return false; // no key or treature
        
        // Craft the Sceptre
        if (!inventory.useItems(materials)) {
            return false;
        }
        
        inventory.add(new Sceptre(null, null));
        return true;
    }

    public static boolean craftable(Inventory inventory) {
        List<String> sunstone = List.of(
            SunStone.STRING_TYPE
        );

        if (inventory.findItems(sunstone) == null) return false;

        // Search for Wood/Arrows
        List<String> wood = List.of(
            Wood.STRING_TYPE
        );

        List<String> arrows = List.of(
            Arrow.STRING_TYPE,
            Arrow.STRING_TYPE
        );
        
        if (inventory.findItems(wood) == null && inventory.findItems(arrows) == null) return false;

        // Search for Key/Treasure
        List<String> key = List.of(
            Key.STRING_TYPE
        );

        List<String> treature = List.of(
            Treasure.STRING_TYPE
        );

        if (inventory.findItems(key) == null && inventory.findItems(treature) == null) return false;

        return true;
    }
    
}
