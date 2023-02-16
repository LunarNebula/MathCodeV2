package Algebra;

import Exception.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

public class Fraction implements Comparable<Fraction> {
    public static final Fraction ZERO = new Fraction(0), ONE = new Fraction(1);
    private BigInteger dividend, divisor;

    /**
     * Creates a new Fraction with the given String representation
     * @param s the String representation of this Fraction
     * @throws IllegalArgumentException if more than 2 numbers are submitted
     */
    public Fraction(String s) throws IllegalArgumentException {
        final BigInteger[] fraction = General.Converter.convertToBigIntegerArray(s, "/");
        if(fraction.length > 2) {
            throw new IllegalArgumentException(ExceptionMessage.TOO_MANY_ARGUMENTS(2));
        }
        this.dividend = fraction[0];
        this.divisor = fraction.length > 1 ? fraction[1] : BigInteger.ONE;
        simplify();
    }

    /**
     * Creates a new Fraction with the given Number numerator and denominator
     * @param fraction the array of two numbers representing the numerator and denominator
     * @throws IllegalArgumentException if more than 2 numbers are submitted
     */
    public Fraction(Number @NotNull ... fraction) throws IllegalArgumentException {
        this.dividend = BigInteger.valueOf(fraction[0].longValue());
        this.divisor = fraction.length > 1 ? BigInteger.valueOf(fraction[1].longValue()) : BigInteger.ONE;
        if(fraction.length > 2) {
            throw new IllegalArgumentException(ExceptionMessage.TOO_MANY_ARGUMENTS(2));
        }
        simplify();
    }

    /**
     * Creates a new Fraction with the given BigInteger numerator and denominator
     * @param fraction the array of two numbers representing the numerator and denominator
     * @throws IllegalArgumentException if more than 2 numbers are submitted
     */
    public Fraction(BigInteger @NotNull ... fraction) throws IllegalArgumentException {
        if(fraction.length > 2) {
            throw new IllegalArgumentException(ExceptionMessage.TOO_MANY_ARGUMENTS(2));
        }
        this.dividend = fraction[0];
        this.divisor = fraction.length > 1 ? fraction[1] : BigInteger.ONE;
        simplify();
    }

    /**
     * Finds the numerator of this Fraction
     * @return this.dividend
     */
    public BigInteger numerator() {
        return this.dividend;
    }

    /**
     * Finds the denominator of this Fraction
     * @return this.divisor
     */
    public BigInteger denominator() {
        return this.divisor;
    }

    /**
     * Adds two Fractions
     * @param addend the addend of the addition operation
     * @return the sum of the addend and this Fraction
     */
    public Fraction add(@NotNull Fraction addend) {
        final Fraction sum = new Fraction(this.dividend.multiply(addend.divisor).add(this.divisor.multiply(addend.dividend)),
                this.divisor.multiply(addend.divisor));
        sum.simplify();
        return sum;
    }

    /**
     * Subtracts another Fraction from this Fraction
     * @param subtrahend the subtrahend of the subtraction operation
     * @return the difference of this Fraction and the subtrahend
     */
    public Fraction subtract(@NotNull Fraction subtrahend) {
        return this.add(subtrahend.negate());
    }

    /**
     * Multiplies two Fractions
     * @param multiplicand the multiplicand of the multiplication operation
     * @return the product of the two Fractions
     */
    public Fraction multiply(@NotNull Fraction multiplicand) {
        return new Fraction(this.dividend.multiply(multiplicand.dividend), this.divisor.multiply(multiplicand.divisor));
    }

    /**
     * Divides this Fraction by another Fraction
     * @param divisor the divisor of the division function
     * @return the quotient of this Fraction and the divisor Fraction
     */
    public Fraction divide(@NotNull Fraction divisor) {
        return new Fraction(this.dividend.multiply(divisor.divisor), this.divisor.multiply(divisor.dividend));
    }

    /**
     * Negates this Fraction
     * @return this * -1
     */
    public Fraction negate() {
        return new Fraction(this.dividend.negate(), this.divisor);
    }

