package dungeonmania.entities.movings;

import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.movement.FollowMovementBehaviour;

public class Mercenary extends MovingEntity implements Fighter {

    public static final String STRING_TYPE = "mercenary";

    public Mercenary(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.addMovementBehaviour(
            new FollowMovementBehaviour(
                0, 
                dungeon.getMap(), 
                dungeon.getMap().getCell(position)
            )
        );
    }

    @Override
    public float getHealth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setHealth(float h) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public float getAttackDamage() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getDefenceCoef() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void usedItemFor(BattleDirection d) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public FighterRelation getFighterRelation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entity getEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTypeAsString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
        
    }
    
}
