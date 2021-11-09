package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.goal.ExitGoal;
import dungeonmania.util.Direction;

public class TestMercenaryBattles {

    @Test
    public void testAllyMercenaryInRange() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon dungeon = new Dungeon("manual", GameMode.STANDARD, map, new ExitGoal());
        dungeon.getRandom().setSeed(1);

        Player player = new Player(dungeon, new Pos2d(5, 5));
        Mercenary ally = new Mercenary(dungeon, new Pos2d(5, 7));
        ally.bribe();
        // two cell right of the player. The zombie is then going to move
        ZombieToast zombie = new ZombieToast(dungeon, new Pos2d(7, 5));
        List.of(player, ally, zombie).forEach(e -> map.getCell(e.getPosition()).addOccupant(e));
        dungeon.setPlayer(player);

        Pos2d battlePosition = new Pos2d(6, 5);

        // the ally has to fight
        assertTrue(ally.getPosition().squareDistance(battlePosition) <= Mercenary.BATTLE_RADIUS * Mercenary.BATTLE_RADIUS);

        float prevAllyHealth = ally.getHealth();
        float prevPlayerHealth = player.getHealth();

        dungeon.tick(null, Direction.RIGHT);

        assertTrue(Utils.isDead(zombie));
        assertTrue(ally.getHealth() < prevAllyHealth); // make sure the mercenary fought
        assertTrue(player.getHealth() < prevPlayerHealth); // make sure the player fought

        // make sure that the mercenary goes back to where he was after the
        // battle, because he fought from a distance
        assertTrue(ally.getPosition().squareDistance(new Pos2d(5, 7)) <= 1);
        assertEquals(player.getPosition(), battlePosition);
    }

}
