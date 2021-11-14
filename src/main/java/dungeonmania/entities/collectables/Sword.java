// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.Fighter;

public class Sword extends CollectableEntity implements BattleItem {

    public static String STRING_TYPE = "sword";

    public static int INITIAL_DURABILITY = 20;

    public int durability;

    public Sword(Dungeon dungeon, Pos2d position) {
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
    public String getTypeAsString() {
        return Sword.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    @Override
    public void tick() {
    }


    @Override
    public float getDefenceCoefBonus() {
        return 1; // doesn't affect defence coef bonus
    }


    @Override
    public float getAttackDamageBonus(Fighter target) {
        return 2; // attacking with a sword is a lot better than with your fists!
    }
}
