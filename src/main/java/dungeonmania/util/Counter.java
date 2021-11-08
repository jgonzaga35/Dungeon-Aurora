package dungeonmania.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Equivalent of the python class collections.Counter
 */
public class Counter<T> {
    private final Map<T, Integer> counts = new HashMap<>();

    public void add(T item, int count) {
        if (this.counts.containsKey(item)) {
            this.counts.put(item, count + this.counts.get(item));
        } else {
            this.counts.put(item, count);
        }
        counts.equals(null);
    }

    public Stream<Integer> values() {
        return this.counts.values().stream();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;

        Counter<?> otherCounter = (Counter<?>) other;
        // for some reason, Object.equals(this.counts, otherCounter.counts)
        // results in an error
        return this.counts != null && this.counts.equals(otherCounter.counts);
    }

    /**
     * Builds a counter given a list
     * 
     * For example, if you give it ["a", "b", "a", "c"], it will yield {"a": 2,
     * "b": 1, "c": 1}
     * 
     * @param <T> can be anything
     * @param iter iterable
     * @return counts
     */
    public static <T> Counter<T> from(Iterator<T> iter) {
        Counter<T> counter = new Counter<>();
        while (iter.hasNext()) {
            counter.add(iter.next(), 1);
        }
        return counter;
    }
}
