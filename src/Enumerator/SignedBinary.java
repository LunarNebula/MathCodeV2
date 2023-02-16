package Enumerator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SignedBinary extends Binary {
    private int capacity;

    /**
     * Makes a new SignedBinary
     * @param value the initial value of this SignedBinary
     * @param capacity the length after which the SignedBinary cycles
     */
    public SignedBinary(int value, int capacity) {
        super(value);
        setCapacity(capacity);
        cycle();
    }

    /**
     * Sets the cycle length of this SignedBinary
     * @param capacity the new cycle length
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the length of the cycle in this SignedBinary
     * @return this.cycleLength
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Adds one to this SignedBinary and cycles if the number exceeds the given bound
     */
    @Override
    public void increment() {
        super.increment();
        cycle();
    }

    /**
     * Forces this SignedBinary to cycle if its length exceeds the given bound
     */
    private void cycle() {
        if(this.digits.size() > this.capacity) {
            this.digits.clear();
            this.digits.add(false);
        }
    }

    /**
     * Returns the digits
     * @return this.digits as an Integer list
     */
    @Override
    public List<Integer> getDigits() {
        List<Integer> digits = new LinkedList<>();
        ListIterator<Boolean> iterator = this.digits.listIterator();
        for(int i = 0; i < this.capacity; i++) {
            digits.add((iterator.hasNext() && iterator.next()) ? 1 : 0);
        }
        return digits;
    }

    /**
     * Returns the original digits of this SignedBinary
     * @return this.digits with extra concatenated digits
     */
    @Override
    public List<Boolean> getBinary() {
        List<Boolean> list = new LinkedList<>(this.digits);
        while(list.size() < this.capacity) {
            list.add(false);
        }
        return list;
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
                hashCode++;
            }
        }
        return hashCode;
    }

    /**
     * Converts this SignedBinary to a printable format
     * @return this SignedBinary as a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        ListIterator<Boolean> iterator = this.digits.listIterator();
        for(int i = 0; i < this.capacity; i++) {
            builder.insert(0, (iterator.hasNext() && iterator.next()) ? 1 : 0);
        }
        return builder.toString();
    }

    /**
     * Prints this SignedBinary
     */
    public void print() {
        System.out.println(this);
    }
}
