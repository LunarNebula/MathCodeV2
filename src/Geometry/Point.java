package Geometry;

import Algebra.Fraction;
import Algebra.LinearEquation;
import Exception.*;

import java.math.BigInteger;

public class Point {
    private final Fraction[] c;

    /**
     * Creates a new Point in zero-dimensional Euclidean space
     */
    public Point() {
        this.c = new Fraction[0];
    }

    /**
     * Creates a new Point from a list of coordinate measures
     * @param c the array of coordinates
     */
    public Point(Fraction... c) {
        this.c = new Fraction[c.length];
        System.arraycopy(c, 0, this.c, 0, c.length);
    }

    /**
     * Adds two Points
     * @param addend the addend Point
     * @return the sum of this Point and the addend Point
     * @throws IllegalDimensionException if this Point and the addend Point do not exist in the same dimension
     */
    public Point add(Point addend) throws IllegalDimensionException {
        checkDimension(addend);
        final Point sum = new Point(this.c);
        for(int i = 0; i < sum.c.length; i++) {
            sum.c[i] = sum.c[i].add(addend.c[i]);
        }
        return sum;
    }

    /**
     * Subtracts a Point from this Point
     * @param subtrahend the subtrahend Point
     * @return the difference between this Point and the subtrahend Point
     * @throws IllegalDimensionException if this Point and the subtrahend Point do not exist in the same dimension
     */
    public Point subtract(Point subtrahend) throws IllegalDimensionException {
        checkDimension(subtrahend);
        final Point difference = new Point(this.c);
        for(int i = 0; i < difference.c.length; i++) {
            difference.c[i] = difference.c[i].subtract(subtrahend.c[i]);
        }
        return difference;
    }

    /**
     * Multiplies this Point by a scalar factor
     * @param scale the scalar factor
     * @return this * scale
     */
    public Point scale(Fraction scale) {
        final Point dilation = new Point(this.c);
        for(int i = 0; i < dilation.c.length; i++) {
            dilation.c[i] = dilation.c[i].multiply(scale);
        }
        return dilation;
    }

    /**
     * Constructs a new Point according to the parameters
     * @param p a comparator Point
     * @param thisRatio the relative distance from this Point
     * @param pRatio the relative distance from the comparator Point
     * @return a Point collinear to this and the comparator whose relative distance to each is thisRatio : pRatio
     * @throws IllegalDimensionException if this Point and the comparator Point do not exist in the same dimension
     */
    public Point pointFromDistanceRatio(Point p, Fraction thisRatio, Fraction pRatio) throws IllegalDimensionException {
        checkDimension(p);
        return scale(thisRatio).add(p.scale(pRatio)).scale(thisRatio.add(pRatio).inverse());
    }

    /**
     * Finds the midpoint of two Points
     * @param p the comparator Point
     * @return the Point halfway between this Point and the comparator Point
     * @throws IllegalDimensionException if this Point and the comparator Point do not exist in the same dimension
     */
    public Point midpoint(Point p) throws IllegalDimensionException {
        checkDimension(p);
        return add(p).scale(new Fraction(1, 2));
    }

    /**
     * Finds the orthogonal bisector of two Points
     * @param p the comparator Point
     * @return the subspace representing all Points equidistant from this Point and the comparator Point
     * @throws IllegalDimensionException if this Point and the comparator Point do not exist in the same dimension
     */
    public LinearEquation orthogonalBisector(Point p) throws IllegalDimensionException {
        checkDimension(p);
        return new LinearEquation(new Vector(subtract(p)), midpoint(p));
    }

    /**
     * Finds the squared distance to another Point
     * @param p the comparator Point
     * @return the sum of the squares of the differences between all corresponding coordinates in this Point and the comparator Point
     */
    public Fraction distanceSquared(Point p) throws IllegalDimensionException {
        checkDimension(p);
        Fraction distanceSquared = Fraction.ZERO;
        for(int i = 0; i < p.dimension(); i++) {
            distanceSquared = distanceSquared.add(this.c[i].subtract(p.c[i]).pow(2));
        }
        return distanceSquared;
    }

    /**
     * Checks whether this Point and a specified comparator Point exist in the same dimension
     * @param p the comparator Point
     * @throws IllegalDimensionException if this Point and the comparator Point do not exist in the same dimension
     */
    private void checkDimension(Point p) throws IllegalDimensionException {
        if(this.c.length != p.c.length) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
        }
    }

    /**
     * Finds the number of the Euclidean dimension containing this Point
     * @return the number of coordinates in this Point
     */
    public int dimension() {
        return this.c.length;
    }

    /**
     * Finds the coordinates of this Point
     * @return this.c
     */
    public Fraction[] getCoordinates() {
        Fraction[] c = new Fraction[this.c.length];
        System.arraycopy(this.c, 0, c, 0, c.length);
        return c;
    }

    /**
     * Converts this Point to a printable format
     * @return this Point as a String
     */
    @Override
    public String toString() {
        String s = "";
        for(Fraction coordinate : c) {
            s = s.concat(",").concat(coordinate.toString());
        }
        return s.length() > 0 ? ("(" + s.substring(1) + ")") : s;
    }

    /**
     * Prints this Point
     */
    public void print() {
        System.out.println(toString());
    }

    // static methods

    /**
     * Converts a String into an array of Points
     * @param s the target String
     * @return the parsed array of Points
     */
    public static Point[] parseCoordinates(String s) {
        BigInteger[][][] bigInts = General.Converter.convertToCoordinateList(s);
        Point[] coordinates = new Point[bigInts.length];
        for(int i = 0; i < bigInts.length; i++) {
            coordinates[i] = new Point(Fraction.valueOf(bigInts[i]));
        }
        return coordinates;
    }

    /**
     * Converts an array of Points into a String format
     * @param p the array of Points
     * @return the converted String
     */
    public static String arrayToString(Point... p) {
        String print = "";
        for(Point point : p) {
            print = print.concat(",").concat(point.toString());
        }
        return print.length() == 0 ? print : print.substring(1);
    }

    /**
     * Prints an array of Points
     * @param p the input array
     */
    public static void print(Point... p) {
        System.out.println(arrayToString(p));
    }
}