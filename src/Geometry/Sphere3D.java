package Geometry;

import Algebra.Fraction;

import java.math.BigInteger;

public class Sphere3D {
    private final Point3D center;
    private final Fraction radius;

    /**
     * Creates a new Sphere3D at the origin with radius zero
     */
    public Sphere3D() {
        this.center = Point3D.ORIGIN;
        this.radius = Fraction.ZERO;
    }

    /**
     * Creates a new Sphere3D with the given center and size
     * @param center the Point3D at the center of the Sphere3D
     * @param radius the Fraction representing the square of the radius
     */
    public Sphere3D(Point3D center, Fraction radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Creates a Sphere3D intersecting four Point3Ds
     * @param a the first Point3D
     * @param b the second Point3D
     * @param c the third Point3D
     * @param d the fourth Point3D
     */
    public Sphere3D(Point3D a, Point3D b, Point3D c, Point3D d) {
        this.center = a.midPlane(b).intersect(a.midPlane(c), a.midPlane(d));
        this.radius = this.center.distanceSquared(a);
    }

    /**
     * Gets the center of this Sphere3D
     * @return this.center
     */
    public Point3D getCenter() {
        return this.center;
    }

    /**
     * Finds the radical plane
     * @param s the comparator Sphere3D
     * @return the unique Plane from which tangent cones from each Circle are of equal length
     */
    public Plane radicalPlane(Sphere3D s) {
        final Fraction TWO = new Fraction(BigInteger.TWO);
        Fraction x = TWO.multiply(this.center.x().subtract(s.center.x()));
        Fraction y = TWO.multiply(this.center.y().subtract(s.center.y()));
        Fraction z = TWO.multiply(this.center.z().subtract(s.center.z()));
        Fraction a_pos = this.center.x().pow(2).subtract(s.center.x().pow(2));
        Fraction b_pos = this.center.y().pow(2).subtract(s.center.y().pow(2));
        Fraction c_pos = this.center.z().pow(2).subtract(s.center.z().pow(2));
        Fraction k = s.radius.subtract(this.radius);
        return new Plane(x, y, z, a_pos.add(b_pos).add(c_pos).add(k));
    }

    /**
     * Finds the radical Vector of three Spheres
     * @param a the second Sphere3D
     * @param b the third Sphere3D
     * @return the Vector parallel to the Line of intersection between all radical Planes of the three Spheres
     */
    public Vector radicalVector(Sphere3D a, Sphere3D b) {
        return radicalPlane(a).intersect(radicalPlane(b));
    }

    /**
     * Finds the radical center of four Spheres
     * @param a the second Circle
     * @param b the third Circle
     * @param c the fourth Circle
     * @return the point where all tangent lines
     */
    public Point3D radicalCenter(Sphere3D a, Sphere3D b, Sphere3D c) {
        return radicalPlane(a).intersect(radicalPlane(b), radicalPlane(c));
    }

    /**
     * Determines whether a given Point3D lies within this Sphere3D
     * @param p the Point3D
     * @return true if the distance from the Point3D to the Sphere3D center is no more than the radius, else false
     */
    public boolean contains(Point3D p) {
        return this.center.distanceSquared(p).compareTo(this.radius) <= 0;
    }

    /**
     * Compares this Sphere3D and another Sphere3D for equality
     * @param o the comparator Sphere3D
     * @return true if both the center and radius are identical, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Sphere3D)) {
            return false;
        }
        Sphere3D comparator = (Sphere3D) o;
        return this.center.equals(comparator.center) && this.radius.equals(comparator.radius);
    }

    /**
     * Converts this Sphere3D to a printable format
     * @return this Sphere3D in String format
     */
    @Override
    public String toString() {
        String x, y, z;
        if(this.center.x().equals(Fraction.ZERO)) {
            x = "x^2+";
        } else {
            x = "(x" + (this.center.x().compareTo(Fraction.ZERO) > 0 ? "-" : "+") + this.center.x().abs() + ")^2+";
        }
        if(this.center.y().equals(Fraction.ZERO)) {
            y = "y^2+";
        } else {
            y = "(y" + (this.center.y().compareTo(Fraction.ZERO) > 0 ? "-" : "+") + this.center.y().abs() + ")^2+";
        }
        if(this.center.z().equals(Fraction.ZERO)) {
            z = "z^2";
        } else {
            z = "(z" + (this.center.z().compareTo(Fraction.ZERO) > 0 ? "-" : "+") + this.center.z().abs() + ")^2";
        }
        return x.concat(y).concat(z).concat("=").concat(this.radius.toString());
    }

    /**
     * Prints this Sphere3D
     */
    public void print() {
        System.out.println(toString());
    }
}