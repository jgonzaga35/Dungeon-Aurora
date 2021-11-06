package dungeonmania.entities.movings;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.Fighter;
import dungeonmania.entities.MovingEntity;

public abstract class Boss extends MovingEntity implements Fighter {

    public Boss(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }
    
}
