// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import dungeonmania.Dungeon; 
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.Fighter;

public class Armour extends CollectableEntity implements BattleItem {

    public static String STRING_TYPE = "armour";

    public static int INITIAL_DURABILITY = 30;
    public int durability;

    public Armour(Dungeon dungeon, Pos2d position) {
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
    public float getDefenceCoefBonus() {
        return 2; // armour multiplies defence by 2
    }

    @Override
    public float getAttackDamageBonus(Fighter target) {
        return 0;
    }

    @Override
    public String getTypeAsString() {
        return Armour.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; 
    }

    @Override
    public void tick() {
        // Do nothing
    }

}
