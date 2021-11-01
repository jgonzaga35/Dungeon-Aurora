package dungeonmania.battlestrategies;

import dungeonmania.Dungeon;

/**
 * No battle ever happen
 */
public class NoBattleStrategy implements BattleStrategy {

    private int precedence;

    public NoBattleStrategy(int precedence) {
        this.precedence = precedence;
    }

    @Override
    public void findAndPerformBattles(Dungeon dungeon) {
        // do nothing
    }

    @Override
    public int getPrecedence() {
        return this.precedence;
    }
    
}
