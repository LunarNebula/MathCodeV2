package DataSet;

import General.TrueTextEncodable;

import java.util.*;

/**
 * Maintains a doubly-linked {@code List-} and {@code HashSet-}organized data structure.
 * @param <Value> the stored data type.
 */
public class HashList<Value> implements Iterable<Value>, TrueTextEncodable {
    private final Map<Value, Cell<Value>> map;
    private Cell<Value> head, tail;

    /**
     * Creates a new, empty {@code HashList}.
     */
    public HashList() {
        this.map = new HashMap<>();
    }

    /**
     * Creates a {@code HashList} with a starter set of {@code Values}.
     * @param values the list of starter {@code Values} for this {@code HashList}.
     */
    public HashList(Iterable<Value> values) {
        HashList<Value> hashList = new HashList<Value>();
        hashList.addAll(values);
        this.map = hashList.map;
        this.head = hashList.head;
        this.tail = hashList.tail;
    }

    /**
     * Adds a {@code Value} into this {@code HashList}.
     * @param value the target {@code Value}.
     * @return {@code true} if the target {@code Value} was successfully added
     * to this {@code HashList}, else {@code false} if the {@code Value} was
     * already contained in this {@code HashList}.
     */
    public boolean add(Value value) {
        if(this.map.containsKey(value)) {
            return false;
        }
        Cell<Value> cell = new Cell<>(value);
        if(this.head == null) {
            this.head = cell;
        } else {
            this.tail.next = cell;
            cell.prev = this.tail;
        }
        this.tail = cell;
        this.map.put(value, cell);
        return true;
    }

    /**
     * Adds a list of HashNodes into this {@code HashList}.
     * @param iterable the target {@code Iterable}.
     * @return {@code true} if all new elements were successfully added to this
     * {@code HashList}, else {@code false} if at least one element already existed
     * in this {@code HashList}.
     */
    public boolean addAll(Iterable<Value> iterable) {
        boolean allElementsAreOriginal = true;
        for(Value value : iterable) {
            allElementsAreOriginal &= add(value);
        }
        return allElementsAreOriginal;
    }

