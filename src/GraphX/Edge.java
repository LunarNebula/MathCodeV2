package GraphX;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Records an {@code Edge} in a {@code Graph}. This {@code Edge} is assigned two
 * {@code Vertex} objects as its head and tail, as well as a label.
 * @param label the label.
 * @param head the head of the vertex.
 * @param tail the tail of the vertex.
 * @param <ELabel> the {@code Vertex} label.
 * @param <VLabel> the {@code Edge} label.
 * @see Graph
 * @see Vertex
 */
public record Edge<ELabel, VLabel>(
        ELabel label,
        Vertex<VLabel, ELabel> head,
        Vertex<VLabel, ELabel> tail) {

    /**
     * Gets the label for this {@code Edge}.
     *
     * @return {@code this.label}
     */
    @Override
    public ELabel label() {
        return this.label;
    }

    /**
     * Gets the head {@code Vertex} for this {@code Edge}.
     *
     * @return {@code this.head}
     */
    public Vertex<VLabel, ELabel> head() {
        return this.head;
    }

    /**
     * Gets the tail {@code Vertex} for this {@code Edge}.
     *
     * @return {@code this.tail}
     */
    public Vertex<VLabel, ELabel> tail() {
        return this.tail;
    }

    /**
     * Determines whether a specified {@code Object} is equal to this {@code Edge}.
     * @param o the target {@code Object}.
     * @return {@code true} if {@code o} is an {@code Edge} with the same head
     * and tail, else {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Edge<?,?> edge)) {
            return false;
        }
        return this.head.equals(edge.head)
                && this.tail.equals(edge.tail)
                && this.label.equals(edge.label);
    }

    /**
     * Converts this {@code Edge} to a printable format.
     * @return this {@code Edge} as a {@code String}.
     */
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return this.head + " -(" + this.label + ")-> " + this.tail;
    }

    /**
     * Prints this {@code Edge}.
     */
    public void print() {
        System.out.println(this);
    }
}
