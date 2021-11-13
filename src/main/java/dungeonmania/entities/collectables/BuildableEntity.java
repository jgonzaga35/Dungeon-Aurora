package dungeonmania.entities.collectables;

import java.util.*;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;

public abstract class BuildableEntity extends CollectableEntity {

    public BuildableEntity(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public void tick() {
    }
    
}
