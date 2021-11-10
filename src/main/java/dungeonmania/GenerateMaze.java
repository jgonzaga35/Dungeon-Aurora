package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * this could be implemented as a single function, but because using streams
 * makes everything so readable (and java doesn't have closures), I use a class.
 */
public class GenerateMaze {

    /**
     * Binary cell, either a wall or empty
     */
    public enum BCell {
        WALL, EMPTY
    }

    private Random r;
    private Pos2d dims;
    private Pos2d start;
    private Pos2d end;
    private List<List<BCell>> rows;

    public static List<List<BCell>> make(Random r, Pos2d dimensions, Pos2d start, Pos2d end) {
        GenerateMaze rp = new GenerateMaze(r, dimensions, start, end);
        return rp.build();
    }

    public GenerateMaze(Random r, Pos2d dimensions, Pos2d start, Pos2d end) {
        this.r = r;
        this.dims = dimensions;
        this.start = start;
        this.end = end;

        assert this.notOnBoundary(start) : "start is on the boundary";
        assert this.notOnBoundary(end) : "end is on the boundary";

        // full with walls
        rows = new ArrayList<>(dims.getY());
        for (int y = 0; y < dims.getY(); y++) {
            rows.add(new ArrayList<>(dims.getX()));
            for (int x = 0; x < dims.getX(); x++) {
                rows.get(y).add(BCell.WALL);
            }
        }
    }

    private static Stream<Pos2d> farNeighbours(Pos2d center) {
        return Stream.of(
            new Pos2d(center.getX(), center.getY()-2),
            new Pos2d(center.getX(), center.getY()+2),
            new Pos2d(center.getX()-2, center.getY()),
            new Pos2d(center.getX()+2, center.getY())
        );
    }

    private boolean isInMaze(Pos2d pos) {
        return 0 <= pos.getX() && pos.getX() < dims.getX() && 0 <= pos.getY() && pos.getY() < dims.getY();
    }

    private boolean isWall(Pos2d pos) {
        assert isInMaze(pos);
        return get(pos) == BCell.WALL;
    }
    private boolean isEmpty(Pos2d pos) {
        assert isInMaze(pos);
        return get(pos) == BCell.EMPTY;
    }

    private boolean notOnBoundary(Pos2d pos) {
        return 1 <= pos.getX() && pos.getX() < dims.getX() - 1 && 1 <= pos.getY() && pos.getY() < dims.getY() - 1;
    }

    private void set(Pos2d pos, BCell v) {
        assert rows != null;
        rows.get(pos.getY()).set(pos.getX(), v);
    }

    private BCell get(Pos2d pos) {
        return rows.get(pos.getY()).get(pos.getX());
    }

    private Pos2d average(Pos2d a, Pos2d b) {
        return new Pos2d((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
    }

    public List<List<BCell>> build() {
        // this doesn't follow the spec to the letter because the spec keeps on
        // changing, being silly, containing typos, or any combinations of the
        // three. I understand what Braedon is trying to specify, and that's
        // enough.


        set(start, BCell.EMPTY);

        List<Pos2d> options = new ArrayList<>();
        farNeighbours(start)
            .filter(this::isInMaze)
            .filter(this::isWall)
            .filter(this::notOnBoundary)
            .forEach(options::add);

        
        while (options.size() > 0) {
            Pos2d next = Utils.choose(options, r);
            boolean removed = options.remove(next);
            assert removed;

            // connect to existing empty cell
            Pos2d connection = Utils.choose(
                farNeighbours(next)
                    .filter(this::isInMaze)
                    .filter(this::isEmpty)
                    .collect(Collectors.toList()),
                r);
            
            set(next, BCell.EMPTY);
            set(average(next, connection), BCell.EMPTY); // empty out the cell between connection and next

            // new neighbours to explore!
            farNeighbours(next)
                .filter(this::isInMaze)
                .filter(this::isWall)
                .forEach(options::add);
        }

        return rows;
    }

    public void print() {
        for (int y = 0; y < dims.getY(); y++) {
            for (int x = 0; x < dims.getX(); x++) {
                if (rows.get(y).get(x) == BCell.EMPTY) {
                    System.out.print(" ");
                } else {
                    System.out.print("#");
                }
            }
            System.out.println();
        }
    }
}
