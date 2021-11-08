package dungeonmania.entities.movings;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;

public class Assassin extends Mercenary {

    public static final String STRING_TYPE = "assassin";

    public Assassin(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.setHealth(18);
    }
    
}
