package dungeonmania.battlestrategies;

import dungeonmania.Dungeon;

/**
 * Component that handles the battles
 */
public interface BattleStrategy {

    public enum BattleDirection {
        ATTACK, DEFENCE;
    }

    /**
     * Finds all the battles on the map, and performs them
     * @param dungeon
     */
    public void findAndPerformBattles(Dungeon dungeon);

    /**
     * Returns the precedence of the Battle Strategy
     * @return int Precedence of the Battle Strategy
     */
    public int getPrecedence();

}
