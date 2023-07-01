package Enumerator;

import Algebra.BooleanOperable;
import DataSet.BST;
import Exception.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores an unsigned binary integer of a set size. The role of this class object is to
 * behave like {@code Integer} or {@code Long}, with the caveat that the size of the value
 * represented can be chosen by the programmer (i.e. without the restriction to 32 or 64 bits).
 * {@code UnsignedInt} objects exhibit any {@code Integer}-available arithmetic operations
 * with other {@code UnsignedInt} objects of the same bit capacity (or "bits" array length).
 * Attempting to perform operations on two {@code UnsignedInt} values of different capacities
 * will result in the program's throwing an {@link IllegalArgumentException}. However, an
 * {@code UnsignedInt} can be converted to an equivalent one of different length using the
 * "resize" method. Other methods for basic factorization have also been added.
 * @see Integer
 * @see Long
 */
public class UnsignedInt implements BooleanOperable<UnsignedInt>, Comparable<UnsignedInt> {
    private final boolean[] bits;
    private final int hashCode;
    private List<UnsignedInt> factors;

    /**
     * Creates a new {@code UnsignedInt} equal to zero and with a specified bit maximum.
     * @param bits the number of bits allocated to this {@code UnsignedInt}.
     * @throws IllegalArgumentException if the number of bits given is negative.
     */
    public UnsignedInt(int bits) throws IllegalArgumentException {
        verifyBitLegality(bits);
        this.bits = new boolean[bits];
        this.hashCode = computeHashcode();
    }

    /**
     * Creates a new {@code UnsignedInt} from an array of bits (as booleans).
     * @param bits the bit array.
     */
    private UnsignedInt(boolean... bits) {
        this.bits = bits;
        this.hashCode = computeHashcode();
    }

    /**
     * Creates a new {@code UnsignedInt} from a specified bit capacity and initial value.
     * @param bits the number of bits.
     * @param value the initial value.
     */
    public UnsignedInt(int bits, int value) {
        verifyBitLegality(bits);
        this.bits = toBinaryArray(bits, value);
        this.hashCode = computeHashcode();
    }

    /**
     * Creates a new {@code UnsignedInt} from a binary string.
     * @param s the {@code String}.
     * @throws IllegalArgumentException if the input {@code String} contains
     * digits other than {@code 0} or {@code 1}.
     */
    public UnsignedInt(String s) {
        this.bits = new boolean[s.length()];
        final char[] digits = s.toCharArray();
        int index = this.bits.length;
        for(int i = 0; i < this.bits.length; i++) {
            index--;
            if(digits[i] == '0') {
                this.bits[index] = false;
            } else if(digits[i] == '1') {
                this.bits[index] = true;
            } else {
                throw new IllegalArgumentException();
            }
        }
        this.hashCode = computeHashcode();
    }

    /**
     * Computes the hashcode for this {@code UnsignedInt}.
     * @return the hashcode.
     */
    private int computeHashcode() {
        int hashCode = 0;
        for(int i = this.bits.length - 1; i >= 0; i--) {
            hashCode <<= 1;
            if(this.bits[i]) {
                hashCode |= 1;
            }
        }
        return hashCode;
    }

    /**
     * Creates a new {@code UnsignedInt} with a value equal to {@code this + 1}.
     * @return the computed {@code UnsignedInt}.
     */
    public UnsignedInt increment() {
        return new UnsignedInt(increment(this.bits));
    }

    /**
     * Adds this {@code UnsignedInt} to another {@code UnsignedInt}.
     * @param addend the addend {@code UnsignedInt}.
     * @return {@code this + addend}
     * @throws IllegalArgumentException if the bit capacities of this {@code UnsignedInt}
     * and the addend {@code UnsignedInt} are unequal.
     */
    public UnsignedInt add(@NotNull UnsignedInt addend) throws IllegalArgumentException {
        verifyBitEquality(this.bits.length, addend.bits.length);
        return new UnsignedInt(add(this.bits, addend.bits));
    }

