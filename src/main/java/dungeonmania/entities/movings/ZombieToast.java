package dungeonmania.entities.movings;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.collectables.Armour;
import dungeonmania.movement.RandomMovementBehaviour;

public class ZombieToast extends MovingEntity implements Fighter {

    public static final String STRING_TYPE = "zombie_toast";
    private float health = 4;

    public ZombieToast(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.addMovementBehaviour(new RandomMovementBehaviour(0, dungeon, dungeon.getMap().getCell(position)));
        Integer roll = dungeon.getRandom().nextInt(100);
        if (roll < 15)
            this.inventory.add(new Armour(dungeon, position));
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
    public float getAttackDamage(Fighter target) {
        return 1 + this.inventory.totalBonus(BattleDirection.ATTACK, target);
    }

    @Override
    public float getDefenceCoef() {
        return 1 * this.inventory.totalBonus(BattleDirection.DEFENCE, null);
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

    @Override
    public boolean isBoss() {
        return false;
    }
}
