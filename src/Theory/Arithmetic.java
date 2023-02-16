package Theory;

import Algebra.Fraction;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class Arithmetic {
    /**
     * Finds the BigInteger approximation of the square root of a BigInteger
     * @param n the target square
     * @return the unique BigInteger s such that s ^ 2 <= n and (s + 1) ^ 2 > n
     */
    public static BigInteger sqrt(BigInteger n) {
        if(n.compareTo(BigInteger.TEN) < 0) {
            return BigInteger.valueOf((int) Math.sqrt(n.intValue()));
        }
        BigInteger sqrt = BigInteger.ONE, proxy = BigInteger.ZERO, backProxy = BigInteger.ZERO;
        while(! (sqrt.equals(backProxy))) {
            backProxy = proxy;
            proxy = sqrt;
            sqrt = sqrt.add(n.divide(sqrt)).divide(BigInteger.TWO);
        }
        return sqrt.min(proxy);
    }

    /**
     * Finds the BigInteger approximation of the nth root of a BigInteger
     * @param n the target base
     * @param pow the exponent of the root
     * @return the unique BigInteger s such that s ^ pow <= n and (s + 1) ^ pow > n
     */
    public static BigInteger nthRoot(BigInteger n, int pow) {
        if(n.compareTo(BigInteger.TEN) < 0) {
            return BigInteger.valueOf((int) Math.sqrt(n.intValue()));
        }
        BigInteger nthRoot = BigInteger.ONE, proxy = BigInteger.ZERO, backProxy = BigInteger.ZERO;
        while(! (nthRoot.equals(backProxy))) {
            backProxy = proxy;
            proxy = nthRoot;
            nthRoot = nthRoot.add(n.divide(nthRoot.pow(pow - 1))).divide(BigInteger.TWO);
        }
        return nthRoot.min(proxy);
    }

    /**
     * Finds the Kronecker Delta of two Integers
     * @param i the first Integer
     * @param j the second Integer
     * @return 1 if i == j, else 0
     */
    public static int KroneckerDelta(int i, int j) {
        return i == j ? 1 : 0;
    }

    /**
     * Finds the result when -1 is raised to an Integer power
     * @param pow the exponent
     * @return (-1) ^ pow
     */
    public static int negativePow(int pow) {
        return (pow & 1) == 0 ? 1 : -1;
    }

    /**
     * Finds the remainder when a Long is raised to a Long power and divided by another Long
     * @param base the base of the exponential operation
     * @param exp the exponent
     * @param mod the divisor
     * @return (base ^ pow) % mod
     */
    public static long modPow(long base, long exp, long mod) {
        List<Boolean> instructions = new LinkedList<>();
        while(exp != 0) {
            instructions.add(0, (exp & 1) == 1);
            exp >>>= 1;
        }
        long remainder = 1;
        for(boolean instruction : instructions) {
            remainder = (remainder * remainder) % mod;
            if(instruction) {
                remainder = (remainder * base) % mod;
            }
        }
        return remainder;
    }

    /**
     * Finds the nth harmonic number
     * @param n the index of the item in the series
     * @return the sum from 1 to n of 1/n
     */
    public static Fraction nthHarmonicNumber(int n) {
        Fraction harmonicNumber = Fraction.ZERO;
        for(int i = 1; i <= n; i++) {
            harmonicNumber = harmonicNumber.add(new Fraction(1, i));
        }
        return harmonicNumber;
    }

    /**
     * Finds the Integer component of a logarithm
     * @param base the base of the exponent
     * @param num the target number
     * @return the logarithm base 'base' of 'num'
     */
    public static int log(int base, int num) {
        int log = 0, product = 1;
        while(product < num) {
            product *= base;
            log++;
        }
        return product > num ? log - 1 : log;
    }
}