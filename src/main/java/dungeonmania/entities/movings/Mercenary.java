package dungeonmania.entities.movings;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.movement.FollowMovementBehaviour;
import dungeonmania.movement.FriendlyMovementBehaviour;

public class Mercenary extends MovingEntity implements Fighter {

    public static final String STRING_TYPE = "mercenary";
    private float health = 6;
    private FighterRelation relationship = FighterRelation.ENEMY;

    public Mercenary(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.addMovementBehaviour(
            new FollowMovementBehaviour(
                5, 
                dungeon.getMap(), 
                dungeon.getMap().getCell(position)
            )
        );
    }

    public void bribe() {
        relationship = FighterRelation.ALLY;
        addMovementBehaviour(
            new FriendlyMovementBehaviour(
                0, 
                dungeon.getMap(), 
                getCell()
            )
        );
    }

    @Override
    public String getTypeAsString() {
        return Mercenary.STRING_TYPE;
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
        return 1;
    }

    @Override
    public float getDefenceCoef() {
        return 1;
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
}
