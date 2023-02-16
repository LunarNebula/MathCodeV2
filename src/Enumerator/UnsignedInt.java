package Enumerator;

import Algebra.BooleanOperable;
import Exception.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class UnsignedInt implements BooleanOperable<UnsignedInt>, Comparable<UnsignedInt> {
    private final boolean[] bits;

    /**
     * Creates a new UnsignedInt with a specified bit maximum
     * @param bits the number of bits allocated to this UnsignedInt
     * @throws IllegalArgumentException if the number of bits given is
     */
    public UnsignedInt(int bits) throws IllegalArgumentException {
        verifyBitLegality(bits);
        this.bits = new boolean[bits];
    }

    /**
     * Creates a new UnsignedInt from an array of bits (as booleans)
     * @param bits the bit array
     */
    private UnsignedInt(boolean[] bits) {
        this.bits = bits;
    }

    /**
     * Creates a new UnsignedInt with a value equal to this + 1
     * @return the computer UnsignedInt
     */
    public UnsignedInt increment() {
        final boolean[] nextBits = new boolean[this.bits.length];
        int index = 0;
        boolean carry = true;
        while(carry & index < nextBits.length) {
            nextBits[index] = ! this.bits[index];
            carry = this.bits[index];
            index++;
        }
        System.arraycopy(this.bits, index, nextBits, index, nextBits.length - index);
        return new UnsignedInt(nextBits);
    }

    /**
     * Adds this UnsignedInt to another UnsignedInt
     * @param addend the addend UnsignedInt
     * @return this + addend
     * @throws IllegalArgumentException if the bit capacities of this UnsignedInt and the addend UnsignedInt are unequal
     */
    public UnsignedInt add(@NotNull UnsignedInt addend) throws IllegalArgumentException {
        final boolean[] nextBits = new boolean[this.bits.length];
        verifyBitEquality(nextBits.length, addend.bits.length);
        boolean carry = false;
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = carry ^ this.bits[i] ^ addend.bits[i];
            carry = ((carry | this.bits[i]) & addend.bits[i]) | (carry & this.bits[i]);
        }
        return new UnsignedInt(nextBits);
    }

    /**
     * Subtracts a specified UnsignedInt from this UnsignedInt
     * @param subtrahend the subtrahend UnsignedInt
     * @return this - subtrahend
     * @throws IllegalArgumentException if the bit capacities of this UnsignedInt and the subtrahend UnsignedInt are unequal
     */
    public UnsignedInt subtract(@NotNull UnsignedInt subtrahend) throws IllegalArgumentException {
        final boolean[] nextBits = new boolean[this.bits.length];
        verifyBitEquality(nextBits.length, subtrahend.bits.length);
        boolean carry = true;
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = (carry == subtrahend.bits[i]);
            if(carry) {
                carry = ! subtrahend.bits[i];
            }
        }
        return add(new UnsignedInt(nextBits));
    }

    /**
     * Finds the product of this UnsignedInt and another, specified UnsignedInt
     * @param multiplicand the multiplicand UnsignedInt
     * @return this * multiplicand
     * @throws IllegalArgumentException if the bit capacities of this and the multiplicand are unequal
     */
    public UnsignedInt multiply(@NotNull UnsignedInt multiplicand) throws IllegalArgumentException {
        final boolean[] nextBits = new boolean[this.bits.length];
        verifyBitEquality(nextBits.length, multiplicand.bits.length);
        return new UnsignedInt(nextBits);
    } //TODO: write this method

    /**
     * Shifts this UnsignedInt a particular number of bits to the left
     * @param shift the left-shift amount
     * @return the shifted UnsignedInt
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

    public UnsignedInt shiftTest(int shift) {
        final boolean[] nextBits = new boolean[this.bits.length];
        if(shift < 0) {} else {}
        return new UnsignedInt(nextBits);
    }

    /**
     * Computes the UnsignedInt formed from taking a bitwise AND function across this UnsignedInt and a comparator UnsignedInt
     * @param o the comparator BooleanSetOperable
     * @return this AND o
     * @throws IllegalArgumentException if the bit capacities of this UnsignedInt and the comparator UnsignedInt are unequal
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
     * Computes the UnsignedInt formed from taking a bitwise OR function across this UnsignedInt and a comparator UnsignedInt
     * @param o the comparator BooleanSetOperable
     * @return this OR o
     * @throws IllegalArgumentException if the bit capacities of this UnsignedInt and the comparator UnsignedInt are unequal
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
     * Computes the UnsignedInt formed from taking a bitwise XOR function across this UnsignedInt and a comparator UnsignedInt
     * @param o the comparator BooleanSetOperable
     * @return this XOR o
     * @throws IllegalArgumentException if the bit capacities of this UnsignedInt and the comparator UnsignedInt are unequal
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
     * Computes the UnsignedInt formed from taking a bitwise AND NOT function across this UnsignedInt and a comparator UnsignedInt
     * @param o the comparator BooleanSetOperable
     * @return this AND NOT o
     * @throws IllegalArgumentException if the bit capacities of this UnsignedInt and the comparator UnsignedInt are unequal
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
     * Finds the logical negation of this UnsignedInt
     * @return NOT this
     */
    @Override
    public UnsignedInt NOT() {
        final boolean[] nextBits = new boolean[this.bits.length];
        for(int i = 0; i < nextBits.length; i++) {
            nextBits[i] = ! this.bits[i];
        }
        return new UnsignedInt(nextBits);
    }

    /**
     * Finds the logical NOT_AND of this UnsignedInt and another, specified UnsignedInt
     * @param o the comparator UnsignedInt
     * @return NOT (this AND o)
     * @throws IllegalArgumentException if the respective bit capacities for this and o are unequal
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
     * Finds the logical NOR of this UnsignedInt and another, specified UnsignedInt
     * @param o the comparator UnsignedInt
     * @return NOT (this OR o)
     * @throws IllegalArgumentException if the respective bit capacities for this and o are unequal
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
     * Finds the number of bits allocated for this UnsignedInt
     * @return the length of the bit array in this UnsignedInt
     */
    public int bits() {
        return this.bits.length;
    }

    /**
     * Compares this UnsignedInt to another, specified UnsignedInt
     * @param o the comparator UnsignedInt
     * @return 0 if the two values are identical, else 1 if this > 0, else -1
     * @throws IllegalArgumentException if the bit capacities of this and o are unequal
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
     * Finds the hashCode of this UnsignedInt
     * @return a unique int value representing this UnsignedInt
     */
    @Override
    public int hashCode() {
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
     * Determines whether this UnsignedInt is equal to a specified Object comparator
     * @param o the comparator
     * @return true if the comparator is an UnsignedInt with identical bit capacity and digits, else false
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
     * Converts this UnsignedInt to a printable format
     * @return this UnsignedInt as a String
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
     * Prints this UnsignedInt
     */
    public void print() {
        System.out.println(this);
    }

    // Static methods

    /**
     * Creates a new UnsignedInt with the bit count and binary value of a specified Integer
     * @param bits the number of bits to be allocated
     * @param value the target Integer value
     * @return the computed UnsignedInt
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
     * Determines whether the bit counts of two UnsignedInts allow for UnsignedInt arithmetic
     * @param bitLength1 the first bit count
     * @param bitLength2 the second bit count
     * @throws IllegalArgumentException if bitLength1 != bitLength2
     */
    private static void verifyBitEquality(int bitLength1, int bitLength2) throws IllegalArgumentException {
        if(bitLength1 != bitLength2) {
            throw new IllegalArgumentException(ExceptionMessage.ARGUMENT_EXCEEDS_REQUIRED_DOMAIN());
        }
    }

    /**
     * Determines whether a given bit count is legal for this UnsignedInt
     * @param bitLength the number of bits allocated to this UnsignedInt
     * @throws IllegalArgumentException if the bit count is negative
     */
    private static void verifyBitLegality(int bitLength) throws IllegalArgumentException {
        if(bitLength < 0) {
            throw new IllegalArgumentException(ExceptionMessage.ARGUMENT_EXCEEDS_REQUIRED_DOMAIN());
        }
    }
}
