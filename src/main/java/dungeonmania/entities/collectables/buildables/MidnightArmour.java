package dungeonmania.entities.collectables.buildables;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.collectables.Armour;
import dungeonmania.entities.collectables.SunStone;

public class MidnightArmour extends Shield {
    public static final String STRING_TYPE = "midnight_armour";
    public static int INITIAL_DURABILITY = 40;

    private int durability;

    public static List<List<String>> RECIPES = List.of(
        List.of(
            SunStone.STRING_TYPE,
            Armour.STRING_TYPE
        )
    );

    public MidnightArmour(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.durability = INITIAL_DURABILITY;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }

    @Override
    public float getDefenceCoefBonus() {
        return 3; // midnight armour multiply defence coef by 3
    }

    @Override
    public float getAttackDamageBonus(Fighter target) {
        return 3;
    }
}
