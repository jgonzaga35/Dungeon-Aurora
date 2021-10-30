package dungeonmania.entities;

import java.util.PriorityQueue;

import dungeonmania.Cell;
import dungeonmania.Dungeon;
import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.entities.collectables.ConsumableEntity;
import dungeonmania.entities.collectables.consumables.InvincibilityPotion;
import dungeonmania.Entity;
import dungeonmania.Pos2d;
import dungeonmania.movement.FleeMovementBehaviour;
import dungeonmania.movement.MovementBehaviour;
import dungeonmania.util.Direction;

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

        this.movementBehaviours = new PriorityQueue<>(3, (a,b) -> a.getPrecendence() - b.getPrecendence());
    }

    /**
     * When you add a strategy, it doesn't mean it's the one that is going to be
     * used. The strategy with the highest precedence will be used.
     * 
     * @see MovementBehaviour
     * @param ms
     */
    public void addMovementBehaviour(MovementBehaviour ms) {
        this.movementBehaviours.add(ms);
    }

    /**
     * @param ms the movement strategy to remove
     * @return true if the movement strategy was present.
     */
    public boolean removeMovementBehaviour(MovementBehaviour ms) {
        return this.movementBehaviours.remove(ms);
    }

    public Cell shouldMoveTo() {
        assert this.movementBehaviours.size() > 0;
        return this.movementBehaviours.peek().move();
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

    /**
     * Activates the effects of the given consumable on this entity. The same 
     * consumable can affect entities in different ways. These changes are handled
     * in the sub classes.
     * 
     * ex. A health potion being consumed heals the player but does nothing to 
     * enemies.
     * 
     * @param consumable the consumable who's effect to apply
     */
    public void activateConsumable(ConsumableEntity consumable) {
        if (consumable instanceof InvincibilityPotion) {
            addMovementBehaviour(new FleeMovementBehaviour(2, dungeon.getMap(), getCell()));
        }
    }

    @Override
    public boolean isInteractable() {
        return false; // i think at least
    }

    @Override
    public LayerLevel getLayerLevel() {
        return LayerLevel.MOVING_ENTITY;
    }
}
