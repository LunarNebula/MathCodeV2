package Enumerator;

import Exception.IllegalDimensionException;
import Exception.ExceptionMessage.TargetedMessage;

public class RadixTest implements Comparable<RadixTest> {
    private boolean zeroFlag, signFlag; //zeroFlag is true when this value is 0; signFlag is true when this value is negative
    private int[] digits;
    private int base;
    private final int DEFAULT_BASE = 10;

    /**
     * Creates a new RadixTest using specified inputs from its parent class. This method is not designed to
     * detect number format issues and is thus made private, for use only by this class.
     * @param digits the array of digits used for this number
     * @param base the base of this Radix
     * @param zeroFlag true if this Radix is zero, else false
     * @param signFlag necessarily true if this Radix is negative
     */
    private RadixTest(int[] digits, int base, boolean zeroFlag, boolean signFlag) {
        formulate(digits, base, zeroFlag, signFlag);
    }

    /**
     * Creates a new {@code RadixTest} in a given base with value zero
     * @param base the target base
     * @return the zero {@code RadixTest}
     */
    private RadixTest ZERO(int base) {
        return new RadixTest(new int[]{}, base, true, false);
    }

    /**
     * Creates a new {@code RadixTest} in this base with value zero in a given base
     * @param base the target base
     * @return the zero {@code RadixTest}
     */
    private RadixTest ONE(int base) {
        return new RadixTest(new int[]{1}, base, false, false);
    }

    /**
     * Creates a new RadixTest in base 10
     * @param value the base 10 representation of this RadixTest
     * @throws NumberFormatException if the value is an invalid String representation of a RadixTest in base 10
     */
    public RadixTest(String value) throws NumberFormatException {
        formulate(value, DEFAULT_BASE);
    }

    /**
     * Creates a new RadixTest in a specific base
     * @param value the String representation of this RadixTest
     * @param base the base of this RadixTest
     * @throws NumberFormatException if the value is an invalid String representation of a RadixTest in the given base
     */
    public RadixTest(String value, int base) throws NumberFormatException {
        formulate(value, base);
    }

    /**
     * Formulates this RadixTest using inputs from another RadixTest
     * @param digits the array of digits
     * @param base the base of this RadixTest
     * @param zeroFlag true if this RadixTest is zero, else false
     * @param signFlag necessarily true if this RadixTest is negative
     */
    private void formulate(int[] digits, int base, boolean zeroFlag, boolean signFlag) {
        this.digits = digits;
        this.base = base;
        this.zeroFlag = zeroFlag;
        this.signFlag = signFlag;
    }

    /**
     * Formulates this RadixTest based on the String input
     * @param value the String value of the RadixTest numeral
     * @param base the base of the RadixTest
     * @throws NumberFormatException when any of the following occur:
     * <ul>
     *      <li><code>@base</code> is less than 2
     *      <li><code>@value</code> contains characters other than digits. Possible exceptions are a single negative
     *      sign as the first character for negative values and/or open/closed brackets for digits > 9
     *      <li><code>@value</code> consists only of a single negative sign
     *      <li><code>@value</code> contains digits greater than or equal to <code>@base</code>
     *      <li><code>@value</code> attempts to initiate a multi-digit input when the setting is already on
     *      (consecutive open brackets)
     *      <li><code>@value</code> attempts to terminate a multi-digit input when the setting is already off
     *      (consecutive closed brackets)
     *      <li><code>@value</code> does not terminate a multi-digit input at the end of the string
     * </ul>
     * Note that an exception is not thrown if a single-digit input exists in bracket notation.
     */
    private void formulate(String value, int base) throws NumberFormatException {
        TargetedMessage.verifyBase(base);
        boolean zeroFlag = true, signFlag = false;
        if(value.startsWith("-")) {
            if(value.length() == 1) {
                throw new NumberFormatException();
            }
            value = value.substring(1);
            signFlag = true;
        }
        int startingZeroIndex = 0;
        while(startingZeroIndex < value.length() && value.charAt(startingZeroIndex) == '0') {
            startingZeroIndex++;
        }
        value = value.substring(startingZeroIndex);
        final int[] initialArray = new int[value.length()];
        int radixLength = 0, digit = 0;
        boolean isContainedInMultiDigitValue = false;
        for(char c : value.toCharArray()) {
            if(c == '[' && (! isContainedInMultiDigitValue)) {
                isContainedInMultiDigitValue = true;
            } else if(c == ']' && isContainedInMultiDigitValue) {
                initialArray[radixLength] = digit;
                digit = 0;
                radixLength++;
                isContainedInMultiDigitValue = false;
            } else if(Character.isDigit(c)) {
                int subDigit = c - '0';
                if(isContainedInMultiDigitValue) {
                    digit = digit * DEFAULT_BASE + subDigit;
                    if(digit >= base) {
                        throw new NumberFormatException();
                    }
                } else {
                    if(subDigit >= base) {
                        throw new NumberFormatException();
                    } else if(zeroFlag && subDigit != 0) {
                        zeroFlag = false;
                    }
                    initialArray[radixLength] = subDigit;
                    radixLength++;
                }
            } else {
                throw new NumberFormatException();
            }
        }
        if(isContainedInMultiDigitValue) {
            throw new NumberFormatException();
        }
        int[] digits = new int[radixLength];
        for(int i = 0; i <= radixLength; i++) {
            radixLength--;
            digits[i] = initialArray[radixLength];
            digits[radixLength] = initialArray[i];
        }
        formulate(digits, base, zeroFlag, signFlag);
    }

