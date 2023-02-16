package Geometry;

import Algebra.Fraction;
import Exception.*;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class Point2D {
    public static Point2D ORIGIN = new Point2D(Fraction.ZERO, Fraction.ZERO);
    private final Fraction x;
    private final Fraction y;

    /**
     * Constructs a new Point2D from the given coordinates
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Point2D(Fraction x, Fraction y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new Point2D from the given coordinates
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Point2D(BigInteger x, BigInteger y) {
        this.x = new Fraction(x);
        this.y = new Fraction(y);
    }

    /**
     * Constructs a new Point2D from a given String listing the coordinates
     * @param s the String containing constructor information
     * @throws IllegalArgumentException if the number of arguments encoded in the String is not equal to 2
     */
    public Point2D(String s) throws IllegalArgumentException {
        final BigInteger[][] coordinates = General.Converter.convertToCoordinates(s);
        if(coordinates.length != 2) {
            throw new IllegalArgumentException(ExceptionMessage.INCORRECT_NUMBER_OF_ARGUMENTS(2));
        }
        this.x = new Fraction(coordinates[0]);
        this.y = new Fraction(coordinates[1]);
    }

    /**
     * Gets this x-coordinate
     * @return this.x
     */
    public Fraction x() {
        return this.x;
    }

    /**
     * Gets this y-coordinate
     * @return this.y
     */
    public Fraction y() {
        return this.y;
    }

    /**
     * Adds two Points
     * @param addend the addend Point2D
     * @return the sum of the Points
     */
    public Point2D add(Point2D addend) {
        return new Point2D(this.x.add(addend.x), this.y.add(addend.y));
    }

    /**
     * Subtracts two Points
     * @param subtrahend the subtrahend point
     * @return the difference between the Points
     */
    public Point2D subtract(Point2D subtrahend) {
        return new Point2D(this.x.subtract(subtrahend.x), this.y.subtract(subtrahend.y));
    }

    /**
     * Multiplies a Point2D by a scalar value
     * @param f the scalar multiplicand
     * @return the scaled Point2D
     */
    public Point2D scale(Fraction f) {
        return new Point2D(this.x.multiply(f), this.y.multiply(f));
    }

    /**
     * Finds the midpoint of two Points
     * @param p the other target point
     * @return the Point2D occurring exactly halfway between this Point2D and the comparator
     */
    public Point2D midpoint(Point2D p) {
        return pointFromDistanceRatio(p, Fraction.ONE, Fraction.ONE);
    }

    /**
     * Constructs a perpendicular bisector from two Points
     * @param p the other end of the Line segment
     * @return the Line running perpendicular to that between this Point2D and the comparator, crossing the midpoint
     */
    public Line perpendicularBisector(Point2D p) {
        return (new Line(this, p)).perpendicularAtPoint(midpoint(p));
    }

    /**
     * Finds the resulting Point2D when this is reflected across another Point2D
     * @param p the reflection axis Point2D
     * @return the resulting Point2D
     */
    public Point2D reflectAcrossPoint(Point2D p) {
        return pointFromDistanceRatio(p, new Fraction(2), Fraction.ONE.negate());
    }

    /**
     * Reflects this Point2D across a Line
     * @param line the reflection point
     * @return the mirror image of the Point2D
     */
    public Point2D reflectAcrossLine(Line line) {
        return reflectAcrossPoint(line.intersection(line.perpendicularAtPoint(this)));
    }

    /**
     * Constructs a new Point2D with the given distance ratios from this Point2D and another
     * @param p the other distance comparison Point2D
     * @param dist1 the ratio distance to this Point2D
     * @param dist2 the ratio distance to the comparator
     * @return the newly constructed Point2D
     */
    public Point2D pointFromDistanceRatio(Point2D p, Fraction dist1, Fraction dist2) throws NoRealSolutionException {
        if(dist1.add(dist2).equals(Fraction.ZERO)) {
            throw new NoRealSolutionException(NoRealSolutionException.INFINITE_POINT_LOCATION);
        }
        return scale(dist2).add(p.scale(dist1)).scale(dist1.add(dist2).inverse());
    }

    /**
     * Horizontally compares the locations of this Point2D and a Line
     * @param line the comparator Line
     * @return -1 if this Point2D is to the left of the Line, else 1 if to the right, else 0 if the Line is horizontal
     * or if this Point2D is on the Line
     */
    public int compareHorizontal(Line line) {
        return this.x.compareTo(line.perpendicularAtPoint(this).intersection(line).x);
    }

    /**
     * Vertically compares the locations of this Point2D and a Line
     * @param line the comparator Line
     * @return -1 if this Point2D is above the Line, else 1 if below, else 0 if the Line is vertical
     * or if this Point2D is on the Line
     */
    public int compareVertical(Line line) {
        return this.y.compareTo(line.perpendicularAtPoint(this).intersection(line).y);
    }

    /**
     * Finds the square of the distance between two Points
     * @param p the comparator Point2D
     * @return the square of the distance between this Point2D and the comparator Point2D
     */
    public Fraction distanceSquared(Point2D p) {
        return this.x.subtract(p.x()).pow(2).add(this.y.subtract(p.y()).pow(2));
    }

    /**
     * Determines if this Point2D is congruent to another Point2D
     * @param o the queried comparison Point2D
     * @return true if the x- and y-coordinates are both the same, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Point2D)) {
            return false;
        }
        Point2D p = (Point2D) o;
        return this.x.equals(p.x) && this.y.equals(p.y);
    }

    /**
     * Converts this Point2D to a printable format
     * @return this Point2D as a String
     */
    @Override
    public String toString() {
        return "(".concat(x.toString()).concat(",").concat(y.toString()).concat(")");
    }

    /**
     * Prints this Point2D
     */
    public void print() {
        System.out.println(toString());
    }

    // static methods

    /**
     * Converts a String into an array of Point2Ds
     * @param s the target String
     * @return the parsed array of Point2Ds
     */
    public static Point2D[] parseCoordinates(String s) {
        BigInteger[][][] bigInts = General.Converter.convertToCoordinateList(s);
        Point2D[] coordinates = new Point2D[bigInts.length];
        for(int i = 0; i < bigInts.length; i++) {
            coordinates[i] = new Point2D(new Fraction(bigInts[i][0]), new Fraction(bigInts[i][1]));
        }
        return coordinates;
    }

    /**
     * Finds the convex hull of a set of Point2Ds
     * @param p the set of Point2Ds
     * @return the List of Point2Ds
     */
    public static List<Point2D> convexHull(Point2D... p) {
        boolean[] validated = new boolean[p.length];
        boolean located = true;
        int i = 0, j = 0;
        while(located) {

        }
        List<Point2D> convexHull = new LinkedList<>();

        return convexHull; //TODO
    }

    /**
     * Converts an array of Point2Ds into a String format
     * @param p the array of Point2Ds
     * @return the converted String
     */
    public static String arrayToString(Point2D... p) {
        String print = "";
        for(Point2D point : p) {
            print = print.concat(",").concat(point.toString());
        }
        return print.substring(print.length() > 0 ? 1 : 0);
    }

    /**
     * Prints an array of Point2Ds
     * @param p the input array
     */
    public static void print(Point2D... p) {
        System.out.println(arrayToString(p));
    }
}