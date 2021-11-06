// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.movings.Boss;
import dungeonmania.entities.movings.bosses.Hydra;

public class Anduril extends Sword {

    public static String STRING_TYPE = "anduril";
    public static int BASE_DAMAGE = 6;

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
        if (target instanceof Boss) {
            if (target instanceof Hydra) {
                ((Hydra) target).cripple();
            }
            return Anduril.BASE_DAMAGE * 3;
        } 
        
        return Anduril.BASE_DAMAGE; // attacking with a sword is a lot better than with your fists!
    }
}
