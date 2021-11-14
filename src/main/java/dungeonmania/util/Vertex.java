package dungeonmania.util;

import java.util.Objects;

/**
 * Vertices for the graph class
 */
public class Vertex<T> {
    private T data;
    private Integer distance;
    
    public Vertex(T data, Integer distance) {
        this.data = data;
        this.distance = distance;
    }
    
    public Vertex(T data) {
        this.data = data;
        this.distance = 1;
    }

    public T getData() {
        return this.data;
    }

    public Integer getDistance() {
        return this.distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Vertex<?> vert = (Vertex<?>) obj;

        return vert.getData().equals(this.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
