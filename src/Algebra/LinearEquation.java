package Algebra;

import Exception.*;
import Geometry.Point;
import Geometry.Vector;

import java.math.BigInteger;

/**
 * <b></b>Stores a linear equation in the following form:
 *
 */
public class LinearEquation {
    private final BigInteger[] c;
    private final BigInteger k;

    /**
     * Creates a new, empty {@code LinearEquation}.
     */
    public LinearEquation() {
        this.c = new BigInteger[0];
        this.k = BigInteger.ZERO;
    }

    /**
     * Creates a new LinearEquation with the given parameters
     * @param k the constant scale factor
     * @param c the linear dimensional coefficients
     */
    public LinearEquation(BigInteger k, BigInteger... c) {
        this.c = new BigInteger[c.length];
        System.arraycopy(c, 0, this.c, 0, c.length);
        this.k = k;
    }

    /**
     * Creates a new LinearEquation with unscaled parameters
     * @param k the constant scale factor
     * @param c the linear dimensional coefficients
     */
    public LinearEquation(Fraction k, Fraction... c) {
        Fraction[] scale = new Fraction[c.length + 1];
        scale[0] = k;
        System.arraycopy(c, 0, scale, 1, c.length);
        scale = Fraction.raiseToIntegers(scale);
        this.k = scale[0].asBigInteger();
        this.c = new BigInteger[c.length];
        for(int i = 0; i < c.length; i++) {
            this.c[i] = scale[i + 1].asBigInteger();
        }
    }

    /**
     * Creates a new LinearEquation around an array of Points
     * @param p the array of Points contained in the target LinearEquation
     */
    public LinearEquation(Point... p) {
        final Vector[] v = new Vector[p.length - 1];
        for(int i = 0; i < v.length; i++) {
            v[i] = new Vector(p[i], p[i + 1]);
        }
        final Vector crossProduct = Vector.crossProduct(v);
        final Fraction constant = crossProduct.dotProduct(new Vector(p[0]));
        final LinearEquation model = new LinearEquation(constant, crossProduct.getCoordinates());
        this.c = model.c;
        this.k = model.k;
    }

    /**
     * Creates a new LinearEquation orthogonal to a given Vector, through a Point
     * @param v the target Vector
     * @param p the target Point
     */
    public LinearEquation(Vector v, Point p) {
        final LinearEquation model = new LinearEquation(v.dotProduct(new Vector(p.getCoordinates())), v.getCoordinates());
        this.c = model.c;
        this.k = model.k;
    }

    /**
     * Finds the dimensionality of this LinearEquation
     * @return this.c.length, or the number of linear terms
     */
    public int dimension() {
        return this.c.length;
    }

    /**
     * Finds the coefficients of this LinearEquation
     * @return this.c
     */
    public BigInteger[] getCoefficients() {
        final BigInteger[] c = new BigInteger[this.c.length];
        System.arraycopy(this.c, 0, c, 0, c.length);
        return c;
    }

    /**
     * Gets the constant term of this {@code LinearEquation}.
     * @return {@code this.k}
     */
    public BigInteger getConstant() {
        return this.k;
    }

    /**
     * Converts this LinearEquation to a printable format
     * @return this LinearEquation as a String
     */
    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < this.c.length; i++) {
            s = s.concat("+" + this.c[i] + "x_" + i);
        }
        return s.length() > 0 ? s.substring(1).replaceAll("\\+-", "-").concat("=" + k) : "null space";
    }

    /**
     * Prints this LinearEquation
     */
    public void print() {
        System.out.println(toString());
    }

    // static methods

    /**
     * Finds the intersection of an array
     * @param e the array of LinearEquations
     * @return the Point of intersection
     * @throws IllegalArgumentException if the LinearEquations do not all occupy the same dimensional space
     */
    public static Point intersect(LinearEquation... e) throws IllegalArgumentException {
        for(LinearEquation space : e) {
            if(space.dimension() != e.length) {
                throw new IllegalArgumentException();
            }
        }
        final Fraction[][] matrix = new Fraction[e.length][], constant = new Fraction[e.length][];
        for(int i = 0; i < e.length; i++) {
            matrix[i] = new Fraction[e[i].dimension()];
            final BigInteger[] proxy = e[i].c;
            for(int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = new Fraction(proxy[j]);
            }
            constant[i] = new Fraction[]{new Fraction(e[i].k)};
        }
        final Matrix solution = new Matrix(matrix).reducedRowEchelon(new Matrix(constant))[1];
        final Fraction[] coordinates = new Fraction[e.length];
        for(int i = 0; i < e.length; i++) {
            coordinates[i] = solution.getElement(i, 0);
        }
        return new Point(coordinates);
    }

    /**
     * Verifies that every member in a set of {@code LinearEquations} has the same dimension.
     * @param equations the set of equations.
     * @throws IllegalDimensionException if at least one {@code LinearEquation} has a different dimension.
     */
    public static void verifyDimensionEquality(LinearEquation... equations) throws IllegalDimensionException {
        int dimension = -1;
        for(LinearEquation equation : equations) {
            if(dimension < 0) {
                dimension = equation.dimension();
            } else if(equation.dimension() != dimension) {
                throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
            }
        }
    }

    /**
     * Determines whether a system of {@code LinearEquations} has at least one solution.
     * @param equations the system of equations.
     * @return {@code true} if the system is consistent, else {@code false}.
     * @throws IllegalDimensionException if not all {@code LinearEquations} have equal dimension.
     */
    public static boolean isConsistent(LinearEquation... equations) throws IllegalDimensionException {
        final Matrix[] system = Matrix.getSystem(equations);
        return true;
    }
}