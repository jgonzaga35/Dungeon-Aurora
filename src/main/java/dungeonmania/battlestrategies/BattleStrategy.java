package dungeonmania.battlestrategies;

import org.json.JSONObject;

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

    public int getPrecedence();

    public JSONObject toJSON();

}
