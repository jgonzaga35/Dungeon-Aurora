package dungeonmania.entities.statics;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;
import dungeonmania.util.BlockingReason;
import dungeonmania.util.Direction;

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

        switch (target.getBlocking()) {
            case NOT:
                this.moveTo(target);
                return true;
            default:
                return false;
        }
    }

    /**
     * A moveTo method, same as the one from moving entity
     * @param target
     */
    public void moveTo(Cell target) {
        Cell from = this.getCell();
        
        from.removeOccupant(this);
        target.addOccupant(this);

        this.position = target.getPosition();
        this.getCell().onWalked(from.getPosition(), this.position);
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
