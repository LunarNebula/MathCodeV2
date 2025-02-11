package Research.NCourt;

import General.TrueTextEncodable;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code OrMatrix} is a mathematical object that behaves like a matrix with boolean elements,
 * with the exception that 1+1=1. As this object is not intended for use outside {@code Research.NCourt}
 * research, the standard dimension checks built into {@code Matrix} are not included in this
 * class - it is assumed that all matrices are square and the same size.
 */
public class OrMatrix implements TrueTextEncodable {
    protected final boolean[][] e;

    /**
     * Creates a new {@code OrMatrix}.
     * @param size the size of the {@code OrMatrix} (row/column count).
     */
    public OrMatrix(int size) {
        this.e = new boolean[size][size];
    }

    /**
     * Creates a new {@code BinaryMatrix}.
     * @param e the elements of the new matrix.
     */
    public OrMatrix(boolean[][] e) {
        this.e = e;
    }

    /**
     * Creates a new {@code OrMatrix} using the elements of another.
     * @param om the model object.
     */
    public OrMatrix(OrMatrix om) {
        final int LENGTH = om.e.length;
        this.e = new boolean[LENGTH][];
        for(int i = 0; i < LENGTH; i++) {
            this.e[i] = new boolean[LENGTH];
            System.arraycopy(om.e[i], 0, this.e[i], 0, LENGTH);
        }
    }

    /**
     * Creates a new {@code BinaryMatrix}.
     * @param str the {@code String} containing the elements of the matrix.
     */
    public OrMatrix(String str) {
        this.e = General.Converter.convertTo2DBooleanArray(str);
    }

    /**
     * Adds I to this {@code BinaryMatrix}.
     * @return {@code this + I}
     */
    public OrMatrix addI() {
        final int LENGTH = this.e.length;

        return add(I(this.e.length));
    }

    /**
     * Checks whether this {@code OrMatrix} is a zero matrix.
     * @return {@code false} if any element is equal tp {@code true}, else {@code false}.
     */
    public boolean isZero() {
        for(boolean[] row : this.e) {
            for(boolean item : row) {
                if(item) return false;
            }
        }
        return true;
    }

    /**
     * Checks whether this {@code OrMatrix} has any zero elements.
     * @return {@code true} if any element is zero, else {@code false}.
     */
    public boolean hasZeros() {
        for(boolean[] row : this.e) {
            for(boolean item : row) {
                if(! item) return true;
            }
        }
        return false;
    }

    /**
     * Finds the inner vector product of a given row and column in two OrMatrices.
     * @param r the row of this {@code OrMatrix}.
     * @param b the other {@code OrMatrix}.
     * @param c the column of {@code b}.
     * @return the element of {@code this * b} in row {@code r} and column {@code c}.
     */
    protected boolean getProductElement(int r, OrMatrix b, int c) {
        for(int i = 0; i < this.e.length; i++) {
            if(this.e[r][i] && b.e[i][c]) return true;
        }
        return false;
    }

    /**
     * Multiplies this {@code OrMatrix} by another {@code OrMatrix}.
     * @param multiplicand the multiplicand {@code OrMatrix}.
     * @return {@code this * multiplicand}
     */
    public OrMatrix multiply(OrMatrix multiplicand) {
        return multiply(multiplicand, false, false).getKey();
    }

    /**
     * Multiplies this {@code OrMatrix} by another and determines whether the new
     * matrix has off-diagonal zeros.
     * @param multiplicand the multiplication {@code OrMatrix}.
     * @param detectZeros {@code true} if the program should detect zero elements,
     *                                else {@code false}.
     * @param cancelZero {@code true} if the multiplication should stop if a zero is
     *                               detected off the main diagonal, else {@code false}.
     * @return a pair containing {@code this * multiplicand} and a boolean valued {@code true}
     * if {@code this * multiplicand} contains an off-diagonal zero, else {@code false}.
     */
    public Map.Entry<OrMatrix, Boolean> multiply(OrMatrix multiplicand,
                                                 boolean detectZeros, boolean cancelZero) {
        final int LENGTH = this.e.length;
        boolean hasZeros = false;
        final boolean[][] e = new boolean[LENGTH][];
        for(int i = 0; i < LENGTH; i++) {
            e[i] = new boolean[LENGTH];
            for(int j = 0; j < LENGTH; j++) {
                e[i][j] = getProductElement(i, multiplicand, j);
                if(detectZeros && i != j && !e[i][j]) {
                    hasZeros = true;
                    if(cancelZero) {
                        i = LENGTH;
                        j = LENGTH;
                    }
                }
            }
        }
        return Map.entry(new OrMatrix(e), hasZeros);
    }

