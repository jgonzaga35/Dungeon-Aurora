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
 * in hard mode, this potion doesn't do anything
 */
public class InvincibilityPotion extends Potion {

    public static String STRING_TYPE = "invincibility_potion";
    public static final int MAX_DURATION = 5;

    private Map<MovingEntity, MovementBehaviour> affectedEntities = new HashMap<>();

    public InvincibilityPotion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.maxDuration = InvincibilityPotion.MAX_DURATION;
        if (this.dungeon.getGameMode() != GameMode.HARD)
            this.battleStrategy = new WinAllBattleStrategy(10);
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }

    @Override
    public void expire() {
        if (this.dungeon.getGameMode() == GameMode.HARD) return;

        affectedEntities.keySet().stream().forEach(e -> {
            boolean removed = e.removeMovementBehaviour(affectedEntities.get(e));
            assert removed;
        });

        this.dungeon.removeBattleStrategy(battleStrategy);

        affectedEntities.clear();
    }

    @Override
    public void onDrink() {
        if (this.dungeon.getGameMode() == GameMode.HARD) return;

        this.dungeon.addBattleStrategy(battleStrategy);
    }

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
