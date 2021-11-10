package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import dungeonmania.GenerateMaze.BCell;

public class TestGenerateMaze {
    @Test
    public void testAlwaysFindPath() {
        Random r = new Random(1);
        Pos2d dim = new Pos2d(50, 50);
        for (int i = 0; i < 100; i++) {
            r.setSeed(i); // easier to debug stuff

            // x2 to get only odd numbers
            Pos2d start = new Pos2d(1 + r.nextInt(dim.getX()/2 - 2)*2, 1 + r.nextInt(dim.getY()/2 - 2)*2);
            Pos2d end = new Pos2d(1 + r.nextInt(dim.getX()/2 - 2)*2, 1 + r.nextInt(dim.getY()/2 - 2)*2);

            r.setSeed(i); // again, easier to debug
            GenerateMaze gen = new GenerateMaze(r, dim, start, end);
            assertTrue(hasPath(gen.build(), start, end), "iteration=" + i);
            gen.print();
        }
    }

    private boolean hasPath(List<List<BCell>> maze, Pos2d start, Pos2d end) {

        // bfs through the maze
        Set<Pos2d> visited = new HashSet<>();
        Deque<Pos2d> q = new LinkedList<Pos2d>();
        q.add(start);

        while (q.size() > 0) {
            Pos2d head = q.poll();
            if (visited.contains(head)) continue;
            if (head.equals(end)) {
                return true;
            }
            visited.add(head);

            neighbours(head)
                // within the maze
                .filter(p -> 0 <= p.getX() && p.getX() < maze.get(0).size() && 0 <= p.getY() && p.getY() < maze.size())
                // not a wall
                .filter(p -> maze.get(p.getY()).get(p.getX()) == BCell.EMPTY)
                // not already visited
                .filter(p -> !visited.contains(p))
                .forEach(q::add);
        }

        return false;
    }

    private static Stream<Pos2d> neighbours(Pos2d center) {
        return Stream.of(
            new Pos2d(center.getX(), center.getY()-1),
            new Pos2d(center.getX(), center.getY()+1),
            new Pos2d(center.getX()-1, center.getY()),
            new Pos2d(center.getX()+1, center.getY())
        );
    }
}
