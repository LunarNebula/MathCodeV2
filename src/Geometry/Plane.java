package Geometry;

import Algebra.Fraction;
import Algebra.Matrix;
import Exception.*;

import java.math.BigInteger;

public class Plane {
    public static final int PLANE_DEGREE = 3;
    private BigInteger x, y, z, k;

    /**
     * Creates a new, empty Plane
     */
    public Plane() {
        this.x = BigInteger.ZERO;
        this.y = BigInteger.ZERO;
        this.z = BigInteger.ZERO;
        this.k = BigInteger.ZERO;
    }

    /**
     * Creates a new Plane with the given coefficients
     * @param x the x coefficient
     * @param y the y coefficient
     * @param z the z coefficient
     * @param k the constant
     */
    public Plane(Fraction x, Fraction y, Fraction z, Fraction k) {
        final Fraction[] bigIntegerConversions = Fraction.raiseToIntegers(x, y, z, k);
        this.x = bigIntegerConversions[0].asBigInteger();
        this.y = bigIntegerConversions[1].asBigInteger();
        this.z = bigIntegerConversions[2].asBigInteger();
        this.k = bigIntegerConversions[3].asBigInteger();
    }

    /**
     * Creates a new Plane through three given Point3Ds
     * @param a the first Point3D
     * @param b the second Point3D
     * @param c the third Point3D
     */
    public Plane(Point3D a, Point3D b, Point3D c) {
        final Vector v_ab = new Vector(a.x().subtract(b.x()), a.y().subtract(b.y()), a.z().subtract(b.z()));
        final Vector v_ac = new Vector(a.x().subtract(c.x()), a.y().subtract(c.y()), a.z().subtract(c.z()));
        final Fraction[] ab = v_ab.getCoordinates(), ac = v_ac.getCoordinates();
        final Fraction x = ab[1].multiply(ac[2]).subtract(ab[2].multiply(ac[1]));
        final Fraction y = ab[2].multiply(ac[0]).subtract(ab[0].multiply(ac[2]));
        final Fraction z = ab[0].multiply(ac[1]).subtract(ab[1].multiply(ac[0]));
        formulate(x, y, z, a.x().multiply(x).add(a.y().multiply(y)).add(a.z().multiply(z)));
    }