    /**
     * Inserts a {@code Value} into this {@code HashList} at the specified index.
     * @param index the index of this {@code HashList}.
     * @param value the target {@code Value}.
     * @throws IndexOutOfBoundsException if the insertion index is not included in
     * the {@code HashList}.
     */
    public boolean insert(int index, Value value) {
        if(this.map.containsKey(value)) {
            return false;
        }
        int size = size();
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        } else if(index == size) {
            add(value);
        } else {
            final int iterations = Math.min(index, size - index + 1);
            Cell<Value> cursor = (iterations == index) ? this.head : this.tail;
            for(int i = 0; i < iterations; i++) {
                cursor = (iterations == index) ? cursor.next : cursor.prev;
            }
            Cell<Value> cell = new Cell<>(value);
            cell.prev = cursor.prev;
            if(cursor.prev == null) {
                this.head = cell;
            } else {
                cell.prev.next = cell;
            }
            cursor.prev = cell;
            cell.next = cursor;
            this.map.put(value, cell);
        }
        return true;
    }

    /**
     * Checks if this {@code HashList} contains a key.
     * @param value the target {@code Value}.
     * @return {@code true} if this {@code HashList} contains the target key,
     * else {@code false}.
     */
    public boolean contains(Value value) {
        return this.map.containsKey(value);
    }

    /**
     * Gets the {@code Value} located in the {@code Cell} of a specific key.
     * @return the desired {@code Value}.
     */
    public Value get(Value value) {
        return this.map.get(value).value;
    }

    /**
     * Gets the {@code Value} located at a specific index in this {@code HashList}.
     * @param index the target index.
     * @return the target {@code Value}.
     */
    public Value get(int index) {
        if(index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } else {
            Cell<Value> cursor = this.head;
            int currentIndex = 0;
            while(currentIndex < index) {
                cursor = cursor.next;
                currentIndex++;
            }
            return cursor.value;
        }
    }

    /**
     * Clears this {@code HashList}.
     */
    public void clear() {
        this.head = null;
        this.tail = null;
        this.map.clear();
    }

    /**
     * Determines if this {@code HashList} contains all {@code Values} in a
     * specified {@code HashList}.
     * @param c the target {@code HashList}.
     * @return {@code true} if all {@code Values} in the {@code HashList} are
     * in this {@code HashList}, else {@code false}.
     */
    public boolean containsAll(HashList<? extends Value> c) {
        for(Value value : c) {
            if(! contains(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether this {@code HashList} contains any values in another
     * specified {@code HashList}.
     * @param c the target {@code HashList}.
     * @return {@code true} if any {@code Values} in the {@code HashList} are contained
     * in this {@code HashList}, else {@code false}.
     */
    public boolean containsAny(HashList<? extends Value> c) {
        for(Value value : c) {
            if(contains(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a specific element from this {@code HashList}.
     * @param value the target {@code Value}.
     * @return {@code true} if the desired element was successfully removed, else
     * {@code false} if the element did not previously exist in this {@code HashList}.
     */
    public boolean remove(Value value) {
        Cell<Value> removedItem = this.map.get(value);
        if(removedItem == null) {
            return false;
        }
        if(removedItem.prev == null) {
            this.head = removedItem.next;
        } else {
            removedItem.prev.next = removedItem.next;
        }
        if(removedItem.next == null) {
            this.tail = removedItem.prev;
        } else {
            removedItem.next.prev = removedItem.prev;
        }
        this.map.remove(value);
        return true;
    }

    /**
     * Removes from this {@code HashList} the element at a specified index.
     * @param index the target index.
     * @return the {@code Value} removed from this {@code HashList}.
     * @throws IndexOutOfBoundsException if the target index is negative or
     * at least the length of this {@code HashList}.
     */
    public Value remove(int index) throws IndexOutOfBoundsException {
        if(index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        int cursorIndex = 0;
        Cell<Value> cursor = this.head;
        while(cursorIndex < index) {
            cursor = cursor.next;
            cursorIndex++;
        }
        Value removedItem = cursor.value;
        remove(removedItem);
        return removedItem;
    }

    /**
     * Inserts a {@code Value} before another target {@code Value} in this
     * {@code HashList}.
     * @param target the {@code Value} to be used as a reference for placement.
     * @param value the new {@code Value} to be added to this {@code HashList}.
     * @return {@code true} if the reference {@code Value} exists in this {@code HashList},
     * else {@code false} if the reference does not exist.
     */
    public boolean insertBefore(Value target, Value value) {
        Cell<Value> cell = this.map.get(target);
        if(cell == null) {
            return false;
        }
        Cell<Value> newNode = new Cell<>(value);
        newNode.prev = cell.prev;
        newNode.next = cell;
        if(cell.prev != null) {
            cell.prev.next = newNode;
        }
        cell.prev = newNode;
        return true;
    }

    /**
     * Inserts a {@code Value} after another target {@code Value} in this {@code HashList}.
     * @param target the {@code Value} to be used as a reference for placement.
     * @param value the new {@code Value} to be added to this {@code HashList}.
     * @return {@code true} if the target {@code Value} exists in this {@code HashList},
     * else {@code false} if the reference does not exist.
     */
    public boolean insertAfter(Value target, Value value) {
        Cell<Value> cell = this.map.get(target);
        if(cell == null) {
            return false;
        } else if(cell.next == null) {
            add(value);
            return true;
        } else {
            return insertBefore(cell.next.value, value);
        }
    }

    /**
     * Creates a {@code HashList} of consecutive elements from this {@code HashList}.
     * @param fromIndex the first index (inclusive) where an element is referenced.
     * @param toIndex the last index (exclusive) where an element is referenced.
     * @return the subset {@code HashList}.
     */
    public HashList<Value> subList(int fromIndex, int toIndex) {
        HashList<Value> list = new HashList<Value>();
        if(fromIndex > toIndex || fromIndex < 0 || toIndex > size()) {
            throw new IndexOutOfBoundsException();
        } else {
            Cell<Value> cursor = this.head;
            int index = 0;
            while(cursor != null) {
                if(fromIndex <= index && toIndex > index) {
                    list.add(cursor.value);
                }
                cursor = cursor.next;
                index++;
            }
        }
        return list;
    }

    /**
     * Finds the {@code Value} in the head of this {@code HashList}.
     * @return {@code this.head.value}
     */
    public Value getHead() {
        return this.head.value;
    }

    /**
     * Finds the {@code Value} in the tail of this {@code HashList}.
     * @return {@code this.tail.value}
     */
    public Value getTail() {
        return this.tail.value;
    }

    /**
     * Finds the size of this {@code HashList}.
     * @return {@code this.map.size}
     */
    public int size() {
        return this.map.size();
    }

    /**
     * Gets the index of a specific {@code Value} in this {@code HashList}.
     * @param value the target {@code Value}.
     * @return the number of nodes traversed before reaching this {@code Value},
     * else {@code -1} if this {@code Value} is not in this {@code HashList}.
     */
    public int indexOf(Value value) {
        if(value == null) {
            return -1;
        }
        Cell<Value> cursor = this.head;
        int index = 0;
        while(cursor != null) {
            if(cursor.value.equals(value)) {
                return index;
            }
            cursor = cursor.next;
            index++;
        }
        return -1;
    }

    /**
     * Determines if this {@code HashList} is empty.
     * @return {@code true} if this {@code HashList} has no head, else {@code false}
     * if the head is a valid {@code Cell}.
     */
    public boolean isEmpty() {
        return this.head == null;
    }

    /**
     * Provides the TrueText of this {@code HashList}.
     * @return this {@code HashList} in a parsable format.
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
     * Converts this {@code HashList} to a printable format.
     * @return this {@code HashList} as a {@code String}.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Cell<Value> cursor = this.head;
        while(cursor != null) {
            builder.append(", ").append(cursor.value.toString());
            cursor = cursor.next;
        }
        return "[" + (this.head == null ? "" : builder.substring(2)) + "]";
    }

    /**
     * Prints this {@code HashList}.
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * Returns an {@code Iterator} over all elements in this {@code HashList}.
     * @return the desired {@code Iterator}.
     */
    @Override
    public Iterator<Value> iterator() {
        return new Iterator<>() {
            private Cell<Value> cursor = HashList.this.head;
            private Cell<Value> prevCursor = cursor;

            /**
             * Checks if there is another unchecked element in this {@code Iterator}.
             * @return {@code true} if the {@code Iterator} has not reached all
             * elements, else {@code false} if there are no unvisited elements.
             */
            @Override
            public boolean hasNext() {
                return this.cursor != null;
            }

            /**
             * Gets the {@code Value} at the target cursor {@code Cell}.
             * @return the target {@code Value} if it exists, else {@code null}
             * if no such {@code Value} exists.
             */
            @Override
            public Value next() {
                if(this.cursor == null) {
                    throw new NoSuchElementException();
                } else {
                    Value value = this.cursor.value;
                    this.prevCursor = this.cursor;
                    this.cursor = this.cursor.next;
                    return value;
                }
            }

            /**
             * Removes the cursor from this {@code HashList}.
             * @throws IllegalStateException if {@code remove()} was called twice consecutively.
             */
            @Override
            public void remove() {
                if(this.prevCursor.equals(this.cursor)) {
                    throw new IllegalStateException();
                }
                this.prevCursor = this.cursor;
                HashList.this.remove(this.prevCursor.value);
            }
        };
    }

    /**
     * Returns a {@code ListIterator} over all elements in this {@code HashList}.
     * @return the desired {@code ListIterator}.
     */
    public ListIterator<Value> listIterator() {
        return new ListIterator<>() {
            private Cell<Value> cursor = HashList.this.head;
            private Cell<Value> prevCursor = cursor;
            private int index = 0;
            boolean removeIllegal = true, setIllegal = true;

            /**
             * Determines if this {@code ListIterator} has another element.
             * @return {@code true} if there is at least one unvisited element,
             * else {@code false} if all elements in this {@code HashList} have
             * been visited.
             */
            @Override
            public boolean hasNext() {
                return this.cursor != null;
            }

            /**
             * Gets the next element of this {@code ListIterator} if one exists.
             * @return the next {@code Value} in this {@code ListIterator}.
             * @throws NoSuchElementException when there is no next element.
             */
            @Override
            public Value next() {
                if(this.cursor == null) {
                    throw new NoSuchElementException();
                } else {
                    Value value = this.cursor.value;
                    this.prevCursor = this.cursor;
                    this.cursor = this.cursor.next;
                    this.index++;
                    this.removeIllegal = false;
                    this.setIllegal = false;
                    return value;
                }
            }

            /**
             * Determines whether there is an element in this {@code ListIterator}
             * before the one last checked.
             * @return {@code true} if such a {@code Value} exists, else {@code false}
             * if the cursor is at the beginning of this {@code HashList}.
             */
            @Override
            public boolean hasPrevious() {
                return (this.cursor == null) ? (this.prevCursor != null) : (this.cursor.prev != null);
            }

            /**
             * Gets the previous element from this {@code ListIterator}.
             * @return the previous {@code Value} if one exists.
             * @throws NoSuchElementException if there is no previous element.
             */
            @Override
            public Value previous() {
                if(this.cursor == null) {
                    this.cursor = this.prevCursor;
                } else if(this.cursor.prev == null) {
                        throw new NoSuchElementException();
                } else {
                    this.cursor = this.cursor.prev;
                }
                Value value = this.cursor.value;
                this.prevCursor = (this.cursor.prev != null) ? this.cursor.prev : this.cursor;
                this.index--;
                this.removeIllegal = false;
                this.setIllegal = false;
                return value;
            }

            /**
             * Gets the next index in this {@code ListIterator}.
             * @return {@code this.index + 1}
             */
            @Override
            public int nextIndex() {
                return this.index + 1;
            }

            /**
             * Gets the previous index in this {@code ListIterator}.
             * @return {@code this.index - 1}
             */
            @Override
            public int previousIndex() {
                return this.index - 1;
            }

            /**
             * Removes an element from this {@code ListIterator}.
             * @throws IllegalStateException if {@code add()} has been called since
             * the last {@code next()} or {@code previous()} invocation.
             */
            @Override
            public void remove() {
                this.setIllegal = true;
                if(this.removeIllegal) {
                    throw new IllegalStateException();
                }
                this.removeIllegal = true;
                HashList.this.remove(this.cursor.value);
            }

            /**
             * Replaces a {@code Value} in this {@code HashList} with a
             * different {@code Value}.
             * @param value the new {@code Value} for the {@code Cell}.
             * @throws IllegalStateException if {@code remove()} or {@code add()}
             * has been called since the last {@code next()} or {@code previous()} invocation.
             */
            @Override
            public void set(Value value) {
                if(this.setIllegal) {
                    throw new IllegalStateException();
                }
                HashList.this.insertBefore(this.prevCursor.value, value);
                HashList.this.remove(this.prevCursor.value);
            }

            /**
             * Adds an element before the cursor in this {@code HashList}.
             * @param value the new {@code Value}.
             */
            @Override
            public void add(Value value) {
                this.removeIllegal = true;
                this.setIllegal = true;
                if(this.cursor == null) {
                    HashList.this.add(value);
                    this.prevCursor = this.prevCursor.next;
                } else {
                    HashList.this.insertBefore(this.cursor.value, value);
                }
                this.index++;
            }
        };
    }

    /**
     * The node component of a {@code HashList}.
     * @param <Value> the item held in the {@code Cell}.
     */
    private static class Cell<Value> {
        private final Value value;
        private Cell<Value> next, prev;

        /**
         * Creates a new {@code Cell}.
         * @param value the item held in the {@code Cell}.
         */
        private Cell(Value value) {
            this.value = value;
        }
    }
}