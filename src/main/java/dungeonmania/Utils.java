package dungeonmania;

import java.util.List;
import java.util.Random;

/**
 * All randomness should come from this class, so that we can seed it, and have
 * reproducible tests
 */
public class Utils {
    private static Random r = new Random(1); // always start with a seed of 1 for consistency

    public static void setSeed(long s) {
        r.setSeed(s);
    }

    /**
     * @param <T> any
     * @param list
     * @return a random item from the list
     */
    public static <T> T choose(List<T> list) {
        return list.get(r.nextInt(list.size()));
    }
}
