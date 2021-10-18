package dungeonmania;

import dungeonmania.DungeonManiaController.LayerLevel;

public abstract class Entity {
    private String id;

    private static int nextEntityId = 1;

    public Entity() {
        this.id = "Entity-" + Entity.nextEntityId;
        Entity.nextEntityId++;
    }

    public String getId() {
        return this.id;
    }
    public abstract boolean isInteractable();

    public abstract LayerLevel getLayerLevel();

    public abstract String getTypeAsString();
}