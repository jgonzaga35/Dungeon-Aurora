package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        }
    }

    /**
     * shouldn't get any wall that isn't connected to another wall
     * same thing for empty cells
     */
    @Test
    public void testNoSingleWall() {
        Random r = new Random(1);
        Pos2d dim = new Pos2d(50, 50);
        for (int i = 0; i < 100; i++) {
            r.setSeed(i); // easier to debug stuff

            // x2 to get only odd numbers
            Pos2d start = new Pos2d(1 + r.nextInt(dim.getX()/2 - 2)*2, 1 + r.nextInt(dim.getY()/2 - 2)*2);
            Pos2d end = new Pos2d(1 + r.nextInt(dim.getX()/2 - 2)*2, 1 + r.nextInt(dim.getY()/2 - 2)*2);

            r.setSeed(i); // again, easier to debug
            GenerateMaze gen = new GenerateMaze(r, dim, start, end);
            List<List<BCell>> maze = gen.build();
            
            for (int y = 0; y < dim.getY(); y++) {
                for (int x = 0; x < dim.getX(); x++) {
                    // make sure that we have a neighbour of the same type
                    BCell type = maze.get(y).get(x);
                    assertTrue(
                        neighbours(new Pos2d(x, y))
                        .filter(p -> 0 <= p.getX() && p.getX() < maze.get(0).size() && 0 <= p.getY() && p.getY() < maze.size())
                        .anyMatch(p -> maze.get(p.getY()).get(p.getX()) == type)
                    , "fail on: " + new Pos2d(x, y));
                }
            }
        }
    }

    /**
     * make sure that all the walls around
     */
    @Test
    public void cleanBoundary() {
        Random r = new Random(1);
        Pos2d dim = new Pos2d(50, 50);
        for (int i = 0; i < 100; i++) {
            r.setSeed(i); // easier to debug stuff

            // x2 to get only odd numbers
            Pos2d start = new Pos2d(1 + r.nextInt(dim.getX()/2 - 2)*2, 1 + r.nextInt(dim.getY()/2 - 2)*2);
            Pos2d end = new Pos2d(1 + r.nextInt(dim.getX()/2 - 2)*2, 1 + r.nextInt(dim.getY()/2 - 2)*2);

            r.setSeed(i); // again, easier to debug
            GenerateMaze gen = new GenerateMaze(r, dim, start, end);
            List<List<BCell>> maze = gen.build();
            
            for (int y = 0; y < dim.getY(); y++) {
                assertEquals(BCell.WALL, maze.get(y).get(0));
                assertEquals(BCell.WALL, maze.get(y).get(dim.getX() - 1));
            }
            for (int x = 0; x < dim.getX(); x++) {
                assertEquals(BCell.WALL, maze.get(x).get(0));
                assertEquals(BCell.WALL, maze.get(x).get(dim.getY() - 1));
            }
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
