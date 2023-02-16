package Algebra;

import General.TrueTextEncodable;
import Geometry.Circle;
import Geometry.Point2D;

import java.util.LinkedList;
import java.util.List;

public class RationalFunction implements TrueTextEncodable {
    public static final RationalFunction ZERO = new RationalFunction(new Polynomial(0));
    public static final RationalFunction ONE = new RationalFunction(new Polynomial(1));
    private Polynomial dividend;
    private Polynomial divisor;

    /**
     * Creates a new Rational Function with denominator 1
     * @param n the numerator Polynomial
     */
    public RationalFunction(Polynomial n) {
        this.dividend = n;
        this.divisor = new Polynomial(1);
        simplify();
    }

    /**
     * Creates a new Rational Function
     * @param n the numerator Polynomial
     * @param d the denominator Polynomial
     */
    public RationalFunction(Polynomial n, Polynomial d) {
        this.dividend = n;
        this.divisor = d;
        simplify();
    }

    /**
     * Creates a new Rational Function
     * @param n the String encoding the numerator Polynomial of this Rational Function
     * @param d the String encoding the denominator Polynomial of this Rational Function
     */
    public RationalFunction(String n, String d) {
        this.dividend = new Polynomial(n);
        this.divisor = new Polynomial(d);
        simplify();
    }

    /**
     * Finds the numerator of this RationalFunction
     * @return the Polynomial 'p' of least degree such that this RationalFunction is equal to p/q for some nonzero Polynomial q
     */
    public Polynomial numerator() {
        return this.dividend;
    }

    /**
     * Finds the denominator of this RationalFunction
     * @return the nonzero Polynomial 'q' of least degree such that this RationalFunction is equal to p/q for some Polynomial p
     */
    public Polynomial denominator() {
        return this.divisor;
    }

    /**
     * Adds two Rational Functions
     * @param addend the addend Rational Function
     * @return the sum of this Rational Function and the addend Rational Function
     */
    public RationalFunction add(RationalFunction addend) {
        Polynomial n = this.dividend.multiply(addend.divisor).add(addend.dividend.multiply(this.divisor));
        Polynomial d = this.divisor.multiply(addend.divisor);
        return new RationalFunction(n, d);
    }

    /**
     * Subtracts two Rational Functions
     * @param subtrahend the subtrahend Rational Function
     * @return the difference between this Rational Function and the subtrahend Rational Function
     */
    public RationalFunction subtract(RationalFunction subtrahend) {
        Polynomial n = this.dividend.multiply(subtrahend.divisor).subtract(subtrahend.dividend.multiply(this.divisor));
        Polynomial d = this.divisor.multiply(subtrahend.divisor);
        return new RationalFunction(n, d);
    }

    /**
     * Scales this Rational Function by a scalar value
     * @param scale the scalar value
     * @return the scaled Rational Function
     */
    public RationalFunction multiply(Fraction scale) {
        return new RationalFunction(this.dividend.multiply(scale), this.divisor);
    }

    /**
     * Multiplies two Rational Functions
     * @param multiplicand the multiplicand Rational Function
     * @return the product of this Rational Function and the multiplicand
     */
    public RationalFunction multiply(RationalFunction multiplicand) {
        Polynomial n = this.dividend.multiply(multiplicand.dividend);
        Polynomial d = this.divisor.multiply(multiplicand.divisor);
        return new RationalFunction(n, d);
    }
    
    /**
     * Divides two Rational Functions
     * @param divisor the divisor Rational Function
     * @return the quotient of this Rational Function and the divisor
     */
    public RationalFunction divide(RationalFunction divisor) {
        Polynomial n = this.dividend.multiply(divisor.divisor);
        Polynomial d = this.divisor.multiply(divisor.dividend);
        return new RationalFunction(n, d);
    }

    /**
     * Finds the quotient and remainder of the component Polynomials of this Rational Function
     * @return the quotient and remainder when the numerator is divided by the denominator
     */
    public Polynomial[] divideAndRemainder() {
        return this.dividend.divideAndRemainder(this.divisor);
    }

    /**
     * Raises this Rational Function to a power
     * @param pow the Integer power
     * @return this ^ pow
     */
    public RationalFunction pow(int pow) {
        RationalFunction antilogarithm = RationalFunction.ONE;
        int powSign = pow;
        final List<Boolean> powers = new LinkedList<>();
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
        return powSign < 0 ? ONE.divide(antilogarithm) : antilogarithm;
    }

    /**
     * Finds the multiplicative inverse of this Rational Function
     * @return the Rational Function R such that this * R = 1
     */
    public RationalFunction inverse() {
        return new RationalFunction(this.divisor, this.dividend);
    }

    /**
     * Finds the derivative of this Rational Function
     * @return df/dx
     */
    public RationalFunction derivative() {
        final Polynomial dividendPositive = this.dividend.derivative().multiply(this.divisor);
        final Polynomial dividendNegative = this.dividend.multiply(this.divisor.derivative());
        final Polynomial divisor = this.divisor.multiply(this.divisor);
        final RationalFunction derivative = new RationalFunction(dividendPositive.subtract(dividendNegative), divisor);
        derivative.simplify();
        return derivative;
    }

    /**
     * Evaluates this Rational Function at a specific x-value
     * @param x the Fraction input
     * @return f(x)
     */
    public Fraction evaluate(Fraction x) {
        return this.dividend.evaluate(x).divide(this.divisor.evaluate(x));
    }

