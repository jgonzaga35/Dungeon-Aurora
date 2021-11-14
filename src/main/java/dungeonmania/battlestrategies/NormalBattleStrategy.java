package dungeonmania.battlestrategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.Utils;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.Fighter.FighterRelation;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.movings.Player;

/**
 * Overview: A battle is a series of "rounds". A "round" is a series a duels.
 * 
 * Battles last until one side has died. (ie. perform rounds) Battles and round
 * oppose a list of enemies to a list of allies Duels oppose one enemy to one
 * ally
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

        // this is important to form duels in a reproducible way (see
        // performRound for more details). Sort by health, attack damage and
        // then id (id last because it's the one that might change the most
        // randomly).
        Comparator<Fighter> sort = (a, b) -> {
            // player should be last
            if (a instanceof Player) {
                return 1;
            } else if (b instanceof Player) {
                return -1;
            }
            int v;
            v = Utils.compareFloat(a.getHealth() - b.getHealth());
            if (v != 0)
                return v;
            v = Utils.compareFloat(a.getAttackDamage(null) - b.getAttackDamage(null));
            if (v != 0)
                return v;
            v = a.getEntity().getId().compareTo(b.getEntity().getId());
            assert v != 0 : "duplicate id break a lot of things, including battles";
            return v;
        };

        Cell cell = map.getCell(dungeon.getPlayer().getPosition());

        List<Fighter> allies = new ArrayList<>();
        List<Fighter> enemies = new ArrayList<>();

        this.prepareBattle(dungeon, cell, allies, enemies);
        assert allies.size() >= 1; // should always have the player

        Collections.sort(allies, sort);
        Collections.sort(enemies, sort);

        if (enemies.size() == 0)
            return; // there is no one to fight

        Set<Fighter> deaths = this.performBattle(allies, enemies);

        for (Fighter dead : deaths) {
            Entity e = dead.getEntity();

            // don't remove the player from the map if he has been resurrected
            if (((MovingEntity) dead).onDeath())
                continue;

            boolean result = map.getCell(e.getPosition()).removeOccupant(e);
            if (result == false) {
                throw new Error("couldn't remove dead entity");
            }
        }
    }

    /**
     * Populates the allies and enemies list with the appropriate fighters (based on
     * FighterRelation)
     * 
     * @param cell
     * @param allies  (written)
     * @param enemies (written)
     */
    private void prepareBattle(Dungeon dungeon, Cell cell, List<Fighter> allies, List<Fighter> enemies) {
        DungeonMap map = dungeon.getMap();

        // add all the allies and the enemies on the current cell
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

        // add all the mercenary allies from other cells
        Pos2d pos = cell.getPosition();
        // check all the cells around for mercenaries
        for (int y = -Mercenary.BATTLE_RADIUS; y <= Mercenary.BATTLE_RADIUS; y++) {
            for (int x = -Mercenary.BATTLE_RADIUS; x <= Mercenary.BATTLE_RADIUS; x++) {
                // battle radius is a circle, if you're not completely in the circle, you're
                // skiped
                if (x * x + y * y > Mercenary.BATTLE_RADIUS * Mercenary.BATTLE_RADIUS)
                    continue;
                Cell c = map.getCell(x + pos.getX(), y + pos.getY());
                if (c == null)
                    continue; // coordinate is outside the map

                c.getOccupants().stream().filter(e -> e instanceof Mercenary).map(e -> (Mercenary) e)
                        .filter(m -> m.getFighterRelation() == FighterRelation.ALLY).forEach(allies::add);
            }
        }
    }

    /**
     * Perform the battle (allies against enemies) and populates the deaths list
     * 
     * A battle is a series of rounds. We do rounds until all the allies or all the
     * enemies have died.
     * 
     * @pre deaths.size() == 0
     * @param allies
     * @param enemies
     * @return deaths
     */
    private Set<Fighter> performBattle(List<Fighter> allies, List<Fighter> enemies) {
        assert allies.size() > 0;
        assert enemies.size() > 0;

        Set<Fighter> deaths = new HashSet<>();

        // fight until death!
        while (allies.size() > 0 && enemies.size() > 0) {
            this.performRound(allies, enemies, deaths);
        }

        return deaths;
    }

    /**
     * Perform one round of the battle, and contributes to popupating the death list
     * 
     * 1. form duels each fighter is assigned to one or more duels. (if it's a 3v5,
     * some fighter will fight 2+ fighters).
     * 
     * How are duels formed? This is a bit hard to explain with text. Code is a bit
     * clearer if you get idea. If you're having trouble understanding it, call me
     * (Mathieu Paturel) i'll show you a picture (i'm too lazy to draw ascii art)
     * 
     * Basic idea: strong people against strong people (we pick a rule to have
     * reproducible battles)
     * 
     * 2. perform duels no ambiguity here, just do what the spec says
     * 
     * @param allies
     * @param enemies
     * @param deaths
     */
    private void performRound(List<Fighter> allies, List<Fighter> enemies, Set<Fighter> deaths) {
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
     * The spec's formula for damage: attack.health * attack.attack_damage /
     * defencer.defence_coef
     * 
     * (for example, defence_coef == 2 when you have an armor for example) Damage is
     * used like this: defence.health -= damage
     * 
     * @param attacker
     * @param defencer
     * @return damage to be done
     */
    private float computeDamage(Fighter attacker, Fighter defender) {
        return attacker.getHealth() * attacker.getAttackDamage(defender) / defender.getDefenceCoef();
    }

    @Override
    public int getPrecedence() {
        return this.precendence;
    }

}
