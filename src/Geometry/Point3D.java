package Geometry;

import Algebra.Fraction;

import java.math.BigInteger;

public class Point3D {
    public static final Point3D ORIGIN = new Point3D();
    private final Fraction x, y, z;

    /**
     * Creates a new Point3D at the origin
     */
    public Point3D() {
        this.x = Fraction.ZERO;
        this.y = Fraction.ZERO;
        this.z = Fraction.ZERO;
    }

    /**
     * Creates a new Point3D with integer coordinates
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    public Point3D(int x, int y, int z) {
        this.x = new Fraction(x);
        this.y = new Fraction(y);
        this.z = new Fraction(z);
    }

    /**
     * Creates a new Point3D with Fraction coordinates
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    public Point3D(Fraction x, Fraction y, Fraction z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds two Point3Ds
     * @param addend the addend Point3D
     * @return the sum of this Point3D and the addend Point3D
     */
    public Point3D add(Point3D addend) {
        return new Point3D(this.x.add(addend.x), this.y.add(addend.y), this.z.add(addend.z));
    }

    /**
     * Subtracts two Point3Ds
     * @param subtrahend the subtrahend Point3D
     * @return the difference between this Point3D and the subtrahend Point3D
     */
    public Point3D subtract(Point3D subtrahend) {
        return new Point3D(this.x.subtract(subtrahend.x), this.y.subtract(subtrahend.y), this.z.subtract(subtrahend.z));
    }

    /**
     * Finds the squared distance between two Point3Ds
     * @param p the comparator Point3D
     * @return dx^2 + dy^2 + dz^2
     */
    public Fraction distanceSquared(Point3D p) {
        return this.x.subtract(p.x).pow(2).add(this.y.subtract(p.y).pow(2)).add(this.z.subtract(p.z).pow(2));
    }

    /**
     * Scales this Point3D by a Fraction factor
     * @param scale the scale factor
     * @return this * scale
     */
    public Point3D scale(Fraction scale) {
        return new Point3D(this.x.multiply(scale), this.y.multiply(scale), this.z.multiply(scale));
    }

    /**
     * Finds the midpoint of two Point3Ds
     * @param p the other endpoint
     * @return the Point3D equidistant from and collinear with this Point2D and the other endpoint
     */
    public Point3D midpoint(Point3D p) {
        final Fraction m = new Fraction(BigInteger.TWO);
        return new Point3D(this.x.add(p.x).divide(m), this.y.add(p.y).divide(m), this.z.add(p.z).divide(m));
    }

    /**
     * Constructs a Plane based on the location of a Point3D
     * @param p the locator Point3D
     * @return the Plane perpendicular to the Vector through the two Point3Ds passing through the locator
     */
    public Plane perpendicularPlaneAtPoint(Point3D p) {
        final Vector direction = new Vector(this.x.subtract(p.x), this.y.subtract(p.y), this.z.subtract(p.z));
        return direction.perpendicularPlaneAtPoint(p);
    }

    /**
     * Constructs a Plane halfway between this Point3D and a locator Point3D
     * @param p the locator Point3D
     * @return the constructed Plane
     */
    public Plane midPlane(Point3D p) {
        return perpendicularPlaneAtPoint(midpoint(p));
    }

    /**
     * Gets the x-coordinate of this Point3D
     * @return this.x
     */
    public Fraction x() {
        return this.x;
    }

    /**
     * Gets the y-coordinate of this Point3D
     * @return this.y
     */
    public Fraction y() {
        return this.y;
    }

    /**
     * Gets the z-coordinate of this Point3D
     * @return this.z
     */
    public Fraction z() {
        return this.z;
    }

    /**
     * Compares this Point3D with another Point3D for equality
     * @param o the comparator Point3D
     * @return true if all three coordinates are congruent, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Point3D)) {
            return false;
        }
        final Point3D p = (Point3D) o;
        return this.x.equals(p.x) && this.y.equals(p.y) && this.z.equals(p.z);
    }

    /**
     * Converts this Point3D to a printable format
     * @return this Point3D as a String
     */
    @Override
    public String toString() {
        return "(" + this.x.toString() + "," + this.y.toString() + "," + this.z.toString() + ")";
    }

    /**
     * Prints this Point3D
     */
    public void print() {
        System.out.println(toString());
    }

    // static methods

    /**
     * Converts a String into an array of Point3Ds
     * @param s the target String
     * @return the parsed array of Point3Ds
     */
    public static Point3D[] parseCoordinates(String s) {
        final BigInteger[][][] bigInts = General.Converter.convertToCoordinateList(s);
        final Point3D[] coordinates = new Point3D[bigInts.length];
        for(int i = 0; i < bigInts.length; i++) {
            coordinates[i] = new Point3D(new Fraction(bigInts[i][0]), new Fraction(bigInts[i][1]), new Fraction(bigInts[i][2]));
        }
        return coordinates;
    }

    /**
     * Converts an array of Point3Ds to a printable format
     * @param p the array of Point3Ds
     * @return the array in String format
     */
    public static String arrayToString(Point3D... p) {
        String print = "";
        for(Point3D point : p) {
            print = print.concat(",").concat(point.toString());
        }
        return print.length() == 0 ? print : print.substring(1);
    }

    /**
     * Prints an array of Point3Ds
     * @param p the Point3D array
     */
    public static void printArray(Point3D... p) {
        System.out.println(arrayToString(p));
    }
}