// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.movings.Hydra;

public class Anduril extends CollectableEntity implements BattleItem {

    public static String STRING_TYPE = "anduril";
    public static int BASE_DAMAGE = 7;

    public static int INITIAL_DURABILITY = 20;

    public int durability;

    public Anduril(Dungeon dungeon, Pos2d position) {
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
        return Anduril.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; 
    }

    @Override
    public void tick() {
        // Do nothing.
    }


    @Override
    public float getDefenceCoefBonus() {
        return 1; // doesn't affect defence coef bonus
    }

    /**
     * Anduril applies Extra effects when used against a boss enemy
     */
    @Override
    public float getAttackDamageBonus(Fighter target) {
        if (target.isBoss()) {
            if (target instanceof Hydra) {
                ((Hydra) target).cripple();
            }
            return Anduril.BASE_DAMAGE * 3;
        } 

        return Anduril.BASE_DAMAGE; 
    }
}
