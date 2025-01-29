package General;

import java.math.BigInteger;

public class Converter {
    /**
     * Converts a String to a BigInteger
     * @param s the String
     * @return the parsed BigInteger
     */
    public static BigInteger convertToBigInteger(String s) {
        return BigInteger.valueOf(Long.parseLong(s));
    }

    /**
     * Converts a String to an array of BigIntegers
     * @param s the String
     * @param del1D the delimiting string
     * @return the parsed array of BigIntegers
     */
    public static BigInteger[] convertToBigIntegerArray(String s, String del1D) {
        String[] parse = s.split(del1D);
        BigInteger[] parsedNumber = new BigInteger[parse.length];
        for(int i = 0; i < parse.length; i++) {
            parsedNumber[i] = convertToBigInteger(parse[i]);
        }
        return parsedNumber;
    }

    /**
     * Converts a String to a 2D array of BigIntegers
     * @param s the String
     * @param del1D the first-degree delimiter
     * @param del2D the second-degree delimiter
     * @return the parsed 2D array of BigIntegers
     */
    public static BigInteger[][] convertTo2DBigIntegerArray(String s, String del1D, String del2D) {
        String[] parse = s.split(del2D);
        BigInteger[][] parsedNumber = new BigInteger[parse.length][];
        for(int i = 0; i < parse.length; i++) {
            parsedNumber[i] = convertToBigIntegerArray(parse[i], del1D);
        }
        return parsedNumber;
    }

    /**
     * Converts a String to a 3D array of BigIntegers
     * @param s the String
     * @param del1D the first-degree delimiter
     * @param del2D the second-degree delimiter
     * @param del3D the third-degree delimiter
     * @return the parsed 3D array of BigIntegers
     */
    public static BigInteger[][][] convertTo3DBigIntegerArray(String s, String del1D, String del2D, String del3D) {
        String[] parse = s.split(del3D);
        BigInteger[][][] parsedNumber = new BigInteger[parse.length][][];
        for(int i = 0; i < parse.length; i++) {
            parsedNumber[i] = convertTo2DBigIntegerArray(parse[i], del1D, del2D);
        }
        return parsedNumber;
    }

    /**
     * Converts a String to a BigInteger fraction-delimited coordinate set
     * @param s the target String
     * @return the parsed BigInteger coordinates
     */
    public static BigInteger[][] convertToCoordinates(String s) {
        String[] parse = s.split(","); // uses comma as a coordinate delimiter
        BigInteger[][] coordinates = new BigInteger[parse.length][];
        for(int i = 0; i < parse.length; i++) {
            coordinates[i] = convertToBigIntegerArray(parse[i], "/");
        }
        return coordinates;
    }

    /**
     * Converts a String to a list of BigInteger coordinates
     * @param s the target String
     * @return the parsed BigInteger coordinates
     */
    public static BigInteger[][][] convertToCoordinateList(String s) {
        String[] parse = s.split(";");
        BigInteger[][][] points = new BigInteger[parse.length][][];
        for(int i = 0; i < parse.length; i++) {
            points[i] = convertToCoordinates(parse[i]);
        }
        return points;
    }

    /**
     * Converts an array of {@code ints} to an array of {@code Integers}.
     * @param n the array of {@code ints}.
     * @return the converted array.
     */
    public static Integer[] convertToArrayOfIntegers(int... n) {
        final Integer[] ar = new Integer[n.length];
        for(int i = 0; i < ar.length; i++) {
            ar[i] = n[i];
        }
        return ar;
    }

    /**
     * Converts a string into an array of {@code booleans}. This method accepts
     * "1", "T", or "true" as {@code true}, and all other values as {@code false}.
     * @param s the {@code String}.
     * @return the array of {@code booleans}.
     */
    public static boolean[] convertToBooleanArray(String s) {
        String[] parse = s.split(",");
        final boolean[] ar = new boolean[parse.length];
        for(int i = 0; i < ar.length; i++) {
            ar[i] = parse[i].equals("1") || parse[i].equals("T") || parse[i].equals("true");
        }
        return ar;
    }

    /**
     * Converts a string into a 2D array of {@code booleans}. This method accepts
     * "1", "T", or "true" as {@code true}, and all other values as {@code false}.
     * @param s the {@code String}.
     * @return the array of {@code booleans}.
     */
    public static boolean[][] convertTo2DBooleanArray(String s) {
        String[] parse = s.split(";");
        final boolean[][] ar = new boolean[parse.length][];
        for(int i = 0; i < ar.length; i++) {
            ar[i] = convertToBooleanArray(parse[i]);
        }
        return ar;
    }
}