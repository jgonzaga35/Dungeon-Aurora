package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
import dungeonmania.entities.Spider;
import dungeonmania.entities.movings.Player;
import dungeonmania.entities.movings.ZombieToast;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TestBattle {
    @Test
    public void testSimpleZombieBattle() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_force_zombie_attack", GameMode.STANDARD.getValue());

        // there are no spiders because the map is too small

        ctr.tick("", Direction.NONE);

        // doing the maths to compute that number is a pain because of the damage formula
        int player_kills_n_zombies = 7;
        
        for (int j = 0; j < player_kills_n_zombies; j++) { 
            for (int i = 0; i < 19; i++) {
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                resp = ctr.tick("", Direction.NONE);
            }
            assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));

            // the zombie no *has* to move on the player's cell, which causes a battle, and the zombie dies
            resp = ctr.tick("", Direction.NONE);

            if (j == player_kills_n_zombies - 1) {
                // the player has been killed
                assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                assertEquals(0, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
            } else {
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
            }
        }
    }

    @Test
    public void testSimpleSpiderBattle() {
        DungeonManiaController ctr = new DungeonManiaController();
        DungeonResponse resp = ctr.newGame("_force_spider_battles", GameMode.STANDARD.getValue());

        // the only place where the spider can spawn is exactly on the spot
        // where the player is

        ctr.tick("", Direction.NONE);

        int player_kills_n_spiders = 100;
        
        for (int j = 0; j < player_kills_n_spiders; j++) {
            resp = ctr.tick("", Direction.NONE);
            resp.getEntities().forEach(e -> System.out.println(e.getType() + " " + e.getPosition()));

            if (j == player_kills_n_spiders - 1) {
                // the player has been killed
                assertEquals(1, TestUtils.countEntitiesOfType(resp, Spider.STRING_TYPE));
                assertEquals(0, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
            } else {
                assertEquals(0, TestUtils.countEntitiesOfType(resp, Spider.STRING_TYPE), "player is still alive after " + j + " spiders");
                assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
            }
        }
    }
}
