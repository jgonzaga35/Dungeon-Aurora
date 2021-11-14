package dungeonmania.entities.collectables.buildables;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Pos2d;
import dungeonmania.entities.collectables.*;

/**
 * Represents a sceptre.
 * Sceptre allows the player to bribe mercenaries/assassins for free.
 */
public class Sceptre extends BuildableEntity {
    public static final String STRING_TYPE = "sceptre";

    // A list of recipes used to contract the buildable item.
    // A Sceptre can be built 4 different ways.
    public static List<List<String>> RECIPES = List.of(
        List.of(
            SunStone.STRING_TYPE,
            Treasure.STRING_TYPE,
            Arrow.STRING_TYPE,
            Arrow.STRING_TYPE
        ),
        List.of(
            SunStone.STRING_TYPE,
            Treasure.STRING_TYPE,
            Wood.STRING_TYPE
        ),
        List.of(
            SunStone.STRING_TYPE,
            Key.STRING_TYPE,
            Arrow.STRING_TYPE,
            Arrow.STRING_TYPE
        ),
        List.of(
            SunStone.STRING_TYPE,
            Key.STRING_TYPE,
            Wood.STRING_TYPE
        )
    );

    /**
     * Constructor for Sceptre
     * @param Dungeon dungeon
     * @param Pos2d position where Sceptre is located
     */
    public Sceptre(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }
    
}
