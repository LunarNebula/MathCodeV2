package Algebra;

import Exception.*;
import General.TrueTextEncodable;
import Geometry.Circle;
import Geometry.Point2D;
import Theory.Combinatorics;
import Theory.Factor;

import java.math.BigInteger;
import java.util.*;

public class Polynomial implements Comparable<Polynomial>, TrueTextEncodable {
    private Fraction[] terms;

    /**
     * Creates a new, empty Polynomial
     */
    public Polynomial() {
        this.terms = new Fraction[0];
    }

    /**
     * Creates a new Polynomial
     * @param f the starting coefficients
     * @throws IllegalArgumentException if any coefficient in this Polynomial is initialized as null
     */
    public Polynomial(Fraction... f) throws IllegalArgumentException {
        this.terms = f;
        for(Fraction term : f) {
            if(term == null) {
                throw new IllegalArgumentException();
            }
        }
        simplify();
    }

    /**
     * Creates a new Polynomial
     * @param n the starting Number coefficients
     */
    public Polynomial(Number... n) {
        this.terms = new Fraction[n.length];
        for(int i = 0; i < n.length; i++) {
            this.terms[i] = new Fraction(n[i]);
        }
        simplify();
    }

    /**
     * Creates a new Polynomial
     * @param b the starting BigInteger coefficients
     */
    public Polynomial(BigInteger... b) {
        this.terms = new Fraction[b.length];
        for(int i = 0; i < b.length; i++) {
            this.terms[i] = new Fraction(b[i]);
        }
        simplify();
    }

    /**
     * Creates a new Polynomial
     * @param s the String encoding the coefficients of this Polynomial
     */
    public Polynomial(String s) {
        final BigInteger[][] coefficients = General.Converter.convertTo2DBigIntegerArray(s, "/", ",");
        this.terms = new Fraction[coefficients.length];
        for(int i = 0; i < this.terms.length; i++) {
            this.terms[i] = new Fraction(coefficients[i]);
        }
        simplify();
    }

    /**
     * Adds two Polynomials
     * @param addend the addend Polynomial
     * @return the sum of this Polynomial and the addend
     */
    public Polynomial add(Polynomial addend) {
        final Polynomial sum = new Polynomial();
        sum.terms = new Fraction[Math.max(this.terms.length, addend.terms.length)];
        for(int i = 0; i < sum.terms.length; i++) {
            Fraction c = Fraction.ZERO;
            if(i < this.terms.length) {
                c = c.add(this.terms[i]);
            }
            if(i < addend.terms.length) {
                c = c.add(addend.terms[i]);
            }
            sum.terms[i] = c;
        }
        sum.simplify();
        return sum;
    }

    /**
     * Subtracts two Polynomials
     * @param subtrahend the subtrahend Polynomial
     * @return the difference between this Polynomial and the subtrahend
     */
    public Polynomial subtract(Polynomial subtrahend) {
        final Polynomial difference = new Polynomial();
        difference.terms = new Fraction[Math.max(this.terms.length, subtrahend.terms.length)];
        for(int i = 0; i < difference.terms.length; i++) {
            Fraction c = Fraction.ZERO;
            if(i < this.terms.length) {
                c = c.add(this.terms[i]);
            }
            if(i < subtrahend.terms.length) {
                c = c.subtract(subtrahend.terms[i]);
            }
            difference.terms[i] = c;
        }
        difference.simplify();
        return difference;
    }

    /**
     * Multiplies this Polynomial by a constant
     * @param multiplicand the Fraction multiplicand
     * @return the product of the multiplicand and this Polynomial
     */
    public Polynomial multiply(Fraction multiplicand) {
        if(multiplicand.equals(Fraction.ZERO)) {
            return new Polynomial(0);
        }
        final Polynomial product = new Polynomial();
        product.terms = new Fraction[this.terms.length];
        for(int i = 0; i < product.terms.length; i++) {
            product.terms[i] = this.terms[i].multiply(multiplicand);
        }
        return product;
    }

