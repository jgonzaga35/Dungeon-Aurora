package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dungeonmania.entities.statics.Wall;
import dungeonmania.entities.statics.ZombieToastSpawner;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class TestUtils {
    public static Position getPlayerPosition(DungeonResponse resp) {
        for (EntityResponse e : resp.getEntities()) {
            if (Objects.equals(e.getType(), "player")) {
                return e.getPosition();
            }
        }
        throw new Error("player wasn't found");
    }

    /**
     * asserts that as and bs contain the same element
     * @param <T>
     * @param as
     * @param bs
     */
    public static <T extends Comparable<T>> void assertEqualsUnordered(List<T> as, List<T> bs) {
        Collections.sort(as);
        Collections.sort(bs);
        assertEquals(as, bs);
    }


    public static boolean isBlocking(EntityResponse resp) {
        return List.of(Wall.STRING_TYPE, ZombieToastSpawner.STRING_TYPE).contains(resp.getType());
    }

    public static long countEntitiesOfType(DungeonResponse resp, String type) {
        return resp.getEntities().stream().filter(e -> Objects.equals(e.getType(), type)).count();
    }
}
