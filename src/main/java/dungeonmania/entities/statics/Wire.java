package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.entities.LogicalEntity;
import dungeonmania.util.BlockingReason;

public class Wire extends LogicalEntity {
    public static String STRING_TYPE = "wire";

    public Wire(Dungeon dungeon, Pos2d position, String logic) {
        super(dungeon, position, logic);
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public String getTypeAsString() {
        return Wire.STRING_TYPE;
    }

    @Override
    public void tick() {

    }

    public BlockingReason isBlocking() {
        return BlockingReason.NOT;
    }

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.STATIC;
    }

    @Override
    public void activate() {
        
    }

    @Override
    public void deactivate() {
        
    }

    public boolean isActivated() {
        if (countActivatedSwitches() > 0) {
            return true;
        }
        return false;
    }

    
}
