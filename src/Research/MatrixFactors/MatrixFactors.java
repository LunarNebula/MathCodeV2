package Research.MatrixFactors;

import Algebra.Fraction;

import java.util.*;

public class MatrixFactors {
    public static List<String> get3Roots(int size, int N, int[] elements) {
        final int[][] matrix = new int[size][];
        fillMatrix(matrix, elements[0]);
        final int LENGTH = size * size;
        final int[] indices = new int[LENGTH];
        Arrays.fill(indices, 1);
        final Stack<Integer> indexStack = new Stack<>();
        indexStack.push(0);
        while(! indexStack.isEmpty()) {
            int index = indexStack.pop();
        }
        return null;
    }

    public static boolean isCubeIdentity(int[][] matrix, int len, int N) {
        for(int i = 0; i < len; i++) {
            for(int j = 0; j < len; j++) {
                int element = 0;
                for(int r1 = 0; r1 < len; r1++) {
                    int subfactor = 1;
                    for(int r2 = 0; r2 < len; r2++) {
                        subfactor *= matrix[r1][r2] * matrix[r2][j];
                    }
                    element += subfactor * matrix[i][r1];
                }
                if(i == j) {
                    if(element != N) {
                        return false;
                    }
                } else if(element != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void fillMatrix(int[][] matrix, int element) {
        Arrays.fill(matrix[0], element);
        for(int i = 1; i < matrix.length; i++) {
            System.arraycopy(matrix[0], 0, matrix[i], 0, matrix[i].length);
        }
    }

    public static String matrixToString(int[][] matrix) {
        final List<Integer> elementLengths = new LinkedList<>();
        for (int[] matrixRow : matrix) {
            final ListIterator<Integer> elementIterator = elementLengths.listIterator();
            for (int matrixElement : matrixRow) {
                final int length = ("" + matrixElement).length();
                if (elementIterator.hasNext()) {
                    final int currentLength = elementIterator.next();
                    if (currentLength < length) {
                        elementIterator.remove();
                        elementIterator.add(length);
                    }
                } else {
                    elementIterator.add(length);
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        String newLine = "";
        int rowNumber = 1;
        for (int[] matrixRow : matrix) {
            StringBuilder row = new StringBuilder();
            final ListIterator<Integer> elementIterator = elementLengths.listIterator();
            for (int matrixElement : matrixRow) {
                String element = matrixElement + "";
                final int length = elementIterator.next();
                while (element.length() <= length) {
                    element = element.concat(" ");
                }
                row.append(element);
            }
            builder.append(newLine).append("[").append(row).append("] ").append(rowNumber++);
            newLine = "\n";
        }
        return builder.toString();
    }
}