    /**
     * Multiplies two Polynomials
     * @param multiplicand the multiplicand Polynomial
     * @return the product of this Polynomial and the multiplicand
     */
    public Polynomial multiply(Polynomial multiplicand) {
        final Polynomial product = new Polynomial();
        product.terms = new Fraction[this.terms.length + multiplicand.terms.length - 1];
        java.util.Arrays.fill(product.terms, Fraction.ZERO);
        for(int i = 0; i < this.terms.length; i++) {
            for(int j = 0; j < multiplicand.terms.length; j++) {
                product.terms[i + j] = product.terms[i + j].add(this.terms[i].multiply(multiplicand.terms[j]));
            }
        }
        product.simplify();
        return product;
    }

    /**
     * Raises this Polynomial to an integer power
     * @param pow the power of the Polynomial
     * @return this Polynomial ^ exp
     */
    public Polynomial pow(int pow) {
        Polynomial antilogarithm = new Polynomial(1);
        List<Boolean> powers = new LinkedList<>();
        while(pow > 0) {
            powers.add(0, (pow & 1) == 1);
            pow >>>= 1;
        }
        for(boolean instruction : powers) {
            antilogarithm = antilogarithm.multiply(antilogarithm);
            if(instruction) {
                antilogarithm = multiply(antilogarithm);
            }
        }
        return antilogarithm;
    }

    /**
     * Finds the quotient and remainder of two Polynomials
     * @param divisor the divisor Polynomial
     * @return the result and residual Polynomial when this is divided by the divisor
     */
    public Polynomial[] divideAndRemainder(Polynomial divisor) {
        Polynomial quotient = new Polynomial(0), test = this;
        while(test.degree() >= divisor.degree() && (! test.equals(new Polynomial(0)))) {
            Fraction lead = test.terms[test.degree()].divide(divisor.terms[divisor.degree()]);
            Polynomial proxy = polynomial(test.degree() - divisor.degree()).multiply(lead);
            quotient = quotient.add(proxy);
            test = subtract(quotient.multiply(divisor));
        }
        return new Polynomial[]{quotient, test};
    }

    /**
     * Divides two Polynomials
     * @param divisor the divisor Polynomial
     * @return the quotient of this Polynomial and the divisor Polynomial
     */
    public Polynomial divide(Polynomial divisor) {
        return divideAndRemainder(divisor)[0];
    }

    /**
     * Divides two Polynomials and finds the remainder
     * @param divisor the divisor Polynomial
     * @return the remainder when this Polynomial is divided by the divisor
     */
    public Polynomial mod(Polynomial divisor) {
        return divideAndRemainder(divisor)[1];
    }

    /**
     * Finds the greatest common divisor of two Polynomials
     * @param b the comparator Polynomial
     * @return the Polynomial of greatest degree that divides both this Polynomial and the comparator Polynomial
     */
    public Polynomial gcd(Polynomial b) {
        Polynomial a = this;
        final Polynomial X = new Polynomial(0, 1), ZERO = new Polynomial(0);
        while(a.degree() > 0 && b.degree() > 0) {
            if(a.degree() < b.degree()) {
                Polynomial switcher = b;
                b = a;
                a = switcher;
            }
            if(! b.equals(ZERO)) {
                b = b.multiply(b.terms[b.degree()].inverse());
            }
            if(! a.equals(ZERO)) {
                a = a.multiply(a.terms[a.degree()].inverse()).subtract(X.pow(a.degree() - b.degree()).multiply(b));
            }
        }
        if(a.degree() < b.degree()) {
            Polynomial proxy = a;
            a = b;
            b = proxy;
        }
        return (a.degree() == 0 || ! b.equals(ZERO)) ? new Polynomial(1) : new Polynomial(Fraction.raiseToIntegers(a.getTerms()));
    }

    /**
     * Finds the additive inverse of this Polynomial
     * @return this * -1
     */
    public Polynomial negate() {
        return multiply(new Fraction(-1));
    }

    /**
     * Evaluates this Polynomial at a specific point
     * @param x the x-coordinate
     * @return the y-coordinate of the given x, or f(x)
     */
    public Fraction evaluate(Fraction x) {
        Fraction y = Fraction.ZERO;
        for(int i = this.terms.length - 1; i >= 0; i--) {
            y = y.multiply(x).add(this.terms[i]);
        }
        return y;
    }

