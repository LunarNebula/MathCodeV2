package GraphX;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Stores a discrete graph. This object consists of a set of objects called
 * vertices, a set of objects called edges, and an assignment to each edge
 * of a head {@code Vertex} and a tail {@code Vertex}. This {@code Graph}
 * object permits a set of operations as is possible on such graphs.
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
     * Creates and adds a {@code Vertex} to this {@code Graph}.
     * @param label the label of the new {@code Vertex}.
     */
    public void addVertex(VLabel label) {
        addVertex(new Vertex<>(label));
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
     * @throws IllegalArgumentException if either the head or the tail of the {@code Edge}
     * is not already contained in this {@code Graph}.
     */
    public void addEdge(@NotNull Edge<ELabel, VLabel> edge) throws IllegalArgumentException {
        final VLabel head = edge.head().label(), tail = edge.tail().label();
        if(this.vertexMap.containsKey(head) && this.vertexMap.containsKey(tail)) {
            Map<VLabel, Edge<ELabel, VLabel>> sourceMap = this.mapByHead.get(head);
            if(sourceMap == null) {
                sourceMap = new HashMap<>();
            }
            sourceMap.put(edge.tail().label(), edge);
            this.mapByHead.replace(head, sourceMap);
        } else {
            throw new IllegalArgumentException("Head or tail of edge not contained in graph.");
        }
    }

    /**
     * Adds a new {@code Edge} to this {@code Graph}.
     * @param label the {@code Edge} label.
     * @param headV the label of the head {@code Vertex}.
     * @param tailV the label of the tail {@code Vertex}.
     * @throws IllegalArgumentException if either the {@code head} or {@code tail} is not
     * contained in this {@code Graph}.
     */
    public void addEdge(ELabel label, VLabel headV, VLabel tailV) throws IllegalArgumentException {
        final Vertex<VLabel, ELabel> head = this.vertexMap.get(headV);
        final Vertex<VLabel, ELabel> tail = this.vertexMap.get(tailV);
        if(head != null && tail != null) {
            head.addNeighbor(tail, label);
        } else {
            throw new IllegalArgumentException("Head or tail of edge not contained in graph.");
        }
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
     * (e.g. only the label) is provided, but is less useful when a faster search
     * algorithm is desired.
     * @param edge the target {@code Edge} label.
     * @return {@code true} if an {@code Edge} with the provided head, tail, and
     * label exists in this {@code Graph}, else {@code false} if no such edge exists.
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
     * Performs a breadth-first search from a {@code Vertex} in this {@code Graph} and
     * records some subset of the {@code Vertices} traversed.
     * @param start the starting {@code Vertex}.
     * @param recordStart {@code true} if this algorithm should record a {@code Vertex}
     *                                when it is first reached, else {@code false}.
     * @param recordLoop {@code true} if this algorithm should record a {@code Vertex}
     *                                 when it is reached in a loop, else {@code false}.
     * @return the list of {@code Vertices} traversed according to the above specifications.
     */
    public List<VLabel> BFS(VLabel start,
                            boolean recordStart,
                            boolean recordLoop) {
        final List<VLabel> traversal = new ArrayList<>();
        final Queue<Vertex<VLabel, ELabel>> queue = new LinkedList<>();
        queue.add(this.vertexMap.get(start));
        final HashSet<VLabel> visited = new HashSet<>();
        while(! queue.isEmpty()) {
            final Vertex<VLabel, ELabel> cursor = queue.poll();
            final VLabel label = cursor.label();
            if(recordStart) {
                traversal.add(label);
            }
            visited.add(label);
            final Map<VLabel, Edge<ELabel, VLabel>> map = cursor.neighbors();
            for(VLabel next : map.keySet()) {
                if(visited.contains(next)) {
                    if(recordLoop) {
                        traversal.add(next);
                    }
                } else {
                    queue.add(map.get(next).tail());
                }
            }
        }
        return traversal;
    }

    /**
     * Performs a depth-first search from a {@code Vertex} in this {@code Graph} and
     * records some subset of the {@code Vertices} traversed.
     * @param start the starting {@code Vertex}.
     * @param ignore a set of all {@code Vertex} labels for which the algorithm should
     *               ignore the associated {@code Vertex}. Essentially, any branch of
     *               DFS stops at a {@code Vertex} if its label is in this {@code HashSet}.
     * @param recordStart {@code true} if this algorithm should record a {@code Vertex}
     *                                when it is first reached, else {@code false}.
     * @param recordLoop {@code true} if this algorithm should record a {@code Vertex}
     *                                 when it is reached in a loop, else {@code false}.
     * @param recordPrev {@code true} if this algorithm should record the previous
     *                               {@code Vertex} added to the {@code Stack} if
     *                               discovered, else {@code false}.
     * @param recordIgnore {@code true} if this algorithm should record a {@code Vertex}
     *                                 if it is listed in the set of "ignore" labels,
     *                                 else {@code false}.
     * @param recordEnd {@code true} if this algorithm should record a {@code Vertex}
     *                              when it is popped off the stack, else {@code false}.
     * @return the list of {@code Vertices} traversed according to the above specifications.
     */
    public List<VLabel> DFS(VLabel start,
                            HashSet<VLabel> ignore,
                            boolean recordStart,
                            boolean recordLoop,
                            boolean recordPrev,
                            boolean recordIgnore,
                            boolean recordEnd) {
        final Stack<Vertex<VLabel, ELabel>> stack = new Stack<>();
        stack.push(this.vertexMap.get(start));
        final Stack<Boolean> checkStack = new Stack<>();
        checkStack.push(true);
        final List<VLabel> traversal = new ArrayList<>();
        final HashSet<VLabel> visited = new HashSet<>();
        final Map<VLabel, VLabel> previousMap = new HashMap<>();
        previousMap.put(start, null);
        while(! stack.isEmpty()) {
            final Vertex<VLabel, ELabel> cursor = stack.pop();
            final VLabel label = cursor.label();
            if(checkStack.pop()) {
                stack.push(cursor);
                checkStack.push(false);
                visited.add(label);
                if(recordStart) {
                    traversal.add(label);
                }
                final Map<VLabel, Edge<ELabel, VLabel>> map = cursor.neighbors();
                final VLabel previous = previousMap.get(label);
                for(VLabel next : map.keySet()) {
                    if(recordPrev || ! next.equals(previous)) {
                        if(recordPrev) {
                            traversal.add(next);
                        } else if(visited.contains(next)) {
                            if(recordLoop) {
                                traversal.add(next);
                            }
                        } else if(recordIgnore || ! ignore.contains(next)) {
                            stack.push(map.get(next).tail());
                            checkStack.push(true);
                        }
                    }
                }
            } else if(recordEnd) {
                traversal.add(label);
            }
        }
        return traversal;
    }

    /**
     * Finds the number of cycles in this {@code Graph}.
     * @param directed {@code true} if this algorithm should look for all directed
     *                             cycles, else {@code false} if this algorithm should
     *                             ignore cycles that return to the previous node.
     * @return the number of unique {@code Paths} with the same head and tail.
     */
    public int numberOfCycles(boolean directed) {
        final HashSet<VLabel> visited = new HashSet<>();
        int count = 0;
        for(VLabel origin : this.vertexMap.keySet()) {
            if(! visited.contains(origin)) {
                for(VLabel traversed : DFS(origin, visited, true, true,
                        directed, false, false)) {
                    if(visited.contains(traversed)) {
                        count++;
                    } else {
                        visited.add(traversed);
                    }
                }
            }
        }
        return count;
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
