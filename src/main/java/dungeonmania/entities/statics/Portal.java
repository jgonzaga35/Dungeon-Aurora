package dungeonmania.entities.statics;

import dungeonmania.Cell;
import dungeonmania.entities.StaticEntity;

public class Portal extends StaticEntity {

    public static String STRING_TYPE = "portal";
    public Portal correspondingPortal;

    public Portal(Cell cell) {
        super(cell);
    }

    public void setCorrespondingPortal(Portal portal) {
        this.correspondingPortal = portal;
    }

    // Returns the cell of the corresponding Portal
    public Cell teleport() {
        // If the portal does not yet have a corresponding portal, return the current cell only
        if (correspondingPortal == null) {
            return getCell();
        }

        Cell target = correspondingPortal.getCell();
        return target;
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