    /**
     * Evaluates this <code>Polynomial</code> at a specific <code>Matrix</code> input
     * @param x the specified <code>Matrix</code>
     * @return <code>f(x)</code>
     */
    public Matrix evaluate(Matrix x) {
        if(x.isNonSquare()) {
            throw new IllegalDimensionException(IllegalDimensionException.NON_SQUARE_MATRIX);
        }
        final Matrix I = Matrix.identityMatrix(x.rowSize());
        Matrix y = new Matrix(x.rowSize());
        for(int i = this.terms.length - 1; i >= 0; i--) {
            y = y.multiply(x).add(I.multiply(this.terms[i]));
        }
        return y;
    }

    /**
     * Composes this Polynomial with another Polynomial
     * @param p the composition function
     * @return f(p)
     */
    public Polynomial compose(Polynomial p) {
        Polynomial composition = new Polynomial(0);
        for(int i = this.terms.length - 1; i >= 0; i--) {
            composition = composition.multiply(p).add(new Polynomial(this.terms[i]));
        }
        return composition;
    }

    /**
     * Finds the derivative of this Polynomial
     * @return the Polynomial describing the rate of change at any point on this Polynomial
     */
    public Polynomial derivative() {
        if(this.terms.length == 1) {
            return new Polynomial(0);
        }
        final Polynomial derivative = new Polynomial(0);
        derivative.terms = new Fraction[this.degree()];
        for(int i = 1; i < this.terms.length; i++) {
            derivative.terms[i - 1] = this.terms[i].multiply(new Fraction(i));
        }
        return derivative;
    }

    /**
     * Finds the antiderivative of this Polynomial
     * @return the Polynomial describing the area under the curve of this Polynomial
     */
    public Polynomial antiDerivative() {
        final Polynomial antiDerivative = new Polynomial(0);
        antiDerivative.terms = new Fraction[this.terms.length + 1];
        antiDerivative.terms[0] = Fraction.ZERO;
        for(int i = 1; i < antiDerivative.terms.length; i++) {
            antiDerivative.terms[i] = this.terms[i - 1].multiply(new Fraction(i).inverse());
        }
        return antiDerivative;
    }

    /**
     * Finds the osculating circle at a certain x-value on this Polynomial
     * @param x the x-value where the curvature is measured
     * @return the Circle most closely describing the curvature of this Polynomial at the given point
     */
    public Circle osculatingCircle(Fraction x) {
        final Polynomial derivative = derivative();
        final Fraction y = evaluate(x), dX = derivative.evaluate(x), dXdX = derivative.derivative().evaluate(x);
        final Fraction factor = dX.pow(2).add(Fraction.ONE).divide(dXdX);
        final Fraction centerX = x.subtract(dX.multiply(factor)), centerY = y.add(factor); // circle center coordinates
        final Point2D center = new Point2D(centerX, centerY);
        return new Circle(center, center.distanceSquared(new Point2D(x, y)));
    }

    /**
     * Finds the degree of this Polynomial
     * @return the highest variable exponent
     */
    public int degree() {
        return this.terms.length - 1;
    }

    /**
     * Clones this Polynomial
     * @return a deep copy of this Polynomial
     */
    public Polynomial copy() {
        final Polynomial copy = new Polynomial(0);
        copy.terms = new Fraction[this.terms.length];
        for(int i = 0; i < copy.terms.length; i++) {
            copy.terms[i] = this.terms[i].copy();
        }
        return copy;
    }

    /**
     * Simplifies this Polynomial
     */
    public void simplify() {
        int index = this.terms.length - 1;
        while(index > 0 && this.terms[index].equals(Fraction.ZERO)) {
            index--;
        }
        if(index == 0) {
            this.terms = new Fraction[]{this.terms[0]};
        } else {
            final Fraction[] proxy = new Fraction[index + 1];
            for(int i = 0; i < proxy.length; i++) {
                proxy[i] = this.terms[i];
                proxy[i].simplify();
            }
            this.terms = proxy;
        }
    }

    /**
     * Depresses this Polynomial
     * @return this Polynomial, shifted to eliminate the term of second-highest degree
     */
    public Polynomial depress() {
        if(degree() < 1) {
            return this;
        }
        final Fraction a = this.terms[degree()], b = this.terms[degree() - 1];
        return compose(new Polynomial(b.divide(a.multiply(new Fraction(-degree()))), Fraction.ONE));
    }

