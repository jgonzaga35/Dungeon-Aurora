package dungeonmania.entities.movings;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.DungeonMap;
import dungeonmania.Entity;
import dungeonmania.Inventory;
import dungeonmania.Pos2d;
import dungeonmania.Utils;
import dungeonmania.battlestrategies.BattleStrategy.BattleDirection;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.collectables.OneRing;
import dungeonmania.entities.statics.Portal;
import dungeonmania.util.Direction;

/**
 * Represents the character that the player controls.
 */
public class Player extends MovingEntity implements Fighter {

    public static String STRING_TYPE = "player";

    private float health;

    private Inventory inventory = new Inventory();
    
    public Player(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.health = this.getStartingHealth();
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

    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public String getTypeAsString() {
        return Player.STRING_TYPE;
    }

    @Override
    public void tick() {
    }

    @Override
    public boolean onDeath() {
        DungeonMap map = this.dungeon.getMap();
        if (
            Utils.isDead(this) // we dead
            && this.inventory.itemsOfType(OneRing.class).count() > 0 // we have a ring!
            && !map.getEntryCell().isBlocking() // we didn't block the entry
        ) {
            this.inventory.remove(OneRing.STRING_TYPE);
            this.health = this.getStartingHealth();
            this.moveTo(map.getEntryCell());
            return true;
        }
        return false;
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
        this.inventory.usedItemsForBattle(d);
    }

    @Override
    public FighterRelation getFighterRelation() {
        return FighterRelation.ALLY;
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public boolean isBoss() {
        return false;
    }

    /**
     * Calculates the player's starting health based on the current game mode.
     * @return
     */
    private float getStartingHealth() {
        if (this.dungeon.getGameMode() == GameMode.HARD) {
            return 8;
        } else {
            return 10;
        }
    }

}
