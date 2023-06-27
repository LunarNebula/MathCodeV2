package GraphX;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * <br>Stores information corresponding to a {@code Vertex} in a {@code Graph}.
 * This data type stores the following information: </br>
 * <ul>
 *     <li>{@code label} - the unique label assigned to this {@code Vertex} for
 *     identification.</li>
 *     <li>{@code mapByTail} - the set of {@code Edges} with this {@code Vertex}
 *     as the head. Each {@code Edge} is indexed in the map by its tail node.</li>
 *     <li>{@code mapByHead} - the set of {@code Edges} with this {@code Vertex}
 *     as the tail. Each {@code Edge} is indexed in the map by its head node.</li>
 * </ul>
 * @param <VLabel> the label class object for this {@code Vertex}.
 * @param <ELabel> the label class object for any {@code Edge} corresponding
 *                   to this {@code Vertex}.
 * @see Edge
 * @see Graph
 */
public class Vertex<VLabel, ELabel> {
    private final VLabel label;
    private final Map<VLabel, Edge<ELabel, VLabel>> mapByTail, mapByHead;

    /**
     * Creates a new {@code Vertex}.
     * @param label the label for this {@code Vertex}.
     */
    public Vertex(VLabel label) {
        this.label = label;
        this.mapByTail = new HashMap<>();
        this.mapByHead = new HashMap<>();
    }

    /**
     * Gets the label for this {@code Vertex}.
     * @return {@code this.label}
     */
    public VLabel label() {
        return this.label;
    }

    /**
     * Adds an edge from this {@code Vertex} to a specified neighbor {@code Vertex}.
     * @param vertex the new {@code Vertex}.
     * @param label the label for the new {@code Edge} incident to this {@code Vertex}
     *              and the target {@code Vertex}.
     * @return {@code true} if the new {@code Vertex} was successfully added as a
     * neighbor, else {@code false} if the target {@code Vertex} was already a neighbor.
     */
    public boolean addNeighbor(@NotNull Vertex<VLabel, ELabel> vertex, @NotNull ELabel label) {
        final boolean isNewNeighbor = (this.mapByTail.get(vertex.label) == null);
        if(isNewNeighbor) {
            final Edge<ELabel, VLabel> edge = new Edge<>(label, this, vertex);
            this.mapByTail.put(vertex.label, edge);
            vertex.mapByHead.put(this.label, edge);
        }
        return isNewNeighbor;
    }

    /**
     * Determines whether a specified {@code Vertex} is a neighbor of this {@code Vertex}.
     * @param vertex the target neighbor {@code Vertex} label.
     * @return {@code true} if there exists an {@code Edge} whose head is this {@code Vertex} and
     * whose tail is the target {@code Vertex}, else {@code false} if no such {@code Edge} exists.
     */
    public boolean isNeighbor(@NotNull VLabel vertex) {
        return this.mapByTail.containsKey(vertex);
    }

    /**
     * Determines whether a specified {@code Edge} is incident to this {@code Vertex}.
     * @param edge the target {@code Edge}.
     * @return {@code true} if the target {@code Edge} has this {@code Vertex} as one
     * of its endpoints, else {@code false} if neither endpoint is equal to this {@code Edge}.
     */
    public boolean isIncident(@NotNull Edge<ELabel, VLabel> edge) {
        return this.label.equals(edge.tail().label) || this.label.equals(edge.head().label);
    }

    /**
     * Gets the {@code Edge} from a specified {@code Vertex} to this {@code Vertex}.
     * @param vertex the target {@code Vertex} label.
     * @return the corresponding {@code Edge}, else {@code null} if no such
     * {@code Edge} exists.
     */
    public Edge<ELabel, VLabel> getEdgeFrom(@NotNull VLabel vertex) {
        return this.mapByHead.get(vertex);
    }

    /**
     * Gets the {@code Edge} to a specified {@code Vertex} from this {@code Vertex}.
     * @param vertex the target {@code Vertex} label.
     * @return the corresponding {@code Edge}, else {@code null} if no such
     * {@code Edge} exists.
     */
    public Edge<ELabel, VLabel> getEdgeTo(@NotNull VLabel vertex) {
        return this.mapByTail.get(vertex);
    }

    /**
     * Gets the set of all neighbors traversable from this {@code Vertex}.
     * @return {@code this.mapByTail}
     */
    public Map<VLabel, Edge<ELabel, VLabel>> neighbors() {
        return this.mapByTail;
    }

    /**
     * Finds the in-degree of this {@code Vertex}, or the number of {@code Edges}
     * with this {@code Vertex} as the tail.
     * @return {@code this.mapByHead.size()}
     */
    public int inDegree() {
        return this.mapByHead.size();
    }

    /**
     * Finds the out-degree of this {@code Vertex}, or the number of {@code Edges}
     * with this {@code Vertex} as the head.
     * @return {@code this.mapByTail.size()}
     */
    public int outDegree() {
        return this.mapByTail.size();
    }

    /**
     * Determines whether this {@code Vertex} is equal to a specified {@code Object}.
     * @param o the target {@code Object}.
     * @return {@code true} if {@code o} is a {@code Vertex} with the same label.
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Vertex<?,?> vertex)) {
            return false;
        }
        return vertex.label.equals(this.label);
    }

    /**
     * Converts this {@code Vertex} to a printable format.
     * @return this {@code Vertex} as a {@code String}.
     */
    @Override
    public String toString() {
        return this.label.toString();
    }

    /**
     * Prints this {@code Vertex}.
     */
    public void print() {
        System.out.println(this);
    }
}
