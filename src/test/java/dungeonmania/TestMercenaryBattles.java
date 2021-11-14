package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

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
        Dungeon dungeon = new Dungeon(new Random(1), "manual", GameMode.STANDARD, map, new ExitGoal());
        dungeon.getRandom().setSeed(1);
        TestUtils.spawnWall(dungeon, 7, 6);
        TestUtils.spawnWall(dungeon, 8, 5);
        TestUtils.spawnWall(dungeon, 7, 4);

        Player player = new Player(dungeon, new Pos2d(5, 5));
        Mercenary ally = new Mercenary(dungeon, new Pos2d(5, 7));
        ally.bribe();
        // two cell right of the player. The zombie is then going to move
        ZombieToast zombie = new ZombieToast(dungeon, new Pos2d(7, 5));
        List.of(player, ally, zombie).forEach(e -> map.getCell(e.getPosition()).addOccupant(e));
        dungeon.setPlayer(player);

        Pos2d battlePosition = new Pos2d(6, 5);

        // the ally has to fight
        assertTrue(
                ally.getPosition().squareDistance(battlePosition) <= Mercenary.BATTLE_RADIUS * Mercenary.BATTLE_RADIUS);

        float prevAllyHealth = ally.getHealth();
        float prevPlayerHealth = player.getHealth();

        TestUtils.clearEnemyInventories(dungeon);
        dungeon.tick(null, Direction.RIGHT);

        assertTrue(Utils.isDead(zombie));
        assertTrue(ally.getHealth() < prevAllyHealth); // make sure the mercenary fought
        assertTrue(player.getHealth() < prevPlayerHealth); // make sure the player fought

        // make sure that the mercenary goes back to where he was after the
        // battle, because he fought from a distance
        assertTrue(ally.getPosition().squareDistance(new Pos2d(5, 7)) <= 1);
        assertEquals(player.getPosition(), battlePosition);
    }

    @Test
    public void testAllyMercenaryOutOfRange() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon dungeon = new Dungeon(new Random(1), "manual", GameMode.STANDARD, map, new ExitGoal());
        dungeon.getRandom().setSeed(1);
        TestUtils.spawnWall(dungeon, 7, 6);
        TestUtils.spawnWall(dungeon, 8, 5);
        TestUtils.spawnWall(dungeon, 7, 4);

        Player player = new Player(dungeon, new Pos2d(5, 5));
        Mercenary ally = new Mercenary(dungeon, new Pos2d(5, 9));
        ally.bribe();
        // two cell right of the player. The zombie is then going to move
        ZombieToast zombie = new ZombieToast(dungeon, new Pos2d(7, 5));
        List.of(player, ally, zombie).forEach(e -> map.getCell(e.getPosition()).addOccupant(e));
        dungeon.setPlayer(player);

        Pos2d battlePosition = new Pos2d(6, 5);

        // the ally has to fight
        assertTrue(
                ally.getPosition().squareDistance(battlePosition) > Mercenary.BATTLE_RADIUS * Mercenary.BATTLE_RADIUS);

        float prevAllyHealth = ally.getHealth();
        float prevPlayerHealth = player.getHealth();

        TestUtils.clearEnemyInventories(dungeon);
        dungeon.tick(null, Direction.RIGHT);

        assertTrue(Utils.isDead(zombie));
        assertEquals(prevAllyHealth, ally.getHealth(), Utils.eps); // make sure the mercenary did NOT fight
        assertTrue(player.getHealth() < prevPlayerHealth); // make sure the player fought

        assertEquals(player.getPosition(), battlePosition);
        assertTrue(ally.getPosition().squareDistance(new Pos2d(5, 9)) <= 1); // make sure the mercenary didn't teleport
    }

    /**
     * make sure the mercenary doesn't fight for you if he isn't bribed
     */
    @Test
    public void testEnemyMercenaryInRange() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon dungeon = new Dungeon(new Random(1), "manual", GameMode.STANDARD, map, new ExitGoal());
        dungeon.getRandom().setSeed(1);
        TestUtils.spawnWall(dungeon, 7, 6);
        TestUtils.spawnWall(dungeon, 8, 5);
        TestUtils.spawnWall(dungeon, 7, 4);

        Player player = new Player(dungeon, new Pos2d(5, 5));
        Mercenary ally = new Mercenary(dungeon, new Pos2d(5, 7));
        // two cell right of the player. The zombie is then going to move
        ZombieToast zombie = new ZombieToast(dungeon, new Pos2d(7, 5));
        List.of(player, ally, zombie).forEach(e -> map.getCell(e.getPosition()).addOccupant(e));
        dungeon.setPlayer(player);

        Pos2d battlePosition = new Pos2d(6, 5);

        // the ally has to fight
        assertTrue(
                ally.getPosition().squareDistance(battlePosition) <= Mercenary.BATTLE_RADIUS * Mercenary.BATTLE_RADIUS);

        float prevAllyHealth = ally.getHealth();
        float prevPlayerHealth = player.getHealth();

        TestUtils.clearEnemyInventories(dungeon);
        dungeon.tick(null, Direction.RIGHT);

        assertTrue(Utils.isDead(zombie));
        assertEquals(prevAllyHealth, ally.getHealth(), Utils.eps); // make sure the mercenary did NOT fight
        assertTrue(player.getHealth() < prevPlayerHealth); // make sure the player fought

        // make sure that the mercenary goes back to where he was after the
        // battle, because he fought from a distance
        assertTrue(ally.getPosition().squareDistance(new Pos2d(5, 7)) <= 1);
        assertEquals(player.getPosition(), battlePosition);
    }

    @Test
    public void testMultipleAllyMercenariesInRange() {
        DungeonMap map = new DungeonMap(10, 10);
        Dungeon dungeon = new Dungeon(new Random(1), "manual", GameMode.STANDARD, map, new ExitGoal());
        dungeon.getRandom().setSeed(1);
        TestUtils.spawnWall(dungeon, 7, 6);
        TestUtils.spawnWall(dungeon, 8, 5);
        TestUtils.spawnWall(dungeon, 7, 4);

        Player player = new Player(dungeon, new Pos2d(5, 5));
        Mercenary ally1 = new Mercenary(dungeon, new Pos2d(6, 6));
        ally1.bribe();
        Mercenary ally2 = new Mercenary(dungeon, new Pos2d(5, 7));
        ally2.bribe();
        // two cell right of the player. The zombie is then going to move
        ZombieToast zombie = new ZombieToast(dungeon, new Pos2d(7, 5));
        List.of(player, ally1, ally2, zombie).forEach(e -> map.getCell(e.getPosition()).addOccupant(e));
        dungeon.setPlayer(player);

        Pos2d battlePosition = new Pos2d(6, 5);

        // the ally has to fight
        assertTrue(ally2.getPosition().squareDistance(battlePosition) <= Mercenary.BATTLE_RADIUS
                * Mercenary.BATTLE_RADIUS);

        float prevAlly1Health = ally1.getHealth();
        float prevAlly2Health = ally2.getHealth();
        float prevPlayerHealth = player.getHealth();

        TestUtils.clearEnemyInventories(dungeon);
        dungeon.tick(null, Direction.RIGHT);

        assertTrue(Utils.isDead(zombie));
        assertTrue(ally1.getHealth() < prevAlly1Health); // make sure the mercenary fought
        assertTrue(ally2.getHealth() < prevAlly2Health); // make sure the mercenary fought
        assertTrue(player.getHealth() < prevPlayerHealth); // make sure the player fought

        // make sure that the mercenary goes back to where he was after the
        // battle, because he fought from a distance
        assertTrue(ally2.getPosition().squareDistance(new Pos2d(5, 7)) <= 1);
        assertEquals(player.getPosition(), battlePosition);
    }

}
