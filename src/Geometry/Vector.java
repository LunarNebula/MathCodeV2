package Geometry;

import Algebra.Fraction;
import Algebra.Matrix;
import Exception.*;

import java.math.BigInteger;

public class Vector {
    private Fraction[] coordinates;

    /**
     * Creates a new Vector
     */
    public Vector() {
        this.coordinates = new Fraction[0];
    }

    /**
     * Creates a new Vector with given dimensions
     * @param f the array of dimensions
     */
    public Vector(Fraction... f) {
        this.coordinates = f;
    }

    /**
     * Creates a new Vector using the coordinates of a Point2D
     * @param p the model Point2D
     */
    public Vector(Point2D p) {
        this.coordinates = new Fraction[]{p.x(), p.y()};
    }

    /**
     * Creates a new Vector using the coordinates of a Point3D
     * @param p the model Point3D
     */
    public Vector(Point3D p) {
        this.coordinates = new Fraction[]{p.x(), p.y(), p.z()};
    }

    /**
     * Creates a new Vector using the coordinates of a Point
     * @param p the model Point
     */
    public Vector(Point p) {
        final Vector model = new Vector(p.getCoordinates());
        this.coordinates = model.coordinates;
    }

    /**
     * Creates a new Vector between two Points
     * @param a the destination Point
     * @param b the start Point
     */
    public Vector(Point a, Point b) {
        final Vector model = new Vector(a.subtract(b));
        this.coordinates = model.coordinates;
    }

    /**
     * Creates a new Vector with given dimensions
     * @param s the dimensions in String format
     */
    public Vector(String s) {
        final BigInteger[][] bigInts = General.Converter.convertTo2DBigIntegerArray(s, "/", ",");
        this.coordinates = new Fraction[bigInts.length];
        for(int i = 0; i < bigInts.length; i++) {
            this.coordinates[i] = new Fraction(bigInts[i]);
        }
    }

