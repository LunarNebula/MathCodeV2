package GraphX;

import DataSet.HashList;

import java.util.Arrays;

/**
 * Designates a {@code Path} in a {@code Graph}.
 * @param <VLabel> the {@code Vertex} label data type.
 * @param <ELabel> the {@code Edge} label data type.
 */
public class Path<VLabel, ELabel> {
    final HashList<Vertex<VLabel, ELabel>> vertices;

    /**
     * Creates a new, empty {@code Path}.
     */
    public Path() {
        this.vertices = new HashList<Vertex<VLabel, ELabel>>();
    }

    /**
     * Creates a new {@code Path} with the specified list of {@code Vertices}.
     * @param v the starting vertices.
     */
    @SafeVarargs
    public Path(Vertex<VLabel, ELabel>... v) {
        this.vertices = new HashList<Vertex<VLabel, ELabel>>();
        this.vertices.addAll(Arrays.asList(v));
    }
}
