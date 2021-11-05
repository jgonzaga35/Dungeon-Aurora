package dungeonmania.response.models;

import java.util.Objects;

import dungeonmania.util.Position;

public final class EntityResponse {
    private final String id;
    private final String type;
    private final Position position;
    private final boolean isInteractable;

    public EntityResponse(String id, String type, Position position, boolean isInteractable) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.isInteractable = isInteractable;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public final String getId() {
        return id;
    }

    public final String getType() {
        return type;
    }

    public final Position getPosition() {
        return position;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EntityResponse)) {
            return false;
        }
        EntityResponse entityResponse = (EntityResponse) o;
        return Objects.equals(id, entityResponse.id) && Objects.equals(type, entityResponse.type) && Objects.equals(position, entityResponse.position) && isInteractable == entityResponse.isInteractable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, position, isInteractable);
    }

}