    /**
     * Adds two Vectors
     * @param addend the addend Vector
     * @return the sum of this Vector and the addend
     */
    public Vector add(Vector addend) throws IllegalDimensionException {
        if(this.coordinates.length != addend.coordinates.length) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
        }
        final Vector sum = new Vector();
        sum.coordinates = new Fraction[addend.coordinates.length];
        for(int i = 0; i < sum.coordinates.length; i++) {
            sum.coordinates[i] = this.coordinates[i].add(addend.coordinates[i]);
        }
        return sum;
    }

    /**
     * Subtracts two Vectors
     * @param subtrahend the subtrahend Vector
     * @return the difference between this Vector and the addend
     */
    public Vector subtract(Vector subtrahend) throws IllegalDimensionException {
        if(this.coordinates.length != subtrahend.coordinates.length) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
        }
        final Vector difference = new Vector();
        difference.coordinates = new Fraction[subtrahend.coordinates.length];
        for(int i = 0; i < difference.coordinates.length; i++) {
            difference.coordinates[i] = this.coordinates[i].add(subtrahend.coordinates[i]);
        }
        return difference;
    }

    /**
     * Multiplies this Vector by a scalar value
     * @param multiplicand the scalar
     * @return the scaled Vector
     */
    public Vector scale(Fraction multiplicand) {
        final Vector sum = new Vector();
        sum.coordinates = new Fraction[this.coordinates.length];
        for(int i = 0; i < sum.coordinates.length; i++) {
            sum.coordinates[i] = this.coordinates[i].multiply(multiplicand);
        }
        return sum;
    }

    /**
     * Reduces this Vector so its leading coordinate is 1
     * @return the reduced Vector
     */
    public Vector reduce() {
        return this.coordinates.length > 0 ? scale(this.coordinates[0].inverse()) : new Vector();
    }

    /**
     * Finds the dot product of two Vectors
     * @param v the second Vector
     * @return the dot product of this Vector and the second Vector
     */
    public Fraction dotProduct(Vector v) throws IllegalDimensionException {
        if(this.coordinates.length != v.coordinates.length) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
        }
        Fraction dotProduct = Fraction.ZERO;
        for(int i = 0; i < this.coordinates.length; i++) {
            dotProduct = dotProduct.add(this.coordinates[i].multiply(v.coordinates[i]));
        }
        return dotProduct;
    }

    /**
     * Finds the squared second norm of this Vector
     * @return the dot product of this Vector and itself
     */
    public Fraction norm2Squared() {
        return dotProduct(this);
    }

    /**
     * Constructs a Plane based on the location of a Point3D
     * @param p the locator Point3D
     * @return the Plane perpendicular to this Vector through the given Point3D
     */
    public Plane perpendicularPlaneAtPoint(Point3D p) throws IllegalDimensionException {
        if(dimension() != Plane.PLANE_DEGREE) {
            throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
        }
        final Fraction k = this.coordinates[0].multiply(p.x()).add(this.coordinates[1].multiply(p.y())).add(this.coordinates[2].multiply(p.z()));
        return new Plane(this.coordinates[0], this.coordinates[1], this.coordinates[2], k);
    }

    /**
     * Retrieves the value of this Vector
     * @return the coordinates
     */
    public Fraction[] getCoordinates() {
        return this.coordinates;
    }

    /**
     * Finds the dimension of this Vector
     * @return the number of coordinates designated to this Vector
     */
    public int dimension() {
        return this.coordinates.length;
    }

    /**
     * Compares this Vector with another Vector for equality
     * @param o the comparator Vector
     * @return true if both Vectors are the same length and have congruent corresponding coordinates, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Vector)) {
            return false;
        }
        final Vector comparator = (Vector) o;
        if(this.coordinates.length == comparator.coordinates.length) {
            return false;
        }
        for(int i = 0; i < this.coordinates.length; i++) {
            if(this.coordinates[i] != comparator.coordinates[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts this Vector to a printable format
     * @return this Vector as a String
     */
    @Override
    public String toString() {
        String print = "";
        for(Fraction coordinate : this.coordinates) {
            print = print.concat(" ").concat(coordinate.toString());
        }
        return "[".concat(print.substring(print.length() > 0 ? 1 : 0)).concat("]");
    }

    /**
     * Prints this Vector
     */
    public void print() {
        System.out.println(toString());
    }

    // static methods

    /**
     * Finds the cross product of an array of Vectors
     * @param v the Vectors
     * @return the Vector of same degree orthogonal to all Vectors in the array
     * @throws IllegalDimensionException if not all Vectors have the same dimension
     */
    public static Vector crossProduct(Vector... v) throws IllegalDimensionException {
        for(Vector vector : v) {
            if(vector.dimension() != v.length + 1) {
                throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
            }
        }
        final Fraction[][][] vectorMatrix = new Fraction[v.length + 1][][];
        for(int i = 0; i < vectorMatrix.length; i++) {
            vectorMatrix[i] = new Fraction[v.length][];
            for(int j = 0; j < vectorMatrix[i].length; j++) {
                vectorMatrix[i][j] = new Fraction[v.length];
                int count = 0;
                for(int k = 0; k < vectorMatrix[i][j].length; k++) {
                    if(i == k) {
                        count = 1;
                    }
                    vectorMatrix[i][j][k] = v[j].getCoordinates()[k + count];
                }
            }
        }
        final Fraction[] crossProduct = new Fraction[v.length + 1];
        Fraction sign = Fraction.ONE;
        for(int i = 0; i < crossProduct.length; i++) {
            crossProduct[i] = new Matrix(vectorMatrix[i]).determinant().multiply(sign);
            sign = sign.negate();
        }
        return new Vector(crossProduct);
    }

    /**
     * Determines whether a set of {@code Vectors} is linearly dependent.
     * @param vectors the array of {@code Vectors}.
     * @return {@code true} if there exists a nontrivial linear combination of the {@code Vectors}
     * that produces a zero {@code Vector}, else {@code false} if all nontrivial combinations yield
     * nonzero {@code Vectors}.
     * @throws IllegalDimensionException if not all {@code Vectors} have the same dimension.
     */
    public static boolean areLinearlyDependent(Vector... vectors) throws IllegalDimensionException {
        return (verifySameDimension(vectors) < vectors.length) | (new Matrix(vectors).rank() < vectors.length);
    }

    /**
     * Verifies that a set of {@code Vectors} contains only Vectors with the same dimension.
     * @param vectors the set of {@code Vectors}.
     * @return the dimension of each {@code Vector} if at least one is entered as a parameter,
     * else {@code -1} if no {@code Vectors} were provided.
     * @throws IllegalDimensionException if any two {@code Vectors} have different dimensions.
     */
    public static int verifySameDimension(Vector... vectors) throws IllegalDimensionException {
        int dimension = -1;
        for(Vector v : vectors) {
            if(dimension < 0) {
                dimension = v.dimension();
            } else if(v.dimension() != dimension) {
                throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
            }
        }
        return dimension;
    }

    /**
     * Generates a null {@code Vector}.
     * @param n the desired dimension of the {@code Vector}.
     * @return {@code 0_n}
     */
    public static Vector ZERO(int n) {
        final Fraction[] nullVector = new Fraction[n];
        for(int i = 0; i < n; i++) {
            nullVector[i] = Fraction.ZERO;
        }
        return new Vector(nullVector);
    }

    /**
     * Converts an array of Vectors to a printable format
     * @param v the array of Vectors
     * @return the Vectors as a String
     */
    public static String arrayToString(Vector... v) {
        String print = "";
        int count = 0;
        for(Vector vector : v) {
            print = print.concat("\n").concat("v" + count++ + " = ").concat(vector.toString());
        }
        return print.substring(v.length > 0 ? 1 : 0);
    }

    /**
     * Prints an array of Vectors
     * @param v the Vector array
     */
    public static void print(Vector... v) {
        System.out.println(arrayToString(v));
    }
}