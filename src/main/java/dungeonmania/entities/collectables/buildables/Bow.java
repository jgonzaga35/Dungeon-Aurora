package dungeonmania.entities.collectables.buildables;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.BattleItem;
import dungeonmania.entities.collectables.BuildableEntity;
import dungeonmania.entities.collectables.Wood;

public class Bow extends BuildableEntity implements BattleItem {

    public static final String STRING_TYPE = "bow";
    public static int INITIAL_DURABILITY = 100;

    private int durability;

    public static List<List<String>> RECIPES = List.of(
        List.of(
            Wood.STRING_TYPE,
            Arrow.STRING_TYPE,
            Arrow.STRING_TYPE,
            Arrow.STRING_TYPE
        )
    );

    public Bow(Dungeon dungeon, Pos2d position) {
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
            this.durability -= 2; // two because it attacks twice
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

    @Override
    public float getDefenceCoefBonus() {
        return 3; // shield multiply defence coef by 3
    }

    @Override
    public float getAttackDamageBonus() {
        return 1; // player normally does 1 attack damage. This doubles it.
    }
    
}
