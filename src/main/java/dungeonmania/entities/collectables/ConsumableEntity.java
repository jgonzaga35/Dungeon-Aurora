package dungeonmania.entities.collectables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Semy;
import dungeonmania.util.Semy.Observer;

public abstract class ConsumableEntity extends CollectableEntity {

    private Semy<ConsumableEntity> onItemUseSemy = new Semy<>();

    public ConsumableEntity(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    /**
     * Passes this entity to the onEvent method of all observers.
     */
    public void use() {
        this.onItemUseSemy.emit(this);
    }

    /**
     * Adds something that happens on the use of this item.
     * @param observer
     */
    public void onItemUse(Observer<ConsumableEntity> observer) {
        this.onItemUseSemy.bind(observer);
    }
}
