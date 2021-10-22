package dungeonmania;

import java.util.stream.Stream;

import dungeonmania.DungeonManiaController.LayerLevel;
import dungeonmania.util.Direction;

public abstract class Entity {
    private String id;

    private static int nextEntityId = 1;

    protected Dungeon dungeon;
    protected Pos2d position;

    public Entity(Dungeon dungeon, Pos2d position) {
        this.id = "Entity-" + Entity.nextEntityId;
        this.dungeon = dungeon;
        this.position = position;
        
        Entity.nextEntityId++;
    }

    public Entity(Cell cell) {
        this.id = "Entity-" + Entity.nextEntityId;
        Entity.nextEntityId++;
    }

    public String getId() {
        return this.id;
    }

    /**
     * @return the cell this entity is on
     */
    public Cell getCell() {
        return dungeon.getMap().getCell(position);
    }

    public Pos2d getPosition() {
        return position;
    }
    
    /**
     * Note that it doesn't always return 4 cells. If you are on a top-most
     * cell, it will only return (left, bottom, right)
     * @return cells around the current cell
     */
    public Stream<Cell> getCellsAround() {
        // this will be easier once we get DungeonMap
        return Stream.of(
            this.dungeon.getMap().getCellAround(this.getCell(), Direction.UP),
            this.dungeon.getMap().getCellAround(this.getCell(), Direction.DOWN),
            this.dungeon.getMap().getCellAround(this.getCell(), Direction.LEFT),
            this.dungeon.getMap().getCellAround(this.getCell(), Direction.RIGHT)
        ).filter(cell -> cell != null);
    }

    public abstract boolean isInteractable();

    public abstract LayerLevel getLayerLevel();

    public abstract String getTypeAsString();

    public abstract void tick();
}