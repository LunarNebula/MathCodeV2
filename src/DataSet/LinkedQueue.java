package DataSet;

import java.util.EmptyStackException;
import java.util.Iterator;

/**
 * A single-link implementation of the LinkedList class that does not store size.
 * @param <Data> the type parameter encoding the class of information
 */
public class LinkedQueue<Data> implements Iterable<Data> {
    private Node<Data> head, tail;

    /**
     * Pushes a Data object into this LinkedQueue
     * @param data the specified Data object
     */
    public void add(Data data) {
        if(data != null) {
            Node<Data> newTail = new Node<>(data);
            if(this.head == null) {
                this.head = newTail;
            } else {
                this.tail.next = newTail;
            }
            this.tail = newTail;
        }
    }

    /**
     * Pops a Data object off this LinkedQueue
     * @return the popped Data object
     * @throws EmptyStackException if this LinkedQueue is empty
     */
    public Data poll() throws EmptyStackException {
        if(this.head == null) {
            throw new EmptyStackException();
        }
        Data ejected = this.head.data;
        this.head = this.head.next;
        return ejected;
    }

    /**
     * Determines whether this LinkedQueue is empty
     * @return true if this LinkedQueue is empty (equivalently, if the head is null) else false
     */
    public boolean isEmpty() {
        return this.head == null;
    }

    /**
     * Provides an Iterator over the elements in this LinkedQueue
     * @return the Data value Iterator
     */
    @Override
    public Iterator<Data> iterator() {
        return new Iterator<>() {
            private Node<Data> cursor = LinkedQueue.this.head;

            /**
             * Determines whether this Iterator has another element.
             * @return true if the cursor is not null, else false
             */
            @Override
            public boolean hasNext() {
                return this.cursor != null;
            }

            /**
             * Gets the next element in this Iterator
             * @return the next Data value
             * @throws IllegalStateException if this Iterator does not have a next element
             */
            @Override
            public Data next() {
                if(this.cursor == null) {
                    throw new IllegalStateException();
                }
                Data target = this.cursor.data;
                this.cursor = this.cursor.next;
                return target;
            }
        };
    }

    /**
     * Converts this LinkedQueue to a printable format
     * @return this LinkedQueue as a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        Node<Data> cursor = this.head;
        while(cursor != null) {
            builder.append(delimiter).append(cursor.data);
            cursor = cursor.next;
            delimiter = ", ";
        }
        return "[" + builder + "]";
    }

    /**
     * A Node containing the individual linked components of this LinkedQueue
     * @param <Data> the Data type parameter for this LinkedQueue
     */
    private static class Node<Data> {
        Data data;
        Node<Data> next;

        /**
         * Creates a new Node
         * @param data the Data object contained in this Node
         */
        public Node(Data data) {
            this.data = data;
        }
    }
}
