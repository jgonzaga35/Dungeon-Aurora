package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.collectables.Armour;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.buildables.Bow;
import dungeonmania.entities.collectables.buildables.Shield;
import dungeonmania.entities.movings.Hydra;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.Spider;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

/**
 * Unfortunately, blackbox testing is pretty hard.
 * Here's the pattern of those tests.
 * 
 * Make a tiny map so that zombies are forced to move somewhere.
 * Move the player there.
 * Battle.
 * Repeat, until the player dies.
 */
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

        int player_kills_n_spiders = 16;
        
        for (int j = 0; j < player_kills_n_spiders; j++) {
            for (int i = 0; i < Spider.SPAWN_EVERY_N_TICKS; i++) {
                resp = ctr.tick(null, Direction.NONE);
            }
            if (j == player_kills_n_spiders - 1) {
                // the player has been killed
                assertEquals(1, TestUtils.countEntitiesOfType(resp, Spider.STRING_TYPE), "j=" + j);
                assertEquals(0, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
            } else {
                assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE), "player is still alive after " + j + " spiders");
                assertEquals(0, TestUtils.countEntitiesOfType(resp, Spider.STRING_TYPE));
            }
        }
    }

    @Test
    public void testSimpleHydraBattle() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_force_hydra_attack", GameMode.HARD.getValue());
        
        // spawn the hydra
        for (int i = 0; i < Hydra.SPAWN_EVERY_N_TICKS; i++) {
            resp = ctr.tick(null, Direction.NONE);
        }
        Position p = resp.getEntities().stream().filter(er -> er.getType().equals(Hydra.STRING_TYPE)).findFirst().get().getPosition();
        assertEquals(0, p.getX());
        assertEquals(1, p.getY());

        assertEquals(1, TestUtils.countEntitiesOfType(resp, Hydra.STRING_TYPE));
        assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));

        // In this tick, the hydra moves onto the player at (0,2)
        resp = ctr.tick(null, Direction.NONE);
        
        // Now the hydra should be on the player's cell
        try {
            p = resp.getEntities().stream().filter(er -> er.getType().equals(Hydra.STRING_TYPE)).findFirst().get().getPosition();
            assertEquals(1, TestUtils.countEntitiesOfType(resp, Hydra.STRING_TYPE));
            assertEquals(0, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
        } catch (NoSuchElementException e) {
            // if there is no hydra it means, the player has killed it successfully
            assertEquals(0, TestUtils.countEntitiesOfType(resp, Hydra.STRING_TYPE));
            assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
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

        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        assertDoesNotThrow(() -> {
            ctr.build(Shield.STRING_TYPE);
        });

        // more than the player can kill without a shield
        int player_kills_n_zombies = 14;
        
        for (int j = 0; j < player_kills_n_zombies; j++) { 
            for (int i = 0; i < 19 - (j == 0 ? 2 : 0); i++) {
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                resp = ctr.tick(null, Direction.NONE);
            }
            assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));

            // the zombie no *has* to move on the player's cell, which causes a battle, and the zombie dies
            resp = ctr.tick(null, Direction.NONE);

            boolean hasShield = resp.getInventory().stream().anyMatch(ir -> ir.getType().equals(Shield.STRING_TYPE));
            if (j < 6) assertTrue(hasShield, "j=" + j);
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

    @Test
    public void testSimpleZombieBattleWithSword() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_sword_battle", GameMode.STANDARD.getValue());

        // there are no spiders because the map is too small

        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);

        // more than the player can kill without a sword
        int player_kills_n_zombies = 16;
        
        for (int j = 0; j < player_kills_n_zombies; j++) { 
            for (int i = 0; i < 19 - (j == 0 ? 2 : 0); i++) {
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                resp = ctr.tick(null, Direction.NONE);
            }
            assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));

            // the zombie no *has* to move on the player's cell, which causes a battle, and the zombie dies
            resp = ctr.tick(null, Direction.NONE);

            boolean hasSword = resp.getInventory().stream().anyMatch(ir -> ir.getType().equals(Sword.STRING_TYPE));
            if (j < 13) assertTrue(hasSword, "j=" + j);
            else assertTrue(!hasSword);

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
    public void testSimpleZombieBattleWithArmour() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_armour_battle", GameMode.STANDARD.getValue());

        // there are no spiders because the map is too small

        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);

        // more than the player can kill without an armour
        int player_kills_n_zombies = 15;
        
        for (int j = 0; j < player_kills_n_zombies; j++) { 
            for (int i = 0; i < 19 - (j == 0 ? 2 : 0); i++) {
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                resp = ctr.tick(null, Direction.NONE);
            }
            assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));

            // the zombie no *has* to move on the player's cell, which causes a battle, and the zombie dies
            resp = ctr.tick(null, Direction.NONE);

            boolean hasArmour = resp.getInventory().stream().anyMatch(ir -> ir.getType().equals(Armour.STRING_TYPE));
            if (j < 9) assertTrue(hasArmour, "j=" + j);
            else assertTrue(!hasArmour);

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
    public void testSimpleZombieBattleWithBow() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_bow_battle", GameMode.STANDARD.getValue());

        // there are no spiders because the map is too small

        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        ctr.tick(null, Direction.RIGHT);
        assertDoesNotThrow(() -> {
            ctr.build(Bow.STRING_TYPE);
        });

        // more than the player can kill without a bow
        int player_kills_n_zombies = 15;
        
        for (int j = 0; j < player_kills_n_zombies; j++) { 
            for (int i = 0; i < 19 - (j == 0 ? 3 : 0); i++) {
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                resp = ctr.tick(null, Direction.NONE);
            }
            assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));

            // the zombie no *has* to move on the player's cell, which causes a battle, and the zombie dies
            resp = ctr.tick(null, Direction.NONE);

            boolean hasBow = resp.getInventory().stream().anyMatch(ir -> ir.getType().equals(Bow.STRING_TYPE));
            if (j < 24) assertTrue(hasBow, "j=" + j);
            else assertTrue(!hasBow);

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

    public void testZombieBattleWithOneRing() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_force_zombie_attack_with_one_ring", GameMode.STANDARD.getValue());

        // there are no spiders because the map is too small

        ctr.tick(null, Direction.NONE);

        // doing the maths to compute that number is a pain because of the damage formula
        int player_kills_n_zombies = 10 * 2; // double, because the player respawns
        
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
}
