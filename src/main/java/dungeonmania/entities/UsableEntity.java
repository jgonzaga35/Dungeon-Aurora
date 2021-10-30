package dungeonmania.entities;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.util.Semy;
import dungeonmania.util.Semy.Observer;

public abstract class UsableEntity extends CollectableEntity {

    private Semy<UsableEntity> onItemUseSemy = new Semy<>();

    public UsableEntity(Dungeon dungeon, Pos2d position) {
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
    public void onItemUse(Observer<UsableEntity> observer) {
        this.onItemUseSemy.bind(observer);
    }
}
