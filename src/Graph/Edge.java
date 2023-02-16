package Graph;

import java.util.List;

public class Edge<Label> {
    private final int weight;
    private final Vertex<Label> a, b;

    /**
     * Creates a new Edge
     * @param a the first endpoint Vertex of this Edge
     * @param b the second endpoint Vertex of this Edge
     */
    public Edge(Vertex<Label> a, Vertex<Label> b, int weight) {
        this.a = a;
        this.b = b;
        this.weight = weight;
    }

    /**
     * Gets the endpoint Vertices of this Edge
     * @return this.a and this.b
     */
    public List<Vertex<Label>> getEndpoints() {
        return List.of(a, b);
    }

    /**
     * Finds the weight of this Edge
     * @return this.weight
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Converts this Edge to a printable format
     * @return this Edge as a String
     */
    @Override
    public String toString() {
        int a = this.a.hashCode(), b = this.b.hashCode();
        return a < b ? (this.a + "-" + this.b) : (this.b + "-" + this.a);
    }
}