    /**
     * Strips leading zeroes from this RadixTest
     * @param requiresSingleCheck true if the parent operation requires only checking the first digit, else false
     */
    private void stripLeadingZeroes(boolean requiresSingleCheck) {
        int finalIndex = this.digits.length - 1;
        if(requiresSingleCheck) {
            if(this.digits[finalIndex] == 0) {
                finalIndex--;
            }
        } else {
            while(finalIndex >= 0 && this.digits[finalIndex] == 0) {
                finalIndex--;
            }
        }
        final int length = finalIndex + 1;
        final int[] digits = new int[length];
        System.arraycopy(this.digits, 0, digits, 0, length);
        this.digits = digits;
    }

    /**
     * Converts this RadixTest into a new RadixTest with the same value and specified base
     * @param base the new target base
     * @return the converted RadixTest
     * @throws NumberFormatException if the base is less than 2
     */
    public RadixTest convertToBase(int base) throws NumberFormatException {
        TargetedMessage.verifyBase(base);
        if(this.zeroFlag) {
            return ZERO(base);
        }
        int prevBase = this.base, testPrevBase = prevBase, count = 1;
        if(testPrevBase > base) {
            count = 2;
            do {
                testPrevBase /= base;
                count++;
            } while(testPrevBase > base);
        }
        int[] iterator = new int[this.digits.length], initialDigits = new int[iterator.length * count];
        int index = 0, endIndex = iterator.length;
        for(int i = 0; i <= endIndex; i++) {
            endIndex--;
            iterator[endIndex] = this.digits[i];
            iterator[i] = this.digits[endIndex];
        }
        endIndex = iterator.length;
        while(iterator[0] > 0) {
            int carry = 0, proxy = 0;
            boolean isRegisteringDigits = false;
            int[] proxyIterator = new int[iterator.length];
            for(int i = 0; i < endIndex; i++) {
                carry = carry * prevBase + iterator[i];
                if(isRegisteringDigits || carry >= base) {
                    proxyIterator[proxy] = carry / base;
                    carry -= proxyIterator[proxy] * base;
                    isRegisteringDigits = true;
                    proxy++;
                }
            }
            endIndex = proxy;
            iterator = proxyIterator;
            initialDigits[index] = carry;
            index++;
        }
        int[] digits = new int[index];
        System.arraycopy(initialDigits, 0, digits, 0, index);
        return new RadixTest(digits, base, false, this.signFlag);
    }
    //TODO: rewrite all class methods to *not* use for-each loops

    /**
     * Converts this RadixTest into a new RadixTest with the same value and specified base using Horner's method
     * @param base the new target base
     * @return the converted RadixTest
     * @throws NumberFormatException if the base is less than 2
     */
    public RadixTest convertToBaseHorner(int base) throws NumberFormatException {
        TargetedMessage.verifyBase(base);
        if(this.zeroFlag) {
            return ZERO(base);
        }
        int prevBase = this.base, testPrevBase = prevBase, count = 1;
        if(testPrevBase > base) {
            count = 2;
            do {
                testPrevBase /= base;
                count++;
            } while(testPrevBase > base);
        }
        int[] originalDigits = this.digits, initialDigits = new int[prevBase * count];
        int endIndex = 0;
        for(int i = originalDigits.length - 1; i >= 0; i--) {
//            int carry = 0;
//            for(int j = 0; j < endIndex; j++) {
//                if(carry < base) {
//                    initialDigits[j] = initialDigits[j] * prevBase + carry;
//                } else {
//                    int nextCarry = carry / base;
//                    carry = initialDigits[j] / base;
//                    initialDigits[j] -= carry * base;
//                }
//            }
//            carry += originalDigits[i];
//            while(carry > 0) {
//                int
//                initialDigits[endIndex] = carry %
//            }
        }
        return new RadixTest(digits, base, false, this.signFlag);
    }

