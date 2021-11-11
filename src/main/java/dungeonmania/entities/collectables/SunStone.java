// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

public class SunStone extends Treasure {

    public static String STRING_TYPE = "sun_stone";

    public SunStone(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public String getTypeAsString() {
        return SunStone.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    @Override
    public void tick() {
    }
}
