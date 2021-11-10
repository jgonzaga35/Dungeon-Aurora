package dungeonmania.entities.collectables.buildables;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.collectables.*;

public class MidnightArmour extends BuildableEntity implements BattleItem {
    public static final String STRING_TYPE = "midnight_armour";
    public static int INITIAL_DURABILITY = 40;

    private int durability;

    public MidnightArmour(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.durability = INITIAL_DURABILITY;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public void usedForBattleRound(BattleDirection d) {
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
            SunStone.STRING_TYPE,
            Armour.STRING_TYPE
        );

        if (!inventory.useItems(materials)) {
            return false;
        }
        
        inventory.add(new MidnightArmour(null, null));
        return true;
    }

    public static boolean craftable(Inventory inventory) {
        List<String> materials = List.of(
            SunStone.STRING_TYPE,
            Armour.STRING_TYPE
        );

        if (inventory.findItems(materials) == null) {
            return false;
        }

        return true;
    }

    @Override
    public float getDefenceCoefBonus() {
        return 3; // midnight armour multiply defence coef by 3
    }

    @Override
    public float getAttackDamageBonus() {
        return 3;
    }
}