    /**
     * Finds the gnomon of this Polynomial
     * @return f(x)-f(x-1)
     */
    public Polynomial gnomon() {
        return subtract(compose(new Polynomial(Fraction.ONE.negate(), Fraction.ONE)));
    }

    /**
     * Finds the anti-gnomon of this Polynomial
     * @return the Polynomial p(x) for which the gnomon is this Polynomial
     */
    public Polynomial antiGnomon() {
        Fraction accumulativeSum = Fraction.ZERO;
        final Fraction[] x_coordinates = new Fraction[this.terms.length + 1], values = new Fraction[this.terms.length + 1];
        x_coordinates[0] = Fraction.ZERO;
        values[0] = Fraction.ZERO;
        for(int i = 1; i < values.length; i++) {
            x_coordinates[i] = new Fraction(i);
            accumulativeSum = accumulativeSum.add(evaluate(x_coordinates[i]));
            values[i] = accumulativeSum;
        }
        final Point2D[] points = new Point2D[x_coordinates.length];
        for(int i = 0; i < points.length; i++) {
            points[i] = new Point2D(x_coordinates[i], values[i]);
        }
        return lagrangePolynomial(points);
    }

    /**
     * Finds all rational roots of this Polynomial
     * @return an array of Fractions 'x' corresponding to this Polynomial p for which p(x) = 0
     */
    public List<Fraction> rationalRoots() {
        if(degree() < 1) {
            return new LinkedList<>();
        }
        final List<Fraction> rationalRoots = new LinkedList<>();
        if(degree() == 1) {
            rationalRoots.add(this.terms[0].negate().divide(this.terms[degree()]));
        } else {
            Polynomial test = this, X = new Polynomial(0, 1);
            if(test.terms[0].equals(Fraction.ZERO)) {
                rationalRoots.add(Fraction.ZERO);
            }
            while(test.terms[0].equals(Fraction.ZERO) && test.degree() > 0) {
                test = test.divide(X);
            } // weeds out all roots r = 0 for Rational Root Test
            final Fraction[] combine = Fraction.raiseToIntegers(this.terms[0], this.terms[degree()], Fraction.ONE);
            final List<BigInteger> numerators = Factor.factors(combine[0].asBigInteger());
            final List<BigInteger> denominators = Factor.factors(combine[1].asBigInteger());
            for(BigInteger n : numerators) {
                for(BigInteger d : denominators) {
                    if(n.gcd(d).equals(BigInteger.ONE)) {
                        final Fraction rootCandidate = new Fraction(n, d), negRoot = new Fraction(n.negate(), d);
                        if(evaluate(rootCandidate).equals(Fraction.ZERO)) {
                            rationalRoots.add(rootCandidate);
                        }
                        if(evaluate(negRoot).equals(Fraction.ZERO)) {
                            rationalRoots.add(negRoot);
                        }
                    }
                }
            }
        }
        return rationalRoots;
    }

