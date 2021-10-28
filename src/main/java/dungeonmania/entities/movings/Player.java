package dungeonmania.entities.movings;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.statics.Portal;
import dungeonmania.util.Direction;

public class Player extends MovingEntity implements Fighter {

    public static String STRING_TYPE = "player";

    private float health = 10;
    
    public Player(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    /**
     * Move the player in the direction, if the cell there isn't blocking
     * @param d direction
     */
    public void handleMoveOrder(Direction d) {
        if (d == Direction.NONE)
            return;

        Cell target = this.inspectCell(d);
        if (target == null || target.isBlocking())
            return;

        Portal portal = target.hasPortal();
        if (portal != null) {
            target = portal.getTeleportDestination();
        }

        this.moveTo(target);
    }

    @Override
    public String getTypeAsString() {
        return Player.STRING_TYPE;
    }

    @Override
    public void tick() {
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
        // TODO: update items durability
        
    }

    @Override
    public FighterRelation getFighterRelation() {
        return FighterRelation.ALLY;
    }

    @Override
    public Entity getEntity() {
        return this;
    }
}
