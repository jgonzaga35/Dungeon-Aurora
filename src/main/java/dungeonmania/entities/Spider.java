package dungeonmania.entities;

import dungeonmania.Cell;

public class Spider extends MovingEntity {
    public Spider(Cell cell) {
        super(cell);
    }

    @Override
    public String getTypeAsString() {
        return "Spider";
    }

    @Override
    public void tick() {
    }
}