    /**
     * Finds the absolute value of this Fraction
     * @return a Fraction with magnitude equal to this Fraction and non-negative sign
     */
    public Fraction abs() {
        return new Fraction(this.dividend.abs(), this.divisor.abs());
    }

    /**
     * Finds the multiplicative inverse of this Fraction
     * @return this ^ -1
     */
    public Fraction inverse() {
        return new Fraction(this.divisor, this.dividend);
    }

    /**
     * Raises this Fraction to an Integer power
     * @param pow the exponent of the function
     * @return this ^ pow
     */
    public Fraction pow(int pow) {
        Fraction antilogarithm = ONE;
        int powSign = pow;
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
        return powSign < 0 ? ONE.divide(antilogarithm) : antilogarithm;
    }

    /**
     * Simplifies this fraction
     */
    public void simplify() {
        if(this.divisor.compareTo(BigInteger.ZERO) < 0) {
            this.dividend = this.dividend.negate();
            this.divisor = this.divisor.negate();
        }
        BigInteger gcd = this.dividend.gcd(this.divisor);
        if(gcd.equals(BigInteger.ZERO)) {
            gcd = BigInteger.ONE;
        }
        this.dividend = this.dividend.divide(gcd);
        this.divisor = this.divisor.divide(gcd);
    }

    /**
     * Compares the values of two Fractions
     * @param comparator the comparison Fraction
     * @return 1 if this is greater, else -1 if the comparator is greater, else 0
     */
    @Override
    public int compareTo(@NotNull Fraction comparator) {
        return this.dividend.multiply(comparator.divisor).compareTo(comparator.dividend.multiply(this.divisor));
    }

    /**
     * Checks for equality between two Fractions
     * @param comparator the comparator Fraction
     * @return true if this Fraction is equal to the comparator, else false
     */
    @Override
    public boolean equals(Object comparator) {
        return comparator instanceof Fraction && compareTo((Fraction) comparator) == 0;
    }

    /**
     * Gets the hashcode of this Fraction
     * @return a function f : (N x Z) -> N
     */
    @Override
    public int hashCode() {
        BigInteger magnitude = this.dividend.abs().add(this.divisor);
        return magnitude.multiply(magnitude.add(BigInteger.ONE)).subtract(this.dividend).intValue();
    }

    /**
     * Copies this Fraction
     * @return a deep copy of this Fraction
     */
    public Fraction copy() {
        return new Fraction(this.dividend, this.divisor);
    }

    /**
     * Finds the sign of this Fraction
     * @return 1 if this Fraction is positive, -1 if negative, else 0
     */
    public int sign() {
        return compareTo(Fraction.ZERO);
    }

    /**
     * Finds the BigInteger representation of this Fraction
     * @return the quotient of this dividend and divisor, rounded down
     */
    public BigInteger asBigInteger() {
        return this.dividend.divide(this.divisor);
    }

    /**
     * Converts this Fraction to a BigDecimal value
     * @param scale the scale to which the BigDecimal should be rounded
     * @return the rounded BigDecimal
     */
    public BigDecimal asBigDecimal(int scale) {
        return new BigDecimal(this.dividend).divide(new BigDecimal(this.divisor), scale, RoundingMode.HALF_UP);
    }

    /**
     * Finds the floor of this Fraction
     * @return the largest BigInteger no larger than this Fraction
     */
    public BigInteger floor() {
        BigInteger floor = this.dividend.divide(this.divisor);
        if(floor.signum() < 0) {
            floor = floor.subtract(BigInteger.ONE);
        }
        return floor;
    }

    /**
     * Rounds this Fraction to the nearest BigInteger
     * @return the floor of this Fraction plus 1/2
     */
    public BigInteger round() {
        return add(new Fraction(1, 2)).floor();
    }

    /**
     * Finds the minimal Polynomial of this Fraction
     * @return the linear polynomial whose root is this Fraction and whose integer coefficients are co-prime
     */
    public Polynomial minimalPolynomial() {
        return new Polynomial(this.dividend.negate(), this.divisor);
    }

