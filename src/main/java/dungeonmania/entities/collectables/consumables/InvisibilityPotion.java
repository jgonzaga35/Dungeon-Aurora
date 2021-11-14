package dungeonmania.entities.collectables.consumables;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.NoBattleStrategy;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.Fighter.FighterRelation;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.movings.Spider;
import dungeonmania.movement.CircleMovementBehaviour;
import dungeonmania.movement.MovementBehaviour;
import dungeonmania.movement.RandomMovementBehaviour;

/**
 * Represents an invisibility potion.
 * When a player picks up an invisibility potion, 
 * they may consume it at any time and they immediately 
 * become invisible and can move past all other entities undetected.
 */
public class InvisibilityPotion extends Potion {

    public static String STRING_TYPE = "invisibility_potion";
    public static final int MAX_DURATION = 10;

    // A map contatining entites that are affected by the player's invincibility
    private Map<MovingEntity, MovementBehaviour> affectedEntities = new HashMap<>();;

    public InvisibilityPotion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.maxDuration = InvisibilityPotion.MAX_DURATION;
        battleStrategy = new NoBattleStrategy(5);
    }

    /**
     * Enemies will stop moving towards the player and instead move with RandomMovementBehaviour.
     */
    @Override
    public void applyEffectsEveryTick() {
        dungeon.getMap().allEntities().stream()
            .filter(e -> !affectedEntities.keySet().contains(e))
            .filter(e -> e instanceof Fighter).map(e -> (Fighter) e)
            .filter(f -> f.getFighterRelation() == FighterRelation.ENEMY) // having this enables allies to see you
            .forEach(f -> {
                MovingEntity enemy = (MovingEntity) f;
                MovementBehaviour effect;
                if (enemy instanceof Spider) {
                    effect = new CircleMovementBehaviour(20,dungeon.getMap(), enemy.getCell());
                } else {
                    effect = new RandomMovementBehaviour(20, dungeon, enemy.getCell());
                }

                enemy.addMovementBehaviour(effect);

                affectedEntities.put(enemy, effect);
            });
    }

    /**
     * Remove all effects from the game.
     */
    @Override
    public void expire() {
        affectedEntities.keySet().stream().forEach(e -> {
            e.removeMovementBehaviour(affectedEntities.get(e));
        });

        this.dungeon.removeBattleStrategy(battleStrategy);

        affectedEntities.clear();
    }

    @Override
    public String getTypeAsString() {
        return InvisibilityPotion.STRING_TYPE;
    }

    @Override
    public void onDrink() {
        this.dungeon.addBattleStrategy(battleStrategy);
    }
    
}
