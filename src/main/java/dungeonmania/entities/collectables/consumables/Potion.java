package dungeonmania.entities.collectables.consumables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.collectables.ConsumableEntity;

public abstract class Potion extends CollectableEntity {
    // Inactive potions have -1 duration
    protected Integer duration = -1;
    protected Integer maxDuration;

    public Potion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }
    
    /**
     * Gives the potion some duration and applies it's effects.
     */
    public void drink() {
        this.duration = maxDuration;
        applyEffects();
    }

    /**
     * Applies the effects of the potion.
     */
    public abstract void applyEffects();


    /**
     * Remove the effects of a potion.
     */
    public abstract void expire();

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public void tick() {
        if (duration == 0) expire();
        else applyEffects();
        duration--;
    }
}
