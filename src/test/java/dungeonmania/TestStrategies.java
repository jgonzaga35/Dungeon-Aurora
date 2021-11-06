package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.battlestrategies.NoBattleStrategy;
import dungeonmania.battlestrategies.WinAllBattleStrategy;
import dungeonmania.entities.collectables.consumables.InvincibilityPotion;
import dungeonmania.entities.collectables.consumables.InvisibilityPotion;
import dungeonmania.entities.movings.Mercenary;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.Spider;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.goal.ExitGoal;
import dungeonmania.movement.CircleMovementBehaviour;
import dungeonmania.movement.FleeMovementBehaviour;
import dungeonmania.movement.FollowMovementBehaviour;
import dungeonmania.movement.RandomMovementBehaviour;
import dungeonmania.util.Direction;

/**
 * Test the which strategy gets applied when.
 * We don't detect the effect, we check the actual strategy object (white box
 * test), because otherwise it's too hard
 */
public class TestStrategies {

    @Test
    public void testPeacefulMode() {
        DungeonMap m = new DungeonMap(20, 20);
        Dungeon d = new Dungeon("manual", GameMode.PEACEFUL, m, new ExitGoal());
        Cell cell;

        Player player;
        ZombieToast zombieToast;
        Mercenary mercenary;
        Mercenary bribedMercenary;
        Spider spider;

        cell = m.getCell(2, 0);
        player = new Player(d, cell.getPosition());
        d.setPlayer(player);
        cell.addOccupant(player);
        d.tick(null, Direction.DOWN);
        d.tick(null, Direction.DOWN);
        TestUtils.lockWithBoulders(d, player.getPosition());
        // move down because the mercenaries would otherwise spawn on the player

        cell = m.getCell(10, 10);
        zombieToast = new ZombieToast(d, cell.getPosition());
        cell.addOccupant(zombieToast);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        cell = m.getCell(4, 5);
        mercenary = new Mercenary(d, cell.getPosition());
        cell.addOccupant(mercenary);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        cell = m.getCell(14, 5);
        bribedMercenary = new Mercenary(d, cell.getPosition());
        bribedMercenary.bribe();
        cell.addOccupant(bribedMercenary);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        cell = m.getCell(15, 15);
        spider = new Spider(d, cell.getPosition());
        cell.addOccupant(spider);
        TestUtils.lockWithBoulders(d, cell.getPosition());

        
        // now we can tick without having to worry about someone getting killed

        assertTrue(zombieToast.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(mercenary.getCurrentMovementBehaviour() instanceof FollowMovementBehaviour);
        assertTrue(spider.getCurrentMovementBehaviour() instanceof CircleMovementBehaviour);
        assertTrue(d.getBattleStrategy() instanceof NoBattleStrategy); // because peaceful mode

        InvincibilityPotion invinc = new InvincibilityPotion(d, player.getPosition());
        m.getCell(player.getPosition()).addOccupant(invinc);
        d.tick(null, Direction.NONE); // player picks up the potion
        d.tick(invinc.getId(), Direction.NONE);

        assertTrue(zombieToast.getCurrentMovementBehaviour() instanceof FleeMovementBehaviour);
        assertTrue(mercenary.getCurrentMovementBehaviour() instanceof FleeMovementBehaviour);
        assertTrue(spider.getCurrentMovementBehaviour() instanceof FleeMovementBehaviour);
        assertTrue(d.getBattleStrategy() instanceof WinAllBattleStrategy);

        // potion runs out
        for (int i = 0; i < InvincibilityPotion.MAX_DURATION; i++) d.tick(null, Direction.NONE);

        assertTrue(zombieToast.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(mercenary.getCurrentMovementBehaviour() instanceof FollowMovementBehaviour);
        assertTrue(spider.getCurrentMovementBehaviour() instanceof CircleMovementBehaviour);
        assertTrue(d.getBattleStrategy() instanceof NoBattleStrategy);

        // new potion
        InvisibilityPotion invis = new InvisibilityPotion(d, player.getPosition());
        m.getCell(player.getPosition()).addOccupant(invis);
        d.tick(null, Direction.NONE); // player picks up the potion
        d.tick(invis.getId(), Direction.NONE);

        assertTrue(zombieToast.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(mercenary.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(spider.getCurrentMovementBehaviour() instanceof CircleMovementBehaviour);
        assertTrue(d.getBattleStrategy() instanceof NoBattleStrategy);
        
        // potion runs out
        for (int i = 0; i < InvisibilityPotion.MAX_DURATION; i++) d.tick(null, Direction.NONE);

        assertTrue(zombieToast.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(mercenary.getCurrentMovementBehaviour() instanceof FollowMovementBehaviour);
        assertTrue(spider.getCurrentMovementBehaviour() instanceof CircleMovementBehaviour);
        assertTrue(d.getBattleStrategy() instanceof NoBattleStrategy);

        // test both potions at the same time
        invis = new InvisibilityPotion(d, player.getPosition());
        m.getCell(player.getPosition()).addOccupant(invis);
        invinc = new InvincibilityPotion(d, player.getPosition());
        m.getCell(player.getPosition()).addOccupant(invinc);
        d.tick(null, Direction.NONE); // player picks up both potions
        d.tick(invis.getId(), Direction.NONE);
        d.tick(invinc.getId(), Direction.NONE);

        assertTrue(zombieToast.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(mercenary.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(spider.getCurrentMovementBehaviour() instanceof CircleMovementBehaviour);
        assertTrue(d.getBattleStrategy() instanceof WinAllBattleStrategy);
    }
}
