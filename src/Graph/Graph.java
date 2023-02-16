package Graph;

import DataSet.HashList;

import java.util.*;

public class Graph<Label> {
    private final Map<Label, Vertex<Label>> vertices;

    /**
     * Creates a new Graph
     */
    public Graph() {
        this.vertices = new HashMap<>();
    }

    /**
     * Creates a new Graph with a specified starting set of Vertices
     * @param vertexMap the map of Vertices
     */
    public Graph(Map<Label, Vertex<Label>> vertexMap) {
        this.vertices = new HashMap<>(vertexMap);
    }

    /**
     * Attempts to add a Vertex to this Graph
     * @param vertex the target Vertex
     * @return true if the Vertex is successfully added to this Graph, else false if the Vertex was already in this Graph
     */
    public boolean addVertex(Vertex<Label> vertex) {
        if(this.vertices.containsKey(vertex.getID())) {
            return false;
        }
        this.vertices.put(vertex.getID(), vertex);
        return true;
    }

    /**
     * Finds the set of Vertices in this Graph
     * @return this.vertices
     */
    public Map<Label, Vertex<Label>> getVertices() {
        return this.vertices;
    }

    /**
     * Finds the number of Vertices in this Graph
     * @return this.vertices.size()
     */
    public int size() {
        return this.vertices.size();
    }

    /**
     * Gets the maximum Vertex degree in this Graph
     * @return the largest number of neighboring Edges belonging to any one Vertex
     */
    public int maxDegree() {
        int maxDegree = 0;
        for(Label key : this.vertices.keySet()) {
            maxDegree = Math.max(maxDegree, this.vertices.get(key).degree());
        }
        return maxDegree;
    }

    /**
     * Gets the minimum Vertex degree in this Graph
     * @return the smallest number of neighboring Edges belonging to any one Vertex
     */
    public int minDegree() {
        int minDegree = -1;
        for(Label key : this.vertices.keySet()) {
            int degree = this.vertices.get(key).degree();
            minDegree = (minDegree < 0) ? degree : Math.min(degree, minDegree);
        }
        return minDegree;
    }

    /**
     * Finds the set of connected component Graphs in this Graph
     * @return all Vertex groups connected by Edges
     */
    public List<Graph<Label>> getConnectedComponents() {
        Map<Label, Boolean> vertexOpen = new HashMap<>();
        for(Label key : this.vertices.keySet()) {
            vertexOpen.put(key, true);
        }
        List<Graph<Label>> graphs = new LinkedList<>();
        for(Label key : this.vertices.keySet()) {
            if(vertexOpen.get(key)) {
                Graph<Label> connectedGraph = this.vertices.get(key).getConnectedGraph();
                graphs.add(connectedGraph);
                for(Label connectedVertexKey : connectedGraph.vertices.keySet()) {
                    vertexOpen.replace(connectedVertexKey, false);
                }
            }
        }
        return graphs; //TODO: fix
    }
}
