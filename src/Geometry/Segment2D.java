package Geometry;

import Algebra.Fraction;

public class Segment2D {
    private final Point2D a, b;

    /**
     * Creates a new Segment2D
     * @param a the first end Point2D of this Segment2D
     * @param b the second end Point2D of this Segment2D
     */
    public Segment2D(Point2D a, Point2D b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Finds the endpoints of this Segment2D
     * @return this.a, this.b
     */
    public Point2D[] getEndpoints() {
        return new Point2D[]{this.a, this.b};
    }

    /**
     * Converts this Segment2D to a Line
     * @return a Line containing both end Point2Ds of this Segment2D
     */
    public Line toLine() {
        return new Line(this.a, this.b);
    }

    /**
     * Gets the squared length of this Segment2D
     * @return the squared distance between this.a and this.b
     */
    public Fraction lengthSquared() {
        return this.a.distanceSquared(this.b);
    }

    /**
     * Finds the minimum distance from
     * @param s the comparator Segment2D
     * @return the minimum squared distance between any point A on this Segment2D and B on the comparator
     */
    public Fraction minimumDistanceSquared(Segment2D s) {
        if(intersects(s)) {
            return Fraction.ZERO;
        }
        Line thisLine = toLine(), sLine = s.toLine();
        final Point2D thisAReflection = this.a.reflectAcrossLine(sLine), thisBReflection = this.b.reflectAcrossLine(sLine);
        final Point2D sAReflection = s.a.reflectAcrossLine(thisLine), sBReflection = s.b.reflectAcrossLine(thisLine);
        Point2D thisReflection, thisOriginal, sReflection, sOriginal, thisEndpoint, sEndpoint;
        if(thisAReflection.distanceSquared(this.a).compareTo(thisBReflection.distanceSquared(this.b)) < 0) {
            thisReflection = thisAReflection;
            thisOriginal = this.a;
        } else {
            thisReflection = thisBReflection;
            thisOriginal = this.b;
        }
        if(sAReflection.distanceSquared(s.a).compareTo(sBReflection.distanceSquared(s.b)) < 0) {
            sReflection = sAReflection;
            sOriginal = s.a;
        } else {
            sReflection = sBReflection;
            sOriginal = s.b;
        }
        if(new Segment2D(thisReflection, thisOriginal).intersects(s)) {
            sEndpoint = thisReflection.midpoint(thisOriginal);
        } else {
            sEndpoint = (s.a.distanceSquared(thisOriginal).compareTo(s.b.distanceSquared(thisOriginal)) < 0) ? s.a : s.b;
        }
        if(intersects(new Segment2D(sReflection, sOriginal))) {
            thisEndpoint = sReflection.midpoint(sOriginal);
        } else {
            thisEndpoint = (this.a.distanceSquared(sOriginal).compareTo(this.b.distanceSquared(sOriginal)) < 0) ? this.a : this.b;
        }
        return thisEndpoint.distanceSquared(sEndpoint);
    }

    /**
     * Determines whether this Segment2D intersects a second specified Segment2D
     * @param o the comparator Segment2D
     * @return true if this Segment2D and the comparator intersect between their respective endpoints, else false
     */
    public boolean intersects(Segment2D o) {
        final Point2D intersection = new Line(this.a, this.b).intersection(new Line(o.a, o.b));
        final Fraction thisDistanceSum = intersection.distanceSquared(this.a).add(intersection.distanceSquared(this.b));
        final boolean intersectsThisLine = thisDistanceSum.compareTo(this.a.distanceSquared(this.b)) <= 0;
        final Fraction oDistanceSum = intersection.distanceSquared(o.a).add(intersection.distanceSquared(o.b));
        final boolean intersectsOLine = oDistanceSum.compareTo(o.a.distanceSquared(o.b)) <= 0;
        return intersectsThisLine && intersectsOLine;
    }

    /**
     * Determines whether this Segment2D is equal to a comparator Object
     * @param o the comparator
     * @return true if o is a Segment2D with congruent endpoints, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof final Segment2D convert)) {
            return false;
        }
        return (this.a.equals(convert.a) && this.b.equals(convert.b)) || (this.a.equals(convert.b) && this.b.equals(convert.a));
    }

    /**
     * Converts this Segment2D to a printable format
     * @return this Segment2D as a String
     */
    @Override
    public String toString() {
        return "{" + this.a + "," + this.b + "}";
    }

    /**
     * Prints this Segment2D
     */
    public void print() {
        System.out.println(this);
    }

    // static methods

    /**
     * Finds the number of intersections in a set of Segment2Ds
     * @param s the Segment2D array
     * @return the number of times two Segment2Ds intersect
     */
    public static int numberOfIntersections(Segment2D... s) {
        int count = 0;
        for(int i = 0; i < s.length; i++) {
            for(int j = i + 1; j < s.length; j++) {
                if(s[i].intersects(s[j])) {
                    count++;
                }
            }
        }
        return count;
    }
}
