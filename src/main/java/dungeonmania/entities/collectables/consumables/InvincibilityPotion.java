package dungeonmania.entities.collectables.consumables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.Fighter.FighterRelation;
import dungeonmania.movement.FleeMovementBehaviour;
import dungeonmania.movement.MovementBehaviour;

public class InvincibilityPotion extends Potion {

    public static String STRING_TYPE = "invincibility_potion";

    private Map<MovingEntity, MovementBehaviour> affectedEntities = new HashMap<>();

    public InvincibilityPotion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.maxDuration = 5;
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }

    @Override
    public void expire() {
        affectedEntities.keySet().stream().forEach(e -> {
            e.removeMovementBehaviour(affectedEntities.get(e));
        });

        affectedEntities.clear();
    }

    @Override
    public void applyEffects() {
        dungeon.getMap().allEntities().stream()
            .filter(e -> !affectedEntities.keySet().contains(e))
            .filter(e -> e instanceof Fighter).map(e -> (Fighter) e)
            .filter(f -> f.getFighterRelation() == FighterRelation.ENEMY)
            .forEach(f -> {
                MovingEntity enemy = (MovingEntity) f;
                MovementBehaviour effect = new FleeMovementBehaviour(1, dungeon.getMap(), enemy.getCell());

                enemy.addMovementBehaviour(effect);

                affectedEntities.put(enemy, effect);
            });
    }
}
