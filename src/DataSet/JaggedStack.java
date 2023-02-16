package DataSet;

import java.util.*;
import Exception.*;

/**
 * Represents a {@code Stack} implementation that does not extend {@code Vector}. What distinguishes
 * {@code JaggedStack} from {@code Stack}, aside from missing the {@code Vector} methods, is that
 * {@code JaggedStack} stores its elements in a 2-D array. The existence of two dimensions allows smaller
 * arrays to be added to and removed from data storage instead of copying over the entire element to
 * increase the {@code JaggedStack} capacity. This implementation improves runtime significantly.
 * {@code JaggedStack} implements the {@code push}, {@code pop}, {@code peek}, {@code empty}, and
 * {@code search} methods from {@code Stack}.
 * @param <Data> the stored element type.
 * @see Stack
 */
public class JaggedStack<Data> implements Iterable<Data> {
    /**
     * Stores the data objects in this {@code JaggedStack}.
     */
    private final Object[][] data;

    /**
     * Stores the dimension size for the first row of this {@code JaggedStack}.
     */
    private static final int INITIAL_DIM_SIZE = 8;

    /**
     * Stores the length of the secondary array in this {@code JaggedStack}.
     */
    private static final int LENGTH = 30;

    /**
     * Stores the array indices for this {@code JaggedStack}.
     */
    private int primaryIndex, secondaryIndex, currentSize;

    /**
     * Determines whether this {@code JaggedStack} is empty ({@code true} if so, else {@code false}).
     */
    private boolean isEmpty;

    /**
     * Creates a new {@code JaggedStack}.
     */
    public JaggedStack() {
        this.currentSize = INITIAL_DIM_SIZE;
        this.data = new Object[LENGTH][];
        this.data[0] = new Object[INITIAL_DIM_SIZE];
        this.primaryIndex = -1;
        this.secondaryIndex = 0;
        this.isEmpty = true;
    }

    /**
     * Pushes a {@code Data} object onto this {@code JaggedStack}.
     * @param data the target {@code Object}
     * @return the pushed {@code Object}
     */
    public Data push(Data data) {
        this.primaryIndex++;
        if(this.primaryIndex == this.currentSize) {
            this.secondaryIndex++;
            if(this.secondaryIndex == LENGTH) {
                throw new StackOverflowError();
            }
            this.currentSize <<= 1;
            this.primaryIndex = 0;
            this.data[this.secondaryIndex] = new Object[this.currentSize];
        }
        this.isEmpty = false;
        this.data[this.secondaryIndex][this.primaryIndex] = data;
        return data;
    }

    /**
     * Retrieves the item at the top of this {@code JaggedStack}.
     * @return the top item
     * @throws EmptyStackException if this {@code JaggedStack} is empty
     */
    @SuppressWarnings(ExceptionMessage.UNCHECKED)
    public Data peek() throws EmptyStackException {
        verifyNonEmpty();
        return (Data) this.data[this.secondaryIndex][this.primaryIndex];
    }

    /**
     * Pops an item off the top of this {@code JaggedStack}.
     * @return the popped item.
     * @throws EmptyStackException if this {@code JaggedStack} was empty before popping.
     */
    @SuppressWarnings(ExceptionMessage.UNCHECKED)
    public Data pop() throws EmptyStackException {
        verifyNonEmpty();
        final Data data = (Data) this.data[this.secondaryIndex][this.primaryIndex];
        this.data[this.secondaryIndex][this.primaryIndex] = null;
        this.primaryIndex--;
        if(this.primaryIndex < 0) {
            if(this.secondaryIndex == 0) {
                this.isEmpty = true;
            } else {
                this.currentSize >>>= 1;
                this.primaryIndex += this.currentSize;
                this.data[this.secondaryIndex] = null;
                this.secondaryIndex--;
            }
        }
        return data;
    }

    /**
     * Gets the size of this {@code JaggedStack}.
     * @return the number of elements stored in this {@code JaggedStack}.
     */
    public int size() {
        if(this.secondaryIndex == 0) {
            return this.primaryIndex + 1;
        }
        return (this.currentSize << 1) + this.primaryIndex - INITIAL_DIM_SIZE;
    }

    /**
     * Searches this {@code JaggedStack} for a specified item.
     * @param o the target element.
     * @return the index of the element if it appears in this {@code JaggedStack}, else {@code -1}.
     */
    public int search(Object o) {
        int currentRowSum = INITIAL_DIM_SIZE;
        for(int i = 0; i < this.secondaryIndex; i++) {
            for(int j = 0; j < this.data[i].length; j++) {
                if(o.equals(this.data[i][j])) {
                    return currentRowSum - INITIAL_DIM_SIZE + j + 1;
                }
            }
            currentRowSum <<= 1;
        }
        for(int i = 0; i < this.primaryIndex; i++) {
            if(o.equals(this.data[this.secondaryIndex][i])) {
                return currentRowSum - INITIAL_DIM_SIZE + i + 1;
            }
        }
        return -1;
    }

    /**
     * Returns an {@code Iterator} over the elements in this {@code JaggedStack}.
     * @return the {@code Iterator}.
     */
    @Override
    public Iterator<Data> iterator() {
        return new Iterator<Data>() {
            /**
             * Stores the main array index of the cursor in this {@code JaggedStack}.
             */
            private int primaryIndex = 0;

            /**
             * Stores the secondary array index of the cursor in this {@code JaggedStack}.
             */
            private int secondaryIndex = 0;

            /**
             * Determines whether there is another element in this {@code JaggedStack}.
             * @return {@code true} if there is a next element, else {@code false}.
             */
            @Override
            public boolean hasNext() {
                if(this.secondaryIndex < JaggedStack.this.secondaryIndex) {
                    return true;
                } else if(this.secondaryIndex == JaggedStack.this.secondaryIndex) {
                    return this.primaryIndex <= JaggedStack.this.primaryIndex;
                }
                return false;
            }

            /**
             * Gets the next element in this {@code JaggedStack Iterator}.
             * @return the next element
             * @throws NoSuchElementException if there is no next element.
             */
            @Override
            @SuppressWarnings(ExceptionMessage.UNCHECKED)
            public Data next() throws NoSuchElementException {
                final Data next = (Data) JaggedStack.this.data[this.secondaryIndex][this.primaryIndex];
                this.primaryIndex++;
                if(this.primaryIndex == JaggedStack.this.data[this.secondaryIndex].length) {
                    this.primaryIndex = 0;
                    this.secondaryIndex++;
                }
                return next;
            }
        };
    }

    /**
     * Determines whether this {@code JaggedStack} is empty.
     * @return {@code true} if there are no elements in this {@code JaggedStack}, else {@code false}.
     */
    public boolean empty() {
        return this.isEmpty;
    }

    /**
     * Verifies that this {@code JaggedStack} is nonempty.
     * @throws EmptyStackException if this {@code JaggedStack} is empty.
     */
    private void verifyNonEmpty() throws EmptyStackException {
        if(this.isEmpty) {
            throw new EmptyStackException();
        }
    }

    /**
     * Converts this {@code JaggedStack} to a printable format.
     * @return this {@code JaggedStack} as a {@code String}.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        builder.append('[');
        for(int i = 0; i < this.secondaryIndex; i++) {
            for(int j = 0; j < this.data[i].length; j++) {
                builder.append(delimiter).append(this.data[i][j]);
                delimiter = ", ";
            }
        }
        for(int i = 0; i <= this.primaryIndex; i++) {
            builder.append(delimiter).append(this.data[this.secondaryIndex][i]);
        }
        builder.append(']');
        return builder.toString();
    }
}
