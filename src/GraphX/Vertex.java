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
 *     <li>{@code mapBySink} - the set of {@code Edges} with this {@code Vertex}
 *     as the source. Each {@code Edge} is indexed in the map by its sink node.</li>
 *     <li>{@code mapBySource} - the set of {@code Edges} with this {@code Vertex}
 *     as the sink. Each {@code Edge} is indexed in the map by its source node.</li>
 * </ul>
 * @param <Label> the label class object for this {@code Vertex}.
 * @param <EdgeLabel> the label class object for any {@code Edge} corresponding
 *                   to this {@code Vertex}.
 * @see Edge
 * @see Graph
 */
public class Vertex<Label, EdgeLabel> {
    private final Label label;
    private final Map<Vertex<Label, EdgeLabel>, Edge<EdgeLabel, Label>> mapBySink, mapBySource;

    /**
     * Creates a new {@code Vertex}.
     * @param label the label for this {@code Vertex}.
     */
    public Vertex(Label label) {
        this.label = label;
        this.mapBySink = new HashMap<>();
        this.mapBySource = new HashMap<>();
    }

    /**
     * Gets the label for this {@code Vertex}.
     * @return {@code this.label}
     */
    public Label label() {
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
    public boolean addNeighbor(@NotNull Vertex<Label, EdgeLabel> vertex, @NotNull EdgeLabel label) {
        final boolean isNewNeighbor = (this.mapBySink.get(vertex) == null);
        if(isNewNeighbor) {
            final Edge<EdgeLabel, Label> edge = new Edge<>(label, this, vertex);
            this.mapBySink.put(vertex, edge);
            vertex.mapBySource.put(this, edge);
        }
        return isNewNeighbor;
    }

    /**
     * Determines whether a specified {@code Vertex} is a neighbor of this {@code Vertex}.
     * @param vertex the target neighbor {@code Vertex} label.
     * @return {@code true} if there exists an {@code Edge} whose source is this {@code Vertex} and
     * whose sink is the target {@code Vertex}, else {@code false} if no such {@code Edge} exists.
     */
    public boolean isNeighbor(@NotNull Label vertex) {
        return this.mapBySink.containsKey(vertex);
    }

    /**
     * Determines whether a specified {@code Edge} is incident to this {@code Vertex}.
     * @param edge the target {@code Edge}.
     * @return {@code true} if the target {@code Edge} has this {@code Vertex} as one
     * of its endpoints, else {@code false} if neither endpoint is equal to this {@code Edge}.
     */
    public boolean isIncident(@NotNull Edge<EdgeLabel, Label> edge) {
        return this.label.equals(edge.sink().label) || this.label.equals(edge.source().label);
    }

    /**
     * Gets the {@code Edge} from a specified {@code Vertex} to this {@code Vertex}.
     * @param vertex the target {@code Vertex} label.
     * @return the corresponding {@code Edge}, else {@code null} if no such
     * {@code Edge} exists.
     */
    public Edge<EdgeLabel, Label> getEdgeFrom(@NotNull Vertex<Label, EdgeLabel> vertex) {
        return this.mapBySource.get(vertex);
    }

    /**
     * Gets the {@code Edge} to a specified {@code Vertex} from this {@code Vertex}.
     * @param vertex the target {@code Vertex} label.
     * @return the corresponding {@code Edge}, else {@code null} if no such
     * {@code Edge} exists.
     */
    public Edge<EdgeLabel, Label> getEdgeTo(@NotNull Vertex<Label, EdgeLabel> vertex) {
        return this.mapBySink.get(vertex);
    }
}
