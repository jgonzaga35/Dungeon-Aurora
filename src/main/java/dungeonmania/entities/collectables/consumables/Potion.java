package dungeonmania.entities.collectables.consumables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy;
import dungeonmania.entities.CollectableEntity;

public abstract class Potion extends CollectableEntity {
    // Inactive potions have -1 duration
    protected Integer duration = -1;
    protected Integer maxDuration;
    protected BattleStrategy battleStrategy;

    public Potion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }
    
    /**
     * Gives the potion some duration and applies it's effects.
     */
    public void drink() {
        this.duration = maxDuration;
        this.dungeon.addBattleStrategy(battleStrategy);
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
        else applyEffects();
        duration--;
    }
}
