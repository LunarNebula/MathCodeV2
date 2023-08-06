package Research.TransitiveTournament;

import Exception.*;
import Exception.ExceptionMessage.TargetedMessage;

/**
 * <code>DiscreteCounter</code> implements a countable base-<code>n</code> object.
 */
public class DiscreteCounter {
    private final int[][] digits;
    private final int base;
    private boolean isZero;

    /**
     * Creates a new <code>DiscreteCounter</code>.
     * @param length the number of vertices
     */
    public DiscreteCounter(int length, int base) {
        this.digits = new int[length][];
        for(int i = 0; i < length; i++) {
            this.digits[i] = new int[i];
        }
        this.base = base;
        this.isZero = true;
    }

    /**
     * Creates a new <code>DiscreteCounter</code>
     * @param digits the set of digits for this <code>DiscreteCounter</code>
     * @param base the given base
     * @throws ArithmeticException if the base is less than 2
     * @throws IllegalDimensionException if the dimensions of the base vector are wrong, or
     * the digits do not conform to the given base
     */
    public DiscreteCounter(int[][] digits, int base) throws ArithmeticException, IllegalDimensionException {
        for(int i = 0; i < digits.length; i++) {
            if(digits[i].length != i) {
                throw new IllegalDimensionException(ExceptionMessage.INCORRECT_NUMBER_OF_ARGUMENTS(i));
            }
            for(int j = 0; j < i; j++) {
                if(digits[i][j] < 0 || digits[i][j] >= base) {
                    throw new IllegalDimensionException(TargetedMessage.INCORRECT_BASE(base));
                }
            }
        }
        this.digits = digits;
        this.base = base;
    }

    /**
     * Increments this <code>DiscreteCounter</code> by one. If overflow occurs, the
     * value will loop back to zero.
     */
    public void increment() {
        int rowDigit = 1, columnDigit = 0;
        boolean continueIteration = true;
        this.isZero = false;
        while(continueIteration) {
            this.digits[rowDigit][columnDigit]++;
            if(this.digits[rowDigit][columnDigit] == this.base) {
                this.digits[rowDigit][columnDigit] = 0;
                columnDigit++;
                if(columnDigit == rowDigit) {
                    columnDigit = 0;
                    rowDigit++;
                    if(rowDigit == this.digits.length) {
                        continueIteration = false;
                        this.isZero = true;
                    }
                }
            } else {
                continueIteration = false;
            }
        }
    }

    /**
     * Resets the digit at a specific location in this <code>DiscreteCounter</code>
     * @param row the row index of the new digit
     * @param column the column index of the new digit
     * @param digit the new digit
     * @throws ArrayIndexOutOfBoundsException if the row or column exceeds legal bounds
     */
    public void setDigit(int row, int column, int digit) throws ArrayIndexOutOfBoundsException {
        if(row >= this.digits.length || column >= row) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.digits[row][column] = digit;
    }

    /**
     * Gets the digits of this <code>DiscreteCounter</code>.
     * @return <code>this.digits</code>
     */
    public int[][] getDigits() {
        return this.digits;
    }

    /**
     * Gets the base of this <code>DiscreteCounter</code>
     * @return <code>this.base</code>
     */
    public int base() {
        return this.base;
    }

    /**
     * Determines whether this <code>DiscreteCounter</code> takes the value of zero
     * @return <code>this.isZero</code>
     */
    public boolean isZero() {
        return this.isZero;
    }

    /**
     * Generates a copy of this <code>DiscreteCounter</code> object
     * @return the copy
     */
    public DiscreteCounter copy() {
        DiscreteCounter copy = new DiscreteCounter(this.digits.length, this.base);
        for(int i = 0; i < this.digits.length; i++) {
            System.arraycopy(this.digits[i], 0, copy.digits[i], 0, i);
        }
        return copy;
    }

    /**
     * Determines a unique <code>int</code> value for this <code>DiscreteCounter</code>
     * @return the <code>int</code> value
     */
    @Override
    public int hashCode() {
        int hashCode = 0;
        for(int i = this.digits.length - 1; i >= 0; i--) {
            for(int j = i - 1; j >= 0; j--) {
                hashCode = hashCode * this.base + this.digits[i][j];
            }
        }
        return hashCode;
    }

    /**
     * Converts this <code>DiscreteCounter</code> to a printable format
     * @return this <code>DiscreteCounter</code> as a <code>String</code>
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String newLine = "";
        for(int i = 1; i < this.digits.length; i++) {
            builder.append(newLine);
            newLine = "\n";
            for(int j = 0; j < i; j++) {
                builder.append(this.digits[i][j]).append("\t");
            }
        }
        return builder.toString();
    }

    /**
     * Prints this <code>DiscreteCounter</code>
     */
    public void print() {
        System.out.println(this);
    }
}
