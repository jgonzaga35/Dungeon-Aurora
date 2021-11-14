package dungeonmania.entities.statics;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.entities.StaticEntity;
import dungeonmania.util.BlockingReason;
import dungeonmania.util.Direction;

/**
 * Represents a boulder.
 * Acts like a wall in most cases. 
 * The only difference is that it can be pushed by the character into cardinally adjacent squares. 
 */
public class Boulder extends StaticEntity {
    public static final String STRING_TYPE = "boulder";

    public Boulder(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    /**
     * A method that attempts to "roll" the boulder to another cell
     * @param d
     * @return true if the boulder is successfully moved
     */
    public boolean roll(Direction d) {
        Cell target = this.inspectCell(d);
        if (target == null) return false;

    
        if (target.getBlocking() == BlockingReason.NOT) {
            Cell from = this.getCell();
    
            // Trigger floor switch if exists
            FloorSwitch sourceSwitch = from.getFloorSwitch();
            if (sourceSwitch != null) {
                sourceSwitch.deactivate();;            
            }
    
            from.removeOccupant(this);
            target.addOccupant(this);
    
            FloorSwitch targetSwitch = target.getFloorSwitch();
            if (targetSwitch != null) {
                targetSwitch.activate();;
            }
    
            this.position = target.getPosition();
            return true;
        }

        return false;
    }

    @Override 
    public LayerLevel getLayerLevel() {
        return LayerLevel.BOULDER;
    }

    @Override
    public BlockingReason isBlocking() {
        return BlockingReason.BOULDER;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public void tick() {
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }
    
}
