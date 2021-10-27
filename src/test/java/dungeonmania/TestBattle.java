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

        for (int j = 0; j < 1; j++) { // a player can sustain 10 zombie attacks before dying
            for (int i = 0; i < 20; i++) {
                assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
                resp = ctr.tick("", Direction.NONE);
            }
            assertEquals(1, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
            assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));

            // the zombie no *has* to move on the player's cell, which causes a battle, and the zombie dies
            resp = ctr.tick("", Direction.NONE);
            assertEquals(0, TestUtils.countEntitiesOfType(resp, ZombieToast.STRING_TYPE));
        }
        // assertEquals(1, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
        // then the player is dead, the game is finished
        assertEquals(0, TestUtils.countEntitiesOfType(resp, Player.STRING_TYPE));
    }
}
