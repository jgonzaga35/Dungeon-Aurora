// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.statics;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.StaticEntity;

public class Wall extends StaticEntity {

    public static String STRING_TYPE = "wall";

    public Wall(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }

    @Override
    public String getTypeAsString() {
        return Wall.STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public void tick() {
    }
}