    /**
     * Finds all Polynomial factors of this Polynomial
     * @return a List of all Polynomials 'p' for which p evenly divides this Polynomial
     */
    public List<Polynomial> factors() {
        Polynomial test = copy();
        final List<Polynomial> factors = new LinkedList<>();
        for (Fraction root : rationalRoots()) {
            final Polynomial rootTerm = root.minimalPolynomial();
            boolean checkEvenDivision = true;
            while (checkEvenDivision) {
                final Polynomial[] division = test.divideAndRemainder(rootTerm);
                if(division[1].equals(new Polynomial(0))) {
                    factors.add(rootTerm);
                    test = division[0];
                } else {
                    checkEvenDivision = false;
                }
            } // eliminates Rational Roots
        }
        final List<BigInteger>[] factorLists = new List[(test.degree() >> 1) + 1];
        for (int i = 0; i < factorLists.length; i++) {
            factorLists[i] = Factor.factors(test.evaluate(new Fraction(i)).asBigInteger());
            if (i + 1 < factorLists.length) {
                final ListIterator<BigInteger> factorNegation = factorLists[i].listIterator();
                while (factorNegation.hasNext()) {
                    factorNegation.add(factorNegation.next().negate());
                }
            }
        }
        for (int i = 2; i <= test.degree() / 2; i++) { // starts at quadratic factor candidates after rationals
            final ListIterator<BigInteger>[] iterators = new ListIterator[i + 1];
            for (int j = 0; j <= i; j++) {
                iterators[j] = factorLists[j].listIterator();
            }
            final BigInteger[] y = new BigInteger[i + 1];
            for (int j = 0; j <= i; j++) {
                y[j] = iterators[j].next();
            }
            boolean checkDegree = true; // becomes false when all point combinations have been checked
            while (checkDegree) {
                boolean shiftState = true; // becomes false when the program reaches the next legal point set
                int incrementationIndex = 0;
                while (shiftState) {
                    if(iterators[incrementationIndex].hasNext()) {
                        y[incrementationIndex] = iterators[incrementationIndex].next();
                        shiftState = false;
                    } else {
                        iterators[incrementationIndex] = factorLists[incrementationIndex].listIterator();
                        y[incrementationIndex] = iterators[incrementationIndex].next();
                        incrementationIndex++;
                    }
                    if(incrementationIndex == iterators.length) {
                        shiftState = false;
                        checkDegree = false;
                    } // enters this statement if the iterators have passed through all point combinations
                }
                if(checkDegree) {
                    final Point2D[] points = new Point2D[i + 1];
                    for(int j = 0; j <= i; j++) {
                        points[j] = new Point2D(BigInteger.valueOf(j), y[j]);
                    }
                    final Polynomial rootCandidate = regression(i, points);
                    boolean checkDivisibility = rootCandidate.degree() >= i; // filters out lower-degree polynomials
                    while(checkDivisibility) {
                        final Polynomial[] division = test.divideAndRemainder(rootCandidate);
                        if(division[1].equals(new Polynomial(0))) {
                            factors.add(rootCandidate);
                            test = division[0];
                        } else {
                            checkDivisibility = false;
                        }
                    }
                }
            }
        }
        if(test.degree() > 0) {
            factors.add(test);
        } else if(! test.equals(new Polynomial(1))) {
            factors.add(0, test);
        }
        return factors;
    }

    /**
     * Gets the terms of this Polynomial
     * @return an array of Fractions with the terms of this Polynomial
     */
    public Fraction[] getTerms() {
        final Fraction[] terms = new Fraction[this.terms.length];
        System.arraycopy(this.terms, 0, terms, 0, terms.length);
        return terms;
    }

    /**
     * Queries two Polynomials to compare their limits at f(infinity)
     * @param comparator the comparator Polynomial
     * @return 1 if this Polynomial has a faster positive expansion, else 0 if this equals the comparator, else -1
     */
    @Override
    public int compareTo(Polynomial comparator) {
        if(degree() > comparator.degree()) {
            return this.terms[degree()].compareTo(Fraction.ZERO);
        } else if(degree() < comparator.degree()) {
            return comparator.terms[comparator.degree()].compareTo(Fraction.ZERO);
        }
        for(int i = this.terms.length - 1; i >= 0; i--) {
            final int compareTo = this.terms[i].compareTo(comparator.terms[i]);
            if(compareTo * compareTo == 1) {
                return compareTo;
            }
        }
        return 0;
    }

    /**
     * Queries another Polynomial for equality
     * @param o the comparator Polynomial
     * @return true if all coefficients of the Polynomials match, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof final Polynomial convert)) {
            return false;
        }
        simplify();
        if(this.terms.length != convert.terms.length) {
            return false;
        }
        for(int i = 0; i < convert.terms.length; i++) {
            if(! this.terms[i].equals(convert.terms[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the Sylvester Matrix of two Polynomials
     * @param p the second Polynomial
     * @return the Matrix containing the coefficients of each Polynomial in each row
     */
    public Matrix sylvesterMatrix(Polynomial p) {
        final Fraction[][] matrix = new Fraction[degree() + p.degree()][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = new Fraction[degree() + p.degree()];
            Arrays.fill(matrix[i], Fraction.ZERO);
        }
        for(int i = 0; i < p.degree(); i++) {
            for(int j = 0; j < this.terms.length; j++) {
                matrix[i][i + j] = this.terms[degree() - j];
            }
        }
        for(int i = 0; i < degree(); i++) {
            for(int j = 0; j < p.terms.length; j++) {
                matrix[i + p.degree()][i + j] = p.terms[p.degree() - j];
            }
        }
        return new Matrix(matrix);
    }

