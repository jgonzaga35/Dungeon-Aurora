package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import dungeonmania.util.Semy;

public class TestSemy {
    
    @Test
    public void testSimpleSemy() {
        Map<String, Integer> results = new HashMap<>();
        Semy<Integer> s = new Semy<Integer>();
        s.bind(n -> results.put("a", n));
        s.bind(n -> results.put("b", n));
        s.bind(n -> results.put("c", n));

        s.emit(1);
        assertEquals(Map.of("a", 1, "b", 1, "c", 1), results);

        s.bind(n -> results.put("d", n));
        s.emit(2);
        assertEquals(Map.of("a", 2, "b", 2, "c", 2, "d", 2), results);
    }
}
