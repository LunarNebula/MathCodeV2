package Algebra;

import Theory.Arithmetic;
import Theory.Factor;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class GaussianInteger {
    public static final GaussianInteger I = new GaussianInteger(BigInteger.ZERO, BigInteger.ONE);
    public static final GaussianInteger ONE = new GaussianInteger(BigInteger.ONE, BigInteger.ZERO);
    public static final GaussianInteger ZERO = new GaussianInteger(BigInteger.ZERO, BigInteger.ZERO);
    private final BigInteger a, b;

    /**
     * Creates a new Gaussian Integer with the given components
     * @param a the real component
     * @param b the imaginary component
     */
    public GaussianInteger(BigInteger a, BigInteger b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Adds two Gaussian Integers
     * @param addend the addend Gaussian Integer
     * @return the sum of this Gaussian Integer and the comparator Gaussian Integer
     */
    public GaussianInteger add(GaussianInteger addend) {
        return new GaussianInteger(this.a.add(addend.a), this.b.add(addend.b));
    }

    /**
     * Subtracts two Gaussian Integers
     * @param subtrahend the subtrahend Gaussian Integer
     * @return the sum of this Gaussian Integer and the comparator Gaussian Integer
     */
    public GaussianInteger subtract(GaussianInteger subtrahend) {
        return new GaussianInteger(this.a.subtract(subtrahend.a), this.b.subtract(subtrahend.b));
    }

    /**
     * Multiplies two Gaussian Integers
     * @param multiplicand the multiplicand Gaussian Integer
     * @return the product of this Gaussian Integer and the comparator Gaussian Integer
     */
    public GaussianInteger multiply(GaussianInteger multiplicand) {
        final BigInteger real = this.a.multiply(multiplicand.a).subtract(this.b.multiply(multiplicand.b));
        final BigInteger complex = this.a.multiply(multiplicand.b).add(this.b.multiply(multiplicand.a));
        return new GaussianInteger(real, complex);
    }

    /**
     * Divides this Gaussian Integer by another Gaussian Integer
     * @param divisor the divisor Gaussian Integer
     * @return the approximate quotient of this Gaussian Integer and the divisor Gaussian Integer
     */
    public GaussianInteger divide(GaussianInteger divisor) {
        final BigInteger denominator = divisor.magnitudeSquared();
        final GaussianInteger conjugate = multiply(divisor.conjugate());
        return new GaussianInteger(conjugate.a.divide(denominator), conjugate.b.divide(denominator));
    }

    /**
     * Finds the conjugate of this
     * @return a-bi, if this Gaussian Integer is a+bi
     */
    public GaussianInteger conjugate() {
        return new GaussianInteger(this.a, this.b.negate());
    }

    /**
     * Negates this Gaussian Integer
     * @return -a-bi, if this Gaussian Integer is a+bi
     */
    public GaussianInteger negate() {
        return new GaussianInteger(this.a.negate(), this.b.negate());
    }

    /**
     * Finds the magnitude of this Gaussian Integer
     * @return the Pythagorean distance from this Gaussian Integer to 0, squared
     */
    public BigInteger magnitudeSquared() {
        return this.a.multiply(this.a).add(this.b.multiply(this.b));
    }

    /**
     * Finds all prime factors of this Gaussian Integer
     * @return all Gaussian Integers g such that g | this and g is prime
     */
    public List<GaussianInteger> primeFactors() {
        final BigInteger FOUR = BigInteger.valueOf(4), THREE = BigInteger.valueOf(3);
        final List<GaussianInteger> primeFactors = new LinkedList<>();
        BigInteger magnitude = magnitudeSquared(), gcd = this.a.gcd(this.b);
        if(! gcd.equals(BigInteger.ONE)) {
            BigInteger product = BigInteger.ONE;
            for(BigInteger candidate : Factor.primeFactors(gcd)) {
                if(candidate.mod(FOUR).equals(THREE)) {
                    product = product.multiply(candidate);
                    primeFactors.add(new GaussianInteger(candidate, BigInteger.ZERO));
                }
            }
            magnitude = magnitude.divide(product.multiply(product));
        }
        GaussianInteger test = this;
        for(BigInteger candidate : Factor.primeFactors(magnitude)) {
            BigInteger counter = BigInteger.ONE;
            boolean continueSearch = true;
            while (continueSearch) {
                BigInteger testSquare = candidate.subtract(counter.pow(2));
                BigInteger testComponent = Arithmetic.sqrt(testSquare);
                if(testComponent.multiply(testComponent).equals(testSquare)) {
                    BigInteger real = test.a.multiply(counter).add(test.b.multiply(testComponent));
                    BigInteger imaginary = test.b.multiply(counter).subtract(test.a.multiply(testComponent));
                    GaussianInteger completeFactor;
                    if(real.mod(candidate).equals(BigInteger.ZERO) && imaginary.mod(candidate).equals(BigInteger.ZERO)) {
                        completeFactor = new GaussianInteger(counter, testComponent);
                    } else {
                        completeFactor = new GaussianInteger(testComponent, counter);
                    }
                    primeFactors.add(completeFactor);
                    test = test.divide(completeFactor);
                    continueSearch = false;
                }
                counter = counter.add(BigInteger.ONE);
            }
        }
        return primeFactors;
    }

    /**
     * Finds a complete list of factors
     * @return all unique subset products of the prime factors of this Gaussian Integer
     */
    public List<GaussianInteger> factors() {
        GaussianInteger prev = ONE, product = ONE;
        List<GaussianInteger> factors = new LinkedList<>(), test = new LinkedList<>();
        factors.add(ONE);
        for(GaussianInteger primeFactor : primeFactors()) {
            if(primeFactor.equals(prev)) {
                product = product.multiply(primeFactor);
            } else {
                factors.addAll(test);
                prev = primeFactor;
                test.clear();
                product = primeFactor;
            }
            for(GaussianInteger factor : factors) {
                test.add(factor.multiply(product));
            }
        }
        factors.addAll(test);
        test.clear();
        for(GaussianInteger factor : factors) {
            test.add(factor.negate());
        }
        factors.addAll(test);
        test.clear();
        for(GaussianInteger factor : factors) {
            test.add(factor.multiply(I));
        }
        factors.addAll(test);
        return factors;
    }

    /**
     * Determines whether this Gaussian Integer is equal to another Gaussian Integer
     * @param comparator the comparator Gaussian Integer
     * @return true if both real and imaginary components are equal
     */
    public boolean equals(GaussianInteger comparator) {
        return this.a.equals(comparator.a) && this.b.equals(comparator.b);
    }

    /**
     * Converts this Gaussian Integer to a printable format
     * @return this Gaussian Integer as a String
     */
    @Override
    public String toString() {
        if(equals(ZERO)) {
            return "0";
        }
        String a = "", b = "";
        boolean b_sign = false;
        if(! this.a.equals(BigInteger.ZERO)) {
            a = this.a.toString();
            b_sign = true;
        }
        if(! this.b.equals(BigInteger.ZERO)) {
            b = this.b.toString();
            if(b.charAt(0) != '-' && b_sign) {
                b = "+".concat(b);
            }
        }
        return a.concat(b).concat(b.length() > 0 ? "i" : "");
    }

    /**
     * Prints this Gaussian Integer
     */
    public void print() {
        System.out.println(toString());
    }
}