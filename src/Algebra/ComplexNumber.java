package Algebra;

import Exception.*;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class ComplexNumber {
    public static final ComplexNumber ONE = new ComplexNumber(Fraction.ONE, Fraction.ZERO);
    public static final ComplexNumber ZERO = new ComplexNumber(Fraction.ZERO, Fraction.ZERO);
    private final Fraction a, b;

    /**
     * Creates a new Complex Number with the given components
     * @param a the real component
     * @param b the imaginary component
     */
    public ComplexNumber(Number a, Number b) {
        this.a = new Fraction(a);
        this.b = new Fraction(b);
    }

    /**
     * Creates a new Complex Number with the given components
     * @param a the real component
     * @param b the imaginary component
     */
    public ComplexNumber(BigInteger a, BigInteger b) {
        this.a = new Fraction(a);
        this.b = new Fraction(b);
    }

    /**
     * Creates a new Complex Number with the given components
     * @param a the real component
     * @param b the imaginary component
     */
    public ComplexNumber(Fraction a, Fraction b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Creates a new Complex Number with given components
     * @param s the components in String format
     * @throws IllegalArgumentException if more than 2 numbers are encoded in the String
     */
    public ComplexNumber(String s) throws IllegalArgumentException {
        final BigInteger[][] bigInts = General.Converter.convertTo2DBigIntegerArray(s, "/", ",");
        if(bigInts.length > 2) {
            throw new IllegalArgumentException(ExceptionMessage.TOO_MANY_ARGUMENTS(2));
        }
        this.a = new Fraction(bigInts[0]);
        this.b = new Fraction(bigInts[1]);
    }

    /**
     * Adds two Complex Numbers
     * @param addend the addend Complex Number
     * @return the sum of this Complex Number and the addend Complex Number
     */
    public ComplexNumber add(ComplexNumber addend) {
        return new ComplexNumber(this.a.add(addend.a), this.b.add(addend.b));
    }

    /**
     * Subtracts two Complex Numbers
     * @param subtrahend the subtrahend Complex Number
     * @return the difference between this Complex Number and the subtrahend Complex Number
     */
    public ComplexNumber subtract(ComplexNumber subtrahend) {
        return new ComplexNumber(this.a.subtract(subtrahend.a), this.b.subtract(subtrahend.b));
    }

    /**
     * Multiplies this Complex Number by a constant
     * @param scale the scale factor
     * @return the scaled Complex Number
     */
    public ComplexNumber multiply(Fraction scale) {
        return new ComplexNumber(this.a.multiply(scale), this.b.multiply(scale));
    }

    /**
     * Multiplies two Complex Numbers
     * @param multiplicand the multiplicand Complex Number
     * @return the product of this Complex Number and the multiplicand Complex Number
     */
    public ComplexNumber multiply(ComplexNumber multiplicand) {
        final Fraction real = this.a.multiply(multiplicand.a).subtract(this.b.multiply(multiplicand.b));
        final Fraction complex = this.a.multiply(multiplicand.b).add(this.b.multiply(multiplicand.a));
        return new ComplexNumber(real, complex);
    }

    /**
     * Divides two Complex Numbers
     * @param divisor the divisor Complex Number
     * @return the quotient of this Complex Number and the divisor Complex Number
     */
    public ComplexNumber divide(ComplexNumber divisor) {
        return multiply(divisor.conjugate()).multiply(divisor.magnitudeSquared().inverse());
    }

    /**
     * Raises this Complex Number to an integer power
     * @param pow the power of the exponent
     * @return this ^ pow
     */
    public ComplexNumber pow(int pow) {
        ComplexNumber antilogarithm = ONE;
        int powSign = pow;
        final List<Integer> powers = new LinkedList<>();
        while(pow > 0) {
            powers.add(0, pow % 2);
            pow /= 2;
        }
        for(int instruction : powers) {
            antilogarithm = antilogarithm.multiply(antilogarithm);
            if(instruction != 0) {
                antilogarithm = multiply(antilogarithm);
            }
        }
        return powSign < 0 ? ONE.divide(antilogarithm) : antilogarithm;
    }

    /**
     * Converts this Complex Number to a generalized Integer
     * @return the product of this Complex Number and a constant that clears the denominator
     */
    public GaussianInteger asMemberOfIntegralDomain() {
        final Fraction[] integers = Fraction.raiseToIntegers(this.a, this.b);
        return new GaussianInteger(integers[0].asBigInteger(), integers[1].asBigInteger());
    }

    /**
     * Gets the real component of this Complex Number
     * @return this.a
     */
    public Fraction re() {
        return this.a;
    }

    /**
     * Gets the imaginary component of this Complex Number
     * @return this.b
     */
    public Fraction im() {
        return this.b;
    }

    /**
     * Finds the complex conjugate of this Complex Number
     * @return the Complex Number equal to a-bi
     */
    public ComplexNumber conjugate() {
        return new ComplexNumber(this.a, this.b.negate());
    }

    /**
     * Finds the square of the magnitude
     * @return the squared complex-plane distance between this Complex Number and zero
     */
    public Fraction magnitudeSquared() {
        return this.a.pow(2).add(this.b.pow(2));
    }

    /**
     * Finds the minimal Polynomial of this Complex Number
     * @return the smallest degree Polynomial containing this Complex Number as a root
     */
    public Polynomial minimalPolynomial() {
        return new Polynomial(Fraction.raiseToIntegers(magnitudeSquared(), this.a.multiply(new Fraction(-2)), Fraction.ONE));
    }

    /**
     * Converts this Complex Number to a Matrix
     * @return this Complex Number in Matrix form
     */
    public Matrix asMatrix() {
        return new Matrix(new Fraction[][]{{this.a, this.b.negate()}, {this.b, this.a}});
    }

    /**
     * Compares two Complex Numbers for equality
     * @param comparator the comparator Complex Number
     * @return true if this Complex Number is equal to the comparator, else false
     */
    public boolean equals(ComplexNumber comparator) {
        return this.a.equals(comparator.a) && this.b.equals(comparator.b);
    }

    /**
     * Converts this Complex Number to a printable format
     * @return this Complex Number as a String
     */
    @Override
    public String toString() {
        if(equals(ZERO)) {
            return "0";
        }
        String a = "", b = "";
        boolean b_sign = false;
        if(! this.a.equals(Fraction.ZERO)) {
            a = this.a.toString();
            b_sign = true;
        }
        if(! this.b.equals(Fraction.ZERO)) {
            b = this.b.toString();
            if(b.charAt(0) != '-' && b_sign) {
                b = "+".concat(b);
            }
        }
        return a.concat(b).concat(b.length() > 0 ? "i" : "");
    }

    /**
     * Prints this Complex Number
     */
    public void print() {
        System.out.println(this);
    }
}