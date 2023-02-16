package DataSet;

import java.util.*;

/**
 * A HashMap of fixed size that pulls data based on previously searched values.
 * @param <Data> the datatype stored within this {@code FixedCacheStack}.
 */
public class FixedCacheStack<Data> implements Iterable<Data> {
    private final Map<Data, Cell<Data>> cache;
    private Cell<Data> head, tail;
    private final int capacity;

    /**
     * Creates a new {@code FixedCacheStack}.
     * @param capacity the maximum number of items that can be stored in this {@code FixedCacheStack}.
     */
    public FixedCacheStack(int capacity) {
        this.cache = new HashMap<>();
        this.capacity = capacity;
    }

    /**
     * Requests a Data value from this {@code FixedCacheStack} and updates the query order.
     * @param data the target Data value.
     * @return true if the Data value was previously in this {@code FixedCacheStack}, else false.
     */
    public boolean request(Data data) {
        Cell<Data> cell = this.cache.get(data);
        boolean dataWasRecentlyQueried = cell != null;
        if(dataWasRecentlyQueried) {
            if(cell.next == null) {
                this.tail = this.tail.prev;
            } else {
                cell.next.prev = cell.prev;
            }
            if(cell.prev != null) {
                cell.prev.next = cell.next;
            }
        } else {
            cell = new Cell<>(data);
            this.cache.put(data, cell);
        }
        cell.queries++;
        if(this.head == null) {
            this.head = cell;
            this.tail = cell;
        } else {
            this.head.prev = cell;
            cell.next = this.head;
            cell.prev = null;
            this.head = this.head.prev;
        }
        if(this.cache.size() > this.capacity) {
            if(this.tail.prev != null) {
                this.tail.prev.next = null;
                this.cache.remove(this.tail.data);
                this.tail = this.tail.prev;
            }
        }
        return dataWasRecentlyQueried;
    }

    /**
     * Finds the number of times a specified Data value has been queried
     * @param data the target Data value
     * @return the number of recorded queries for the specified Data, else 0 if the value does not exist in this FixedCacheStack
     */
    public int queries(Data data) {
        Cell<Data> cell = this.cache.get(data);
        return cell == null ? 0 : cell.queries;
    }

    /**
     * Finds the capacity of this FixedCacheStack
     * @return this.capacity
     */
    public int capacity() {
        return this.capacity;
    }

    /**
     * Finds the current size of this FixedCacheStack
     * @return this.cache.size
     */
    public int size() {
        return this.cache.size();
    }

    /**
     * Assembles a List of all Data values in this FixedCacheStack
     * @return the desired List
     */
    public List<Data> asList() {
        List<Data> dataList = new LinkedList<>();
        Cell<Data> cursor = this.head;
        while(cursor != null) {
            dataList.add(cursor.data);
            cursor = cursor.next;
        }
        return dataList;
    }

    /**
     * Creates an Iterator over the elements of this FixedCacheStack
     * @return the Iterator
     */
    @Override
    public Iterator<Data> iterator() {
        return new Iterator<>() {
            private Cell<Data> cursor = FixedCacheStack.this.head;

            /**
             * Determines whether this Iterator has a next element
             * @return true if the cursor is not null, else false
             */
            @Override
            public boolean hasNext() {
                return this.cursor == null;
            }

            /**
             * Gets the next element in this FixedCacheStack and moves the cursor
             * @return the next Data value
             * @throws IllegalStateException if there is no next Data element
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
     * Determines whether this FixedCacheStack is equal to a specified Object
     * @param o the comparator Object
     * @return true if the Object is a FixedCacheStack with equivalent Data objects, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof FixedCacheStack<?>)) {
            return false;
        }
        FixedCacheStack<?> oStack = (FixedCacheStack<?>) o;
        if(oStack.cache.size() != this.cache.size()) {
            return false;
        }
        Cell<Data> thisCursor = this.head;
        Cell<?> oCursor = oStack.head;
        while(thisCursor != null) {
            if(! thisCursor.data.equals(oCursor.data)) {
                return false;
            }
            thisCursor = thisCursor.next;
            oCursor = oCursor.next;
        }
        return true;
    }

    /**
     * Converts this FixedCacheStack to a printable format
     * @return this FixedCacheStack as a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Cell<Data> cursor = this.head;
        String delimiter = "";
        while(cursor != null) {
            builder.append(delimiter).append(cursor.data);
            cursor = cursor.next;
            delimiter = ", ";
        }
        return "[" + builder + "]";
    }

    /**
     * Prints this FixedCacheStack
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Stores different data values for
     * @param <Data> the data type stored as payload in this FixedCacheStack
     */
    private static class Cell<Data> {
        Data data;
        Cell<Data> next, prev;
        int queries;

        /**
         * Creates a new Cell with a specified payload
         * @param data the target Data payload
         */
        public Cell(Data data) {
            this.data = data;
            this.queries = 0;
        }
    }
}
