package Enumerator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Binary {
    protected final List<Boolean> digits;

    /**
     * Creates a new Binary
     * @param value the decimal representation of the initial value of this Binary
     */
    public Binary(int value) {
        this.digits = new LinkedList<>();
        set(value);
    }

    /**
     * Sets this Binary to a new value
     * @param value the decimal representation of the new value
     */
    public void set(int value) {
        this.digits.clear();
        if(value < 0) {
            value = 0;
        }
        do {
            this.digits.add((value & 1) == 1);
            value >>>= 1;
        } while(value != 0);
    }

    /**
     * Adds one to this Binary
     */
    public void increment() {
        ListIterator<Boolean> iterator = this.digits.listIterator();
        boolean carry = true;
        while(carry) {
            boolean hasNext = iterator.hasNext();
            carry = hasNext && iterator.next();
            if(hasNext) {
                iterator.remove();
            }
            iterator.add(! carry);
        }
    }

    /**
     * Returns the digits
     * @return this.digits as an Integer list
     */
    public List<Integer> getDigits() {
        List<Integer> digits = new LinkedList<>();
        this.digits.forEach(digit -> digits.add(digit ? 1 : 0));
        return digits;
    }

    /**
     * Returns the original digits of this Binary
     * @return this.digits
     */
    public List<Boolean> getBinary() {
        return new LinkedList<>(this.digits);
    }

    /**
     * Determines the hashCode of this SignedBinary
     * @return the decimal representation of this SingedBinary
     */
    @Override
    public int hashCode() {
        Iterator<Boolean> iterator = ((LinkedList<Boolean>) this.digits).descendingIterator();
        int hashCode = 0;
        while(iterator.hasNext()) {
            boolean nextDigit = iterator.next();
            hashCode <<= 1;
            if(nextDigit) {
                hashCode |= 1;
            }
        }
        return hashCode;
    }

    /**
     * Determines whether this Binary is equal to a comparator Object
     * @param o the comparator Object
     * @return true if the Object is a Binary with congruent digits, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Binary b)) {
            return false;
        }
        if(this.digits.size() != b.digits.size()) {
            return false;
        }
        Iterator<Boolean> iterator = b.digits.iterator();
        for(boolean digit : this.digits) {
            if(digit != iterator.next()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts this Binary to a printable format
     * @return this Binary as a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.digits.forEach(digit -> builder.insert(0, digit ? 1 : 0));
        return builder.toString();
    }

    /**
     * Prints this Binary
     */
    public void print() {
        System.out.println(this);
    }
}
