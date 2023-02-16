package DataSet;

import General.TrueTextEncodable;

import java.util.HashMap;
import java.util.Iterator;

public class LinkedHashTape<Value> implements Iterable<Value>, TrueTextEncodable {
    protected HashMap<Integer, Cell<Value>> tape;
    protected int start, end, memoryStart, memoryEnd;

    /**
     * Creates a new LinkedHashTape
     */
    public LinkedHashTape() {
        this.tape = new HashMap<>();
        clear();
    }

    /**
     * Resets the values in this LinkedHashTape
     */
    public void clear() {
        this.start = 0;
        this.end = 0;
        this.memoryStart = 0;
        this.memoryEnd = 0;
        this.tape.clear();
    }

    /**
     * Adds a Value to the beginning of this LinkedHashTape
     * @param value the added Value
     */
    public void addToStart(Value value) {
        if(this.tape.size() == 0) {
            setOriginalValue(value);
        } else {
            Cell<Value> cell = new Cell<>(value), neighbor = this.tape.get(this.start);
            cell.next = neighbor;
            neighbor.prev = cell;
            this.start--;
            this.tape.put(this.start, cell);
            crop();
        }
    }

    /**
     * Adds a Value to the beginning of this LinkedHashTape
     * @param value the added Value
     */
    public void addToEnd(Value value) {
        if(this.tape.size() == 0) {
            setOriginalValue(value);
        } else {
            Cell<Value> cell = new Cell<>(value), neighbor = this.tape.get(this.end);
            cell.prev = neighbor;
            neighbor.next = cell;
            this.end++;
            this.tape.put(this.end, cell);
            crop();
        }
    }

    /**
     * Sets the first Value in this empty LinkedHashTape
     * @param value the initial Value in this LinkedHashTape
     */
    private void setOriginalValue(Value value) {
        this.tape.put(this.start, new Cell<>(value));
    }

    /**
     * Gets the Value at a specific index
     * @param index the target index
     * @return the Value
     * @throws IndexOutOfBoundsException if the specified index does not occur in this LinkedHashTape
     */
    public Value get(int index) {
        verifyLegalIndex(index);
        crop();
        return this.tape.get(index).value;
    }

    /**
     * Resets a Value
     * @param index the index to be changed
     * @param value the new Value
     * @throws IndexOutOfBoundsException if the specified index does not occur in this LinkedHashTape
     */
    public void set(int index, Value value) {
        verifyLegalIndex(index);
        this.tape.get(index).value = value;
        crop();
    }

