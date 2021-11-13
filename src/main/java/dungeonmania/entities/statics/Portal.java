package dungeonmania.entities.statics;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;
import dungeonmania.util.BlockingReason;

public class Portal extends StaticEntity {

    public static String STRING_TYPE = "portal";
    public Portal correspondingPortal;
    private String colour;

    public Portal(Dungeon dungeon, Pos2d pos, String colour) {
        super(dungeon, pos);
        this.colour = colour;
    }

    public String getColour() {
        return this.colour;
    }

    public void setCorrespondingPortal(Portal portal) {
        this.correspondingPortal = portal;
    }

    // Returns cell of the corresponding Portal
    public Cell getTeleportDestination() {
        // If the portal does not yet have a corresponding portal, return the current cell only
        if (correspondingPortal == null) {
            return getCell();
        }

        Cell target = correspondingPortal.getCell();
        return target;
    }

    @Override
    public BlockingReason isBlocking() {
        return BlockingReason.NOT;
    }

    @Override
    public String getTypeAsString() {
        return Portal.STRING_TYPE + "_" + colour;
    }

    @Override
    public boolean isInteractable() {
        return true; // not too sure what this function does yet but I will figure it out later - justin
    }

    @Override
    public void tick() {
    }
}
