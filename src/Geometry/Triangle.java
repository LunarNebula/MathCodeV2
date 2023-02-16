package Geometry;

import Algebra.Fraction;

public class Triangle {
    private final Point2D a, b, c;

    /**
     * Constructs a new Triangle with the given vertices
     * @param a vertex a
     * @param b vertex b
     * @param c vertex c
     */
    public Triangle(Point2D a, Point2D b, Point2D c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Finds the circumcenter of this Triangle
     * @return the intersection of the perpendicular bisectors of the vertices
     */
    public Point2D circumcenter() {
        return this.a.perpendicularBisector(this.b).intersection(this.a.perpendicularBisector(this.c));
    }

    /**
     * Finds the circumcircle of this Triangle
     * @return the unique Circle that contains all three vertices of this Triangle
     */
    public Circle circumcircle() {
        return new Circle(this.a, this.b, this.c);
    }

    /**
     * Finds the orthocenter of this Triangle
     * @return the intersection of the altitudes
     */
    public Point2D orthocenter() {
        final Line AB = new Line(this.a, this.b), AC = new Line(this.a, this.c);
        return AB.perpendicularAtPoint(this.c).intersection(AC.perpendicularAtPoint(this.b));
    }

    /**
     * Finds an orthic foot of this Triangle
     * @param v the opposing Vertex of the foot
     * @return the intersection of the altitude from and the line opposite the designated vertex
     */
    public Point2D orthicFoot(Vertex v) {
        final Point2D[] targetPoints = vertexPermutation(v);
        final Line line = new Line(targetPoints[1], targetPoints[2]);
        return line.intersection(line.perpendicularAtPoint(targetPoints[0]));
    }

    /**
     * Finds the orthic Triangle of this Triangle
     * @return the unique Triangle whose vertices are the orthic feet of this Triangle
     */
    public Triangle orthicTriangle() {
        final Line AB = new Line(this.a, this.b), BC = new Line(this.b, this.c), CA = new Line(this.c, this.a);
        final Point2D a = BC.intersection(BC.perpendicularAtPoint(this.a)); // orthic foot A
        final Point2D b = CA.intersection(CA.perpendicularAtPoint(this.b)); // orthic foot B
        final Point2D c = AB.intersection(AB.perpendicularAtPoint(this.c)); // orthic foot C
        return new Triangle(a, b, c);
    }

    /**
     * Finds a median of this Triangle
     * @param v the Vertex label intersecting the target median
     * @return the line intersecting the target vertex and the midpoint of the opposite side
     */
    public Line median(Vertex v) {
        final Point2D[] targetPoints = vertexPermutation(v);
        return new Line(targetPoints[0], targetPoints[1].midpoint(targetPoints[2]));
    }

    /**
     * Creates a permutation of the vertices of this <code>Triangle</code>.
     * @param v the chosen <code>Triangle.Vertex</code>
     * @return the permutation
     */
    private Point2D[] vertexPermutation(Vertex v) {
        return switch (v) {
            case A -> new Point2D[]{this.a, this.b, this.c};
            case B -> new Point2D[]{this.b, this.c, this.a};
            case C -> new Point2D[]{this.c, this.a, this.b};
        };
    }

    /**
     * Finds the centroid of this Triangle
     * @return the intersection of the medians
     */
    public Point2D centroid() {
        return (new Line(this.a, this.b.midpoint(this.c))).intersection(new Line(this.b, this.a.midpoint(this.c)));
    }

    /**
     * Finds the Euler line of this Triangle
     * @return the unique Line containing the circumcenter, orthocenter, and centroid
     */
    public Line eulerLine() {
        return new Line(circumcenter(), centroid());
    }

    /**
     * Finds the center of the nine-point Circle
     * @return ninePointCircle().getCenter()
     */
    public Point2D ninePointCenter() {
        return ninePointCircle().getCenter();
    }

    /**
     * Finds the nine-point Circle of this Triangle
     * @return the Circle that intersects all side midpoints, all orthic feet, and all vertex-centroid midpoints
     */
    public Circle ninePointCircle() {
        return new Circle(this.a.midpoint(this.b), this.b.midpoint(this.c), this.c.midpoint(this.a));
    }

    /**
     * Finds the symmedian point of this Triangle
     * @return the intersection of the symmedians
     */
    public Point2D symmedianPoint() {
        final Point2D altMidpointA = orthicFoot(Vertex.A).midpoint(this.a);
        final Point2D altMidpointB = orthicFoot(Vertex.B).midpoint(this.b);
        final Point2D midpointA = this.b.midpoint(this.c), midpointB = this.a.midpoint(this.c);
        return (new Line(altMidpointA, midpointA)).intersection(new Line(altMidpointB, midpointB));
    }

    /**
     * Finds the symmedian corresponding to a specific
     * @param v the Vertex denoting the intersecting vertex
     * @return the symmedian intersecting the given vertex
     */
    public Line symmedian(Vertex v) {
        return new Line(switch (v) {
            case A -> this.a;
            case B -> this.b;
            case C -> this.c;
        }, symmedianPoint());
    }

    /**
     * Finds the auxiliary Triangle of this Triangle
     * @return the Triangle formed from the midpoints of these vertices
     */
    public Triangle auxiliaryTriangle() {
        return new Triangle(this.b.midpoint(this.c), this.a.midpoint(this.c), this.a.midpoint(this.b));
    }

    /**
     * Finds the anti-complementary Triangle of this Triangle
     * @return the Triangle for which this Triangle is its auxiliary Triangle
     */
    public Triangle antiComplementaryTriangle() {
        final Fraction DOUBLE_DISTANCE_RATIO = new Fraction(2), NEGATE = Fraction.ONE.negate();
        final Point2D vertexA = this.b.midpoint(this.c).pointFromDistanceRatio(this.a, NEGATE, DOUBLE_DISTANCE_RATIO);
        final Point2D vertexB = this.a.midpoint(this.c).pointFromDistanceRatio(this.b, NEGATE, DOUBLE_DISTANCE_RATIO);
        final Point2D vertexC = this.a.midpoint(this.b).pointFromDistanceRatio(this.c, NEGATE, DOUBLE_DISTANCE_RATIO);
        return new Triangle(vertexA, vertexB, vertexC);
    }

    /**
     * Compares two Triangles for equality
     * @param o the comparator Triangle
     * @return true if all three vertices are shared between this Triangle and the comparator, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof final Triangle t)) {
            return false;
        }
        if(this.a.equals(t.a)) {
            return (this.b.equals(t.b) && this.c.equals(t.c)) || (this.b.equals(t.c) && this.c.equals(t.b));
        } else if(this.a.equals(t.b)) {
            return (this.b.equals(t.c) && this.c.equals(t.a)) || (this.b.equals(t.a) && this.c.equals(t.c));
        } else if(this.a.equals(t.c)) {
            return (this.c.equals(t.b) && this.b.equals(t.a)) || (this.c.equals(t.a) && this.b.equals(t.b));
        }
        return false;
    }

    /**
     * Enumerates the Vertices of this Triangle
     */
    public enum Vertex {
        A, B, C
    }

    /**
     * Converts this Triangle into a printable format
     * @return this Triangle as a String
     */
    @Override
    public String toString() {
        return General.Print.toString(this.a, this.b, this.c);
    }

    /**
     * Prints this Triangle
     */
    public void print() {
        System.out.println(this);
    }
}