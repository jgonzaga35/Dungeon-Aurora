package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.StaticEntity;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.util.BlockingReason;

/**
 * Represents a door.
 * Exists in conjunction with a single key that can open it. 
 * If the character holds the key, they can open the door by moving through it. 
 * Once open, it remains open.
 */
public class Door extends StaticEntity {

    public static String STRING_TYPE = "door";

    // locked = true if shut, locked = false if open
    private boolean locked = true;

    // doorId is the same as the matching key's keyID
    public int doorId;

    public Door(Dungeon dungeon, Pos2d position, int doorId) {
        super(dungeon, position);
        this.doorId = doorId;
    }

    /**
     * checks through the player inventory to see if there is a key to this door
     * @return true if door is successfully opened
     */
    public boolean open() {
        Inventory inventory = this.dungeon.getPlayer().getInventory();
        for (CollectableEntity c : inventory.getCollectables()) {
            if ((c instanceof Key && ((Key)c).getKeyId() == doorId) || (c instanceof SunStone)) {
                // key matches, remove the key from inventory and unlock door
                if (c instanceof Key) inventory.remove(c);
                locked = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockingReason isBlocking() {
        if (locked) return BlockingReason.DOOR;
        else return BlockingReason.NOT;
    }

    @Override
    public String getTypeAsString() {
        if (locked) return "door";
        else return "door_unlocked";
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    @Override
    public void tick() {
    }

    
}
