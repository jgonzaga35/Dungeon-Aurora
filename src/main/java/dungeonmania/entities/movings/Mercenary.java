package dungeonmania.entities.movings;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.collectables.Armour;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.movement.FollowMovementBehaviour;
import dungeonmania.movement.FriendlyMovementBehaviour;
import dungeonmania.movement.MovementBehaviour;

public class Mercenary extends MovingEntity implements Fighter {

    public static final String STRING_TYPE = "mercenary";
    public static final int SPAWN_EVERY_N_TICKS = 50;
    public static final int BATTLE_RADIUS = 3;

    protected List<Class<? extends CollectableEntity>> price = new ArrayList<>();

    private float health = 6;
    private FighterRelation relationship = FighterRelation.ENEMY;
    private Integer bribeDuration = -1;
    private MovementBehaviour followMovementBehaviour;
    private MovementBehaviour friendlyMovementBehaviour;

    public Mercenary(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.followMovementBehaviour = new FollowMovementBehaviour(0, dungeon.getMap(),
                dungeon.getMap().getCell(position));
        this.friendlyMovementBehaviour = new FriendlyMovementBehaviour(0, dungeon.getMap(), getCell());
        this.addMovementBehaviour(this.followMovementBehaviour);
        this.price.add(Treasure.class);

        Integer roll = dungeon.getRandom().nextInt(100);
        if (roll < 30)
            this.inventory.add(new Armour(dungeon, position));
    }

    public void bribe() {
        this.bribeDuration = -1;
        relationship = FighterRelation.ALLY;
        // order matters! add first, then remove
        this.addMovementBehaviour(this.friendlyMovementBehaviour);
        this.removeMovementBehaviour(this.followMovementBehaviour);
    }

    public void bribe(Integer bribeDuration) {
        this.bribe();
        this.bribeDuration = bribeDuration;
    }

    public void betray() {
        relationship = FighterRelation.ENEMY;
        this.addMovementBehaviour(this.followMovementBehaviour);
        this.removeMovementBehaviour(this.friendlyMovementBehaviour);
    }

    public List<Class<? extends CollectableEntity>> getPrice() {
        return this.price;
    }

    @Override
    public String getTypeAsString() {
        return Mercenary.STRING_TYPE;
    }

    @Override
    public void tick() {
        this.move();
        this.bribeDuration--;
        if (bribeDuration == 0)
            this.betray();
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
        // mercenaries don't use items, so that doesn't matter
    }

    @Override
    public FighterRelation getFighterRelation() {
        return relationship;
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public boolean isInteractable() {
        return relationship == FighterRelation.ENEMY;
    }

    @Override
    public boolean isBoss() {
        return false;
    }

    public void removeArmour() {
        inventory.clear();
    }
}
