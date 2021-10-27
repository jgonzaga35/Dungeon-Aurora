package dungeonmania.entities.movings;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.MovingEntity;
import dungeonmania.util.*;

public class Player extends MovingEntity {

    public static String STRING_TYPE = "player";
    
    public Player(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    /**
     * Move the player in the direction, if the cell there isn't blocking
     * @param d direction
     */
    public void handleMoveOrder(Direction d) {
        if (d == Direction.NONE) 
            //do nothing if no direcition
            return;

        Cell target = this.inspectCell(d);
        if (target == null) 
            //do nothing if target cell is null
            return;

        BlockingReason blockingReason = target.getBlocking();
        switch (blockingReason) {
            case NOT:
                this.moveTo(target);
            case BOULDER:
                if (target.pushBoulder(d)) {
                    this.moveTo(target);
                }
                return;
            default:
                return;
        }

        
    }

    @Override
    public String getTypeAsString() {
        return Player.STRING_TYPE;
    }

    @Override
    public void tick() {
    }
}
