package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.buildables.Shield;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.Spider;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class TestBattle {
    @Test
    public void testSimpleZombieBattle() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_force_zombie_attack", GameMode.STANDARD.getValue());

        // there are no spiders because the map is too small

        ctr.tick(null, Direction.NONE);

        // doing the maths to compute that number is a pain because of the damage formula
        int player_kills_n_zombies = 10;
        
        for (int j = 0; j < player_kills_n_zombies; j++) { 
            for (int i = 0; i < 19; i++) {
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                resp = ctr.tick(null, Direction.NONE);
            }
            assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));

            // the zombie no *has* to move on the player's cell, which causes a battle, and the zombie dies
            resp = ctr.tick(null, Direction.NONE);

            if (j == player_kills_n_zombies - 1) {
                // the player has been killed
                assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                assertEquals(0, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
            } else {
                assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE), "player is still alive after " + j + " zombies");
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            }
        }
    }

    @Test
    public void testSimpleSpiderBattle() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_force_spider_battles", GameMode.STANDARD.getValue());

        // the only place where the spider can spawn is exactly on the spot
        // where the player is

        ctr.tick(null, Direction.NONE);

        int player_kills_n_spiders = 15;
        
        for (int j = 0; j < player_kills_n_spiders; j++) {
            resp = ctr.tick(null, Direction.NONE);
            resp.getEntities().forEach(e -> System.out.println(e.getType() + " " + e.getPosition()));

            if (j == player_kills_n_spiders - 1) {
                // the player has been killed
                assertEquals(1, TestUtils.countEntitiesOfType(resp, Spider.STRING_TYPE));
                assertEquals(0, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
            } else {
                assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE), "player is still alive after " + j + " spiders");
                assertEquals(0, TestUtils.countEntitiesOfType(resp, Spider.STRING_TYPE));
            }
        }
    }

    /**
     * case when the zombie goes onto the player's cell, and the player onto the
     * zombie's (they swap). There is no battle.
     */
    @Test
    public void testBattleSwapCase() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_force_zombie_attack", GameMode.STANDARD.getValue());

        // there are no spiders because the map is too small

        ctr.tick(null, Direction.NONE);

        for (int i = 0; i < 19; i++) {
            assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            resp = ctr.tick(null, Direction.NONE);
        }
        assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
        assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));

        resp = ctr.tick(null, Direction.UP);

        // the zombie no *has* to move on the player's cell (ie. down)
        // BUT, the player moves up
        // visually:
        //
        // time=1   time=2
        // z        p
        // p        z

        assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE), "player should still be alive");
        assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE), "zombie should still be alive");
        Position p = TestUtils.getPlayerPosition(resp);
        assertEquals(0, p.getX());
        assertEquals(1, p.getY());

        p = resp.getEntities().stream().filter(er -> er.getType().equals(ZombieToast.STRING_TYPE)).findFirst().get().getPosition();
        assertEquals(0, p.getX());
        assertEquals(2, p.getY());
    }

    @Test
    public void testSimpleZombieBattleWithShield() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_shield_battle", GameMode.STANDARD.getValue());

        // there are no spiders because the map is too small

        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        ctr.tick("", Direction.RIGHT);
        assertDoesNotThrow(() -> {
            ctr.build(Shield.STRING_TYPE);
        });

        // more than the player can kill without a shield
        int player_kills_n_zombies = 14;
        
        for (int j = 0; j < player_kills_n_zombies; j++) { 
            for (int i = 0; i < 19 - (j == 0 ? 2 : 0); i++) {
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                resp = ctr.tick("", Direction.NONE);
            }
            assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));

            // the zombie no *has* to move on the player's cell, which causes a battle, and the zombie dies
            resp = ctr.tick("", Direction.NONE);

            boolean hasShield = resp.getInventory().stream().anyMatch(ir -> ir.getType().equals(Shield.STRING_TYPE));
            if (j < 10) assertTrue(hasShield);
            else assertTrue(!hasShield);

            if (j == player_kills_n_zombies - 1) {
                // the player has been killed
                assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                assertEquals(0, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
            } else {
                assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE), "player is still alive after " + j + " zombies");
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            }
        }
    }
}