    /**
     * Finds the resultant of two Polynomials
     * @param p the second Polynomial
     * @return the determinant of the Sylvester Matrix
     */
    public Fraction resultant(Polynomial p) {
        return sylvesterMatrix(p).determinant();
    }

    /**
     * Finds the discriminant of this Polynomial
     * @return the resultant of this Polynomial and its derivative
     */
    public Fraction discriminant() {
        final int sign = (((degree() * (degree() - 1)) >> 1) & 1) == 0 ? 1 : -1;
        return resultant(derivative()).divide(this.terms[degree()]).multiply(new Fraction(sign));
    }

    /**
     * Provides the TrueText of this Polynomial
     * @return this Polynomial in a parsable format
     */
    @Override
    public String trueText() {
        final StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(Fraction term : this.terms) {
            builder.append(delimiter).append(term.toString());
            delimiter = ",";
        }
        return builder.toString();
    }

    /**
     * Converts this Polynomial to a printable format
     * @return this Polynomial as a String
     */
    @Override
    public String toString() {
        if(equals(new Polynomial(0))) {
            return "0";
        }
        final String[] terms = new String[this.terms.length];
        for(int i = 0; i < terms.length; i++) {
            String sign = "", c = "", var = "";
            final Fraction term = this.terms[i];
            if(term.sign() < 0) {
                sign = "-";
            } else if(! term.equals(Fraction.ZERO)) {
                sign = "+";
            }
            if(! term.equals(Fraction.ZERO)) {
                if(i > 1) {
                    var = "x^" + i;
                } else if(i > 0) {
                    var = "x";
                }
                if((! term.abs().equals(Fraction.ONE)) || i == 0) {
                    c = term.abs().toString();
                }
            }
            terms[i] = sign.concat(c).concat(var);
        }
        String print = "";
        for(String term : terms) {
            print = print.concat(term);
        }
        return print.substring(print.startsWith("+") ? 1 : 0); //TODO: fix with StringBuilder
    }

    /**
     * Prints this Polynomial
     */
    public void print() {
        System.out.println("f(x) = " + this);
    }

    // static methods

    /**
     * Creates a Polynomial with leading coefficient 1 and given degree
     * @param degree the degree of the Polynomial
     * @return the new Polynomial
     */
    public static Polynomial polynomial(int degree) {
        final BigInteger[] coefficients = new BigInteger[degree + 1];
        java.util.Arrays.fill(coefficients, BigInteger.ZERO);
        coefficients[degree] = BigInteger.ONE;
        return new Polynomial(coefficients);
    }

    /**
     * Constructs a Polynomial given a list of Points
     * @param p the list of Points
     * @return the Polynomial of least degree intersecting all Points
     */
    public static Polynomial lagrangePolynomial(Point2D... p) {
        Polynomial polynomial = new Polynomial(0), completeProduct = new Polynomial(1);
        final Polynomial[] roots = new Polynomial[p.length];
        for(int i = 0; i < roots.length; i++) {
            roots[i] = p[i].x().minimalPolynomial();
            completeProduct = completeProduct.multiply(roots[i]);
        }
        for(Point2D point : p) {
            final Polynomial partialProduct = completeProduct.divide(point.x().minimalPolynomial());
            polynomial = polynomial.add(partialProduct.multiply(point.y().divide(partialProduct.evaluate(point.x()))));
        }
        return polynomial;
    }

    /**
     * Finds the cyclotomic Polynomial with respect to degree n
     * @param degree the degree of the roots
     * @return the Polynomial whose roots are nth-degree roots of unity
     */
    public static Polynomial cyclotomicPolynomial(int degree) {
        final Map<Integer, Polynomial> functionMap = new HashMap<>();
        for(Integer factor : Factor.factors(degree)) {
            Polynomial minimalPolynomial = new Polynomial(0, 1).pow(factor).subtract(new Polynomial(1));
            for(Integer proxyFactor : Factor.factors(factor)) {
                if(proxyFactor < factor) {
                    minimalPolynomial = minimalPolynomial.divide(functionMap.get(proxyFactor));
                }
            }
            functionMap.put(factor, minimalPolynomial);
        }
        return functionMap.get(degree);
    }

