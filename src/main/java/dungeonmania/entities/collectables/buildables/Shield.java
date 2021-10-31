package dungeonmania.entities.collectables.buildables;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.collectables.BuildableEntity;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Shield extends BuildableEntity {

    public static final String STRING_TYPE = "shield";

    private int durability;    

    public Shield(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.durability = 10; // it can be used 10 times
    }

    /**
     * If d == BattleDirection.DEFENCE, decreases the durability by one
     * @param d
     */
    public void usedFor(BattleDirection d) {
        if (d == BattleDirection.DEFENCE) {
            this.durability--;
        }
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public String getTypeAsString() {
        return Shield.STRING_TYPE;
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
        if (!inventory.useItems(List.of(
            Wood.STRING_TYPE,
            Wood.STRING_TYPE,
            Treasure.STRING_TYPE
        )) && !inventory.useItems(List.of(
            Wood.STRING_TYPE,
            Wood.STRING_TYPE,
            Key.STRING_TYPE
        ))) {
            return false;
        }
        inventory.add(new Shield(null, null));
        return true;
    }
    
}
