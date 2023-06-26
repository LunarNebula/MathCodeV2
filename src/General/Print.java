package General;

import Algebra.*;
import Geometry.Point2D;
import java.math.BigInteger;

public class Print {
    /**
     * Converts an array of BigIntegers to a printable format
     * @param b the array of BigIntegers
     * @return the BigIntegers in String format
     */
    public static String toString(BigInteger... b) {
        String print = "";
        for(BigInteger bigInt : b) {
            print = print.concat(", " + bigInt);
        }
        return print.substring(print.length() > 0 ? 2 : 0);
    }

    /**
     * Prints an array of BigIntegers
     * @param b the target array
     */
    public static void print(BigInteger... b) {
        System.out.println(toString(b));
    }

    /**
     * Converts an array of Point2Ds to a printable format
     * @param p the array of Point2Ds
     * @return the coordinates in String format
     */
    public static String toString(Point2D... p) {
        String print = "";
        for(Point2D point : p) {
            print = print.concat(",").concat(point.toString());
        }
        return print.substring(print.length() > 0 ? 1 : 0);
    }

    /**
     * Prints an array of Point2Ds
     * @param p the array of coordinates
     */
    public static void print(Point2D... p) {
        System.out.println(toString(p));
    }

    /**
     * Converts an array of Matrices to a printable format
     * @param m the array of Matrices
     * @return the Matrices as a String
     */
    public static String toString(Matrix... m) {
        String print = "";
        for(Matrix matrix : m) {
            print = print.concat("\n").concat(matrix.toString());
        }
        return print.substring(print.length() > 0 ? 1 : 0);
    }

    /**
     * Prints an array of {@code Matrices}.
     * @param m the array of {@code Matrices}.
     */
    public static void print(Matrix... m) {
        System.out.println(toString(m));
    }

    /**
     * Converts a 2D array of integers to a printable format.
     * @param n the array.
     * @return the array of integers as a {@code String}.
     */
    public static String toString(int[]... n) {
        final StringBuilder builder = new StringBuilder();
        String rowDelimiter = "";
        for(int[] row : n) {
            String delimiter = "";
            builder.append(rowDelimiter).append("[");
            rowDelimiter = "\n";
            for(int i : row) {
                builder.append(delimiter).append(i);
                delimiter = ", ";
            }
            builder.append("]");
        }
        return builder.toString();
    }

    /**
     * Prints a 2D array of integers.
     * @param n the array.
     */
    public static void print(int[]... n) {
        System.out.println(toString(n));
    }
}