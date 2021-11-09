package dungeonmania.entities.movings;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;

public class Assassin extends Mercenary {

    public static final String STRING_TYPE = "assassin";
    public static final Integer SPAWN_PERCENTAGE = 25;

    public Assassin(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.setHealth(18);
    }

    @Override
    public String getTypeAsString() {
        return Assassin.STRING_TYPE;
    }
    
}
