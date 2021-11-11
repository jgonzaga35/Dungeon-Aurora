package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.StaticEntity;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.util.BlockingReason;

public class Door extends StaticEntity {

    public static String STRING_TYPE = "door";

    private boolean locked = true;

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
        Inventory inventory = this.dungeon.getInventory();
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
