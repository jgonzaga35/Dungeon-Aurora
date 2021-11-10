package dungeonmania.entities.movings;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.collectables.OneRing;

public class Assassin extends Mercenary {

    public static final String STRING_TYPE = "assassin";
    public static final Integer SPAWN_PERCENTAGE = 25;

    /**
     * Assassin is essentially just a mercenary with more health (damage)
     * and additionally requiring the one ring to bribe.
     * 
     * @param dungeon the dungeon that the assassin is added to.
     * @param position the position of the assassin in the dungeon.
     */
    public Assassin(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
        this.setHealth(18);
        this.price.add(OneRing.class);
    }

    @Override
    public String getTypeAsString() {
        return Assassin.STRING_TYPE;
    }
    
}
