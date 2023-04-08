package GraphX;

public record Edge<Label, VertexLabel>(
        Label label,
        Vertex<VertexLabel, Label> source,
        Vertex<VertexLabel, Label> sink) {

    /**
     * Gets the label for this {@code Edge}.
     *
     * @return {@code this.label}
     */
    @Override
    public Label label() {
        return this.label;
    }

    /**
     * Gets the source {@code Vertex} for this {@code Edge}.
     *
     * @return {@code this.source}
     */
    @Override
    public Vertex<VertexLabel, Label> source() {
        return this.source;
    }

    /**
     * Gets the sink {@code Vertex} for this {@code Edge}.
     *
     * @return {@code this.sink}
     */
    @Override
    public Vertex<VertexLabel, Label> sink() {
        return this.sink;
    }
}
