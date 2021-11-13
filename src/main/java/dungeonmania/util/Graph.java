package dungeonmania.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adjacency list implementation of a graph structure to aid with path finding.
 */
public class Graph<T> {
    
    // static class Edge<E> {
    //     Integer weight;
    //     Vertex<E> source;
    //     Vertex<E> destination;
        
    //     public Edge(Vertex<E> src, Vertex<E> dst, Integer weight) {
    //         this.destination = dst;
    //         this.source = src;
    //         this.weight = weight;
    //     }
    // }

    private Map<Vertex<T>, List<Vertex<T>>> adjacencyList;
    // private Map<Vertex<T>, List<Edge<T>>> vertices;
    
    public Graph() {
        // this.vertices = new HashMap<Vertex<T>,List<Edge<T>>>();
        this.adjacencyList = new HashMap<Vertex<T>,List<Vertex<T>>>();
    }

    public void addVertex(Vertex<T> vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
        // vertices.putIfAbsent(new Vertex<T>(vertex), new ArrayList<>());
    }

    public void removeVertex(Vertex<T> vertex) {
        adjacencyList.values().stream().forEach(l -> l.remove(vertex));
        adjacencyList.remove(vertex);
    }

    // public void removeVertex(T vertex) {
    //     Vertex<T> v = new Vertex<T>(vertex);
    //     vertices.values().stream().forEach(e -> {
    //         List<Edge<T>> edges = new ArrayList<>(e);
    //         edges.stream().forEach(edge -> {
    //             if (edge.destination.equals(v)) e.remove(edge);
    //         });
    //     });
    //     vertices.remove(v);
    // }

    public void addEdge(Vertex<T> src, Vertex<T> dst) {
        if (!adjacencyList.containsKey(src)) addVertex(src);
        if (!adjacencyList.containsKey(dst)) addVertex(dst);
        adjacencyList.get(src).add(dst);
    }

    // public void addEdge(T src, T dst, Integer weight) {
    //     Vertex<T> vSrc = new Vertex<T>(src);
    //     Vertex<T> vDst = new Vertex<T>(dst);
    //     Edge<T> edge = new Edge<T>(vSrc, vDst, weight);
    //     vertices.get(vSrc).add(edge);
    // }
    
    public void removeEdge(Vertex<T> src, Vertex<T> dst) {
        if (adjacencyList.get(src) != null) adjacencyList.get(src).remove(dst);
    }
    
    // public void removeEdge(Edge<T> edge) {
    //     if (vertices.get(edge.source) != null) 
    //         vertices.get(edge.source).remove(edge);
    // }

    public List<Vertex<T>> getDestinations(Vertex<T> vert) {
        return adjacencyList.get(vert);
    }

    // public List<Vertex<T>> getDestinations(Vertex<T> vert) {
    //     return vertices.get(vert).stream().map(e -> e.destination).collect(Collectors.toList());
    // }

    // /**
    //  * Returns the list of vertices tracing back from the @param start vertex to
    //  * the root of the graph by visiting all the sources.
    //  * 
    //  * @Precondition The graph is a tree with each vertex having 1 or 0 sources
    //  * @param start
    //  * @return
    //  */
    // public List<Vertex<T>> tracebackFrom(Vertex<T> start) {
    //     List<Vertex<T>> path = new ArrayList<>();

    //     Vertex<T> cur = start;
    //     while (cur.getDistance() != 0) {
    //         path.add(cur);
    //         Vertex<T> curCopy = new Vertex<T>(cur.getData(), cur.getDistance());
    //         cur = adjacencyList.keySet().stream()
    //             .filter(v -> adjacencyList.get(v).contains(curCopy)).findFirst().get();
    //     }

    //     return path;
    // }

    /**
     * Returns the list of vertices tracing back from the @param start vertex to
     * the root of the graph by visiting all the sources.
     * 
     * @Precondition The graph is a tree with each vertex having 1 or 0 sources
     * @param start
     * @return
     */
    public List<Vertex<T>> tracebackFrom(Vertex<T> start) {
        List<Vertex<T>> path = new ArrayList<>();

        Vertex<T> cur = start;
        while (cur.getDistance() != 0) {
            path.add(cur);
            Vertex<T> curCopy = new Vertex<T>(cur.getData(), cur.getDistance());
            cur = adjacencyList.keySet().stream().filter(v -> adjacencyList.get(v).contains(curCopy)).findFirst().get();
        }
        path.add(cur);
        Collections.reverse(path);

        return path;
    }

    // @Override
    // public String toString() {
    //     String str = "";

    //     for (Vertex<T> vert : vertices.keySet()) {
    //         str += vert.getData().toString() + " -> ";
    //         for (Edge<T> edge : vertices.get(vert)) {
    //             str += edge.destination.getData().toString() + " ";
    //         }
    //         str += "\n";
    //     }

    //     return str;
    // }

    @Override
    public String toString() {
        String str = "";

        for (Vertex<T> vert : adjacencyList.keySet()) {
            str += vert.getData().toString() + " -> ";
            for (Vertex<T> edge : adjacencyList.get(vert)) {
                str += edge.getData().toString() + " ";
            }
            str += "\n";
        }

        return str;
    }

    public static void main(String[] args) {

        Graph<String> graph = new Graph<String>();
        Vertex<String> maria = new Vertex<String>("Maria");
        graph.addVertex(new Vertex<String>("Bob", 0));
        graph.addVertex(new Vertex<String>("Alice"));
        graph.addVertex(new Vertex<String>("Mark"));
        graph.addVertex(new Vertex<String>("Rob"));
        graph.addVertex(new Vertex<String>("Maria"));
        graph.addEdge(new Vertex<String>("Bob"), new Vertex<String>("Alice"));
        graph.addEdge(new Vertex<String>("Bob"), new Vertex<String>("Rob"));
        graph.addEdge(new Vertex<String>("Alice"), new Vertex<String>("Mark"));
        graph.addEdge(new Vertex<String>("Rob"), new Vertex<String>("Mark"));
        graph.addEdge(new Vertex<String>("Alice"), maria);
        graph.addEdge(new Vertex<String>("Rob"), new Vertex<String>("Maria"));

        System.out.println(graph.toString());

        System.out.println(graph.tracebackFrom(maria));
    }
}