    /**
     * Composes two Rational Functions
     * @param r the composing Rational Function
     * @return f(r)
     */
    public RationalFunction compose(RationalFunction r) {
        RationalFunction numerator = ZERO, denominator = ZERO;
        final Fraction[] numeratorTerms = this.dividend.getTerms(), denominatorTerms = this.divisor.getTerms();
        for(int i = numeratorTerms.length - 1; i >= 0; i--) {
            numerator = numerator.multiply(r).add(new RationalFunction(new Polynomial(numeratorTerms[i])));
        }
        for(int i = denominatorTerms.length - 1; i >= 0; i--) {
            denominator = denominator.multiply(r).add(new RationalFunction(new Polynomial(denominatorTerms[i])));
        }
        return numerator.divide(denominator);
    }

    /**
     * Finds the partial fraction decomposition of this Rational Function
     * @return the sum of simplified Rational Functions that compose this Rational Function
     */
    public List<RationalFunction> partialFractions() {
        Polynomial dividend = this.dividend;
        final List<RationalFunction> partialFractions = new LinkedList<>();
        final Polynomial[] completeIntegerPart = dividend.divideAndRemainder(this.divisor);
        if(! completeIntegerPart[0].equals(new Polynomial(0))) {
            partialFractions.add(new RationalFunction(completeIntegerPart[0]));
            dividend = completeIntegerPart[1];
        }
        final List<Polynomial> factors = this.divisor.factors();
        if(factors.get(0).degree() == 0) {
            factors.remove(0);
        }
        final Fraction[][] coefficients = new Fraction[this.divisor.degree()][], constants = new Fraction[this.divisor.degree()][];
        final Fraction[] numTerms = dividend.getTerms();
        int index = 0;
        Polynomial prevFactor = new Polynomial(0), proxy = this.divisor;
        for(Polynomial factor : factors) {
            proxy = prevFactor.equals(factor) ? proxy.divide(factor) : this.divisor.divide(factor);
            final Fraction[] quotient = proxy.getTerms();
            for(int i = 0; i < factor.degree(); i++) {
                coefficients[index] = new Fraction[this.divisor.degree()];
                java.util.Arrays.fill(coefficients[index], Fraction.ZERO);
                System.arraycopy(quotient, 0, coefficients[index], i, quotient.length);
                constants[index] = new Fraction[]{index < numTerms.length ? numTerms[index] : Fraction.ZERO};
                index++;
            }
            prevFactor = factor;
        }
        final Matrix solutions = new Matrix(coefficients).transpose().reducedRowEchelon(new Matrix(constants))[1];
        int termMapIndex = 0;
        Polynomial prevRecordedFactor = new Polynomial(0), prevRecordedProduct = new Polynomial(1);
        for(Polynomial factor : factors) {
            prevRecordedProduct = prevRecordedFactor.equals(factor) ? prevRecordedProduct.multiply(factor) : factor;
            final Fraction[] numerator = new Fraction[factor.degree()];
            for(int i = 0; i < numerator.length; i++) {
                numerator[i] = solutions.getElement(termMapIndex++, 0);
            }
            Polynomial polynomialNumerator = new Polynomial(numerator);
            if(! polynomialNumerator.equals(new Polynomial(0))) {
                partialFractions.add(new RationalFunction(polynomialNumerator, prevRecordedProduct));
            }
            prevRecordedFactor = factor;
        }
        return partialFractions;
    }

    /**
     * Simplifies this Rational Function
     */
    public void simplify() {
        Polynomial gcd = this.dividend.gcd(this.divisor);
        this.dividend = this.dividend.divide(gcd);
        this.divisor = this.divisor.divide(gcd);
    }

    /**
     * Finds the osculating circle at a certain x-value on this Rational Function
     * @param x the x-value where the curvature is measured
     * @return the Circle most closely describing the curvature of this Rational Function at the given point
     */
    public Circle osculatingCircle(Fraction x) {
        final RationalFunction derivative = derivative();
        final Fraction y = evaluate(x), dX = derivative.evaluate(x), dXdX = derivative.derivative().evaluate(x);
        final Fraction factor = dX.pow(2).add(Fraction.ONE).divide(dXdX);
        final Fraction centerX = x.subtract(dX.multiply(factor)), centerY = y.add(factor); // circle center coordinates
        final Point2D center = new Point2D(centerX, centerY);
        return new Circle(center, center.distanceSquared(new Point2D(x, y)));
    }

    /**
     * Provides the TrueText of this RationalFunction
     * @return this RationalFunction in a parsable format
     */
    @Override
    public String trueText() {
        return this.dividend.trueText().concat(";").concat(this.divisor.trueText());
    }

    /**
     * Converts this Rational Function to a printable format
     * @return this Rational Function as a String
     */
    @Override
    public String toString() {
        return "(".concat(this.dividend.toString()).concat(")/(").concat(this.divisor.toString()).concat(")");
    }

    /**
     * Prints this Rational Function
     */
    public void print() {
        System.out.println(toString());
    }

    // static methods

    /**
     * Converts an array of Rational Functions to a printable format
     * @param ar the array of Rational Functions
     * @return the array in String format
     */
    public static String arrayToString(RationalFunction[] ar) {
        String print = "";
        for(RationalFunction item : ar) {
            print = print.concat(", ").concat(item.toString());
        }
        return "[".concat(print.substring(print.length() > 0 ? 2 : 0)).concat("]");
    }

    /**
     * Prints an array of Rational Functions
     * @param ar the array of Rational Functions
     */
    public static void printArray(RationalFunction[] ar) {
        System.out.println(arrayToString(ar));
    }
}