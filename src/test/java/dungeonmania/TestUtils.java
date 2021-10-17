package dungeonmania;

import java.util.Objects;

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
}
