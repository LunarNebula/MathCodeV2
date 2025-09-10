package Research.MatrixFactors;

import Algebra.Fraction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MatrixFactors {
    public static List<String> getRoots(int size, int N, int[] elements) {
        final int[][] matrix = new int[size][];
        fillMatrix(matrix, elements[0]);
        final int LENGTH = size * size;
        final int[] indices = new int[LENGTH];
        int index = 0;
        while(index < LENGTH) {
        }
        return null;
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
