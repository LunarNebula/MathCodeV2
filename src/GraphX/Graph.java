package GraphX;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores a discrete graph. This object consists of a set of objects called
 * vertices, a set of objects called edges, and assigns to each edge a head
 * {@code Vertex} and a tail {@code Vertex}. This {@code Graph} object permits
 * a series of operations as is possible on such graphs.
 * @param <VLabel> the label data type given to each {@code Vertex}.
 * @param <ELabel> the label data type given to each {@code Edge}.
 * @see Edge
 * @see Vertex
 */
public class Graph<VLabel, ELabel> {
    private final Map<VLabel, Vertex<VLabel, ELabel>> vertexMap;
    private final Map<VLabel, Map<VLabel, Edge<ELabel, VLabel>>> mapByHead;

    /**
     * Creates a new {@code Graph} object.
     */
    public Graph() {
        this.vertexMap = new HashMap<>();
        this.mapByHead = new HashMap<>();
    }

    /**
     * Adds a {@code Vertex} to this {@code Graph}.
     * @param vertex the new {@code Vertex}.
     */
    public void addVertex(@NotNull Vertex<VLabel, ELabel> vertex) {
        final VLabel label = vertex.label();
        if(! this.vertexMap.containsKey(label)) {
            this.vertexMap.put(label, vertex);
            this.mapByHead.put(label, new HashMap<>());
        }
    }

    /**
     * Adds an {@code Edge} to this {@code Graph}.
     * @param edge the new {@code Edge}.
     */
    public void addEdge(@NotNull Edge<ELabel, VLabel> edge) {
        final VLabel label = edge.head().label();
        Map<VLabel, Edge<ELabel, VLabel>> sourceMap = this.mapByHead.get(label);
        if(sourceMap == null) {
            sourceMap = new HashMap<>();
        }
        sourceMap.put(edge.tail().label(), edge);
        this.mapByHead.replace(label, sourceMap);
    }

    /**
     * Determines whether this {@code Graph} contains a specified {@code Vertex}.
     * @param vertex the target {@code Vertex}.
     * @return {@code true} if the provided {@code Vertex} exists in this
     * {@code Graph}, else {@code false}.
     */
    public boolean containsVertex(@NotNull VLabel vertex) {
        return this.vertexMap.containsKey(vertex);
    }

    /**
     * Determines whether a specified {@code Edge} exists in this {@code Graph}.
     * @param edge the target {@code Edge}.
     * @return {@code true} if an {@code Edge} with the provided head, tail, and
     * label exists in this {@code Graph}, else {@code false}.
     */
    public boolean containsEdge(@NotNull Edge<ELabel, VLabel> edge) {
        final Map<VLabel, Edge<ELabel, VLabel>> sourceMap = this.mapByHead.get(edge.head().label());
        if(sourceMap != null) {
            final Edge<ELabel, VLabel> mapEdge = sourceMap.get(edge.tail().label());
            if(mapEdge != null) {
                return mapEdge.label().equals(edge.label());
            }
        }
        return false;
    }

    /**
     * Determines whether a specified {@code Edge} exists in this {@code Graph}.
     * While this implementation of {@code containsEdge} requires only a label
     * instead of the complete associated {@code Edge} object, it has to search
     * the entire {@code Graph} instead of pulling the head and tail directly
     * from a map. As such, this method is more useful when less information
     * (e.g. only the label) is provided, but less useful when a faster search
     * algorithm is desired.
     * @param edge the target {@code Edge} label.
     * @return {@code true} if an {@code Edge} with the provided head, tail, and
     * label exists in this {@code Graph}, else {@code false}.
     */
    public boolean containsEdge(@NotNull ELabel edge) {
        for(VLabel source : this.mapByHead.keySet()) {
            final Map<VLabel, Edge<ELabel, VLabel>> map = this.mapByHead.get(source);
            for(VLabel sink : map.keySet()) {
                return map.get(sink).label().equals(edge);
            }
        }
        return false;
    }

    /**
     * Converts this {@code Graph} to a printable format.
     * @return this {@code Graph} as a {@code String}.
     */
    @Override
    public String toString() {
        final StringBuilder vertexBuilder = new StringBuilder();
        final StringBuilder edgeBuilder = new StringBuilder();
        String delimiter = "";
        for(VLabel source : this.mapByHead.keySet()) {
            vertexBuilder.append(delimiter).append(source);
            delimiter = "\n";
            final Map<VLabel, Edge<ELabel, VLabel>> sourceMap = this.mapByHead.get(source);
            for(VLabel sink : sourceMap.keySet()) {
                edgeBuilder.append(delimiter).append(sourceMap.get(sink));
            }
        }
        return vertexBuilder.toString() + edgeBuilder;
    }

    /**
     * Prints this {@code Graph}.
     */
    public void print() {
        System.out.println(this);
    }
}
