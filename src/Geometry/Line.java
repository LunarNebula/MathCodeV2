package Geometry;

import Algebra.Fraction;
import Exception.*;
import java.math.BigInteger;

public class Line {
    private BigInteger A, B, C;

    /**
     * Constructs a new Line with the given coefficients
     * @param A the x-coefficient
     * @param B the y-coefficient
     * @param C the scaling constant
     */
    public Line(BigInteger A, BigInteger B, BigInteger C) {
        this.A = A;
        this.B = B;
        this.C = C;
        simplify();
    }

    /**
     * Constructs a new Line with the given coefficients
     * @param A the x-coefficient
     * @param B the y-coefficient
     * @param C the scaling constant
     */
    public Line(Number A, Number B, Number C) {
        this.A = BigInteger.valueOf(A.longValue());
        this.B = BigInteger.valueOf(B.longValue());
        this.C = BigInteger.valueOf(C.longValue());
        simplify();
    }

    /**
     * Constructs a new Line that intersects the given Points
     * @param p1 the first Point2D
     * @param p2 the second Point2D
     */
    public Line(Point2D p1, Point2D p2) {
        final Fraction A = p1.y().subtract(p2.y());
        final Fraction B = p2.x().subtract(p1.x());
        final Fraction C = p2.x().multiply(p1.y()).subtract(p1.x().multiply(p2.y()));
        final Fraction[] line = Fraction.raiseToIntegers(A, B, C);
        this.A = line[0].asBigInteger();
        this.B = line[1].asBigInteger();
        this.C = line[2].asBigInteger();
        simplify();
    }

    /**
     * Finds the intersection between two Lines
     * @param line the comparator Line
     * @return the Point2D at the intersection between this Line and the comparator
     * @throws NoRealSolutionException if this Line and the comparator Line are parallel
     */
    public Point2D intersection(Line line) {
        final BigInteger x = this.C.multiply(line.B).subtract(this.B.multiply(line.C));
        final BigInteger y = this.C.multiply(line.A).subtract(this.A.multiply(line.C));
        final BigInteger determinant = this.A.multiply(line.B).subtract(this.B.multiply(line.A));
        if(determinant.equals(BigInteger.ZERO)) {
           throw new NoRealSolutionException(NoRealSolutionException.PARALLEL_LINE_INTERSECTION);
        }
        return new Point2D(new Fraction(x, determinant), new Fraction(y, determinant).negate());
    }

    /**
     * Constructs a Line perpendicular to this Line that intersects a specific Point2D
     * @param p the target Point2D of intersection
     * @return the constructed Line
     */
    public Line perpendicularAtPoint(Point2D p) {
        final Fraction A = new Fraction(this.B.negate());
        final Fraction B = new Fraction(this.A);
        final Fraction C = A.multiply(p.x()).add(B.multiply(p.y()));
        final Fraction[] line = Fraction.raiseToIntegers(A, B, C);
        return new Line(line[0].asBigInteger(), line[1].asBigInteger(), line[2].asBigInteger());
    }

    /**
     * Constructs a Line parallel to this Line that intersects a specific Point2D
     * @param p the target Point2D of intersection
     * @return the constructed Line
     */
    public Line parallelAtPoint(Point2D p) {
        final Fraction A = new Fraction(this.A);
        final Fraction B = new Fraction(this.B);
        final Fraction C = A.multiply(p.x()).add(B.multiply(p.y()));
        final Fraction[] line = Fraction.raiseToIntegers(A, B, C);
        return new Line(line[0].asBigInteger(), line[1].asBigInteger(), line[2].asBigInteger());
    }

    /**
     * Reflects this Line across a Point2D
     * @param p the Point2D axis of reflection
     * @return the reflected Line
     */
    public Line reflectAcrossPoint(Point2D p) {
        return parallelAtPoint(intersection(perpendicularAtPoint(p)).reflectAcrossPoint(p));
    }

    /**
     * Simplifies the coefficients of this Line
     */
    public void simplify() {
        final BigInteger GCD = this.A.gcd(this.B).gcd(this.C);
        this.A = this.A.divide(GCD);
        this.B = this.B.divide(GCD);
        this.C = this.C.divide(GCD);
        if(this.A.compareTo(BigInteger.ZERO) < 0) {
            this.A = this.A.negate();
            this.B = this.B.negate();
            this.C = this.C.negate();
        }
    }

    /**
     * Queries the direction of another Line
     * @param line the comparator Line
     * @return true if this Line is parallel to the comparator Line, else false
     */
    public boolean isParallel(Line line) {
        return this.A.multiply(line.B).equals(this.B.multiply(line.A));
    }

    /**
     * Queries the direction of another Line
     * @param line the comparator Line
     * @return true if this Line is perpendicular to the comparator Line, else false
     */
    public boolean isPerpendicular(Line line) {
        return this.A.multiply(line.A).equals(this.B.multiply(line.B).negate());
    }

    /**
     * Determines whether this Line contains a given Point2D
     * @param p the Point2D
     * @return true if the Point2D lies on this Line, else false
     */
    public boolean contains(Point2D p) {
        return new Fraction(this.A).multiply(p.x()).add(new Fraction(this.B).multiply(p.y())).equals(new Fraction(this.C));
    }

    /**
     * Queries another Line for equality
     * @param o the comparator Line
     * @return true if all respective coefficients are equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof final Line line)) {
            return false;
        }
        return this.A.equals(line.A) && this.B.equals(line.B) && this.C.equals(line.C);
    }

    /**
     * Converts this Line to a printable format
     * @return this Line in String form
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(! this.A.equals(BigInteger.ZERO)) {
            if(! this.A.abs().equals(BigInteger.ONE)) {
                builder.append(this.A);
            }
            builder.append("x");
        }
        if(! this.B.equals(BigInteger.ZERO)) {
            builder.append(this.B.compareTo(BigInteger.ZERO) < 0 ? "" : "+");
            if(! this.B.abs().equals(BigInteger.ONE)) {
                builder.append(this.B);
            }
            builder.append("y");
        }
        return builder.length() == 0 ? "null" : builder.append("=").append(this.C).toString();
    }

    /**
     * Prints this Line
     */
    public void print() {
        System.out.println(this);
    }
}