package dungeonmania.entities;

import dungeonmania.Entity;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;

/**
 * Anything that can take part in a battle must implement this interface.
 */
public interface Fighter {

    /**
     * Neutral isn't included, because it's not used yet.
     */
    public enum FighterRelation {
        ALLY, ENEMY
    }

    /**
     * if health <= 0, it means the fighter is dead
     * @return the health of the fighter. 
     */
    public float getHealth();

    /**
     * sets the health. If health <= 0, then the fighter is dead
     */
    public void setHealth(float h);

    /**
     * @return the damage done by the fighter
     */
    public float getAttackDamage();
    /**
     * @return the attack damage of the attacker will be divided by this
     * coefficient
     */
    public float getDefenceCoef();

    /**
     * Used to keep track of how many times an item has been used.
     * 
     * For example, when Player::usedItemFor(attack) is called,
     * the player should decrease his sword's durability, if he has one
     * @param d the direction
     */
    public void usedItemFor(BattleDirection d);

    /**
     * @return relation of the fighter to the player
     */
    public FighterRelation getFighterRelation();


    public Entity getEntity();
}