    /**
     * Checks whether the index is legal
     * @param index the target index
     * @throws IndexOutOfBoundsException if the specified index does not exist in this LinkedHashTape
     */
    private void verifyLegalIndex(int index) {
        if(index < this.start || index > this.end || this.tape.get(0) == null) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Attempts to remove an element from the start of this LinkedHashTape
     * @return true if the previous size of this LinkedHashTape was nonzero, else false
     */
    public boolean removeFromStart() {
        Cell<Value> cell = this.tape.get(this.start);
        if(cell == null) {
            return false;
        }
        Cell<Value> neighbor = cell.next;
        if(neighbor != null) {
            neighbor.prev = null;
        }
        this.tape.remove(this.start);
        if(this.start < this.end) {
            this.start++;
            crop();
        } else {
            clear();
        }
        return true;
    }

    /**
     * Attempts to remove an element from the start of this LinkedHashTape
     * @return true if the previous size of this LinkedHashTape was nonzero, else false
     */
    public boolean removeFromEnd() {
        Cell<Value> cell = this.tape.get(this.end);
        if(cell == null) {
            return false;
        }
        Cell<Value> neighbor = cell.prev;
        if(neighbor != null) {
            neighbor.next = null;
        }
        this.tape.remove(this.end);
        if(this.start < this.end) {
            this.end--;
            crop();
        } else {
            clear();
        }
        return true;
    }

    /**
     * Clips the start of this LinkedHashTape at a specified point
     * @param index the target index
     * @throws IndexOutOfBoundsException if the specified index occurs outside the bounds of this LinkedHashTape
     */
    public void clipStart(int index) {
        verifyLegalIndex(index);
        this.start = index;
        crop();
    }

    /**
     * Clips the end of this LinkedHashTape at a specified point
     * @param index the target index
     * @throws IndexOutOfBoundsException if the specified index occurs outside the bounds of this LinkedHashTape
     */
    public void clipEnd(int index) {
        verifyLegalIndex(index);
        this.end = index;
        crop();
    }

    /**
     * Crops any unused ends of the LinkedHashTape. A single iteration of this method removes only one item from each end.
     */
    public void crop() {
        if(this.memoryStart >= this.start) {
            this.memoryStart = this.start;
        } else {
            Cell<Value> cell = this.tape.get(this.memoryStart);
            cell.next.prev = null;
            this.tape.remove(this.memoryStart);
            this.memoryStart++;
        }
        if(this.memoryEnd <= this.end) {
            this.memoryEnd = this.end;
        } else {
            Cell<Value> cell = this.tape.get(this.memoryEnd);
            cell.prev.next = null;
            this.tape.remove(this.memoryEnd);
            this.memoryEnd--;
        }
    }

    /**
     * Finds the starting index of this LinkedHashTape
     * @return this.start
     */
    public int getStartIndex() {
        return this.start;
    }

    /**
     * Finds the end index of this LinkedHashTape
     * @return this.end
     */
    public int getEndIndex() {
        return this.end;
    }

    /**
     * Gets the size of this LinkedHashTape
     * @return the inclusive difference between the start and end
     */
    public int length() {
        crop();
        return this.end - this.start + 1;
    }

    /**
     * Returns an Iterator over the Values in this LinkedHashTape
     * @return the Iterator
     */
    public Iterator<Value> iterator() {
        return new Iterator<>() {
            private int index = LinkedHashTape.this.start;
            private Cell<Value> cursor = LinkedHashTape.this.tape.get(this.index);

            /**
             * Determines whether there is another element in this LinkedHashTape Iterator
             * @return true if another element exists, else false
             */
            @Override
            public boolean hasNext() {
                return this.index <= LinkedHashTape.this.end;
            }

            /**
             * Gets the next Value in this LinkedHashTape
             * @return the target Value
             */
            @Override
            public Value next() {
                if(! hasNext()) {
                    throw new IllegalStateException();
                }
                Value value = this.cursor.value;
                this.cursor = this.cursor.next;
                this.index++;
                return value;
            }
        };
    }

    /**
     * Returns a descending Iterator over the Values in this LinkedHashTape
     * @return the Iterator
     */
    public Iterator<Value> descendingIterator() {
        return new Iterator<>() {
            private int index = LinkedHashTape.this.end;
            private Cell<Value> cursor = LinkedHashTape.this.tape.get(this.index);

            /**
             * Determines whether there is another element in this LinkedHashTape Iterator
             * @return true if another element exists, else false
             */
            @Override
            public boolean hasNext() {
                return this.index >= LinkedHashTape.this.start;
            }

            /**
             * Gets the next Value in this LinkedHashTape
             * @return the target Value
             */
            @Override
            public Value next() {
                if(! hasNext()) {
                    throw new IllegalStateException();
                }
                Value value = this.cursor.value;
                this.cursor = this.cursor.prev;
                this.index--;
                return value;
            }
        };
    }

    /**
     * Provides the TrueText of this LinkedHashTape
     * @return this LinkedHashTape in a parsable format
     */
    @Override
    public String trueText() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        Cell<Value> cursor = this.tape.get(this.start);
        while(cursor != null) {
            builder.append(delimiter).append(cursor.value);
            delimiter = "|";
            cursor = cursor.next;
        }
        return builder.toString();
    }

    /**
     * Converts this LinkedHashTape to a printable format
     * @return this LinkedHashTape as a String
     */
    @Override
    public String toString() {
        String print = "";
        for(Value value : this) {
            String textValue = "null";
            if(value != null) {
                textValue = value.toString();
            }
            print = print.concat(", ").concat(textValue);
        }
        if(print.length() > 0) {
            print = print.substring(2);
        }
        crop();
        return "[" + print + "]";
    }

    /**
     * Prints this LinkedHashTape
     */
    public void print() {
        System.out.println(this);
    }

    /**
     * A cell of the LinkedHashTape
     * @param <Value>
     */
    private static class Cell<Value> {
        private Value value;
        private Cell<Value> next, prev;

        /**
         * Creates a new Cell
         * @param value the value of this Cell
         */
        public Cell(Value value) {
            this.value = value;
        }
    }
}
