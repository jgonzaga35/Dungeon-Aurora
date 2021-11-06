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
import dungeonmania.movement.FriendlyMovementBehaviour;
import dungeonmania.movement.RandomMovementBehaviour;
import dungeonmania.util.Direction;

/**
 * Test the which strategy gets applied when.
 * We don't detect the effect, we check the actual strategy object (white box
 * test), because otherwise it's too hard
 */
public class TestStrategies {

    private DungeonMap m;
    private Dungeon d;
    private GameMode mode;

    private Player player;
    private ZombieToast zombieToast;
    private Mercenary mercenary;
    private Mercenary bribedMercenary;
    private Spider spider;

    @Test
    public void testAllModes() {
        // I know about junit's parameterized testing but the vscode integration
        // actually makes it harder to debug, this is easier

        this.testMode(GameMode.PEACEFUL);
    }

    public void testMode(GameMode _mode) {
        this.mode = _mode;

        m = new DungeonMap(20, 20);
        d = new Dungeon("manual", this.mode, m, new ExitGoal());
        Cell cell;

        // SETUP THE MAP
        // each entity has a ring of boulders around it so that it cannot move

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
        
        // SETUP FINISHED: now we can tick without having to worry about someone getting killed
        
        this.noPotionActive();

        InvincibilityPotion invinc = new InvincibilityPotion(d, player.getPosition());
        m.getCell(player.getPosition()).addOccupant(invinc);
        d.tick(null, Direction.NONE); // player picks up the potion
        d.tick(invinc.getId(), Direction.NONE);

        this.justInvicibilityPotionActive();

        // potion runs out
        for (int i = 0; i < InvincibilityPotion.MAX_DURATION; i++) d.tick(null, Direction.NONE);

        // back to normal
        this.noPotionActive();

        // new potion
        InvisibilityPotion invis = new InvisibilityPotion(d, player.getPosition());
        m.getCell(player.getPosition()).addOccupant(invis);
        d.tick(null, Direction.NONE); // player picks up the potion
        d.tick(invis.getId(), Direction.NONE);

        // invisibility potion active
        this.justInvisibilityPotionActive();
        
        // potion runs out
        for (int i = 0; i < InvisibilityPotion.MAX_DURATION; i++) d.tick(null, Direction.NONE);

        // back to normal
        this.noPotionActive();

        // test both potions at the same time
        invis = new InvisibilityPotion(d, player.getPosition());
        m.getCell(player.getPosition()).addOccupant(invis);
        invinc = new InvincibilityPotion(d, player.getPosition());
        m.getCell(player.getPosition()).addOccupant(invinc);
        d.tick(null, Direction.NONE); // player picks up both potions
        d.tick(invis.getId(), Direction.NONE);
        d.tick(invinc.getId(), Direction.NONE);

        this.invincibilityPlusInvisibilityActive();

        assertTrue(InvincibilityPotion.MAX_DURATION < InvisibilityPotion.MAX_DURATION, "you changed potion durations, update the test");
        for (int i = 0; i < InvincibilityPotion.MAX_DURATION; i++) d.tick(null, Direction.NONE);

        // only invisbility potion left
        this.justInvisibilityPotionActive();

        for (int i = 0; i < InvisibilityPotion.MAX_DURATION - InvincibilityPotion.MAX_DURATION; i++) d.tick(null, Direction.NONE);

        // back to normal
        this.noPotionActive();
    }

    private void noPotionActive() {
        assertTrue(bribedMercenary.getCurrentMovementBehaviour() instanceof FriendlyMovementBehaviour);
        assertTrue(zombieToast.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(mercenary.getCurrentMovementBehaviour() instanceof FollowMovementBehaviour);
        assertTrue(spider.getCurrentMovementBehaviour() instanceof CircleMovementBehaviour);
        assertTrue(d.getBattleStrategy() instanceof NoBattleStrategy); // because peaceful mode
    }

    private void justInvicibilityPotionActive() {
        assertTrue(bribedMercenary.getCurrentMovementBehaviour() instanceof FriendlyMovementBehaviour);
        assertTrue(zombieToast.getCurrentMovementBehaviour() instanceof FleeMovementBehaviour);
        assertTrue(mercenary.getCurrentMovementBehaviour() instanceof FleeMovementBehaviour);
        assertTrue(spider.getCurrentMovementBehaviour() instanceof FleeMovementBehaviour);
        assertTrue(d.getBattleStrategy() instanceof WinAllBattleStrategy);
    }

    private void justInvisibilityPotionActive() {
        assertTrue(bribedMercenary.getCurrentMovementBehaviour() instanceof FriendlyMovementBehaviour); // allies can see the player when invisible
        assertTrue(zombieToast.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(mercenary.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(spider.getCurrentMovementBehaviour() instanceof CircleMovementBehaviour);
        assertTrue(d.getBattleStrategy() instanceof NoBattleStrategy);
    }

    private void invincibilityPlusInvisibilityActive() {
        assertTrue(bribedMercenary.getCurrentMovementBehaviour() instanceof FriendlyMovementBehaviour);
        assertTrue(zombieToast.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(mercenary.getCurrentMovementBehaviour() instanceof RandomMovementBehaviour);
        assertTrue(spider.getCurrentMovementBehaviour() instanceof CircleMovementBehaviour);
        assertTrue(d.getBattleStrategy() instanceof WinAllBattleStrategy);

    }
}
