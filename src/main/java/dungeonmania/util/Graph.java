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

    private Map<Vertex<T>, List<Vertex<T>>> adjacencyList;
    
    public Graph() {
        this.adjacencyList = new HashMap<Vertex<T>,List<Vertex<T>>>();
    }

    private Vertex<T> getRoot() {
        return this.adjacencyList.keySet().stream()
            .filter(v -> v.getDistance() == 0).findAny().orElse(null);
    }

    public void addVertex(Vertex<T> vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void removeVertex(Vertex<T> vertex) {
        adjacencyList.values().stream().forEach(l -> l.remove(vertex));
        adjacencyList.remove(vertex);
    }

    public void addEdge(Vertex<T> src, Vertex<T> dst) {
        if (!adjacencyList.containsKey(src)) addVertex(src);
        if (!adjacencyList.containsKey(dst)) addVertex(dst);
        adjacencyList.get(src).add(dst);
    }
    
    public void removeEdge(Vertex<T> src, Vertex<T> dst) {
        if (adjacencyList.get(src) != null) adjacencyList.get(src).remove(dst);
    }

    public List<Vertex<T>> getDestinations(Vertex<T> vert) {
        return adjacencyList.get(vert);
    }

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

        // check for merc spawning on top of player
        if (start.getData().equals(getRoot().getData())) {
            path.add(start);
            return path;
        }

        Vertex<T> cur = start;
        while (cur.getDistance() != 0) {
            path.add(cur);
            // make a copy of cur to avoid concurrent modification
            Vertex<T> curCopy = new Vertex<T>(cur.getData(), cur.getDistance());
            cur = adjacencyList.keySet().stream().filter(v -> adjacencyList.get(v).contains(curCopy)).findFirst().get();
        }
        path.add(cur);
        Collections.reverse(path);

        return path;
    }

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
