package dungeonmania;

import java.util.List;
import java.util.Random;
import java.util.Set;

import dungeonmania.entities.Fighter;

/**
 * A class containing helper methods.
 */
public class Utils {
    /**
     * Everything smaller than that is zero
     */
    public static float eps = 0.001f;

    /**
     * @pre {@code list.size() > 0}
     * 
     * @param <T>  any
     * @param list
     * @param r    Random instance
     * @return a random item from the list
     */
    public static <T> T choose(List<T> list, Random r) {
        assert list.size() > 0;
        return list.get(r.nextInt(list.size()));
    }

    /**
     * @pre {@code set.size() > 0}
     * 
     * @param <T> any
     * @param set
     * @param r   Random instance
     * @return a random item from the set
     */
    public static <T> T choose(Set<T> set, Random r) {
        assert set.size() > 0;
        return set.stream().skip(r.nextInt(set.size())).findFirst().get();
    }

    /**
     * Because health is a float, we *might* (probably wont) get values smaller that
     * machine precision. And so, we'll have to do health < eps = 2.2E-16
     * 
     * Just in case, I'm writing a helper so that it's easy to fix.
     * 
     * (example where this might happen: fire effect. It divides your health by 2
     * every second. That geometric serie never reaches 0, where do you draw the
     * line? This would be the role of this helper)
     * 
     * @param fighter
     * @return true if the fighter is dead
     */
    public static boolean isDead(Fighter fighter) {
        return fighter.getHealth() < Utils.eps;
    }

    /**
     * Float arithmetic is annoying. x - x can be != 0, because finite machine
     * precision. Not only is it annoying, but it might not be reproducible. And I
     * don't want to debug tests between machines because float arithmetic is
     * slightly different.
     * 
     * Allows to do {@code Comparator sort = (a, b) -> Utils.compareFloat(a.v -
     * b.v);} when you would want to do {@code Comparator sort (a, b) -> a.v - b.v}
     * 
     * @param f
     * @return
     */
    public static int compareFloat(float f) {
        if (Math.abs(f) < Utils.eps)
            return 0;
        else if (f > 0)
            return 1;
        else
            return -1;
    }

}
