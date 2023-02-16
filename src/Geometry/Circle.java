package Geometry;

import Algebra.Fraction;
import Exception.*;

public class Circle {
    private final Point2D center;
    private final Fraction radius;

    /**
     * Constructs a new Circle with the given location and dimension
     * @param center the Point2D at the center of the Circle
     * @param radius the square of the radius of the Circle
     */
    public Circle(Point2D center, Fraction radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Constructs a circle through three Points
     * @param a the first Point2D
     * @param b the second Point2D
     * @param c the third Point2D
     * @throws NoRealSolutionException if a, b, and c are collinear
     */
    public Circle(Point2D a, Point2D b, Point2D c) throws NoRealSolutionException {
        final Point2D center = a.perpendicularBisector(b).intersection(a.perpendicularBisector(c));
        this.center = center;
        this.radius = center.distanceSquared(a);
    }

    /**
     * Constructs a Circle through two Point2Ds signifying the endpoints of the diameter
     * @param a endpoint a
     * @param b endpoint b
     */
    public Circle(Point2D a, Point2D b) {
        final Point2D midpoint = a.midpoint(b);
        final Circle circle = new Circle(midpoint, midpoint.distanceSquared(a));
        this.center = circle.center;
        this.radius = circle.radius;
    }

    /**
     * Gets the center of this Circle
     * @return this.center
     */
    public Point2D getCenter() {
        return this.center;
    }

    /**
     * Gets the square of the radius of this Circle
     * @return this.radius
     */
    public Fraction getRadiusSquared() {
        return this.radius;
    }

    /**
     * Finds the radical axis of two Circles
     * @param c the comparator Circle
     * @return the unique Line from which tangents to this Circle and the comparator are of equal length
     */
    public Line radicalAxis(Circle c) {
        final Fraction CONSTANT_COEFFICIENT = new Fraction(2);
        final Fraction A = CONSTANT_COEFFICIENT.multiply(this.center.x().subtract(c.center.x()));
        final Fraction B = CONSTANT_COEFFICIENT.multiply(this.center.y().subtract(c.center.y()));
        final Fraction positiveC = this.center.x().pow(2).add(this.center.y().pow(2)).add(c.radius);
        final Fraction negativeC = c.center.x().pow(2).add(c.center.y().pow(2)).add(this.radius);
        final Fraction[] line = Fraction.raiseToIntegers(A, B, positiveC.subtract(negativeC));
        return new Line(line[0].asBigInteger(), line[1].asBigInteger(), line[2].asBigInteger());
    }

    /**
     * Finds the radical center of three Circles
     * @param b the second Circle
     * @param c the third Circle
     * @return the intersection of the radical axes of the three Circles
     */
    public Point2D radicalCenter(Circle b, Circle c) {
        return radicalAxis(b).intersection(radicalAxis(c));
    }

    /**
     * Finds the radical Circle of three Circles
     * @param b the second Circle
     * @param c the third Circle
     * @return the circle orthogonal to this Circle and the two parameter Circles
     */
    public Circle radicalCircle(Circle b, Circle c) {
        final Point2D radicalCenter = radicalCenter(b, c);
        return new Circle(radicalCenter, radicalCenter.distanceSquared(this.center).subtract(this.radius));
    }

    /**
     * Finds a diameter of this Circle
     * @param p the proxy Point2D
     * @return the unique Line intersecting the center of this Circle and the proxy Point2D
     */
    public Line getDiameter(Point2D p) {
        return new Line(this.center, p);
    }

    /**
     * Determines whether a given Point2D lies within this Circle
     * @param p the Point2D
     * @return true if the distance from the Point2D to the Circle center is no more than the radius, else false
     */
    public boolean contains(Point2D p) {
        return this.center.distanceSquared(p).compareTo(this.radius) <= 0;
    }

    /**
     * Compares this Circle with another Circle for equality
     * @param comparator the comparator Circle
     * @return true if the center and radius of the two Circles are identical, else false
     */
    public boolean equals(Circle comparator) {
        return this.center.equals(comparator.center) && this.radius.equals(comparator.radius);
    }

    /**
     * Converts this Circle to a printable format
     * @return this Circle in String format
     */
    @Override
    public String toString() {
        String x, y;
        if(this.center.x().equals(Fraction.ZERO)) {
            x = "x^2+";
        } else {
            x = "(x" + (this.center.x().compareTo(Fraction.ZERO) > 0 ? "-" : "+") + this.center.x().abs() + ")^2+";
        }
        if(this.center.y().equals(Fraction.ZERO)) {
            y = "y^2";
        } else {
            y = "(y" + (this.center.y().compareTo(Fraction.ZERO) > 0 ? "-" : "+") + this.center.y().abs() + ")^2";
        }
        return x.concat(y).concat("=").concat(this.radius.toString());
    }

    /**
     * Prints this Circle
     */
    public void print() {
        System.out.println(toString());
    }
}