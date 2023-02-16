package Geometry;

import Algebra.Fraction;
import Algebra.Matrix;

import java.math.BigInteger;

public class Conic {
    private final BigInteger A, B, C, D, E, F;

    /**
     * Creates a new, empty Conic Section
     */
    public Conic() {
        this.A = BigInteger.ZERO;
        this.B = BigInteger.ZERO;
        this.C = BigInteger.ZERO;
        this.D = BigInteger.ZERO;
        this.E = BigInteger.ZERO;
        this.F = BigInteger.ZERO;
    }

    /**
     * Creates a new Conic Section from Integer coefficients
     * @param A the quadratic x-coefficient
     * @param B the 2nd-degree xy-coefficient
     * @param C the quadratic y-coefficient
     * @param D the linear x-coefficient
     * @param E the linear y-coefficient
     * @param F the constant
     */
    public Conic(int A, int B, int C, int D, int E, int F) {
        this.A = BigInteger.valueOf(A);
        this.B = BigInteger.valueOf(B);
        this.C = BigInteger.valueOf(C);
        this.D = BigInteger.valueOf(D);
        this.E = BigInteger.valueOf(E);
        this.F = BigInteger.valueOf(F);
    }

    /**
     * Creates a new Conic Section from BigInteger coefficients
     * @param A the quadratic x-coefficient
     * @param B the 2nd-degree xy-coefficient
     * @param C the quadratic y-coefficient
     * @param D the linear x-coefficient
     * @param E the linear y-coefficient
     * @param F the constant
     */
    public Conic(BigInteger A, BigInteger B, BigInteger C, BigInteger D, BigInteger E, BigInteger F) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;
        this.F = F;
    }

    /**
     * Creates a new Conic Section that intersects five Cartesian points
     * @param a point 1
     * @param b point 2
     * @param c point 3
     * @param d point 4
     * @param e point 5
     */ // is inoperable at the moment
    public Conic(Point2D a, Point2D b, Point2D c, Point2D d, Point2D e) {
        final Vector[] v = new Vector[5];
        final Point2D[] points = {a, b, c, d, e};
        for(int i = 0; i < v.length; i++) {
            Fraction x = points[i].x(), y = points[i].y();
            v[i] = new Vector(x.pow(2), x.multiply(y), y.pow(2), x, y, Fraction.ONE);
        }
        final Fraction[] coefficients = Fraction.raiseToIntegers(Vector.crossProduct(v).getCoordinates());
        this.A = coefficients[0].asBigInteger();
        this.B = coefficients[1].asBigInteger();
        this.C = coefficients[2].asBigInteger();
        this.D = coefficients[3].asBigInteger();
        this.E = coefficients[4].asBigInteger();
        this.F = coefficients[5].asBigInteger();
    }

    /**
     * Finds the center of this Conic
     * @return the unique Point2D from which the Conic has rotational symmetry
     */
    public Point2D center() {
        final Fraction A = new Fraction(this.A.multiply(BigInteger.TWO));
        final Fraction B = new Fraction(this.B);
        final Fraction C = new Fraction(this.C.multiply(BigInteger.TWO));
        final Fraction D = new Fraction(this.D.negate());
        final Fraction E = new Fraction(this.E.negate());
        final Matrix m = new Matrix(new Fraction[][]{{A, B}, {B, C}});
        final Matrix c = new Matrix(new Fraction[][]{{D}, {E}});
        final Matrix finalPoint = m.reducedRowEchelon(c)[1];
        return new Point2D(finalPoint.getElement(0, 0), finalPoint.getElement(1, 0));
    }

    /**
     * Reflects this Conic over the x-axis
     * @return the reflected Conic
     */
    public Conic reflectOverXAxis() {
        return new Conic(this.A, this.B.negate(), this.C, this.D, this.E.negate(), this.F);
    }

    /**
     * Reflects this Conic over the y-axis
     * @return the reflected Conic
     */
    public Conic reflectOverYAxis() {
        return new Conic(this.A, this.B.negate(), this.C, this.D.negate(), this.E, this.F);
    }


    /**
     * Reflects this Conic over the x- and y-axes
     * @return the reflected Conic
     */
    public Conic rotateAroundOrigin() {
        return new Conic(this.A, this.B.negate(), this.C, this.D.negate(), this.E.negate(), this.F);
    }

    /**
     * Finds the discriminant of this Conic
     * @return the BigInteger discriminant 'd' of this Conic, which is one of the following:
     *          d < 0 if this Conic is an ellipse
     *          d = 0 if this Conic is a parabola
     *          d > 0 if this Conic is a hyperbola
     */
    public BigInteger discriminant() {
        return this.B.multiply(this.B).subtract(BigInteger.valueOf(4).multiply(this.A).multiply(this.C));
    }

    /**
     * Determines whether this Conic is a Circle
     * @return true if this Conic is congruent to a Circle, else false
     */
    public boolean isCircle() {
        return (! this.B.equals(BigInteger.ZERO)) && this.A.equals(this.C);
    }

    /**
     * Determines whether this Conic is equal to a specified Object
     * @param o the target Object
     * @return true if the target Object is a Conic with the same coefficients, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof final Conic convert)) {
            return false;
        }
        return this.A.equals(convert.A) && this.B.equals(convert.B) && this.C.equals(convert.C) &&
                this.D.equals(convert.D) && this.E.equals(convert.E) && this.F.equals(convert.F);
    }

    /**
     * Converts this Conic Section to a printable format
     * @return this Conic Section as a String
     */
    @Override
    public String toString() {
        String s = this.A + "x^2+" + this.B + "xy+" + this.C + "y^2+" + this.D + "x+" + this.E + "y+" + this.F + "=0";
        if(s.startsWith("1x")) {
            s = s.substring(1);
        } else if(s.startsWith("-1x")) {
            s = "-" + s.substring(2);
        }
        return s.replaceAll("\\+-", "-")
                .replaceAll("-1x", "-x")
                .replaceAll("\\+1x", "+x")
                .replaceAll("-1y", "-y")
                .replaceAll("\\+1y", "+y");
    }

    /**
     * Prints this Conic Section
     */
    public void print() {
        System.out.println(this);
    }
}