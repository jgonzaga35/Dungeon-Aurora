package dungeonmania.battlestrategies;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.Fighter.FighterRelation;

/**
 * kills all the enemies straight away
 */
public class WinAllBattleStrategy implements BattleStrategy {

    private int precedence;

    public WinAllBattleStrategy(int precedence) {
        this.precedence = precedence;
    }

    @Override
    public void findAndPerformBattles(Dungeon dungeon) {

        DungeonMap map = dungeon.getMap();
        Cell cell = map.getCell(dungeon.getPlayerPosition());
        List<Fighter> enemies = new ArrayList<>();

        for (Entity e : cell.getOccupants()) {
            if (e instanceof Fighter) {
                Fighter f = (Fighter) e;
                if (f.getFighterRelation() == FighterRelation.ENEMY) {
                    enemies.add(f);
                }
            }
        }

        for (Fighter enemy : enemies) {
            cell.removeOccupant(enemy.getEntity());
        }
        
    }

    @Override
    public int getPrecedence() {
        return this.precedence;
    }
    
}
