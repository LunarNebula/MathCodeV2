package Enumerator;

import Algebra.BooleanOperable;
import Exception.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class UnsignedInt implements BooleanOperable<UnsignedInt>, Comparable<UnsignedInt> {
    private final boolean[] bits;
    private final int hashCode;

    /**
     * Creates a new UnsignedInt with a specified bit maximum
     * @param bits the number of bits allocated to this UnsignedInt
     * @throws IllegalArgumentException if the number of bits given is
     */
    public UnsignedInt(int bits) throws IllegalArgumentException {
        verifyBitLegality(bits);
        this.bits = new boolean[bits];
        this.hashCode = computeHashcode();
    }

    /**
     * Creates a new UnsignedInt from an array of bits (as booleans)
     * @param bits the bit array
     */
    private UnsignedInt(boolean... bits) {
        this.bits = bits;
        this.hashCode = computeHashcode();
    }

    /**
     * Creates a new {@code UnsignedInt} from a binary string.
     * @param s the {@code String}.
     * @throws IllegalArgumentException if the input {@code String} contains
     * digits other than 0 or 1.
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
     * Computes the square of this {@code UnsignedInt}.
     * @return {@code this * this}, or {@code this^2}
     */
    public UnsignedInt square() {
        return new UnsignedInt(square(this.bits));
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
     * Raises this {@code UnsignedInt} to a specified exponent.
     * @param exp the exponent.
     * @return {@code this ^ exp}
     */
    public UnsignedInt pow(int exp) {
        final boolean[] pow = new boolean[Integer.SIZE];
        int index = 0;
        while(exp != 0) {
            pow[index++] = (exp & 1) == 1;
            exp >>>= 1;
        }
        boolean[] nextBits = new boolean[this.bits.length];
        nextBits[0] = true;
        while(index > 0) {
            index--;
            nextBits = square(nextBits);
            if(pow[index]) {
                nextBits = multiply(this.bits, nextBits);
            }
        }
        return new UnsignedInt(nextBits);
    }

    /**
     * Shifts this {@code UnsignedInt} a particular number of bits to the left.
     * @param shift the left-shift amount. If {@code shift} is negative, the
     *              shift will be {@code -shift} digits to the right.
     * @return the shifted {@code UnsignedInt}.
     */
    public UnsignedInt shift(int shift) {
        final boolean[] nextBits = new boolean[this.bits.length];
        int index = 0, nextIndex = shift;
        if(shift < 0) {
            index = -shift;
            nextIndex = 0;
        }
        while(index < nextBits.length & nextIndex < nextBits.length) {
            nextBits[nextIndex] = this.bits[index];
            index++;
            nextIndex++;
        }
        return new UnsignedInt(nextBits);
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
        for(int i = this.bits.length - 1; i >= 0; i--) {
            if(this.bits[i] ^ o.bits[i]) {
                return this.bits[i] ? 1 : -1;
            }
        }
        return 0;
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
        for(int i = 0; i < this.bits.length; i++) {
            if(this.bits[i] ^ convert.bits[i]) {
                return false;
            }
        }
        return true;
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
     * Creates a new {@code UnsignedInt} with the bit count and binary value
     * of a specified int.
     * @param bits the number of bits to be allocated.
     * @param value the target int value.
     * @return the computed {@code UnsignedInt}.
     */
    @Contract("_, _ -> new")
    public static @NotNull UnsignedInt valueOf(int bits, int value) {
        verifyBitLegality(bits);
        final boolean[] nextBits = new boolean[bits];
        int index = 0;
        while(index < bits && value != 0) {
            nextBits[index++] = (value & 1) == 1;
            value >>>= 1;
        }
        return new UnsignedInt(nextBits);
    }

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
}
