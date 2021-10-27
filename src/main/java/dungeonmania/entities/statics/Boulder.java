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

    public boolean roll(Direction d) {
        Cell target = this.inspectCell(d);
        if (target == null) 
            //do nothing if target cell is null
            return false;

        BlockingReason blockingReason = target.getBlocking();
        switch (blockingReason) {
            case NOT:
                this.moveTo(target);
                return true;
            default:
                return false;
        }
    }

    public void moveTo(Cell target) {
        Cell from = this.getCell();
        from.removeOccupant(this);
        target.addOccupant(this);

        this.position = target.getPosition();
        this.getCell().onWalked(from.getPosition(), this.position);
    }

    @Override
    public BlockingReason isBlocking() {
        // TODO Auto-generated method stub
        return BlockingReason.BOULDER;
    }

    @Override
    public boolean isInteractable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }
    
}