    private void printArray(int[] ar) {
        if(ar.length == 0) {
            System.out.println("[]");
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append('[');
            String delimiter = "";
            for(int i : ar) {
                builder.append(delimiter).append(i);
                delimiter = ", ";
            }
            builder.append(']');
            System.out.println(builder.toString());
        }
    }

    /**
     * Adds this RadixTest to another RadixTest
     * @param addend the addend RadixTest
     * @return this + addend
     * @throws NumberFormatException if the base of this RadixTest does not match the base of the addend
     */
    public RadixTest add(RadixTest addend) throws NumberFormatException {
        final int base = this.base;
        verifyBaseMatch(base, addend.base);
        if(this.zeroFlag) {
            return addend;
        } else if(addend.zeroFlag) {
            return this;
        }
        return null; //TODO: write
    }

    /**
     * Subtracts another specified RadixTest from this RadixTest
     * @param subtrahend the subtrahend RadixTest
     * @return this - subtrahend
     * @throws NumberFormatException if the base of this RadixTest does not match the base of the subtrahend
     */
    public RadixTest subtract(RadixTest subtrahend) throws NumberFormatException {
        final int base = this.base;
        verifyBaseMatch(base, subtrahend.base);
        if(this.zeroFlag) {
            return subtrahend.negate();
        } else if(subtrahend.zeroFlag) {
            return this;
        } else if(this.signFlag ^ subtrahend.signFlag) {
            return add(subtrahend.negate());
        }
        return add(subtrahend.negate()); //TODO: rewrite for differently-signed values
    }

    /**
     * Adds two RadixTests in a given base. This method assumes specific characteristics about the parameter arrays,
     * so it is designed only for use by other methods in this class
     * @param addend1 the first addend
     * @param addend2 the second addend
     * @param base the given base of the sum operation
     * @param negate true if the second value should be negated to model subtraction, else false
     * @return the un-cropped sum of the two addend RadixTests
     */
    private int[] add(int[] addend1, int[] addend2, int base, boolean negate) {
        final int[] sum = new int[addend1.length];
        boolean carry = false;
        final int LENGTH = sum.length;
        for(int i = 0; i < LENGTH; i++) {
            int digit = addend1[i] + (negate ? -addend2[i] : addend2[i]);
            if(carry) {
                digit++;
            }
            carry = (digit >= base);
            if(carry) {
                digit -= base;
            }
            sum[i] = digit;
        }
        return sum;
    }

    /**
     * Scales this RadixTest by an int value
     * @param n the scalar value
     * @return this * n
     * @throws NumberFormatException if the scalar value is greater than or equal to the base
     */
    public RadixTest scale(int n) throws NumberFormatException {
        final int base = this.base;
        if(n >= base) {
            throw new NumberFormatException();
        }
        if(this.zeroFlag || n == 0) {
            return ZERO(base);
        }
        boolean signFlag = this.signFlag;
        if(n < 0) {
            signFlag = ! signFlag;
            n = -n;
        }
        int[] digits, initialDigits = new int[this.digits.length + 1];
        int index = 0, carry = 0;
        for(int thisDigit : this.digits) {
            int digit = thisDigit * n + carry;
            if(digit >= base) {
                carry = digit / base;
                digit -= carry * base;
            }
            initialDigits[index] = digit;
            index++;
        }
        if(carry > 0) {
            initialDigits[index] = carry;
            digits = initialDigits;
        } else {
            digits = new int[initialDigits.length - 1];
            System.arraycopy(initialDigits, 0, digits, 0, digits.length);
        }
        return new RadixTest(digits, base, false, signFlag);
    }

