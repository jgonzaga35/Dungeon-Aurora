package dungeonmania.entities.movings;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.movement.RandomMovementBehaviour;

public class ZombieToast extends MovingEntity implements Fighter {

    public static final String STRING_TYPE = "zombie";
    private float health = 4;

    public ZombieToast(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        health = 4f;
        this.addMovementBehaviour(new RandomMovementBehaviour(0, dungeon.getMap(), dungeon.getMap().getCell(position)));
    }

    public ZombieToast(Dungeon dungeon, JSONObject json) {
        super(dungeon, json);
        if (Float.compare(health, -1f) == 0) health = 4f;
        this.addMovementBehaviour(new RandomMovementBehaviour(0, dungeon.getMap(), dungeon.getMap().getCell(position)));
    }

    @Override
    public String getTypeAsString() {
        return ZombieToast.STRING_TYPE;
    }

    @Override
    public void tick() {
        this.move();
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