    /**
     * Creates a new Plane perpendicular to a given Vector through a Point3D
     * @param v the Vector
     * @param p the Point2D
     * @throws IllegalDimensionException if the degree of the Vector does not equal the degree of the Plane
     */
    public Plane(Vector v, Point3D p) throws IllegalDimensionException {
        if(v.dimension() != PLANE_DEGREE) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
        }
        final Fraction[] k = v.getCoordinates();
        formulate(k[0], k[1], k[2], k[0].multiply(p.x()).add(k[1].multiply(p.y())).add(k[2].multiply(p.z())));
    }

    /**
     * Creates a new Plane containing two given Vectors and a Point3D
     * @param a the first Vector
     * @param b the second Vector
     * @param p the locator Point3D
     * @throws IllegalDimensionException if the dimension of either Vector does not equal the degree of the Plane
     */
    public Plane(Vector a, Vector b, Point3D p) throws IllegalDimensionException {
        if(a.dimension() != Plane.PLANE_DEGREE || b.dimension() != Plane.PLANE_DEGREE) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
        }
        Fraction[] c = Vector.crossProduct(a, b).getCoordinates();
        formulate(c[0], c[1], c[2], c[0].multiply(p.x()).add(c[1].multiply(p.y())).add(c[2].multiply(p.z())));
    }

    /**
     * Converts a group of Plane coordinates to BigIntegers and reassigns these field values
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @param k the constant
     */
    private void formulate(Fraction x, Fraction y, Fraction z, Fraction k) {
        final Fraction[] bigIntegerConversions = Fraction.raiseToIntegers(x, y, z, k);
        this.x = bigIntegerConversions[0].asBigInteger();
        this.y = bigIntegerConversions[1].asBigInteger();
        this.z = bigIntegerConversions[2].asBigInteger();
        this.k = bigIntegerConversions[3].asBigInteger();
    }

    /**
     * Finds the intersection of two Planes
     * @param p the comparator Plane
     * @return the Vector representing the line between this Plane and the comparator Plane
     */
    public Vector intersect(Plane p) {
        return Vector.crossProduct(
                new Vector(new Fraction(this.x), new Fraction(this.y), new Fraction(this.z)),
                new Vector(new Fraction(p.x), new Fraction(p.y), new Fraction(p.z)));
    }

    /**
     * Finds the intersection of three Planes
     * @param a the first comparator Plane
     * @param b the second comparator Plane
     * @return the Point3D representing the intersection of this Plane and the two comparator Planes
     */
    public Point3D intersect(Plane a, Plane b) {
        final Fraction[][] coefficients = new Fraction[][]{
                {new Fraction(this.x), new Fraction(this.y), new Fraction(this.z)},
                {new Fraction(a.x), new Fraction(a.y), new Fraction(a.z)},
                {new Fraction(b.x), new Fraction(b.y), new Fraction(b.z)}};
        final Fraction[][] constants = new Fraction[][]{{new Fraction(this.k)}, {new Fraction(a.k)}, {new Fraction(b.k)}};
        final Matrix intersection = new Matrix(coefficients).reducedRowEchelon(new Matrix(constants))[1];
        return new Point3D(
                intersection.getElement(0, 0),
                intersection.getElement(1, 0),
                intersection.getElement(2, 0)
        );
    }

    /**
     * Finds the intersection of a Vector with this Plane
     * @param v the intersecting Vector
     * @return the Point3D of intersection
     * @throws IllegalDimensionException if not all Vectors are of equal dimension
     */
    public Point3D intersect(Vector v) throws IllegalDimensionException {
        if(v.dimension() != PLANE_DEGREE) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
        }
        final Fraction[] V = v.getCoordinates();
        final Fraction x = new Fraction(this.x), y = new Fraction(this.y), z = new Fraction(this.z), k = new Fraction(this.k);
        final Fraction scale = k.divide(V[0].multiply(x).add(V[1].multiply(y)).add(V[2].multiply(z)));
        return new Point3D(V[0], V[1], V[2]).scale(scale);
    }

    /**
     * Determines if this Plane contains a given Point3D
     * @param p the comparator Point3D
     * @return true if the Point3D lies on this Plane, else false
     */
    public boolean contains(Point3D p) {
        final Fraction x = new Fraction(this.x), y = new Fraction(this.y), z = new Fraction(this.z);
        return x.multiply(p.x()).add(y.multiply(p.y())).add(z.multiply(p.z())).equals(new Fraction(this.k));
    }

    /**
     * Determines whether this Plane is equal to another Plane
     * @param o the comparator Plane
     * @return true if all corresponding coefficients between the two Planes are congruent, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Plane)) {
            return false;
        }
        final Plane comparator = (Plane) o;
        return this.x.equals(comparator.x) &&
                this.y.equals(comparator.y) &&
                this.z.equals(comparator.z) &&
                this.k.equals(comparator.k);
    }

    /**
     * Converts this Plane to a printable format
     * @return this Plane as a String
     */
    @Override
    public String toString() {
        String print = "";
        if(! this.x.equals(BigInteger.ZERO)) {
            if(! this.x.abs().equals(BigInteger.ONE)) {
                print += this.x;
            }
            print = print.concat("x");
        }
        if(! this.y.equals(BigInteger.ZERO)) {
            print = print.concat(this.y.compareTo(BigInteger.ZERO) < 0 ? "" : "+");
            if(! this.y.abs().equals(BigInteger.ONE)) {
                print += this.y;
            }
            print = print.concat("y");
        }
        if(! this.z.equals(BigInteger.ZERO)) {
            print = print.concat(this.z.compareTo(BigInteger.ZERO) < 0 ? "" : "+");
            if(! this.z.abs().equals(BigInteger.ONE)) {
                print += this.z;
            }
            print = print.concat("z");
        }
        return print.length() == 0 ? "null" : print.concat("=" + this.k);
    }

    /**
     * Prints this Plane
     */
    public void print() {
        System.out.println("p: ".concat(toString()));
    }
}