package dungeonmania;

import java.util.List;

import dungeonmania.util.Direction;

public class Player {

    private int x;
    private int y;

    public Player(Pos2d pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    public void move(List<List<Cell>> map, Direction d) {
        if (d == Direction.NONE)
            return;

        int fromX = this.x;
        int fromY = this.y;
        if (d == Direction.LEFT) {
            // on the edge of the map
            if (this.x == 0) return;

            // blocking cell on the left
            if (map.get(this.y).get(this.x - 1).isBlocking()) return;
            
            this.x -= 1;
        } else if (d == Direction.RIGHT) {
            if (this.x == map.get(0).size() - 1) return;
            if (map.get(this.y).get(this.x + 1).isBlocking()) return;
            
            this.x += 1;
        } else if (d == Direction.UP) {
            if (this.y == 0) return;
            if (map.get(this.y-1).get(this.x).isBlocking()) return;

            this.y -= 1;
        } else if (d == Direction.DOWN) {
            if (this.y == map.size() - 1) return;
            if (map.get(this.y+1).get(this.x).isBlocking()) return;

            this.y += 1;
        } else {
            assert false;
        }

        map.get(this.y).get(this.x).onWalked(new Pos2d(fromX, fromY), new Pos2d(this.x, this.y));
    }

    public Pos2d getPos() {
        return new Pos2d(this.x, this.y);
    }
}
