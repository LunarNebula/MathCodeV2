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
     * Prints an array of Matrices
     * @param m the array of Matrices
     */
    public static void print(Matrix... m) {
        System.out.println(toString(m));
    }
}