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
import dungeonmania.Utils;
import dungeonmania.Inventory;


public class OneRing extends Rare {
    public boolean active = true;

    public static String STRING_TYPE = "one_ring";

    public OneRing(Dungeon dungeon, Pos2d position) {
        super(dungeon, position);
    }

    public void selfDestruct() {
        Inventory inventory = dungeon.getInventory();
        inventory.remove(this);
    }

    @Override
    public void applyEffects() {
        //Increase Health if Player is Dead
        Dungeon dungeon = this.dungeon;
        DungeonMap map = dungeon.getMap();
        Cell playerCell = map.getPlayerCell();
        if (playerCell == null) {
            return;
        }
        List<Entity> occupants = playerCell.getOccupants();
        for (Entity occupant : occupants) {
            if (occupant instanceof Player) {
                Player player = (Player)occupant;
                System.out.println("Ran before dead check");
                if (Utils.isDead(player) == true) {
                    System.out.println("Player Dead");
                    player.setHealth(100);
                    //Remove The One Ring
                    active = false;
                    //selfDestruct();
                    return;
                } 
            }
        }

        return;
    }

    public boolean isActive() {
        return active;
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
        this.applyEffects();
    }
}
