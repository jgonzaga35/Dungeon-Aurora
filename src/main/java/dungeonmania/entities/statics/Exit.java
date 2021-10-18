// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.statics;

import dungeonmania.Cell;
import dungeonmania.entities.StaticEntity;

public class Exit extends StaticEntity {

    public Exit(Cell cell) {
        super(cell);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public String getTypeAsString() {
        return "exit";
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    @Override
    public void tick() {
    }
}
