package dungeonmania;

import dungeonmania.entities.MovingEntity;
import dungeonmania.util.Direction;

public class Player extends MovingEntity {

    public static String STRING_TYPE = "player";
    
    public Player(Cell cell) {
        super(cell);
    }

    /**
     * Move the player in the direction, if the cell there isn't blocking
     * @param d direction
     */
    public void handleMoveOrder(Direction d) {
        if (d == Direction.NONE)
            return;

        Cell target = this.getCellAround(d);
        if (target == null || target.isBlocking())
            return;

        this.moveTo(target);
    }

    @Override
    public String getTypeAsString() {
        return Player.STRING_TYPE;
    }

    @Override
    public void tick() {
    }
}
