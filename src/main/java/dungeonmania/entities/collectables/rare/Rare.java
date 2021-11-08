package dungeonmania.entities.collectables.rare;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.battlestrategies.BattleStrategy;
import dungeonmania.entities.CollectableEntity;

public abstract class Rare extends CollectableEntity {
    // Inactive rare collectables have -1 duration
    protected Integer duration = -1;
    protected Integer maxDuration;

    public Rare(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    /**
     * Applies the effects of the rare collectable.
     */
    public abstract void applyEffects();


    /**
     * Remove the effects of a rare collectable.
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
