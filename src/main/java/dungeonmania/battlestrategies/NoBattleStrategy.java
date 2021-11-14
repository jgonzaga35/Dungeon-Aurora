package dungeonmania.battlestrategies;

import dungeonmania.Dungeon;

/**
 * No battle ever happen
 */
public class NoBattleStrategy implements BattleStrategy {

    private int precedence;

    /**
     * Constructor for NoBattleStrategy
     * @param int precedence
     */
    public NoBattleStrategy(int precedence) {
        this.precedence = precedence;
    }

    /**
     * Finds all the battles on the map, and performs them
     * @param dungeon
     */
    @Override
    public void findAndPerformBattles(Dungeon dungeon) {
        // do nothing
    }

    /**
     * Returns the precedence of the Battle Strategy
     * @return int Precedence of the Battle Strategy
     */
    @Override
    public int getPrecedence() {
        return this.precedence;
    }
    
}
