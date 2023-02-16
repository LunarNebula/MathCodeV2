package DataSet;

import java.util.*;
import Exception.*;
//TODO: fix later

public class Stack3<Data> {
    /**
     * Stores the data objects in this {@code Stack3}.
     */
    private final Object[][][] data;

    /**
     * Stores the dimension size for the first row of this {@code Stack3}.
     */
    private static final int INITIAL_DIM_SIZE = 8;

    /**
     * Stores the first and second dimension sizes of this {@code Stack3}.
     */
    private static final int LENGTH1 = 6, LENGTH2 = 10;

    /**
     * Stores the array indices for this {@code Stack3}.
     */
    private int primaryIndex, secondaryIndex, tertiaryIndex;

    /**
     * Stores the current sizes for each new row in this {@code Stack3}.
     */
    private int currentSizeMain, currentSizeBackup;

    /**
     * Determines whether this {@code Stack3} is empty.
     */
    private boolean isEmpty;

    /**
     * Creates a new {@code Stack3}.
     */
    public Stack3() {
        this.data = new Object[LENGTH1][][];
        this.data[0] = new Object[LENGTH2][];
        this.data[0][0] = new Object[INITIAL_DIM_SIZE];
        this.primaryIndex = -1;
        this.secondaryIndex = 0;
        this.tertiaryIndex = 0;
        this.currentSizeMain = INITIAL_DIM_SIZE;
        this.currentSizeBackup = INITIAL_DIM_SIZE;
        this.isEmpty = true;
    }

    /**
     * Pushes an element onto this {@code Stack3}.
     * @param data the specified element.
     * @return the pushed {@code Object}.
     * @throws OutOfMemoryError if the size of this {@code Stack3} becomes too large.
     */
    public Data push(Data data) throws OutOfMemoryError {
        this.primaryIndex++;
        if(this.primaryIndex == this.currentSizeMain) {
            this.primaryIndex = 0;
            final int proxy = this.currentSizeMain << 1;
            this.currentSizeMain = this.currentSizeBackup;
            this.currentSizeBackup = proxy;
            this.secondaryIndex++;
            if(this.secondaryIndex == LENGTH2) {
                this.secondaryIndex = 0;
                this.tertiaryIndex++;
                if(this.tertiaryIndex == LENGTH1) {
                    throw new OutOfMemoryError();
                }
                this.data[this.tertiaryIndex] = new Object[LENGTH2][];
            }
            this.data[this.tertiaryIndex][this.secondaryIndex] = new Object[this.currentSizeMain];
        }
        this.data[this.tertiaryIndex][this.secondaryIndex][this.primaryIndex] = data;
        this.isEmpty = false;
        return data;
    }

    /**
     * Retrieves the item at the top of this {@code Stack3}.
     * @return the top item.
     * @throws EmptyStackException if this {@code Stack3} is empty.
     */
    @SuppressWarnings(ExceptionMessage.UNCHECKED)
    public Data peek() throws EmptyStackException {
        verifyNonEmpty();
        return (Data) this.data[this.tertiaryIndex][this.secondaryIndex][this.primaryIndex];
    }

    /**
     * Pops an item off the top of this {@code Stack3}.
     * @return the popped item.
     * @throws EmptyStackException if this {@code Stack3} was empty before popping.
     */
    @SuppressWarnings(ExceptionMessage.UNCHECKED)
    public Data pop() throws EmptyStackException {
        verifyNonEmpty();
        final Data data = (Data) this.data[this.tertiaryIndex][this.secondaryIndex][this.primaryIndex];
        this.data[this.tertiaryIndex][this.secondaryIndex][this.primaryIndex] = null;
        this.primaryIndex--;
        if(this.primaryIndex < 0) {
            final int proxy = this.currentSizeBackup >>> 1;
            this.currentSizeBackup = this.currentSizeMain;
            this.currentSizeMain = proxy;
            this.primaryIndex += this.currentSizeMain;
            this.data[this.tertiaryIndex][this.secondaryIndex] = null;
            this.secondaryIndex--;
            if(this.secondaryIndex < 0) {
                if(this.tertiaryIndex == 0) {
                    this.secondaryIndex = 0;
                    this.primaryIndex = -1;
                    this.data[0][0] = new Object[INITIAL_DIM_SIZE];
                    this.isEmpty = true;
                } else {
                    this.secondaryIndex += LENGTH2;
                    this.data[this.tertiaryIndex] = null;
                    this.tertiaryIndex--;
                }
            }
        }
        return data;
    }

    /**
     * Asserts that this {@code Stack3} is empty.
     * @throws EmptyStackException if there are no elements in this {@code Stack3}.
     */
    private void verifyNonEmpty() throws EmptyStackException {
        if(this.isEmpty) {
            throw new EmptyStackException();
        }
    }

    /**
     * Converts this {@code Stack3} to a printable format.
     * @return this {@code Stack3} as a {@code String}.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        String delimiter = "";
        for(int i = 0; i < this.tertiaryIndex; i++) {
            for(int j = 0; j < this.data[i].length; j++) {
                for(int k = 0; k < this.data[i][j].length; k++) {
                    builder.append(delimiter).append(this.data[i][j][k]);
                    delimiter = ", ";
                }
            }
        }
        for(int i = 0; i < this.secondaryIndex; i++) {
            for(int j = 0; j < this.data[this.tertiaryIndex][i].length; j++) {
                builder.append(delimiter).append(this.data[this.tertiaryIndex][i][j]);
                delimiter = ", ";
            }
        }
        for(int i = 0; i <= this.primaryIndex; i++) {
            builder.append(delimiter).append(this.data[this.tertiaryIndex][this.secondaryIndex][i]);
            delimiter = ", ";
        }
        builder.append(']');
        return builder.toString();
    }

    /**
     * Prints this {@code Stack3} as a layered {@code String}.
     * @return the {@code String}.
     */
    public String toLayeredString() {
        StringBuilder builder = new StringBuilder();
        builder.append("-----[");
        for(int i = 0; i < LENGTH1; i++) {
            builder.append("\n{").append(i).append('}');
            if(this.data[i] == null) {
                builder.append("\nnull");
            } else {
                for(int j = 0; j < this.data[i].length; j++) {
                    builder.append("\n\t{").append(j).append('}');
                    for(int k = 0; k < this.data[i].length; k++) {
                    }
                }
            }
        }
        builder.append("\n]-----");
        return builder.toString();
    }
}
