package dungeonmania.entities.collectables.consumables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy;
import dungeonmania.entities.CollectableEntity;

/**
 * The parent class for ALL potions.
 * A potion can be consumed by the player at anytime.
 */
public abstract class Potion extends CollectableEntity {
    // Inactive potions have -1 duration
    protected Integer duration = -1;
    protected Integer maxDuration;
    protected BattleStrategy battleStrategy;

    /**
     * Constructor for Potion
     * @param Dungeon dungeon
     * @param Pos2d position that Potion is located
     */
    public Potion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }
    
    /**
     * Gives the potion some duration and applies it's effects.
     * Calls the potion's onDrink method (template pattern)
     */
    public final void drink() {
        this.duration = maxDuration;
        onDrink();
    }

    /**
     * Applies the ongoing effects of the potion every tick.
     */
    public abstract void applyEffectsEveryTick();

    /**
     * Applies the initial effects of the potion. 
     */
    public abstract void onDrink();


    /**
     * Remove the effects of a potion.
     */
    public abstract void expire();

    /**
     * 
     * @return true if the potion is currently active, false otherwise.
     */
    public boolean isActive() {
        return duration >= 0;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public void tick() {
        if (duration == 0) expire();
        else applyEffectsEveryTick();
        duration--;
    }
}
