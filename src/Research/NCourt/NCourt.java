package Research.NCourt;

import Enumerator.Radix;
import Enumerator.UnsignedInt;

import java.util.List;
import java.util.Map;

public class NCourt {
    public static void run(OrMatrix initial, OrMatrix mask, int upperBound, int lowerBound) {
        final int SIZE = mask.e.length;
        final OrMatrix om = new OrMatrix(initial);
        OrMatrix answer = new OrMatrix(0);
        final int[][] rowCols = counters(mask, initial);
        final int[] indices = new int[rowCols[0].length];
        indices[0] = rowCols[0][0] + rowCols[1][0];
        int max = upperBound;
        boolean continueIteration = true;
        while(continueIteration) {
            if(indices[0] < max && indices[0] >= lowerBound) {
                Map.Entry<OrMatrix, Boolean> entry = om.multiply(om.addI(), true, true);
                if(!entry.getValue()) {
                    max = indices[0];
                    answer = new OrMatrix(om);
                    if(max == lowerBound) {
                        continueIteration = false;
                    }
                    System.out.println(answer);
                    System.out.println(answer.trueText());
                    System.out.println(max);
                }
            }
            continueIteration &= addOne(om, indices, rowCols);
        }
        answer.print();
        System.out.println(max);
    }

    /**
     * Creates the row and column index counters.
     * @param mask the set of indices considered fixed by the program.
     * @param initial the initial set of 1s in the {@code OrMatrix}.
     * @return the array of (row, column) pairs for the program to iterate over.
     */
    public static int[][] counters(OrMatrix mask, OrMatrix initial) {
        final int LENGTH = mask.e.length;
        final int[][] vals = new int[2][LENGTH * LENGTH]; // one each for row and column
        int row = 1, column = 0, validCounter = 0, topCounter = 0, bottomCounter = 0;
        while(row < LENGTH) {
            if(mask.e[row][column] || mask.e[column][row]) {
                if(mask.e[row][column] ^ mask.e[column][row]) {
                    throw new IllegalStateException("Mask matrix not symmetric at (" + row + "," + column + ")");
                }
            } else {
                vals[0][validCounter] = row;
                vals[1][validCounter] = column;
                validCounter++;
            }
            if(initial.e[row][column]) {
                if(initial.e[column][row]) {
                    throw new IllegalStateException("Double edge at (" + row + "," + column + ")");
                } else {
                    bottomCounter++;
                }
            } else if(initial.e[column][row]) {
                topCounter++;
            }
            row++;
            column++;
            if(LENGTH == row) {
                row -= column - 1;
                column = 0;
            }
        }
        final int[][] counters = new int[2][validCounter + 1];
        counters[0][0] = bottomCounter;
        counters[1][0] = topCounter;
        System.arraycopy(vals[0], 0, counters[0], 1, validCounter);
        System.arraycopy(vals[1], 0, counters[1], 1, validCounter);
        return counters;
    }

    /**
     * Adds one to the index counter and toggles the {@code OrMatrix} to the next mode.
     * @param om the {@code OrMatrix}.
     * @param indices the set of indices indicating the status of each matrix element.
     * @param rowCols the row and column indices to iterate over.
     * @return {@code true} if all possible indices have been exhausted, else {@code false}.
     */
    public static boolean addOne(OrMatrix om, int[] indices, int[][] rowCols) {
        int index = 1;
        boolean carry = true;
        while(carry && index < indices.length) {
            indices[index]++;
            if(om.e[rowCols[0][index]][rowCols[1][index]]) { // 10
                om.e[rowCols[0][index]][rowCols[1][index]] = false;
                om.e[rowCols[1][index]][rowCols[0][index]] = true;
            } else if(om.e[rowCols[1][index]][rowCols[0][index]]) { // 01
                om.e[rowCols[1][index]][rowCols[0][index]] = false;
                indices[0]--;
            } else { // 00
                om.e[rowCols[0][index]][rowCols[1][index]] = true;
                indices[0]++;
            }
            if(indices[index] < 3) {
                carry = false;
            } else {
                indices[index] = 0;
                index++;
            }
        }
        return !carry;
    }

    /**
     * Switches the element of the adjacency {@code OrMatrix} depending on the index.
     * @param om the {@code OrMatrix}.
     * @param r the row.
     * @param c the column.
     * @param indices the array whose first element indicates the number of edges.
     */
    public static void toggleElementMode(OrMatrix om, int r, int c, int[] indices) {
        if(om.e[r][c]) { // 10
            om.e[r][c] = false;
            om.e[c][r] = true;
        } else if(om.e[c][r]) { // 01
            om.e[c][r] = false;
            indices[0]--;
        } else { // 00
            om.e[r][c] = true;
            indices[0]++;
        }
    }
}
