package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController.GameMode;
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
        
        for (int j = 0; j < player_kills_n_zombies; j++) { // a player can sustain 10 zombie attacks before dying
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
}
