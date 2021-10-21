package dungeonmania;

import dungeonmania.DungeonManiaController.LayerLevel;

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
    }

    public String getId() {
        return this.id;
    }

    // /**
    //  * Moves an entity from the current cell to the target cell
    //  * @param target
    //  */
    // public void moveTo(Cell target) {
    //     Cell from = this.getCell();
    //     from.removeOccupant(this);
    //     target.addOccupant(this);

    //     this.cell = target;
    //     this.cell.onWalked(from.getPosition(), this.cell.getPosition());
    // }

    /**
     * @return the cell this entity is on
     */
    public Cell getCell() {
        return dungeon.getCell(position);
    }

    public abstract boolean isInteractable();

    public abstract LayerLevel getLayerLevel();

    public abstract String getTypeAsString();

    public abstract void tick();
}