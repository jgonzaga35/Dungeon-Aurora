package dungeonmania.entities;

import java.util.PriorityQueue;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.movement.MovementBehaviour;

public abstract class MovingEntity extends Entity {

    /**
     * @see MovementBehaviour
     */
    PriorityQueue<MovementBehaviour> movementBehaviours;

    /**
     * Each moving entity should set, in their constructor, their default
     * movement strategy (with a precedence of 0)
     * @param dungeon
     * @param position
     */
    public MovingEntity(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);

        this.movementBehaviours = new PriorityQueue<>(3, (a,b) -> b.getPrecendence() - a.getPrecendence());
    }

    /**
     * When you add a strategy, it doesn't mean it's the one that is going to be
     * used. The strategy with the highest precedence will be used.
     * 
     * @see MovementBehaviour
     * @param ms
     */
    public void addMovementBehaviour(MovementBehaviour ms) {
        Cell curr = this.getCell();
        this.movementBehaviours.add(ms);
        this.movementBehaviours.peek().setCurrentCell(curr);
    }

    /**
     * @param ms the movement strategy to remove
     * @return true if the movement strategy was present.
     */
    public boolean removeMovementBehaviour(MovementBehaviour ms) {
        Cell curr = this.getCell();
        boolean removed = this.movementBehaviours.remove(ms);
        this.movementBehaviours.peek().setCurrentCell(curr);
        return removed;
    }

    public MovementBehaviour getCurrentMovementBehaviour() {
        return this.movementBehaviours.peek();
    }

    /**
     * Moves to wherever the movement behaviour tells it to
     * @return the cell the entity is now on
     */
    public Cell move() {
        assert this.movementBehaviours.size() > 0;
        assert this.position.equals(this.movementBehaviours.peek().getCurrentCell().getPosition());

        Cell cell = this.movementBehaviours.peek().move();
        this.movementBehaviours.stream().forEach(b -> b.setCurrentCell(cell));
        this.moveTo(cell);
        return cell;
    }


    /**
     * Moves an entity from the current cell to the target cell
     * @param target
     */
    public void moveTo(Cell target) {
        Cell from = this.getCell();
        from.removeOccupant(this);
        target.addOccupant(this);

        this.position = target.getPosition();
        this.getCell().onWalked(from.getPosition(), this.position);
    }

    // /**
    //  * Activates the effects of the given consumable on this entity. The same 
    //  * consumable can affect entities in different ways. These changes are handled
    //  * in the sub classes.
    //  * 
    //  * ex. A health potion being consumed heals the player but does nothing to 
    //  * enemies.
    //  * 
    //  * @param consumable the consumable who's effect to apply
    //  */
    // public void activateConsumable(ConsumableEntity consumable) {
    //     if (consumable instanceof InvincibilityPotion) {
    //         addMovementBehaviour(new FleeMovementBehaviour(2, dungeon.getMap(), getCell()));
    //     }
    // }

    @Override
    public boolean isInteractable() {
        return false; // i think at least
    }

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.MOVING_ENTITY;
    }
}
