// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

public class Key extends CollectableEntity {

    public static String STRING_TYPE = "key";

    public String keyId;

    public Key(Dungeon dungeon, Pos2d position, String keyId) {
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
        return Treasure.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    @Override
    public void tick() {
    }
}
