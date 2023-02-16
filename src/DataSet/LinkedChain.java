package DataSet;

import General.TrueTextEncodable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedChain<Value> implements Iterable<Value>, TrueTextEncodable {
    private Cell<Value> head, tail, cursor;
    private int size, cursorIndex;

    /**
     * Creates a new LinkedChain
     */
    public LinkedChain() {
        clear();
    }

    /**
     * Empties this LinkedChain
     */
    public void clear() {
        this.head = null;
        this.tail = null;
        this.cursor = null;
        this.size = 0;
        this.cursorIndex = 0;
    }

    /**
     * Adds a Value to the start of this LinkedChain
     * @param value the target Value
     */
    public void addToStart(Value value) {
        if(this.size == 0) {
            addOriginalNode(value);
        } else {
            Cell<Value> cell = new Cell<>(value);
            cell.next = this.head;
            this.head.prev = cell;
            this.head = cell;
            this.size++;
            this.cursorIndex++;
        }
    }

    /**
     * Adds a Value to the end of this LinkedChain
     * @param value the target Value
     */
    public void addToEnd(Value value) {
        if(this.size == 0) {
            addOriginalNode(value);
        } else {
            Cell<Value> cell = new Cell<>(value);
            cell.prev = this.tail;
            this.tail.next = cell;
            this.tail = cell;
            this.size++;
        }
    }

    /**
     * Adds the first Value into this LinkedChain
     * @param value the target Value
     */
    private void addOriginalNode(Value value) {
        Cell<Value> node = new Cell<>(value);
        this.head = node;
        this.cursor = node;
        this.tail = node;
        this.size = 1;
    }

    /**
     * Moves the cursor to the next Cell
     * @param defaultValue the default Value to set the next Cell if no cell exists
     */
    public void next(Value defaultValue) {
        if(this.cursor.next == null) {
            addToEnd(defaultValue);
        }
        this.cursor = this.cursor.next;
        this.cursorIndex++;
    }

    /**
     * Moves the cursor to the next Cell
     * @param defaultValue the default Value to set the next Cell if no cell exists
     */
    public void prev(Value defaultValue) {
        if(this.cursor.prev == null) {
            addToStart(defaultValue);
        }
        this.cursor = this.cursor.prev;
        this.cursorIndex--;
    }

    /**
     * Sets a new Value in this cursor
     * @param value the replacement Value
     * @throws NoSuchElementException if the cursor does not exist
     */
    public void set(Value value) {
        if(this.cursor == null) {
            throw new NoSuchElementException();
        }
        this.cursor.value = value;
    }

    /**
     * Gets the Value at the cursor in this LinkedChain
     * @return this.cursor.value
     * @throws NoSuchElementException if the cursor does not exist
     */
    public Value get() {
        if(this.cursor == null) {
            throw new NoSuchElementException();
        }
        return this.cursor.value;
    }

    /**
     * Finds the index of the cursor in this LinkedChain
     * @return this.cursorIndex
     */
    public int getCursorIndex() {
        return this.cursorIndex;
    }

    /**
     * Gets the size of this LinkedChain
     * @return this.size
     */
    public int size() {
        return this.size;
    }

    /**
     * Finds an Iterator over the Cells in this LinkedChain
     * @return the Iterator
     */
    @Override
    public Iterator<Value> iterator() {
        return new Iterator<>() {
            private Cell<Value> cursor = LinkedChain.this.head;

            /**
             * Determines whether this Iterator has another element
             * @return true if the cursor is not null, else false
             */
            @Override
            public boolean hasNext() {
                return this.cursor != null;
            }

            /**
             * Gets the next element in this LinkedChain
             * @return this.cursor.value
             */
            @Override
            public Value next() {
                if(! hasNext()) {
                    throw new IllegalStateException();
                }
                Value value = this.cursor.value;
                this.cursor = this.cursor.next;
                return value;
            }
        };
    }

    /**
     * Provides the TrueText of this LinkedChain
     * @return this LinkedChain in a parsable format
     */
    @Override
    public String trueText() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        Cell<Value> cursor = this.head;
        while(cursor != null) {
            builder.append(delimiter).append(cursor.value);
            delimiter = "|";
            cursor = cursor.next;
        }
        return builder.toString();
    }

    /**
     * Converts this LinkedChain to a printable format
     * @return this LinkedChain as a String
     */
    @Override
    public String toString() {
        StringBuilder print = new StringBuilder();
        String delimiter = "";
        Cell<Value> cursor = this.head;
        while(cursor != null) {
            print.append(delimiter).append(cursor.value);
            delimiter = ", ";
            cursor = cursor.next;
        }
        return "[" + print + "]";
    }

    /**
     * Prints this LinkedChain
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * A Cell of this LinkedChain
     * @param <Value> the type parameter defining the contents of the Cell
     */
    private static class Cell<Value> {
        private Value value;
        private Cell<Value> prev, next;

        /**
         * Creates a new Cell with a specified value
         * @param value the target value
         */
        private Cell(Value value) {
            this.value = value;
        }
    }
}
