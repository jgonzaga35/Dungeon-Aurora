// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

public class Sword extends CollectableEntity {

    public static String STRING_TYPE = "sword";

    public static int INITIAL_DURABILITY = 10;

    public int durabilityLeft;

    public Sword(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.durabilityLeft = INITIAL_DURABILITY;
    }

    /**
     * 
     * @return Remaining Durability (int)
     */
    public int getDurability() {
        return durabilityLeft;
    }

    /**
     * 
     * @param numTimes : int (Number of Times Sword Used)
     * @return Remaining Durability (int)
     */
    public int used(int numTimes) {
        durabilityLeft = durabilityLeft - numTimes;
        return durabilityLeft;
    }

    @Override
    public String getTypeAsString() {
        return Sword.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    @Override
    public void tick() {
    }
}
