package dungeonmania.entities.collectables.consumables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.collectables.ConsumableEntity;

public abstract class Potion extends ConsumableEntity {
    protected Integer duration;

    public Potion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

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
