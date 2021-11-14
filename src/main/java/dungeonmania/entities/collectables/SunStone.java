// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;

/**
 * Represents a Sun stone.
 * Can be picked up by the player, 
 * and can be used to open doors and interchangeably with treasure. 
 * The player retains the stone after usage. 
 */
public class SunStone extends Treasure {

    public static final String STRING_TYPE = "sun_stone";

    public SunStone(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public void tick() {
    }

    @Override
    public String getTypeAsString() {
        return SunStone.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

}
