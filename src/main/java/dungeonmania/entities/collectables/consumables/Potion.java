package dungeonmania.entities.collectables.consumables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.entities.collectables.ConsumableEntity;

public abstract class Potion extends CollectableEntity {
    protected Integer duration;

    public Potion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    /**
     * Applies the effects of the potion.
     */
    public abstract void drink();

    public void setDuration(Integer ticks) {
        this.duration = ticks;
    }

    @Override
    public boolean isInteractable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void tick() {
        duration--;
    }
}