    /**
     * Subtracts a specified UnsignedInt from this UnsignedInt
     * @param subtrahend the subtrahend UnsignedInt
     * @return this - subtrahend
     * @throws IllegalArgumentException if the bit capacities of this UnsignedInt and the subtrahend UnsignedInt are unequal
     */
    public UnsignedInt subtract(@NotNull UnsignedInt subtrahend) throws IllegalArgumentException {
        verifyBitEquality(this.bits.length, subtrahend.bits.length);
        return new UnsignedInt(subtract(this.bits, subtrahend.bits));
    }

    /**
     * Finds the product of this {@code UnsignedInt} and another, specified {@code UnsignedInt}.
     * @param multiplicand the multiplicand {@code UnsignedInt}.
     * @return {@code this * multiplicand}
     * @throws IllegalArgumentException if the bit capacities of this and the multiplicand are unequal.
     */
    public UnsignedInt multiply(@NotNull UnsignedInt multiplicand) throws IllegalArgumentException {
        verifyBitEquality(this.bits.length, multiplicand.bits.length);
        return new UnsignedInt(multiply(this.bits, multiplicand.bits));
    }

    /**
     * Computes the square of this {@code UnsignedInt}.
     * @return {@code this * this}, or {@code this ^ 2}.
     */
    public UnsignedInt square() {
        return new UnsignedInt(square(this.bits));
    }

    /**
     * Raises this {@code UnsignedInt} to a specified exponent.
     * @param exp the exponent.
     * @return {@code this ^ exp}
     */
    public UnsignedInt pow(int exp) {
        return new UnsignedInt(pow(this.bits, exp));
    }

    /**
     * Finds the quotient and remainder of this {@code UnsignedInt} and another,
     * specified {@code UnsignedInt}.
     * @param divisor the divisor.
     * @return an array of {@code UnsignedInts} with {@code (this / divisor)} at index
     * {@code 0} and {@code (this % remainder)} at index {@code 1}.
     */
    public UnsignedInt[] divideAndRemainder(@NotNull UnsignedInt divisor) {
        verifyBitEquality(this.bits.length, divisor.bits.length);
        final boolean[][] bits = divideAndRemainder(this.bits, divisor.bits);
        return new UnsignedInt[]{new UnsignedInt(bits[0]), new UnsignedInt(bits[1])};
    }

    /**
     * Finds the remainder when an integer power of this {@code UnsignedInt} is
     * divided by a specified modulus.
     * @param divisor the divisor.
     * @param exp the integer power.
     * @return {@code (this ^ exp) % divisor}
     */
    public UnsignedInt modPow(UnsignedInt divisor, int exp) {
        return new UnsignedInt(modPow(this.bits, divisor.bits, exp));
    }

    /**
     * Finds the greatest common divisor of this {@code UnsignedInt} and another,
     * specified {@code UnsignedInt}.
     * @param n the other specified value.
     * @return an {@code UnsignedInt} of the greatest positive integer value {@code g}
     * satisfying {@code (g | this)} and {@code (g | n)}.
     */
    public UnsignedInt gcd(@NotNull UnsignedInt n) {
        final int length = this.bits.length;
        verifyBitEquality(length, n.bits.length);
        boolean[] a = new boolean[length], b = new boolean[length];
        System.arraycopy(this.bits, 0, a, 0, length);
        System.arraycopy(n.bits, 0, b, 0, length);
        if(compareTo(a, b) > 0) {
            final boolean[] switcher = a;
            a = b;
            b = switcher;
        }
        while(! isZero(a)) {
            final boolean[] modulus = divideAndRemainder(b, a)[1];
            b = a;
            a = modulus;
        }
        return new UnsignedInt(b);
    }

