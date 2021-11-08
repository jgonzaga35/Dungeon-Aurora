// java doesn't support static folders, since it's a folder, so we use the plural
package dungeonmania.entities.collectables.rare;

import dungeonmania.Dungeon;
import dungeonmania.Pos2d;
import dungeonmania.entities.CollectableEntity;
import dungeonmania.Cell;
import dungeonmania.Entity;
import dungeonmania.entities.movings.Player;

import java.util.List;

import dungeonmania.DungeonMap;


public class OneRing extends Rare {

    public static String STRING_TYPE = "one_ring";

    public OneRing(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    @Override
    public void applyEffects() {
        //Increase Health
        Dungeon dungeon = this.dungeon;
        DungeonMap map = dungeon.getMap();
        Cell playerCell = map.getPlayerCell();
        List<Entity> occupants = playerCell.getOccupants();
        for (Entity occupant : occupants) {
            if (occupant instanceof Player) {
                Player player = (Player)occupant;
                player.setHealth(100); 
            }
        }

        return;
    }

    @Override
    public void expire() {
        return;
    }

    @Override
    public String getTypeAsString() {
        return STRING_TYPE;
    }

    @Override
    public boolean isInteractable() {
        return false; // i don't think so at least
    }

    @Override
    public void tick() {
        System.out.println("One Ring tick ran");
    }
}
