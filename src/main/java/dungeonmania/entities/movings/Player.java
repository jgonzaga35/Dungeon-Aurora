package dungeonmania.entities.movings;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.MovingEntity;
import dungeonmania.entities.statics.Portal;
import dungeonmania.util.Direction;

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
            return;

        Cell target = this.inspectCell(d);
        if (target == null || target.isBlocking())
            return;

        Portal portal = target.hasPortal();
        if (portal != null) {
            target = portal.teleport();
        }

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
