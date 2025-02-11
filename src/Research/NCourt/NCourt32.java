package Research.NCourt;

public class NCourt32 {
    public static void run(int[] initial, int lowerBound, int upperBound) {
        final int LENGTH = initial.length;
        int[] answer;
        final int[] masks = getMasks(initial.length);
        final int[] transpose = loadTranspose(LENGTH, initial, masks);
        final int[] indices = new int[(LENGTH * (LENGTH - 1)) >> 1];
        indices[0] = countBits(LENGTH, initial);
        boolean continueIteration = true;
        while (continueIteration) {
            if(indices[0] < upperBound && indices[0] >= lowerBound && productSum(LENGTH, initial, transpose, masks)) {
                upperBound = indices[0];
                answer = initial;
                if(upperBound == lowerBound) {
                    continueIteration = false;
                }
                General.Print.print(answer);
                System.out.println(upperBound);
            }
            continueIteration &= addOne(LENGTH, initial, transpose, indices, masks);
        }
        System.out.println("stopped");
    }

    /**
     * Gets the masks for each integer.
     * @param n the number of integers.
     * @return an array A of ints where A[i]=2^i.
     */
    public static int[] getMasks(int n) {
        final int[] indexer = new int[n];
        int index = 1;
        for(int i = 0; i < n; i++) {
            indexer[i] = index;
            index <<= 1;
        }
        return indexer;
    }

    /**
     * Counts the number of flipped bits in an int matrix.
     * @param LENGTH the length of the matrix.
     * @param matrix the target matrix.
     * @return the number of bits set to 1.
     */
    public static int countBits(int LENGTH, int[] matrix) {
        int count = 0;
        for (int row : matrix) {
            while (row != 0) {
                count += row & 1;
                row >>>= 1;
            }
        }
        return count;
    }

    /**
     * Computes the integer
     * @param LENGTH the length of the matrix.
     * @param matrix the matrix.
     * @param transpose the transpose of the matrix.
     * @param masks an array of bit masks.
     * @return {@code true} if the resulting matrix has no zero entries, else {@code false}.
     */
    public static boolean productSum(int LENGTH, int[] matrix, int[] transpose, int[] masks) {
        for(int i = 1; i < LENGTH; i++) {
            for(int j = 0; j < i; j++) {
                if(((matrix[i] & (transpose[j] | masks[j])) == 0)
                        || ((matrix[j] & (transpose[i] | masks[i])) == 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Adds one to the normal matrix and updates the transpose.
     * @param LENGTH the length of the matrix.
     * @param matrix the adjacency matrix.
     * @param transpose the transpose of the adjacency matrix.
     * @param indices the counter denoting the arrangement of the edges.
     * @param masks an array of single bit masks.
     * @return the "next" matrix.
     */
    public static boolean addOne(int LENGTH, int[] matrix, int[] transpose, int[] indices, int[] masks) {
        int row = 3, column = 1, index = 1;
        boolean carry = true;
        while(carry && row < LENGTH) {
            indices[index]++;
            if((matrix[row] & masks[column]) != 0) {
                matrix[row] = matrix[row] ^ masks[column];
                matrix[column] = matrix[column] ^ masks[row];
                transpose[row] = transpose[row] ^ masks[column];
                transpose[column] = transpose[column] ^ masks[row];
            } else if((matrix[column] & masks[row]) != 0) {
                matrix[column] = matrix[column] ^ masks[row];
                transpose[row] = transpose[row] ^ masks[column];
                indices[0]--;
            } else {
                matrix[row] = matrix[row] ^ masks[column];
                transpose[column] = transpose[column] ^ masks[row];
                indices[0]++;
            }
            if(indices[index] < 3) {
                carry = false;
            } else {
                indices[index] = 0;
                index++;
                column++;
            }
            if(column == row) {
                column = 1;
                row++;
            }
        }
        return !carry;
    }

    /**
     * Generates the transpose of an int "matrix".
     * @param LENGTH the length of the matrix.
     * @param matrix the original matrix.
     * @param masks an array of bit masks.
     * @return a new matrix with bits transposed.
     */
    public static int[] loadTranspose(int LENGTH, int[] matrix, int[] masks) {
        final int[] transpose = new int[LENGTH];
        for(int i = 0; i < LENGTH; i++) {
            for(int j = 0; j <= i; j++) {
                if((matrix[i] & masks[j]) != 0) {
                    transpose[j] |= masks[i];
                }
                if((matrix[j] & masks[i]) != 0) {
                    transpose[i] |= masks[j];
                }
            }
        }
        return transpose;
    }
}
