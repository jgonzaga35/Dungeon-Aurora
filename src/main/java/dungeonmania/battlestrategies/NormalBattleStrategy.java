package dungeonmania.battlestrategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.Utils;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.Fighter.FighterRelation;

/**
 * Overview:
 *  A battle is a series of "rounds".
 *  A "round" is a series a duels.
 * 
 *  Battles last until one side has died. (ie. perform rounds)
 *  Battles and round oppose a list of enemies to a list of allies
 *  Duels oppose one enemy to one ally
 * 
 * @author Mathieu Paturel
 */
public class NormalBattleStrategy implements BattleStrategy {

    private int precendence = -1;
    public NormalBattleStrategy(int precendence) {
        this.precendence = precendence;
    }

    @Override
    public void findAndPerformBattles(Dungeon dungeon) {
        DungeonMap map = dungeon.getMap();

        // buffer (to not allocate a list every time. I like useless optimisations)
        List<Fighter> allies = new ArrayList<>();
        List<Fighter> enemies = new ArrayList<>();
        List<Fighter> deaths = new ArrayList<>();

        // this is important to form duels in a reproducible way (see
        // performRound for more details). Sort by health, attack damage and
        // then id (id last because it's the one that might change the most
        // randomly).
        Comparator<Fighter> sort = (a, b) -> {
            int v;
            v = Utils.compareFloat(a.getHealth() - b.getHealth());
            if (v != 0) return v;
            v = Utils.compareFloat(a.getAttackDamage() - b.getAttackDamage());
            if (v != 0) return v;
            v = a.getEntity().getId().compareTo(b.getEntity().getId());
            assert v != 0 : "duplicate id break a lot of things, including battles";
            return v;
        };

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                allies.clear();
                enemies.clear();
                deaths.clear();

                this.prepareBattle(map.getCell(x, y), allies, enemies);

                // no one's going the fight on that cell
                if (allies.size() == 0 || enemies.size() == 0) continue;

                Collections.sort(allies, sort);
                Collections.sort(enemies, sort);

                this.performBattle(
                    allies,
                    enemies,
                    deaths
                );

                for (Fighter dead: deaths) {
                    // TODO: check if dead is player. This should be weird
                    boolean result = map.getCell(x, y).removeOccupant(dead.getEntity());
                    if (result == false) {
                        throw new Error("couldn't remove dead entity");
                    }
                }
            }
        }
    }

    /**
     * Populates the allies and enemies list with the appropriate fighters
     * (based on FighterRelation)
     * @param cell
     * @param allies
     * @param enemies
     */
    private void prepareBattle(Cell cell, List<Fighter> allies, List<Fighter> enemies) {
        for (Entity entity : cell.getOccupants()) {
            if (entity instanceof Fighter) {
                Fighter fighter = (Fighter) entity;
                if (fighter.getFighterRelation() == FighterRelation.ALLY) {
                    allies.add(fighter);
                } else {
                    enemies.add(fighter);
                }
            }
        }
    }

    /**
     * Perform the battle (allies against enemies) and populates the deaths list
     * 
     * A battle is a series of rounds. We do rounds until all the allies or all
     * the enemies have died.
     * 
     * @pre deaths.size() == 0
     * @param allies
     * @param enemies
     * @param deaths
     */
    private void performBattle(List<Fighter> allies, List<Fighter> enemies, List<Fighter> deaths) {
        assert allies.size() > 0;
        assert enemies.size() > 0;
        assert deaths.size() == 0;

        // fight until death!
        while (allies.size() > 0 && enemies.size() > 0) {
            this.performRound(allies, enemies, deaths);
        }

    }

    /**
     * Perform one round of the battle, and contributes to popupating the death list
     * 
     * 1. form duels
     *    each fighter is assigned to one or more duels. (if it's a 3v5, some
     *    fighter will fight 2+ fighters).
     * 
     *    How are duels formed?
     *    This is a bit hard to explain with text. Code is a bit clearer if you
     *    get idea. If you're having trouble understanding it, call me (Mathieu
     *    Paturel) i'll show you a picture (i'm too lazy to draw ascii art)
     * 
     *    Basic idea: strong people against strong people (we pick a rule to
     *    have reproducible battles)
     * 
     * 2. perform duels
     *    no ambiguity here, just do what the spec says
     * 
     * @param allies
     * @param enemies
     * @param deaths
     */
    private void performRound(List<Fighter> allies, List<Fighter> enemies, List<Fighter> deaths) {

        // form duels

        int baseSize = Math.min(allies.size(), enemies.size());
        List<List<Fighter>> duels = new ArrayList<>(); // list of pairs of fighter

        for (int i = 0; i < baseSize; i++) {
            duels.add(List.of(allies.get(i), enemies.get(i)));
        }

        // assign the remaining allies
        for (int i = baseSize; i < allies.size(); i++) {
            duels.add(List.of(allies.get(i), enemies.get(enemies.size() - 1 - i % enemies.size())));
        }

        // assign the remaining enemies
        for (int i = baseSize; i < enemies.size(); i++) {
            duels.add(List.of(allies.get(allies.size() - 1 - i % allies.size()), enemies.get(i)));
        }

        // perform the duels

        for (List<Fighter> duel : duels) {
            assert duel.size() == 2;
            Fighter ally = duel.get(0);
            Fighter enemy = duel.get(1);
            assert ally.getFighterRelation() == FighterRelation.ALLY;
            assert enemy.getFighterRelation() == FighterRelation.ENEMY;

            // the enemy attacks the ally
            // the /10 comes from the spec
            ally.setHealth(ally.getHealth() - this.computeDamage(enemy, ally) / 10);
            enemy.usedItemFor(BattleDirection.ATTACK);
            ally.usedItemFor(BattleDirection.DEFENCE);
            
            if (Utils.isDead(ally)) {
                deaths.add(ally);
                allies.remove(ally);
                continue;
            }

            // the ally attacks the enemy
            // the /5 comes from the spec
            enemy.setHealth(enemy.getHealth() - this.computeDamage(ally, enemy) / 5);
            ally.usedItemFor(BattleDirection.ATTACK);
            enemy.usedItemFor(BattleDirection.DEFENCE);

            if (Utils.isDead(enemy)) {
                deaths.add(enemy);
                enemies.remove(enemy);
                continue;
            }
        }
    }

    /**
     * The spec's formula for damage: attack.health * attack.attack_damage / defencer.defence_coef
     * 
     * (for example, defence_coef == 2 when you have an armor for example)
     * Damage is used like this: defence.health -= damage
     * 
     * @param attacker
     * @param defencer
     * @return damage to be done
     */
    private float computeDamage(Fighter attacker, Fighter defencer) {
        return attacker.getHealth() * attacker.getAttackDamage() / defencer.getDefenceCoef();
    }

    @Override
    public int getPrecendence() {
        return this.precendence;
    }
    
}
