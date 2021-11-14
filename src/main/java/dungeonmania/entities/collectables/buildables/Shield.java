package dungeonmania.entities.collectables.buildables;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.Fighter;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.collectables.BattleItem;
import dungeonmania.entities.collectables.BuildableEntity;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Shield extends BuildableEntity implements BattleItem {

    public static final String STRING_TYPE = "shield";
    public static int INITIAL_DURABILITY = 20;

    private int durability;

    public static List<List<String>> RECIPES = List.of(List.of(Wood.STRING_TYPE, Wood.STRING_TYPE, Key.STRING_TYPE),
            List.of(Wood.STRING_TYPE, Wood.STRING_TYPE, Treasure.STRING_TYPE));

    public Shield(Dungeon dungeon, Pos2d position) {
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
        return Shield.STRING_TYPE;
    }

    @Override
    public float getDefenceCoefBonus() {
        return 3; // shield multiply defence coef by 3
    }

    @Override
    public float getAttackDamageBonus(Fighter target) {
        return 0;
    }

}
