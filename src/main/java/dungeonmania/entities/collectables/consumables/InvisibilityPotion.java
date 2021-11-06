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

public class InvisibilityPotion extends Potion {

    public static String STRING_TYPE = "invisibility_potion";
    public static final int MAX_DURATION = 10;

    private Map<MovingEntity, MovementBehaviour> affectedEntities = new HashMap<>();

    public InvisibilityPotion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.maxDuration = InvisibilityPotion.MAX_DURATION;
        battleStrategy = new NoBattleStrategy(5);
    }

    @Override
    public void applyEffects() {
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
                    effect = new RandomMovementBehaviour(20, dungeon.getMap(), enemy.getCell());
                }

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
