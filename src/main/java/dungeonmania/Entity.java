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

    private void setPosition(int XCoord, int YCoord) {
        this.position.setX(XCoord);
        this.position.setY(YCoord);
    }

    public void updatePosition(int XCoord, int YCoord) {
        setPosition(XCoord, YCoord);
        return;
    }
    public Cell inspectCell(Direction d) {
        return dungeon.getMap().getCellAround(dungeon.getMap().getCell(position), d);
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

    @Override
    public String toString()
    {
        return getTypeAsString();
    }

    public abstract boolean isInteractable();

    public abstract LayerLevel getLayerLevel();

    public abstract String getTypeAsString();

    public abstract void tick();
}