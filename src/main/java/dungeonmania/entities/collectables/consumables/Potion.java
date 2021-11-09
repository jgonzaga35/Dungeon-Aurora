package dungeonmania.entities.collectables.consumables;

import org.json.JSONObject;

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
     * 
     * Calls the potion's onDrink method (template pattern)
     */
    public final void drink() {
        this.duration = maxDuration;
        onDrink();
    }

    /**
     * Applies the effects of the potion.
     */
    public abstract void applyEffectsEveryTick();

    public abstract void onDrink();


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
        else applyEffectsEveryTick();
        duration--;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();

        json.put("duration", duration);

        return json;
    }
}
