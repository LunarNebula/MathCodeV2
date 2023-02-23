package DataSet;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class LinkedHashCluster<Value> implements Iterable<Value> {
    private final Map<Value, HashChain.HashNode<Value>> links;
    private final Map<Value, HashChain<Value>> chains;
    private int numberOfChains;
    public Value focus;

    /**
     * Creates a new LinkedHashCluster
     */
    public LinkedHashCluster() {
        this.links = new HashMap<>();
        this.chains = new HashMap<>();
        this.numberOfChains = 0;
        this.focus = null;
    }

    /**
     * Attempts to add a new Value to this LinkedHashCluster
     * @param addend the addend Value
     * @return true if the Value was successfully added to this LinkedHashCluster, else false
     */
    public boolean add(Value addend) {
        if(this.links.containsKey(addend)) {
            return false;
        }
        HashChain.HashNode<Value> node = new HashChain.HashNode<>(addend);
        this.links.put(addend, node);
        this.chains.put(addend, new HashChain<>(node, node));
        this.numberOfChains++;
        return true;
    }

    /**
     * Attempts to join two Payloads in this LinkedHashCluster
     * @param a the first Value
     * @param b the second Value
     */
    public void join(Value a, Value b) {
        HashChain<Value> aChain = this.chains.get(a), bChain = this.chains.get(b);
        if(!(aChain == null || bChain == null || aChain.equals(bChain))) {
            HashChain.HashNode<Value> aNode = this.links.get(a), bNode = this.links.get(b);
            if(aNode.node2 == null) {
                aNode.node2 = bNode;
            } else {
                aNode.node1 = bNode;
            }
            if(bNode.node2 == null) {
                bNode.node2 = aNode;
            } else {
                bNode.node1 = aNode;
            }
            HashChain.HashNode<Value> otherA = aChain.getOtherEnd(this.links.get(a));
            HashChain.HashNode<Value> otherB = bChain.getOtherEnd(this.links.get(b));
            this.chains.replace(a, null);
            this.chains.replace(b, null);
            this.chains.replace(otherA.payload, aChain);
            this.chains.replace(otherB.payload, aChain);
            aChain.end1 = otherA;
            aChain.end2 = otherB;
            this.numberOfChains--;
        }
    }

    /**
     * Attempts to remove a Value from this LinkedHashCluster
     * @param a the target Value
     * @return true if an element was successfully removed from the LinkedHashCluster, else false
     */
    public boolean remove(Value a) {
        if(a == null) {
            return false;
        }
        HashChain.HashNode<Value> node = this.links.get(a);
        if(node == null) {
            return false;
        }
        HashChain<Value> chain = this.chains.get(a);
        if(isIsolated(a)) {
            this.numberOfChains--;
        }
        if(chain == null) {
            HashChain.HashNode<Value> node1 = node.node1, node2 = node.node2;
            if(node2.node2 != null && node2.node2.equals(node)) {
                node2.node2 = node1;
            } else {
                node2.node1 = node1;
            }
            if(node1.node2 != null && node1.node2.equals(node)) {
                node1.node2 = node2;
            } else {
                node1.node1 = node2;
            }
        } else if(node.node1 != null || node.node2 != null) {
            HashChain.HashNode<Value> nextNode = (node.node1 == null) ? node.node2 : node.node1;
            if(nextNode.node1 != null && nextNode.node1.equals(node)) {
                nextNode.node1 = null;
            } else {
                nextNode.node2 = null;
            }
            this.chains.replace(nextNode.payload, chain);
            if(chain.end1.equals(node)) {
                chain.end1 = nextNode;
            } else {
                chain.end2 = nextNode;
            }
        }
        this.links.remove(a);
        this.chains.remove(a);
        if(a.equals(this.focus)) {
            this.focus = null;
        }
        return true;
    }

    /**
     * Removes a {@code Value} from its chain in this {@code LinkedHashCluster} and creates a new
     * chain with only that value. If the value does not exist in this {@code LinkedHashCluster},
     * then the item is not added and no new chain is created.
     * @param a the target {@code Value}.
     * @return {@code true} if the provided {@code Value} was successfully extracted, else
     * {@code false}.
     */
    public boolean extract(Value a) {
        boolean extracted = remove(a);
        if(extracted) {
            add(a);
        }
        return extracted;
    }

    /**
     * Finds the number of HashChains in this LinkedHashCluster
     * @return this.numberOfChains
     */
    public int getNumberOfChains() {
        return this.numberOfChains;
    }

    /**
     * Finds the other end of the HashChain
     * @param value the target Value
     * @return null if the Value does not occur on the end of a chain, else the other end
     */
    public Value getOtherEnd(Value value) {
        HashChain<Value> chain = this.chains.get(value);
        return chain == null ? null : chain.getOtherEnd(this.links.get(value)).payload;
    }

    /**
     * Determines whether a given Value in this LinkedHashCluster is isolated
     * @param p the target Value
     * @return true if the Value exists in this LinkedHashCluster and has no neighbors, else false
     */
    public boolean isIsolated(Value p) {
        HashChain.HashNode<Value> node = this.links.get(p);
        return node != null && node.node1 == null && node.node2 == null;
    }

    /**
     * Prints this LinkedHashCluster
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String newLine = "";
        for(Value key : this.links.keySet()) {
            HashChain.HashNode<Value> value = this.links.get(key);
            builder.append(newLine).append(value.node1).append(" - ").append(value.payload).append(" - ").append(value.node2);
            newLine = "\n";
        }
        return builder.toString();
    }

    /**
     * Returns an iterator over Value elements. Note that if the Iterator begins on
     * @return an Iterator.
     */
    @Override
    public Iterator<Value> iterator() {
        return new Iterator<>() {
            private HashChain.HashNode<Value> currentNode = LinkedHashCluster.this.links.get(LinkedHashCluster.this.focus), prevNode;

            /**
             * Determines whether there is another element left to iterate over in the HashChain
             * @return true if another element exists, else false
             */
            @Override
            public boolean hasNext() {
                return this.currentNode != null;
            }

            /**
             * Gets the next element in this
             * @return the next element if one exists, else false
             */
            @Override
            public Value next() {
                if(this.currentNode == null) {
                    throw new IllegalStateException();
                }
                if(this.prevNode == null) {
                    this.prevNode = this.currentNode;
                    this.currentNode = (this.currentNode.node1 == null) ? this.currentNode.node2 : this.currentNode.node1;
                } else {
                    HashChain.HashNode<Value> cursor = this.currentNode;
                    if(this.currentNode.node1 == null || this.currentNode.node2 == null) {
                        this.currentNode = null;
                    } else {
                        this.currentNode = (this.currentNode.node1.equals(this.prevNode)) ?
                                this.currentNode.node2 : this.currentNode.node1;
                    }
                    this.prevNode = cursor;
                }
                return this.prevNode.payload;
            }
        };
    }

    /**
     * Keeps a sequential but unordered list of HashNodes
     * @param <Payload> the Value contained in the chain
     */
    private static class HashChain<Payload> {
        private HashNode<Payload> end1, end2;

        /**
         * Creates a new HashChain
         * @param end1 one end of the HashChain
         * @param end2 the other end of the HashChain
         */
        private HashChain(HashNode<Payload> end1, HashNode<Payload> end2) {
            this.end1 = end1;
            this.end2 = end2;
        }

        /**
         * Gets the other end of this HashChain
         * @param p the comparator Value
         * @return null if neither end is a HashNode containing p, else the end not containing p
         */
        private HashNode<Payload> getOtherEnd(HashNode<Payload> p) {
            return this.end1.equals(p) ? this.end2 : this.end1;
        }

        /**
         * Determines whether this HashChain is equal to another HashChain
         * @param chain the comparator Chain
         * @return true if the end nodes are equal, else false
         */
        private boolean equals(HashChain<Payload> chain) {
            return (chain.end1.equals(this.end1) && chain.end2.equals(this.end2))
                    || (chain.end2.equals(this.end1) && chain.end1.equals(this.end2));
        }

        public String toString() {
            return "[" + this.end1 + " " + this.end2 + "]";
        }

        /**
         * Holds a Value object in a linked node
         * @param <Payload> the type parameter
         */
        private static class HashNode<Payload> {
            HashNode<Payload> node1, node2;
            Payload payload;

            /**
             * Creates a new HashNode
             * @param payload the payload of this HashNode
             */
            private HashNode(Payload payload) {
                this.payload = payload;
            }

            /**
             * Gets the other neighbor
             * @param p the comparator Value
             * @return null if neither neighbor is a HashNode containing p, else the neighbor not containing p
             */
            private HashNode<Payload> getOtherNeighbor(Payload p) {
                if(this.node1 != null && this.node1.equals(p)) {
                    return this.node2;
                } else if(this.node2 != null && this.node2.equals(p)) {
                    return this.node1;
                } else {
                    return null;
                }
            }

            /**
             * Determines whether this HashNode is equal to another HashNode
             * @param node the comparator HashNode
             * @return true if the Payloads are equal, else false
             */
            private boolean equals(HashNode<Payload> node) {
                if(this.payload == null || node.payload == null) {
                    return this.payload == node.payload;
                }
                return this.payload.equals(node.payload);
            }

            /**
             * Converts this HashNode to a printable format
             * @return this HashNode as a String
             */
            @Override
            public String toString() {
                return "" + this.payload;
            }
        }
    }
}