    /**
     * Adds two {@code OrMatrices}.
     * @param addend the addend {@code OrMatrix}.
     * @return {@code this + addend}
     */
    public OrMatrix add(OrMatrix addend) {
        final int LENGTH = this.e.length;
        final boolean[][] e = new boolean[LENGTH][];
        for(int i = 0; i < LENGTH; i++) {
            e[i] = new boolean[LENGTH];
            for(int j = 0; j < LENGTH; j++) {
                e[i][j] = this.e[i][j] | addend.e[i][j];
            }
        }
        return new OrMatrix(e);
    }

    /**
     * Raises this {@code OrMatrix} to an Integer power.
     * @param pow the exponent.
     * @return {@code this ^ pow}
     */
    public OrMatrix pow(int pow) {
        if(pow <= 0) {
            return I(this.e.length);
        }
        OrMatrix antilogarithm = I(this.e.length);
        int powTest = Math.abs(pow), index = 0;
        boolean[] powers = new boolean[Integer.SIZE];
        while(powTest > 0) {
            powers[index++] = (powTest & 1) == 1;
            powTest >>>= 1;
        }
        while(index > 0) {
            index--;
            antilogarithm = antilogarithm.multiply(antilogarithm);
            if(powers[index]) {
                antilogarithm = multiply(antilogarithm);
            }
        }
        return antilogarithm;
    }

    /**
     * Finds the transpose of this {@code OrMatrix}.
     * @return a new {@code OrMatrix} with the same elements as this one, reflected over
     * the main diagonal.
     */
    public OrMatrix transpose() {
        final int LENGTH = this.e.length;
        final boolean[][] e = new boolean[LENGTH][LENGTH];
        e[0][0] = this.e[0][0];
        for(int i = 1; i < LENGTH; i++) {
            e[i][i] = this.e[i][i];
            for(int j = 0; j < i; j++) {
                e[i][j] = this.e[j][i];
                e[j][i] = this.e[i][j];
            }
        }
        return new OrMatrix(e);
    }

    /**
     * Sets a bit.
     * @param r the row.
     * @param c the column.
     * @param bit the new bit value.
     */
    public void setBit(int r, int c, boolean bit) {
        this.e[r][c] = bit;
    }

    /**
     * Gets a particular element of this {@code OrMatrix}.
     * @param r the row index.
     * @param c the column index.
     * @return {@code this.e[r][c]}
     */
    public boolean getBit(int r, int c) {
        return this.e[r][c];
    }

    /**
     * Provides the TrueText of this {@code BinaryMatrix}.
     * @return this {@code BinaryMatrix} in a parsable format.
     */
    @Override
    public String trueText() {
        final StringBuilder builder = new StringBuilder();
        boolean del2 = false;
        for(boolean[] row : this.e) {
            if(del2) {
                builder.append(';');
            }
            del2 = true;
            boolean del1 = false;
            for(boolean item : row) {
                if(del1) {
                    builder.append(',');
                }
                del1 = true;
                builder.append(item ? 1 : 0);
            }
        }
        return builder.toString();
    }

    /**
     * Compares this {@code OrMatrix} with another object.
     * @param obj the comparator object.
     * @return {@code true} if {@code obj} is an {@code OrMatrix} with the same dimensions
     * and elements, else {@code false}.
     */
    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof OrMatrix convert) || convert.e.length != this.e.length) {
            return false;
        }
        for(int i = 0; i < this.e.length; i++) {
            for(int j = 0; j < this.e.length; j++) {
                if(convert.e[i][j] ^ this.e[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Converts this {@code BinaryMatrix} to a printable format.
     * @return this {@code BinaryMatrix} as a {@code String}.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        int rowNum = 1;
        String lineSplit = "";
        for(boolean[] row : this.e) {
            builder.append(lineSplit).append("[");
            lineSplit = "\n";
            for(boolean item : row) {
                builder.append(item ? 1 : 0).append(" ");
            }
            builder.append("] ").append(rowNum++);
        }
        return builder.toString();
    }

    /**
     * Prints this {@code BinaryMatrix}.
     */
    public void print() {
        System.out.println(this);
    }

    // Static methods

    /**
     * Creates a new "identity" {@code OrMatrix}.
     * @param size the size of the matrix.
     * @return an {@code OrMatrix} with an all-1s diagonal.
     */
    public static OrMatrix I(int size) {
        final boolean[][] e = new boolean[size][size];
        for(int i = 0; i < size; i++) {
            e[i][i] = true;
        }
        return new OrMatrix(e);
    }
}
