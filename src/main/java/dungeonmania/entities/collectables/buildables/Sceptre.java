package dungeonmania.entities.collectables.buildables;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Pos2d;
import dungeonmania.entities.collectables.*;

public class Sceptre extends BuildableEntity {
    public static final String STRING_TYPE = "sceptre";

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
