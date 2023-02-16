package Graph;

import DataKey.ComparableMapEntry;
import DataSet.BST;

import java.util.HashMap;
import java.util.Map;

public class Vertex<Label> {
    private final Label ID;
    private final Map<Vertex<Label>, Edge<Label>> edges;

    /**
     * Creates a new Vertex
     * @param ID the ID of this Vertex
     */
    public Vertex(Label ID) {
        this.ID = ID;
        this.edges = new HashMap<>();
    }

    /**
     * Gets the ID of this Vertex
     * @return this.ID
     */
    public Label getID() {
        return this.ID;
    }

    /**
     * Finds the Edge connected to a specified neighboring Vertex
     * @param end the neighboring Vertex
     * @return the associated Edge, or null if no such Vertex exists as a neighbor
     */
    public Edge<Label> getEdge(Vertex<Label> end) {
        return this.edges.get(end);
    }

    /**
     * Connects this Vertex to another Vertex
     * @param neighbor the prospective neighboring Vertex
     * @param weight the weight of the connecting Edge
     * @param isUndirected true if the Edge should be undirected, else false
     * @return true if the new Edge was successfully added to this Vertex, else false
     */
    public boolean addEdge(Vertex<Label> neighbor, int weight, boolean isUndirected) {
        if(this.edges.containsKey(neighbor)) {
            return false;
        }
        final Edge<Label> edge = new Edge<Label>(this, neighbor, weight);
        this.edges.put(neighbor, edge);
        if(isUndirected) {
            neighbor.edges.put(this, edge);
        }
        return true;
    }

    /**
     * Removes an Edge between this Vertex and a specified neighbor Vertex
     * @param neighbor the target neighbor Vertex
     * @param removeUndirected true if any Edge from the neighbor Vertex to this Vertex should be removed, else false
     * @return true if an Edge from this Vertex to the target Vertex (and if [removeUndirected == true], a second Edge
     *         from the neighbor to this Vertex) has/have been removed
     */
    public boolean removeEdge(Vertex<Label> neighbor, boolean removeUndirected) {
        return (this.edges.remove(neighbor) != null) && (! removeUndirected || neighbor.removeEdge(this, false));
    }

    /**
     * Determines a Path from this Vertex to a specified Vertex using Dijkstra's algorithm
     * @param a the destination Vertex
     * @return the Path, else null if the two Vertices are not connected
     * @throws IllegalArgumentException if the destination Vertex is null
     */
    public Path<Label> getPath(Vertex<Label> a) {
        if(a == null) {
            throw new IllegalArgumentException();
        }
        final Map<Label, Integer> weights = new HashMap<>();
        final BST<ComparableMapEntry<Integer, Vertex<Label>>> openVertices = new BST<ComparableMapEntry<Integer, Vertex<Label>>>();
        final Map<Label, Vertex<Label>> graph = getConnectedGraph().getVertices(), linkages = new HashMap<>();
        for(Label key : graph.keySet()) {
            linkages.put(key, null);
            weights.put(key, -1);
        }
        openVertices.insert(new ComparableMapEntry<>(0, this));
        weights.replace(this.ID, 0);
        Vertex<Label> cursor = this;
        while(openVertices.size() > 0 && ! cursor.equals(a)) {
            cursor = openVertices.removeMin().value();
            for(Vertex<Label> key : cursor.edges.keySet()) {
                Edge<Label> edge = cursor.edges.get(key);
                int neighborWeight = weights.get(key.getID()), candidateWeight = weights.get(cursor.getID()) + edge.getWeight();
                boolean neighborWeightIsReplaceable = neighborWeight > candidateWeight;
                if(neighborWeightIsReplaceable) {
                    openVertices.remove(new ComparableMapEntry<>(neighborWeight, key));
                }
                if(neighborWeightIsReplaceable || neighborWeight < 0) {
                    weights.replace(key.getID(), candidateWeight);
                    openVertices.insert(new ComparableMapEntry<>(candidateWeight, key));
                    linkages.replace(key.getID(), cursor);
                }
            }
        }
        Path<Label> path = null;
        if(cursor.equals(a)) {
            path = new Path<Label>();
            path.addVertex(cursor);
            while (cursor != null) {
                path.addVertexToStart(cursor);
                cursor = linkages.get(cursor.getID());
            }
        }
        return path;
    }

    /**
     * Finds the entire connected Graph containing this Vertex
     * @return a Graph of all Vertices connected to this Vertex
     */
    public Graph<Label> getConnectedGraph() {
        final Map<Label, Vertex<Label>> map = new HashMap<>();
        getConnectedGraph(map);
        return new Graph<Label>(map);
    }

    /**
     * Finds the entire map of Vertices connected to this Vertex
     * @param map the current map of searched Vertices
     */
    private void getConnectedGraph(Map<Label, Vertex<Label>> map) {
        for(Vertex<Label> key : this.edges.keySet()) {
            if(! map.containsKey(key.getID())) {
                map.put(key.getID(), key);
                key.getConnectedGraph(map);
            }
        }
    }

    /**
     * Determines whether a specified Vertex is a neighbor of this Vertex
     * @param candidate the candidate neighbor
     * @return true if this map of edges contains an edge to the specified Vertex, else false
     */
    public boolean isNeighbor(Vertex<Label> candidate) {
        return this.edges.containsKey(candidate);
    }

    /**
     * Finds the degree of this Vertex
     * @return the number of neighboring Vertices
     */
    public int degree() {
        return this.edges.keySet().size();
    }

    /**
     * Computes the hashcode of this Vertex
     * @return the hashcode of this Vertex ID
     */
    @Override
    public int hashCode() {
        return this.ID.hashCode();
    }

    /**
     * Determines whether this Vertex is equal to a specified Object
     * @param obj the comparator Object
     * @return true if the specified Object is a Vertex with identical ID, else false
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vertex<?> && ((Vertex<?>) obj).ID.equals(this.ID);
    }

    /**
     * Converts this Vertex to a printable format
     * @return this Vertex as a String
     */
    @Override
    public String toString() {
        return this.ID.toString();
    }
}