    /**
     * Multiplies this RadixTest by another specified RadixTest
     * @param multiplicand the multiplicand RadixTest
     * @return {@code this * multiplicand}
     * @throws NumberFormatException if the base of this RadixTest is not equal to the base of the multiplicand
     */
    public RadixTest multiply(RadixTest multiplicand) throws NumberFormatException {
        final int base = this.base;
        verifyBaseMatch(base, multiplicand.base);
        if(this.zeroFlag || multiplicand.zeroFlag) {
            return ZERO(base);
        }
        int[] initialDigits = multiply(this.digits, multiplicand.digits, base), digits;
        int finalIndex = initialDigits.length - 1;
        if(initialDigits[finalIndex] == 0) {
            digits = new int[finalIndex];
            System.arraycopy(initialDigits, 0, digits, 0, finalIndex);
        } else {
            digits = initialDigits;
        }
        return new RadixTest(digits, base, false, this.signFlag ^ multiplicand.signFlag);
    }

    /**
     * Squares this RadixTest.
     * @return {@code this^2}, or {@code this * this}
     */
    public RadixTest square() {
        final int base = this.base;
        if(this.zeroFlag) {
            return this;
        }
        int[] initialDigits = square(this.digits, base), digits;
        int finalIndex = initialDigits.length - 1;
        if(initialDigits[finalIndex] == 0) {
            digits = new int[finalIndex];
            System.arraycopy(initialDigits, 0, digits, 0, finalIndex);
        } else {
            digits = initialDigits;
        }
        return new RadixTest(digits, base, false, false);
    }

    /**
     * Raises this RadixTest to a specified integer power
     * @param pow the target power
     * @return this ^ pow
     * @throws ArithmeticException if the computation 0^n for n<=0 is attempted
     */
    public RadixTest pow(int pow) throws ArithmeticException {
        final int base = this.base;
        if(this.zeroFlag) {
            if(pow <= 0) {
                throw new ArithmeticException();
            }
            return this;
        } else if(pow == 0) {
            return ONE(base);
        }else if(this.digits.length == 1 && this.digits[0] == 1) { // if this is 1 or -1
            return (this.signFlag && ((pow & 1) == 1)) ? this.negate() : this;
        } else if(pow < 0) {
            return ZERO(base);
        }
        boolean[] binaryPow = new boolean[Integer.SIZE];
        int binaryIndex = 0;
        while(pow != 1) {
            binaryPow[binaryIndex] = ((pow & 1) == 1);
            pow >>>= 1;
            binaryIndex++;
        }
        int[] antilogarithm = this.digits, initialDigits = this.digits;
        while(binaryIndex > 0) {
            binaryIndex--;
            antilogarithm = square(antilogarithm, base);
            if(binaryPow[binaryIndex]) {
                antilogarithm = multiply(antilogarithm, initialDigits, base);
            }
        }
        int finalIndex = antilogarithm.length - 1;
        while(antilogarithm[finalIndex] == 0) {
            finalIndex--;
        }
        final int[] digits = new int[finalIndex + 1];
        System.arraycopy(antilogarithm, 0, digits, 0, digits.length);
        return new RadixTest(digits, base, false, this.signFlag && binaryPow[0]);
    }

    /**
     * Multiplies two numbers represented by two integer arrays and inserts the resulting digits into a new array
     * @param multiplier the first "number" array to be multiplied
     * @param multiplicand the second "number" array to be multiplied
     * @param base the base in which multiplication occurs
     * @return the final location of the multiplication digits
     */
    private int[] multiply(int[] multiplier, int[] multiplicand, int base) {
        final int[] product = new int[multiplier.length + multiplicand.length];
        int index1 = 0;
        for(int digit1 : multiplier) {
            int index2 = index1, carry = 0;
            for(int digit2 : multiplicand) {
                int digit = product[index2] + digit1 * digit2 + carry;
                if(digit < base) {
                    carry = 0;
                } else {
                    carry = digit / base;
                    digit -= carry * base;
                }
                product[index2] = digit;
                index2++;
            }
            if(carry > 0) {
                product[index2] = carry;
            }
            index1++;
        }
        return product;
    }

