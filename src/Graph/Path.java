package Graph;

import DataSet.HashList;

public class Path<Label> {
    private final HashList<Vertex<Label>> path;
    private int weight;

    /**
     * Creates a new Path
     */
    public Path() {
        this.path = new HashList<Vertex<Label>>();
        this.weight = 0;
    }

    /**
     * Adds a Vertex into this Path
     * @param vertex the target Vertex
     * @return true if this Vertex was successfully added to the end of this Path, else false
     */
    public boolean addVertex(Vertex<Label> vertex) {
        if(this.path.contains(vertex)) {
            return false;
        }
        if(this.path.size() > 0) {
            Edge<Label> edge = this.path.getTail().getEdge(vertex);
            if(edge == null) {
                return false;
            }
            this.weight += edge.getWeight();
        }
        this.path.add(vertex);
        return true;
    }

    /**
     * Adds a Vertex into this Path
     * @param vertex the target Vertex
     * @return true if this Vertex was successfully added to the end of this Path, else false
     */
    public boolean addVertexToStart(Vertex<Label> vertex) {
        if(this.path.contains(vertex)) {
            return false;
        }
        Edge<Label> edge = this.path.getHead().getEdge(vertex);
        if(edge == null) {
            return false;
        }
        this.weight += edge.getWeight();
        this.path.insert(0, vertex);
        return true;
    }

    /**
     * Finds the total weight of this Path
     * @return this.weight
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Gets the interior Vertex list in this Path
     * @return this.path
     */
    public HashList<Vertex<Label>> getPath() {
        return this.path;
    }
}
