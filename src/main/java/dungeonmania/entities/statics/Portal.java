package dungeonmania.entities.statics;

import dungeonmania.Cell;
import dungeonmania.entities.StaticEntity;

public class Portal extends StaticEntity {

    public static String STRING_TYPE = "portal";

    public Portal(Cell cell) {
        super(cell);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public String getTypeAsString() {
        return Portal.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return true; // not too sure what this function does yet but I will figure it out later - justin
    }

    @Override
    public void tick() {
    }
}
