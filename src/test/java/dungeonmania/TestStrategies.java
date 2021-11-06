package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.battlestrategies.NoBattleStrategy;
import dungeonmania.battlestrategies.NormalBattleStrategy;
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
        this.testMode(GameMode.STANDARD);
        this.testMode(GameMode.HARD);
    }

    public void testMode(GameMode _mode) {
        this.mode = _mode;

        m = new DungeonMap(20, 20);
        d = new Dungeon("manual", this.mode, m, new ExitGoal());
        d.getRandom().setSeed(1);
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
        this.nobodyMoved();

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

        this.nobodyMoved();
    }

    /**
     * make sure that nothing moves, even when ticking
     * it's in this file because the other (main) test depends on it,
     * and it's probably going to break again (looking at the quality
     * of the movement code)
     */
    @Test
    public void testNoMovement() {
        m = new DungeonMap(20, 20);
        d = new Dungeon("manual", this.mode, m, new ExitGoal());
        d.getRandom().setSeed(1);
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
        this.nobodyMoved();
        this.noPotionActive();

        for (int i = 0; i < 100; i++)
            d.tick(null, Direction.NONE);
        this.nobodyMoved();
    }

    private void noPotionActive() {
        assertInstanceOf(FriendlyMovementBehaviour.class, bribedMercenary.getCurrentMovementBehaviour());
        assertInstanceOf(RandomMovementBehaviour.class, zombieToast.getCurrentMovementBehaviour());
        assertInstanceOf(FollowMovementBehaviour.class, mercenary.getCurrentMovementBehaviour());
        assertInstanceOf(CircleMovementBehaviour.class, spider.getCurrentMovementBehaviour());
        if (this.mode == GameMode.PEACEFUL) 
            assertInstanceOf(NoBattleStrategy.class, d.getBattleStrategy());
        else
            assertInstanceOf(NormalBattleStrategy.class, d.getBattleStrategy());
    }

    private void justInvicibilityPotionActive() {
        if (this.mode == GameMode.HARD) { // invincibility potion doesn't do anything in hard mode
            this.noPotionActive();
            return;
        }

        assertInstanceOf(FriendlyMovementBehaviour.class, bribedMercenary.getCurrentMovementBehaviour());
        assertInstanceOf(FleeMovementBehaviour.class, zombieToast.getCurrentMovementBehaviour());
        assertInstanceOf(FleeMovementBehaviour.class, mercenary.getCurrentMovementBehaviour());
        assertInstanceOf(FleeMovementBehaviour.class, spider.getCurrentMovementBehaviour());
        assertInstanceOf(WinAllBattleStrategy.class, d.getBattleStrategy());
    }

    private void justInvisibilityPotionActive() {
        assertInstanceOf(FriendlyMovementBehaviour.class, bribedMercenary.getCurrentMovementBehaviour()); // allies can see the player even when it's invisible
        assertInstanceOf(RandomMovementBehaviour.class, zombieToast.getCurrentMovementBehaviour());
        assertInstanceOf(RandomMovementBehaviour.class, mercenary.getCurrentMovementBehaviour());
        assertInstanceOf(CircleMovementBehaviour.class, spider.getCurrentMovementBehaviour());
        assertInstanceOf(NoBattleStrategy.class, d.getBattleStrategy());
    }

    private void invincibilityPlusInvisibilityActive() {
        assertInstanceOf(FriendlyMovementBehaviour.class, bribedMercenary.getCurrentMovementBehaviour());
        assertInstanceOf(RandomMovementBehaviour.class, zombieToast.getCurrentMovementBehaviour());
        assertInstanceOf(RandomMovementBehaviour.class, mercenary.getCurrentMovementBehaviour());
        assertInstanceOf(CircleMovementBehaviour.class, spider.getCurrentMovementBehaviour());
        if (this.mode == GameMode.HARD)
            assertInstanceOf(NoBattleStrategy.class, d.getBattleStrategy());
        else
            assertInstanceOf(WinAllBattleStrategy.class, d.getBattleStrategy());

    }

    private void nobodyMoved() {
        assertEquals(new Pos2d(2, 2), player.getPosition());
        assertEquals(new Pos2d(10, 10), zombieToast.getPosition(), "zombie toast out of position");
        assertEquals(new Pos2d(4, 5), mercenary.getPosition());
        assertEquals(new Pos2d(14, 5), bribedMercenary.getPosition());
        assertEquals(new Pos2d(15, 15), spider.getPosition());
    }
}
