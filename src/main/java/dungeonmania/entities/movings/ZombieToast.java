package dungeonmania.entities.movings;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.Utils;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.movement.RandomMovementBehaviour;

public class ZombieToast extends MovingEntity implements Fighter {

    public static final String STRING_TYPE = "zombie";
    private float health = 4;

    public ZombieToast(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.addMovementBehaviour(
            new RandomMovementBehaviour(
                0, 
                dungeon.getMap(), 
                dungeon.getMap().getCell(position)
            )
            // coment
        );
    }

    @Override
    public String getTypeAsString() {
        return ZombieToast.STRING_TYPE;
    }

    @Override
    public void tick() {
        // move in a random direction        
        if (shouldMoveTo() != null) this.moveTo(shouldMoveTo());
    }

    @Override
    public float getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(float h) {
        this.health = h;
    }

    @Override
    public float getAttackDamage() {
        return 1;
    }

    @Override
    public float getDefenceCoef() {
        return 1;
    }

    @Override
    public void usedItemFor(BattleDirection d) {
        // zombies don't use items, so that doesn't matter
    }

    @Override
    public FighterRelation getFighterRelation() {
        return FighterRelation.ENEMY;
    }

    @Override
    public Entity getEntity() {
        return this;
    }
}
