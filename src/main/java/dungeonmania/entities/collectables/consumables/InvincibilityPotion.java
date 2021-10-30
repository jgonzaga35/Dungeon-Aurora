package dungeonmania.entities.collectables.consumables;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;

public class InvincibilityPotion extends Potion {

    public static String STRING_TYPE = "invincibility_potion";

    public InvincibilityPotion(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }
}
