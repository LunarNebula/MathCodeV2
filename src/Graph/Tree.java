package Graph;

import java.util.HashMap;

public class Tree<Label> {
    private final HashMap<Label, Node<Label>> nodes;
    private Node<Label> center;

    /**
     * Creates a new Tree
     */
    public Tree() {
        this.nodes = new HashMap<>();
        this.center = null;
    }

    /**
     * Adds a Label to the center of this Tree
     * @param a the added Label
     * @return true if the Label is added, else false if it is already contained in this Tree
     */
    public boolean add(Label a) {
        if(this.nodes.get(a) != null) {
            return false;
        } else {
            Node<Label> node = new Node<>(a, this.center);
            if(this.center == null) {
                this.center = node;
            }
            this.nodes.put(a, node);
            return true;
        }
    }

    /**
     * Adds a Label to this Tree
     * @param a the added Label
     * @param parent the Label of the Node to which the added Label will be connected
     * @return true if the Node was successfully connected, else false if the parent value does not exist
     */
    public boolean add(Label a, Label parent) {
        Node<Label> parentNode = this.nodes.get(parent);
        if(parentNode == null) {
            return false;
        }
        this.nodes.put(a, new Node<>(a, parentNode));
        return true;
    }

    /**
     * Removes a Label from this Tree
     * @param a the removed Label
     * @return true if a Label was successfully removed, else false if it never existed in the Tree
     */
    public boolean remove(Label a) {
        Node<Label> node = this.nodes.get(a);
        if(node == null) {
            return false;
        }
        node.unlink();
        this.nodes.remove(a);
        if(this.center.value.equals(a)) {
            HashMap<Label, Node<Label>> map = node.nodes;
            if(map.size() > 0) {
                this.center = map.get(map.keySet().iterator().next());
            } else {
                this.center = null;
            }
        }
        return true;
    }

    /**
     * Converts this Tree to a printable format
     * @return this Tree as a String
     */
    @Override
    public String toString() {
        return this.center == null ? "[empty]" : this.center.toString("");
    }

    /**
     * Prints this Tree
     */
    public void print() {
        System.out.println(toString());
    }

    /**
     * Contains the information of a single Vertex in this Tree
     * @param <Value> the information contained in the Node
     */
    private static class Node<Value> {
        private Value value;
        private Node<Value> head;
        private final HashMap<Value, Node<Value>> nodes;

        /**
         * Creates a new TreeNode
         * @param value the Label to be placed in this TreeNode
         * @param head the original TreeNode this TreeNode is linked to
         */
        private Node(Value value, Node<Value> head) {
            this.value = value;
            this.head = head;
            if(this.head != null) {
                this.head.nodes.put(this.value, this);
            }
            this.nodes = new HashMap<>();
        }

        /**
         * Removes this Node from the system
         */
        private void unlink() {
            if(this.nodes.size() == 0) {
                if(this.head == null) {
                    this.value = null;
                } else {
                    this.head.nodes.remove(this.value);
                }
            } else {
                for(Value value : this.nodes.keySet()) {
                    Node<Value> node = this.nodes.get(value);
                    if(this.head == null) {
                        this.head = node;
                    } else {
                        this.head.nodes.put(value, node);
                        node.head = this.head;
                    }
                }
            }
        }

        /**
         * Gets the hashCode of this Node
         * @return the hashCode of the Label contained in this Node
         */
        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        /**
         * Converts this Node to a printable format
         * @param indent the indent marking the distance in nodes from the center of the parent Tree
         * @return this Node as a String
         */
        private String toString(String indent) {
            String print = indent.concat(this.value == null ? "null" : this.value.toString()).concat("\n");
            String nextIndent = indent.concat("\t");
            for(Value value : this.nodes.keySet()) {
                print = print.concat(this.nodes.get(value).toString(nextIndent));
            }
            return print;
        }
    }
}
