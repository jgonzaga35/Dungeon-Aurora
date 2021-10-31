package dungeonmania.entities.movings;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.collectables.buildables.Shield;
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
            //do nothing if no direction
            return;

        Cell target = this.inspectCell(d);
        if (target == null) 
            //do nothing if target cell is null
            return;

        Portal portal = target.hasPortal();
        if (portal != null) {
            target = portal.getTeleportDestination();
            this.moveTo(target);
            return;
        }

        switch (target.getBlocking()) {
            case NOT:
                //move if target is unblocked
                this.moveTo(target);
                return;
            case BOULDER:
                //try to push boulder
                if (target.pushBoulder(d)) this.moveTo(target);
                return;
            case DOOR:
                //try to unlocked door
                if (target.unlockDoor()) this.moveTo(target);
                return;
            default:
                return;
        }

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
        float defenceCoef = 1;
        if (this.dungeon.getInventory().itemsOfType(Shield.class).count() > 0) {
            // if you have 2 shield, it's the same as having 1 shield
            defenceCoef += Shield.DEFENCE_COEF_BONUS;
        }
        return defenceCoef;
    }

    @Override
    public void usedItemFor(BattleDirection d) {
        this.dungeon.getInventory().usedItemsForBattle(d);
        
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
