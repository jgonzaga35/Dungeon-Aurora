package dungeonmania.entities.collectables.consumables;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.NoBattleStrategy;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.Fighter.FighterRelation;
import dungeonmania.movement.MovementBehaviour;
import dungeonmania.movement.RandomMovementBehaviour;

public class InvisibilityPotion extends Potion {

    public static String STRING_TYPE = "invisibility_potion";

    private Map<MovingEntity, MovementBehaviour> affectedEntities = new HashMap<>();

    public InvisibilityPotion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.maxDuration = 10;
        battleStrategy = new NoBattleStrategy(1); // less priority than invincibility.
    }

    @Override
    public void applyEffects() {
        dungeon.getMap().allEntities().stream()
            .filter(e -> !affectedEntities.keySet().contains(e))
            .filter(e -> e instanceof Fighter).map(e -> (Fighter) e)
            .filter(f -> f.getFighterRelation() == FighterRelation.ENEMY) // having this enables allies to see you
            .forEach(f -> {
                MovingEntity enemy = (MovingEntity) f;
                MovementBehaviour effect = new RandomMovementBehaviour(0, dungeon.getMap(), enemy.getCell()); // prioritized over invincibility (1)

                enemy.addMovementBehaviour(effect);

                affectedEntities.put(enemy, effect);
            });
    }

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
    
}
