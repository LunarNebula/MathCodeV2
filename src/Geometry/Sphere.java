package Geometry;

import Algebra.Fraction;
import Algebra.LinearEquation;
import Exception.*;

public class Sphere {
    private final Point center;
    private final Fraction radius;

    /**
     * Creates a new Sphere with the given parameters
     * @param center the center Point
     * @param radius the square of the radius
     */
    public Sphere(Point center, Fraction radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Creates a new Sphere representing the circumsphere of a set of Points
     * @param p the array of Points
     * @throws IllegalDimensionException if any two Points do not exist in the same dimension
     */
    public Sphere(Point... p) throws IllegalDimensionException {
        final int DIMENSION = p.length - 1;
        for(Point point : p) {
            if(DIMENSION != point.dimension()) {
                throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
            }
        }
        final LinearEquation[] orthogonalBisectors = new LinearEquation[p.length - 1];
        for(int i = 0; i < orthogonalBisectors.length; i++) {
            orthogonalBisectors[i] = p[i].orthogonalBisector(p[i + 1]);
        }
        this.center = LinearEquation.intersect(orthogonalBisectors);
        this.radius = p[0].distanceSquared(this.center);
    }

    /**
     * Finds the radical LinearEquation of two NSpheres
     * @param n the comparator Sphere
     * @return the LinearEquation between the two NSpheres from which all Sphere tangent lines have congruent length
     */
    public LinearEquation radicalSubspace(Sphere n) {
        final Vector center = new Vector(this.center), n_center = new Vector(n.center);
        final Fraction constant = center.norm2Squared().subtract(n_center.norm2Squared()).add(n.radius).subtract(this.radius);
        return new LinearEquation(constant, new Vector(this.center, n.center).scale(new Fraction(2)).getCoordinates());
    }

    /**
     * Finds the center of this Sphere
     * @return this.center
     */
    public Point getCenter() {
        return this.center;
    }

    /**
     * Finds the squared radius of this Sphere
     * @return this.radius
     */
    public Fraction getRadius() {
        return this.radius;
    }

    /**
     * Finds the Euclidean dimension of this Sphere
     * @return this.center.dimension()
     */
    public int dimension() {
        return this.center.dimension();
    }

    /**
     * Converts this Sphere to a printable format
     * @return this Sphere as a String
     */
    @Override
    public String toString() {
        final String[] terms = new String[this.center.dimension()];
        final Fraction[] coordinates = this.center.getCoordinates();
        for(int i = 0; i < terms.length; i++) {
            terms[i] = "x_" + i;
            if(! coordinates[i].equals(Fraction.ZERO)) {
                String printedTerm = coordinates[i].abs().toString();
                printedTerm = (coordinates[i].sign() > 0 ? "-" : "+").concat(printedTerm);
                terms[i] = "(".concat(terms[i]).concat(printedTerm).concat(")");
            }
        }
        String print = "";
        for(String term : terms) {
            print = print.concat("+").concat(term).concat("^2");
        }
        return (print.length() == 0 ? "r" : print.substring(1)).concat("=").concat(this.radius.toString());
    }

    /**
     * Prints this Sphere
     */
    public void print() {
        System.out.println(toString());
    }

    // static methods

    /**
     * Finds the radical center of an array of NSpheres
     * @param n the array of NSpheres
     * @return the Point from which all tangent line segments to each Sphere is of equal length
     */
    public static Point radicalCenter(Sphere... n) throws IllegalDimensionException {
        for(Sphere sphere : n) {
            if(sphere.center.dimension() != n.length - 1) {
                throw new IllegalDimensionException(IllegalDimensionException.UNEQUAL_VECTOR_DIMENSION);
            }
        }
        LinearEquation[] radicalLinearEquations = new LinearEquation[n.length - 1];
        for(int i = 0; i < radicalLinearEquations.length; i++) {
            radicalLinearEquations[i] = n[i].radicalSubspace(n[i + 1]);
        }
        return LinearEquation.intersect(radicalLinearEquations);
    }
}