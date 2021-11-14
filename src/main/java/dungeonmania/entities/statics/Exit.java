// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;
import dungeonmania.util.BlockingReason;

/**
 * Represents an exit on the map.
 * If the character goes through it, the puzzle is complete.
 */
public class Exit extends StaticEntity {

    public static String STRING_TYPE = "exit";

    public Exit(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public BlockingReason isBlocking() {
        return BlockingReason.NOT;
    }

    @Override
    public String getTypeAsString() {
        return Exit.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    @Override
    public void tick() {
    }
}