    /**
     * Computes the square of a number represented by an integer array and inserts the digits into a new array.
     * @param root the number array to be squared
     * @param base the base in which multiplication occurs
     * @return the digit array representing root * root
     */
    private int[] square(int[] root, int base) {
        final int[] square = new int[root.length << 1];
        for(int i = 0; i < root.length; i++) {
            final int nextIndex = i << 1;
            final int nextIndexIncrement = nextIndex | 1;
            final int digitProduct = root[i] * root[i];
            square[nextIndexIncrement] = digitProduct / base;
            square[nextIndex] = digitProduct - square[nextIndexIncrement] * base;
        }
        for(int i = 0; i < root.length; i++) {
            int carry = 0;
            for(int j = i + 1; j < root.length; j++) {
                final int nextIndex = i + j;
                int digit = square[nextIndex] + ((root[i] * root[j]) << 1) + carry;
                if(digit < base) {
                    carry = 0;
                } else {
                    carry = digit / base;
                    digit -= carry * base;
                }
                square[nextIndex] = digit;
            }
            square[i + root.length] += carry;
        }
        return square;
    }

    /**
     * Negates this RadixTest
     * @return this * -1
     */
    public RadixTest negate() {
        return new RadixTest(this.digits, this.base, this.zeroFlag, ! this.signFlag);
    }

    /**
     * Finds the parity of this RadixTest
     * @return true if the parity is even, else false
     */
    public boolean parity() {
        if(this.zeroFlag) {
            return true;
        } else if((this.base & 1) == 0) {
            return (this.digits[0] & 1) == 0;
        }
        int parity = 0;
        for(int digit : this.digits) {
            parity ^= digit;
        }
        return (parity & 1) == 0;
    }

    /**
     * Determines whether two bases match
     * @param base1 the first base
     * @param base2 the second base
     * @throws NumberFormatException if base1 != base2
     */
    private void verifyBaseMatch(int base1, int base2) throws NumberFormatException {
        if(base1 != base2) {
            throw new IllegalDimensionException(TargetedMessage.INCORRECT_BASE(base));
        }
    }

    /**
     * Compares this RadixTest to another specified comparator RadixTest
     * @param o the comparator RadixTest
     * @return one of the following:
     * <ul>
     * <li>{@code 1} if {@code this > 0}
     * <li>{@code 0} if {@code this == 0}
     * <li>{@code -1} if {@code this < 0}
     * </ul>
     */
    @Override
    public int compareTo(RadixTest o) {
        verifyBaseMatch(this.base, o.base);
        if(this.zeroFlag && o.zeroFlag) {
            return 0;
        } else if(this.digits.length > o.digits.length) {
            return this.signFlag ? -1 : 1;
        } else if(this.digits.length != o.digits.length) {
            return this.signFlag ? 1 : -1;
        }
        final int compare1 = this.zeroFlag ? 0 : (this.signFlag ? -1 : 1);
        final int compare2 = o.zeroFlag ? 0 : (o.signFlag ? -1 : 1);
        if(compare1 != compare2) {
            return compare1 > compare2 ? 1 : -1;
        }
        int index = this.digits.length;
        while(index > 0) {
            index--;
            if(this.digits[index] > o.digits[index]) {
                return 1;
            } else if(this.digits[index] != o.digits[index]) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Determines whether this RadixTest is equal to a specified Object
     * @param o the comparator Object
     * @return true if o is a RadixTest with equivalent digits, flags, and base, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof final RadixTest r)) {
            return false;
        }
        if(this.base != r.base || (this.zeroFlag ^ r.zeroFlag) || this.digits.length != r.digits.length) {
            return false;
        } else if(this.zeroFlag) {
            return true;
        } else if(this.signFlag ^ r.signFlag) {
            return false;
        }
        for(int i = 0; i < this.digits.length; i++) {
            if(this.digits[i] != r.digits[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the hashCode of this RadixTest
     * @return an int identifier for this RadixTest
     */
    @Override
    public int hashCode() {
        int hashCode = 0, base = this.base;
        if(! this.zeroFlag) {
            int index = this.digits.length;
            while (index > 0) {
                index--;
                hashCode = hashCode * base + this.digits[index];
            }
        }
        return this.signFlag ? -hashCode : hashCode;
    }

    /**
     * Converts this RadixTest to a printable format
     * @return this RadixTest as a String
     */
    @Override
    public String toString() {
        if(this.zeroFlag) {
            return "0";
        }
        StringBuilder builder = new StringBuilder();
        if(this.signFlag) {
            builder.append('-');
        }
        int index = this.digits.length;
        while(index > 0) {
            index--;
            final int digit = this.digits[index];
            if(digit < base) {
                builder.append(digit);
            } else {
                builder.append('[').append(digit).append(']');
            }
        }
        return builder.toString();
    }

    /**
     * Prints this RadixTest
     */
    public void print() {
        System.out.println(this);
    }
}
