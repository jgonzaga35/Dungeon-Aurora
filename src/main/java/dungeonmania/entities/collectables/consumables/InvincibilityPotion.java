package dungeonmania.entities.collectables.consumables;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.WinAllBattleStrategy;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.Fighter.FighterRelation;
import dungeonmania.entities.MovingEntity;
import dungeonmania.movement.FleeMovementBehaviour;
import dungeonmania.movement.MovementBehaviour;

/**
 * Represents an invincibility potion.
 * Any battles that occur when the character has the effects of the potion end immediately, 
 * with the character immediately winning. Because of this, all enemies will run away from 
 * the character when they are invincible. 
 * in hard mode, this potion doesn't do anything.
 */
public class InvincibilityPotion extends Potion {

    public static String STRING_TYPE = "invincibility_potion";
    public static final int MAX_DURATION = 5;

    // A map contatining entites that are affected by the player's invincibility
    private Map<MovingEntity, MovementBehaviour> affectedEntities = new HashMap<>();

    public InvincibilityPotion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.maxDuration = InvincibilityPotion.MAX_DURATION;
        if (this.dungeon.getGameMode() != GameMode.HARD) {
            // Player with invincibility automatically wins all battles
            this.battleStrategy = new WinAllBattleStrategy(10);
        }
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }

    /**
     * When the invincibility potion expires, all effects are removed
     */
    @Override
    public void expire() {
        // potion does not work in hard mode
        if (this.dungeon.getGameMode() == GameMode.HARD) return; 

        // reset the movement behaviour of all previously affected entities
        affectedEntities.keySet().stream().forEach(e -> {
            boolean removed = e.removeMovementBehaviour(affectedEntities.get(e));
            assert removed;
        });

        this.dungeon.removeBattleStrategy(battleStrategy);

        affectedEntities.clear();
    }

    @Override
    public void onDrink() {
        // potion does not work in hard mode
        if (this.dungeon.getGameMode() == GameMode.HARD) return;

        this.dungeon.addBattleStrategy(battleStrategy);
    }

    /**
     * The invincibility potion changes the movement behaviours of enemies.
     * Affected enemies flee from the player with FleeMovementBehaviour.
     */
    @Override
    public void applyEffectsEveryTick() {
        if (this.dungeon.getGameMode() == GameMode.HARD) return;

        dungeon.getMap().allEntities().stream()
            .filter(e -> !affectedEntities.keySet().contains(e))
            .filter(e -> e instanceof Fighter).map(e -> (Fighter) e)
            .filter(f -> f.getFighterRelation() == FighterRelation.ENEMY)
            .forEach(f -> {
                MovingEntity enemy = (MovingEntity) f;
                MovementBehaviour effect = new FleeMovementBehaviour(10, dungeon.getMap(), enemy.getCell());

                enemy.addMovementBehaviour(effect);

                affectedEntities.put(enemy, effect);
            });
    }
}