    /**
     * Finds the continued Fraction expansion of this Fraction
     * @return a List of Integers A such that A0 + 1/(A1 + 1/(A2 + ...)) = this Fraction
     */
    public List<BigInteger> continuedFraction() {
        final List<BigInteger> expansion = new LinkedList<>();
        BigInteger dividend = this.dividend, divisor = this.divisor;
        while(! divisor.equals(BigInteger.ZERO)) {
            BigInteger[] divideAndRemainder = dividend.divideAndRemainder(divisor);
            expansion.add(divideAndRemainder[0]);
            dividend = divisor;
            divisor = divideAndRemainder[1];
        }
        return expansion;
    }

    /**
     * Converts this Fraction to a printable format
     * @return this Fraction as a String
     */
    @Override
    public String toString() {
        return this.dividend + (this.divisor.equals(BigInteger.ONE) ? "" : ("/" + this.divisor));
    }

    /**
     * Prints this Fraction
     */
    public void print() {
        System.out.println(toString());
    }

    // static methods

    /**
     * Converts an array of Fractions to co-prime integer Fractions while preserving common ratios
     * @param f the array of Fractions
     * @return the scaled array
     */
    public static Fraction @NotNull [] raiseToIntegers(Fraction @NotNull ... f) {
        BigInteger LCM = BigInteger.ONE;
        for(Fraction value : f) {
            BigInteger gcd = LCM.gcd(value.denominator());
            if(gcd.equals(BigInteger.ZERO)) {
                gcd = BigInteger.ONE;
            }
            LCM = LCM.multiply(value.denominator()).divide(gcd);
        }
        final Fraction fractionLCM = new Fraction(LCM);
        final Fraction[] array = new Fraction[f.length];
        for(int i = 0; i < f.length; i++) {
            array[i] = f[i].multiply(fractionLCM);
        }
        BigInteger GCD = BigInteger.ZERO;
        for(Fraction value : array) {
            GCD = GCD.gcd(value.asBigInteger());
        }
        final Fraction fractionGCD = new Fraction(GCD);
        if(! fractionGCD.equals(ZERO)) {
            for(int i = 0; i < f.length; i++) {
                array[i] = array[i].divide(fractionGCD);
            }
        }
        return array;
    }

    /**
     * Converts an array of Numbers to an array of Fractions
     * @param n the Number array
     * @return the converted Fraction array
     */
    public static Fraction @NotNull [] valueOf(Number @NotNull ... n) {
        final Fraction[] fractions = new Fraction[n.length];
        for(int i = 0; i < fractions.length; i++) {
            fractions[i] = new Fraction(n[i]);
        }
        return fractions;
    }

    /**
     * Converts an array of {@code BigIntegers} to an array of {@code Fractions}. Note that each
     * {@code BigInteger} is the numerator of its own {@code Fraction}. Then each {@code Fraction}
     * is the fractional equivalent of an integer.
     * @param n the BigInteger array
     * @return the converted Fraction array
     */
    public static Fraction @NotNull [] valueOf(BigInteger @NotNull ... n) {
        final Fraction[] fractions = new Fraction[n.length];
        for(int i = 0; i < fractions.length; i++) {
            fractions[i] = new Fraction(n[i]);
        }
        return fractions;
    }

    /**
     * Converts an array of BigIntegers[] to an array of Fractions
     * @param n the BigInteger[] array
     * @return the converted Fraction array
     */
    public static Fraction @NotNull [] valueOf(@NotNull BigInteger[] ... n) {
        final Fraction[] fractions = new Fraction[n.length];
        for(int i = 0; i < fractions.length; i++) {
            fractions[i] = new Fraction(n[i]);
        }
        return fractions;
    }

    /**
     * Converts an array of Fractions to a printable format
     * @param ar the array of Fractions
     * @return the array in String format
     */
    public static @NotNull String arrayToString(Fraction @NotNull ... ar) {
        String print = "";
        for(Fraction item : ar) {
            print = print.concat(", ").concat(item.toString());
        }
        return "[".concat(print.length() > 0 ? print.substring(2) : print).concat("]");
    }

    /**
     * Prints an array of Fractions
     * @param ar the array of Fractions
     */
    public static void printArray(Fraction... ar) {
        System.out.println(arrayToString(ar));
    }
}