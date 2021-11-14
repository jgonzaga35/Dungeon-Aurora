// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

/**
 * Represents a key that can be used to unlock doors.
 */
public class Key extends CollectableEntity {

    public static String STRING_TYPE = "key";

    public int keyId;

    public Key(Dungeon dungeon, Pos2d position, int keyId) {
        super(dungeon, position);
        this.keyId = keyId;
    }

    /**
     * 
     * @return keyId : int
     */
    public int getKeyId() {
        return keyId;
    }

    @Override
    public String getTypeAsString() {
        return Key.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public void tick() {
    }
}
