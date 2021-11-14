package dungeonmania.entities.collectables;

import java.util.*;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

public abstract class BuildableEntity extends CollectableEntity {

    /**
     * Constructor for Buildable Entities
     * @param Dungeon dungeon
     * @param Pos2d position where Buildable Entity is located
     */
    public BuildableEntity(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public void tick() {
        // Do nothing by default
    }
    
}
