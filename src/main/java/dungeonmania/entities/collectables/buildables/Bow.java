package dungeonmania.entities.collectables.buildables;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
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

    /**
     * Constructor for Bow
     * @param Dungeon dungeon
     * @param Pos2d position that bow is located
     */
    public Bow(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.durability = INITIAL_DURABILITY;
    }

    /**
     * <= 0 means dead
     * @return the durability
     */
    @Override
    public int getDurability() {
        return this.durability;
    }

    /**
     * decreases the durability by one, if the direction is correct
     * 
     * Note that this method isn't called on every battle, but on every battle
     * round (there may be multiple round per battle)
     * 
     * For example, if the object is a shield and d is DEFENCE, then the it will
     * decrease it's durability. However, if the direction was attack, then it
     * wouldn't do anything (because you can't use a shield for attack)
     * 
     * @param d direction of the battle
     */
    @Override
    public void usedForBattleRound(BattleDirection d) {
        if (d == BattleDirection.DEFENCE) {
            this.durability -= 2; // two because it attacks twice
        }
    }

    /**
     * Find if there is a relationship between entities
     * @return boolean If Relationship Exists
     */
    @Override
    public boolean isInteractable() {
        return false;
    }

    /**
     * Return the Type of the Entity as a String
     * @return String type of entity
     */
    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }

    @Override
    public float getDefenceCoefBonus() {
        return 1;
    }

    /**
     * This bonus will be added to the the attack damage of the fighter
     * @param Fighter target
     * @return float Attack Damage Bonus
     */
    @Override
    public float getAttackDamageBonus(Fighter target) {
        return 1; // player normally does 1 attack damage. This doubles it.
    }
    
}