    /**
     * Creates a Polynomial regression function for a data set
     * @param degree the degree of the function
     * @param p the set of Points representing the data
     * @return the regression Polynomial
     */
    public static Polynomial regression(int degree, Point2D... p) {
        final Fraction[][] coefficients = new Fraction[p.length][], constant = new Fraction[p.length][];
        for(int i = 0; i < p.length; i++) {
            Fraction value = Fraction.ONE;
            coefficients[i] = new Fraction[degree + 1];
            for(int j = 0; j < coefficients[i].length; j++) {
                coefficients[i][j] = value;
                value = value.multiply(p[i].x());
            }
            constant[i] = new Fraction[]{p[i].y()};
        }
        final Matrix coefficientMatrix = new Matrix(coefficients);
        final Matrix coefficientMatrixTranspose = coefficientMatrix.transpose();
        final Matrix polynomialSolution = coefficientMatrixTranspose.multiply(coefficientMatrix).reducedRowEchelon(coefficientMatrixTranspose)[1].multiply(new Matrix(constant));
        final Polynomial polynomial = new Polynomial();
        polynomial.terms = new Fraction[degree + 1];
        for(int i = 0; i < polynomial.terms.length; i++) {
            polynomial.terms[i] = polynomialSolution.getElement(i, 0);
        }
        polynomial.simplify();
        return polynomial;
    }

    /**
     * Finds the coefficients of the root power sum of a polynomial of specific degree
     * @param n the degree of the input polynomial
     * @return a multivariate polynomial in 'n' variables representing the highest (n + 1) coefficients of a general
     *          polynomial such that it evaluates to the sum of the nth powers of each roots
     */
    public static Matrix powerSumCoefficients(int n) {
        final List<List<Integer>> partitions = Combinatorics.listOfPartitionLists(n);
        final Fraction[][] matrix = new Fraction[partitions.size()][];
        final ListIterator<List<Integer>> partitionsIterator = partitions.listIterator();
        for(int i = 0; i < matrix.length; i++) {
            matrix[i] = new Fraction[matrix.length];
        }
        for(int i = 0; i < matrix.length; i++) {
            final List<Integer> nextPartition = partitionsIterator.next();
            final ListIterator<List<Integer>> secondaryPartitionsIterator = partitions.listIterator(i);
            for(int j = i; j < matrix[i].length; j++) {
                matrix[i][j] = new Fraction(Combinatorics.numberOfCounterArrangements(nextPartition, secondaryPartitionsIterator.next()));
                matrix[j][i] = matrix[i][j];
            }
        }
        final Fraction[][] constant = new Fraction[partitions.size()][];
        for(int i = 0; i < constant.length; i++) {
            constant[i] = new Fraction[]{i == constant.length - 1 ? Fraction.ONE : Fraction.ZERO};
        }
        Matrix solution = new Matrix(matrix).reducedRowEchelon(new Matrix(constant))[1].transpose();
        if(n % 2 == 1) {
            solution = solution.multiply(new Fraction(-1));
        }

        Matrix a = new Matrix(matrix), b = a.inverse();

        a.print();
        System.out.println();
        b.print();

        return solution;
    }

    /**
     * Constructs a Polynomial with given leading coefficient and rational roots
     * @param lead the leading coefficient
     * @param roots the roots
     * @return the computed Polynomial
     */
    public static Polynomial valueOf(Fraction lead, Fraction... roots) {
        Polynomial polynomial = new Polynomial(lead);
        for(Fraction root : roots) {
            polynomial = polynomial.multiply(root.minimalPolynomial());
        }
        return polynomial;
    }

    /**
     * Converts an array of Polynomials to a printable format
     * @param ar the array of Polynomials
     * @return the array in String format
     */
    public static String arrayToString(Polynomial[] ar) {
        String print = "";
        for(Polynomial item : ar) {
            print = print.concat(", ").concat(item.toString());
        }
        return "[".concat(print.substring(print.length() > 0 ? 2 : 0)).concat("]");
    }

    /**
     * Prints an array of Polynomials
     * @param ar the array of Polynomials
     */
    public static void printArray(Polynomial[] ar) {
        System.out.println(arrayToString(ar));
    }
}