    /**
     * Shifts this {@code UnsignedInt} a particular number of bits to the left.
     * @param shift the left-shift amount. If {@code shift} is negative, the
     *              shift will be {@code -shift} digits to the right.
     * @return the shifted {@code UnsignedInt}.
     */
    public UnsignedInt shift(int shift) {
        return new UnsignedInt(shift(this.bits, shift));
    }

    /**
     * Computes the integral square root of this {@code UnsignedInt}.
     * @return the unique {@code UnsignedInt i} such that {@code i^2 <= this} and
     * {@code (i+1)^2 > this}.
     */
    public UnsignedInt sqrt() {
        return new UnsignedInt(sqrt(this.bits));
    }

    /**
     * Finds all factors of this {@code UnsignedInt}.
     * @return a list of all {@code n} such that {@code (n | this)}.
     */
    public List<UnsignedInt> factors() {
        if(this.factors == null) {
            final List<boolean[]> factors = factors(this.bits);
            final List<UnsignedInt> proxyList = new ArrayList<>(factors.size());
            for(boolean[] factor : factors) {
                proxyList.add(new UnsignedInt(factor));
            }
            this.factors = new BST<>(proxyList).getOrderedList();
        }
        return new ArrayList<>(this.factors);
    }

    /**
     * Computes the {@code UnsignedInt} formed from taking a bitwise {@code AND}
     * function across this {@code UnsignedInt} and a comparator {@code UnsignedInt}.
     * @param o the comparator {@code BooleanSetOperable}.
     * @return {@code this AND o}
     * @throws IllegalArgumentException if the bit capacities of this and the comparator
     * are unequal.
     */
    @Override
    public UnsignedInt AND(@NotNull UnsignedInt o) throws IllegalArgumentException {
        final boolean[] nextBits = new boolean[this.bits.length];
        verifyBitEquality(nextBits.length, o.bits.length);
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = this.bits[i] & o.bits[i];
        }
        return new UnsignedInt(nextBits);
    }

    /**
     * Computes the {@code UnsignedInt} formed from taking a bitwise {@code OR}
     * function across this {@code UnsignedInt} and a comparator {@code UnsignedInt}.
     * @param o the comparator {@code BooleanSetOperable}.
     * @return {@code this OR o}
     * @throws IllegalArgumentException if the bit capacities of this and the comparator
     * are unequal.
     */
    @Override
    public UnsignedInt OR(@NotNull UnsignedInt o) throws IllegalArgumentException {
        final boolean[] nextBits = new boolean[this.bits.length];
        verifyBitEquality(nextBits.length, o.bits.length);
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = this.bits[i] | o.bits[i];
        }
        return new UnsignedInt(nextBits);
    }

    /**
     * Computes the {@code UnsignedInt} formed from taking a bitwise {@code XOR}
     * function across this {@code UnsignedInt} and a comparator {@code UnsignedInt}.
     * @param o the comparator {@code BooleanSetOperable}.
     * @return {@code this XOR o}
     * @throws IllegalArgumentException if the bit capacities of this and the comparator
     * are unequal.
     */
    @Override
    public UnsignedInt XOR(@NotNull UnsignedInt o) throws IllegalArgumentException {
        final boolean[] nextBits = new boolean[this.bits.length];
        verifyBitEquality(nextBits.length, o.bits.length);
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = this.bits[i] ^ o.bits[i];
        }
        return new UnsignedInt(nextBits);
    }

    /**
     * Computes the {@code UnsignedInt} formed from taking a bitwise {@code AND NOT}
     * function across this {@code UnsignedInt} and a comparator {@code UnsignedInt}.
     * @param o the comparator {@code BooleanSetOperable}.
     * @return {@code this AND NOT o}
     * @throws IllegalArgumentException if the bit capacities of this and the comparator
     * are unequal.
     */
    @Override
    public UnsignedInt AND_NOT(@NotNull UnsignedInt o) throws IllegalArgumentException {
        final boolean[] nextBits = new boolean[this.bits.length];
        verifyBitEquality(nextBits.length, o.bits.length);
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = this.bits[i] & (! o.bits[i]);
        }
        return new UnsignedInt(nextBits);
    }

    /**
     * Finds the logical negation of this {@code UnsignedInt}.
     * @return {@code NOT this}
     */
    @Override
    public UnsignedInt NOT() {
        return new UnsignedInt(NOT(this.bits));
    }

    /**
     * Finds the logical {@code NOT_AND} of this {@code UnsignedInt} and another,
     * specified {@code UnsignedInt}.
     * @param o the comparator {@code UnsignedInt}.
     * @return {@code NOT (this AND o)}
     * @throws IllegalArgumentException if the respective bit capacities for {@code this}
     * and {@code o} are unequal.
     */
    @Override
    public UnsignedInt NOT_AND(@NotNull UnsignedInt o) throws IllegalArgumentException {
        final boolean[] nextBits = new boolean[this.bits.length];
        verifyBitEquality(nextBits.length, o.bits.length);
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = ! (this.bits[i] & o.bits[i]);
        }
        return new UnsignedInt(nextBits);
    }

    /**
     * Finds the logical {@code NOR} of this {@code UnsignedInt} and another,
     * specified {@code UnsignedInt}.
     * @param o the comparator {@code UnsignedInt}.
     * @return {@code NOT (this OR o)}
     * @throws IllegalArgumentException if the respective bit capacities for {@code this}
     * and {@code o} are unequal.
     */
    @Override
    public UnsignedInt NOR(@NotNull UnsignedInt o) throws IllegalArgumentException {
        final boolean[] nextBits = new boolean[this.bits.length];
        verifyBitEquality(nextBits.length, o.bits.length);
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = ! (this.bits[i] | o.bits[i]);
        }
        return new UnsignedInt(nextBits);
    }

    /**
     * Finds the number of bits allocated for this {@code UnsignedInt}.
     * @return the length of the bit array in this {@code UnsignedInt}.
     */
    public int bits() {
        return this.bits.length;
    }

    /**
     * Resizes this {@code UnsignedInt} to a different bit capacity.
     * @param size the new size.
     * @return an equivalent {@code UnsignedInt} with a different bit array length. Note
     * that choosing a smaller size for the array may cause some flipped bits to be lost.
     */
    public UnsignedInt resize(int size) {
        final boolean[] nextBits = new boolean[size];
        System.arraycopy(this.bits, 0, nextBits, 0, Math.min(size, this.bits.length));
        return new UnsignedInt(nextBits);
    }

    /**
     * Determines whether this {@code UnsignedInt} is a zero value.
     * @return {@code true} if any bits of this {@code UnsignedInt} are flipped, else {@code false}.
     */
    public boolean isZero() {
        return isZero(this.bits);
    }

    /**
     * Compares this {@code UnsignedInt} to another, specified {@code UnsignedInt}.
     * @param o the comparator {@code UnsignedInt}.
     * @return {@code 0} if the two values are identical, else {@code 1} if
     * {@code this > 0}, else {@code -1}.
     * @throws IllegalArgumentException if the bit capacities of {@code this} and
     * {@code o} are unequal.
     */
    @Override
    public int compareTo(@NotNull UnsignedInt o) throws IllegalArgumentException {
        verifyBitEquality(this.bits.length, o.bits.length);
        return compareTo(this.bits, o.bits);
    }

    /**
     * Finds the hashCode of this {@code UnsignedInt}.
     * @return {@code this.hashCode}.
     */
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /**
     * Determines whether this {@code UnsignedInt} is equal to a specified
     * {@code Object} comparator.
     * @param o the comparator.
     * @return {@code true} if the comparator is an {@code UnsignedInt} with
     * identical bit capacity and digits, else {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof UnsignedInt convert)) {
            return false;
        }
        if(this.bits.length != convert.bits.length) {
            return false;
        }
        return equals(this.bits, convert.bits);
    }

    /**
     * Converts this {@code UnsignedInt} to a printable format.
     * @return this {@code UnsignedInt} as a {@code String}.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = this.bits.length - 1; i >= 0; i--) {
            builder.append(this.bits[i] ? 1 : 0);
        }
        return builder.toString();
    }

    /**
     * Prints this {@code UnsignedInt}.
     */
    public void print() {
        System.out.println(this);
    }

    // Static methods

    /**
     * Determines whether the bit counts of two {@code UnsignedInts} allow for
     * {@code UnsignedInt} arithmetic.
     * @param bitLength1 the first bit count.
     * @param bitLength2 the second bit count.
     * @throws IllegalArgumentException if bitLength1 != bitLength2.
     */
    private static void verifyBitEquality(int bitLength1, int bitLength2) throws IllegalArgumentException {
        if(bitLength1 != bitLength2) {
            throw new IllegalArgumentException(ExceptionMessage.ARGUMENT_EXCEEDS_REQUIRED_DOMAIN());
        }
    }

    /**
     * Determines whether a given bit count is legal for this {@code UnsignedInt}.
     * @param bitLength the number of bits allocated to this {@code UnsignedInt}.
     * @throws IllegalArgumentException if the bit count is negative.
     */
    private static void verifyBitLegality(int bitLength) throws IllegalArgumentException {
        if(bitLength < 0) {
            throw new IllegalArgumentException(ExceptionMessage.ARGUMENT_EXCEEDS_REQUIRED_DOMAIN());
        }
    }

    /**
     * Creates a new binary array with a specified bit capacity and initial binary value.
     * @param bits the capacity.
     * @param value the initial value.
     * @return the converted bit array.
     */
    private static boolean[] toBinaryArray(int bits, int value) {
        final boolean[] nextBits = new boolean[bits];
        for(int i = 0; i < bits && value != 0; i++) {
            nextBits[i] = ((1 & value) != 0);
            value >>>= 1;
        }
        return nextBits;
    }

    /**
     * Increments a number represented by a bit array by 1.
     * @param bits the bit array.
     * @return {@code bits + 1}
     */
    private static boolean[] increment(boolean... bits) {
        final boolean[] nextBits = new boolean[bits.length];
        int index = 0;
        boolean carry = true;
        while(carry & (index < nextBits.length)) {
            nextBits[index] = ! bits[index];
            carry = bits[index];
            index++;
        }
        System.arraycopy(bits, index, nextBits, index, nextBits.length - index);
        return nextBits;
    }

    /**
     * Finds the sum of two values represented as bit (boolean) arrays.
     * @param a the first addend.
     * @param b the second addend.
     * @return {@code a + b}
     */
    private static boolean[] add(boolean[] a, boolean[] b) {
        final boolean[] nextBits = new boolean[a.length];
        boolean carry = false;
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = carry ^ a[i] ^ b[i];
            carry = ((carry | a[i]) & b[i]) | (carry & a[i]);
        }
        return nextBits;
    }

    /**
     * Computes the difference between two numbers represented as bit (boolean) arrays.
     * @param a the minuend.
     * @param b the subtrahend.
     * @return {@code a - b}
     */
    private static boolean[] subtract(boolean[] a, boolean[] b) {
        return add(a, negate(b));
    }

    /**
     * Finds the negation of a number represented as a bit array.
     * @param bits the number.
     * @return {@code -bits}
     */
    private static boolean[] negate(boolean... bits) {
        final boolean[] nextBits = new boolean[bits.length];
        boolean carry = true;
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = (carry == bits[i]);
            if(carry) {
                carry = ! bits[i];
            }
        }
        return nextBits;
    }

    /**
     * Computes the bitwise negation of a number represented as a bit array.
     * @param bits the number.
     * @return {@code ~bits}
     */
    private static boolean[] NOT(boolean... bits) {
        final boolean[] nextBits = new boolean[bits.length];
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = ! bits[i];
        }
        return nextBits;
    }

    /**
     * Computes the product of two numbers represented as arrays of binary digits.
     * @param a the first multiplicand.
     * @param b the second multiplicand.
     * @return {@code a * b}
     */
    private static boolean[] multiply(boolean[] a, boolean[] b) {
        final boolean[] nextBits = new boolean[a.length];
        for(int i = 0; i < nextBits.length; i++) {
            boolean carry = false;
            final int limit = nextBits.length - i;
            if(a[i]) {
                for(int j = 0; j < limit; j++) {
                    final int index = i + j;
                    final boolean digit = nextBits[index] ^ b[j] ^ carry;
                    carry = (((carry | nextBits[index]) & b[j]) | (carry & nextBits[index]));
                    nextBits[i + j] = digit;
                }
            }
        }
        return nextBits;
    }

    /**
     * Computes the square of a value represented as an array of binary (boolean) digits.
     * @return the (truncated) square of the number.
     */
    private static boolean[] square(boolean... bits) {
        final boolean[] nextBits = new boolean[bits.length];
        final int lastIndex = nextBits.length - 1, digitMatchLimit = lastIndex >> 1;
        for(int i = 0; i <= digitMatchLimit; i++) {
            nextBits[i << 1] = bits[i];
        }
        for(int i = 0; i < nextBits.length; i++) {
            boolean carry = false;
            final int limit = lastIndex - i;
            if(bits[i]) {
                for(int j = i + 1; j < limit; j++) {
                    final int index = i + j + 1;
                    final boolean digit = nextBits[index] ^ bits[j] ^ carry;
                    carry = (((carry | nextBits[index]) & bits[j]) | (carry & nextBits[index]));
                    nextBits[index] = digit;
                }
            }
        }
        return nextBits;
    }

    /**
     * Computes an integer power of a binary number represented by a bit array.
     * @param bits the target number.
     * @param exp the integer power.
     * @return {@code (bits ^ exp)} as a bit array.
     */
    private static boolean[] pow(boolean[] bits, int exp) {
        final boolean[] pow = new boolean[Integer.SIZE];
        int index = 0;
        while(exp != 0) {
            pow[index++] = (exp & 1) == 1;
            exp >>>= 1;
        }
        boolean[] nextBits = new boolean[bits.length];
        if(index == 0) {
            nextBits[0] = true;
        } else {
            index--;
            System.arraycopy(bits, 0, nextBits, 0, nextBits.length);
            while(index > 0) {
                index--;
                nextBits = square(nextBits);
                if(pow[index]) {
                    nextBits = multiply(bits, nextBits);
                }
            }
        }
        return nextBits;
    }

    /**
     * Computes the quotient and remainder of two numbers represented as bit arrays.
     * @param dividend the dividend value.
     * @param divisor the divisor value.
     * @return an array containing two bit arrays, that at index {@code 0} designating
     * a binary value equal to {@code dividend / divisor} and that at index {@code 1}
     * designating {@code dividend % divisor}.
     */
    private static boolean[] [] divideAndRemainder(boolean[] dividend, boolean[] divisor) {
        final int length = divisor.length, lastIndex = length - 1;
        final boolean[] quotientBits = new boolean[length];
        final boolean[] remainderBits = new boolean[length];
        System.arraycopy(dividend, 0, remainderBits, 0, length);
        int dividendIndex = lastIndex, divisorIndex = lastIndex;
        while(divisorIndex >= 0 && ! divisor[divisorIndex]) {
            divisorIndex--;
        }
        if(divisorIndex < 0) {
            throw new ArithmeticException(ExceptionMessage.ARGUMENT_EXCEEDS_REQUIRED_DOMAIN());
        }
        while(dividendIndex >= 0 && ! remainderBits[dividendIndex]) {
            dividendIndex--;
        }
        while(dividendIndex >= divisorIndex) {
            final int startIndex = dividendIndex - divisorIndex;
            int digitCopyIndex = startIndex;
            final boolean[] newRemainder = new boolean[divisorIndex + 1];
            boolean carry = false;
            for(int i = 0; i < newRemainder.length; i++) {
                newRemainder[i] = remainderBits[digitCopyIndex] ^ divisor[i] ^ carry;
                carry = ((carry || divisor[i]) && !remainderBits[digitCopyIndex]) ||
                        (carry & remainderBits[digitCopyIndex] & divisor[i]);
                digitCopyIndex++;
            }
            if((! carry) || (dividendIndex < lastIndex && remainderBits[dividendIndex + 1])) {
                if(carry) {
                    remainderBits[dividendIndex + 1] = false;
                }
                System.arraycopy(newRemainder, 0, remainderBits,
                        startIndex, newRemainder.length);
                quotientBits[startIndex] = true;
            }
            dividendIndex--;
        }
        return new boolean[][]{quotientBits, remainderBits};
    }

    /**
     * Finds the remainder when an integer power of a specified number is
     * divided by a particular modulus. Both the base and the modulus are
     * binary values represented as bit arrays.
     * @param bits the base value.
     * @param mod the modulus.
     * @param exp the integer power.
     * @return {@code (bits ^ exp) % mod}
     */
    private static boolean[] modPow(boolean[] bits, boolean[] mod, int exp) {
        final boolean[] pow = new boolean[Integer.SIZE];
        int index = 0;
        while(exp != 0) {
            pow[index++] = (exp & 1) == 1;
            exp >>>= 1;
        }
        boolean[] nextBits = new boolean[bits.length];
        if(index == 0) {
            nextBits[0] = true;
            return divideAndRemainder(nextBits, mod)[1];
        }
        index--;
        System.arraycopy(bits, 0, nextBits, 0, nextBits.length);
        while(index > 0) {
            index--;
            nextBits = divideAndRemainder(square(nextBits), mod)[1];
            if(pow[index]) {
                nextBits = divideAndRemainder(multiply(nextBits, bits), mod)[1];
            }
        }
        return nextBits;
    }

    /**
     * Performs a bit-shift on a bit array representing a binary value.
     * @param bits the bit array.
     * @param shift the shift amount. If {@code shift > 0}, then the bits will be moved
     *              {@code shift} steps to the left (for a value greater than or equal
     *              to the previous). If {@code shift < 0}, then the bits will be moved
     *              {@code (-shift)} steps to the right.
     * @return the shifted binary array.
     */
    private static boolean[] shift(boolean[] bits, int shift) {
        final boolean[] nextBits = new boolean[bits.length];
        if(shift > 0) {
            System.arraycopy(bits, 0, nextBits, shift, nextBits.length - shift);
        } else {
            System.arraycopy(bits, -shift, nextBits, 0, nextBits.length + shift);
        }
        return nextBits;
    }

    /**
     * Computes the integral square root of a binary number represented as a bit array.
     * @param bits the target number.
     * @return a bit array of the unique {@code UnsignedInt i} such that {@code i^2 <= bits}
     * and {@code (i+1)^2 > bits}.
     */
    private static boolean[] sqrt(boolean... bits) {
        boolean[] nextBits = new boolean[bits.length];
        System.arraycopy(bits, 0, nextBits, 0, nextBits.length);
        boolean[] prevBits = nextBits, prevPrevBits;
        do {
            prevPrevBits = prevBits;
            prevBits = nextBits;
            nextBits = shift(add(nextBits, divideAndRemainder(bits, nextBits)[0]), -1);
        } while(! equals(prevPrevBits, nextBits));
        return prevBits;
    }

    /**
     * Computes a list of prime factors for a number represented as a bit array.
     * @param bits the target number.
     * @return the list of all numbers {@code n} satisfying {@code (n | bits)}.
     */
    private static List<boolean[]> primeFactors(boolean... bits) {
        final List<boolean[]> primeFactors = new ArrayList<>();
        int nonZeroIndex = 0;
        while(nonZeroIndex < bits.length && ! bits[nonZeroIndex]) {
            nonZeroIndex++;
        }
        if(nonZeroIndex != bits.length) {
            for(int i = 0; i < nonZeroIndex; i++) {
                primeFactors.add(toBinaryArray(bits.length, 2));
            }
            bits = shift(bits, -nonZeroIndex);
            boolean[] sqrt = sqrt(bits), divisor = toBinaryArray(bits.length, 3);
            final boolean[] TWO = toBinaryArray(bits.length, 2);
            while (compareTo(divisor, sqrt) <= 0) {
                boolean recompute = false;
                boolean[][] quotientRemainder = divideAndRemainder(bits, divisor);
                while(isZero(quotientRemainder[1])) {
                    final boolean[] digits = new boolean[bits.length];
                    System.arraycopy(divisor, 0, digits, 0, bits.length);
                    primeFactors.add(digits);
                    bits = quotientRemainder[0];
                    quotientRemainder = divideAndRemainder(bits, divisor);
                    recompute = true;
                }
                if(recompute) {
                    sqrt = sqrt(bits);
                }
                divisor = add(divisor, TWO);
            }
            if(compareTo(bits, toBinaryArray(bits.length, 1)) > 0) {
                primeFactors.add(bits);
            }
        }
        return primeFactors;
    }

    /**
     * Computes all factors of a number represented as a bit array.
     * @param bits the target number.
     * @return a list of bit arrays representing all numbers that evenly divide
     * the target value.
     */
    private static List<boolean[]> factors(boolean... bits) {
        final List<boolean[]> primes = primeFactors(bits), factors = new ArrayList<>();
        List<boolean[]> test = new ArrayList<>();
        boolean[] prev = toBinaryArray(bits.length, 1);
        boolean[] product = toBinaryArray(bits.length, 1);
        factors.add(toBinaryArray(bits.length, 1));
        for(boolean[] primeFactor : primes) {
            if(equals(primeFactor, prev)) {
                product = multiply(product, primeFactor);
            } else {
                factors.addAll(test);
                prev = primeFactor;
                test = new ArrayList<>();
                product = primeFactor;
            }
            for(boolean[] factor : factors) {
                test.add(multiply(factor, product));
            }
        }
        factors.addAll(test);
        return factors;
    }

    /**
     * Determines whether a specified bit array designates a zero value.
     * @param bits the bit array.
     * @return {@code true} if no bits in the array are flipped, else {@code false}.
     */
    private static boolean isZero(boolean[] bits) {
        for(boolean b : bits) {
            if(b) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds a comparison between two numbers represented by bit arrays.
     * @param a the first number.
     * @param b the second number.
     * @return {@code 1} if {@code a > b}, else {@code 0} if {@code a = b},
     * else {@code -1} when {@code a < b}.
     */
    private static int compareTo(boolean[] a, boolean[] b) {
        for(int i = a.length - 1; i >= 0; i--) {
            if(a[i] ^ b[i]) {
                return a[i] ? 1 : -1;
            }
        }
        return 0;
    }

    /**
     * Determines whether two numbers represented as bit arrays are equal.
     * @param a the first number.
     * @param b the second number.
     * @return {@code true} if each bit of {@code a} is equal to the corresponding
     * bit of {@code b}, else {@code false} if at least one bit is different.
     */
    private static boolean equals(boolean[] a, boolean[] b) {
        for(int i = 0; i < a.length; i++) {
            if(a[i] ^ b[i]) {
                return false;
            }
        }
        return true;
    }
}
