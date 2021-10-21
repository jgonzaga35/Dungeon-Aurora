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

    /**
     * @return the cell this entity is on
     */
    public Cell getCell() {
        return dungeon.getMap().getCell(position);
    }

    public abstract boolean isInteractable();

    public abstract LayerLevel getLayerLevel();

    public abstract String getTypeAsString();

    public abstract void tick();
}