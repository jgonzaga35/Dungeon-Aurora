package dungeonmania.entities.collectables;

import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;

public interface BattleItem {
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
    public void usedForBattleRound(BattleDirection d);

    /**
     * <= 0 means dead
     * @return the durability
     */
    public int getDurability();

    /**
     * The defence coefficient of the fighter will be multiplied by this bonus
     */
    public float getDefenceCoefBonus();

    /**
     * This bonus will be added to the the attack damange of the fighter
     */
    public float getAttackDamageBonus();
}
