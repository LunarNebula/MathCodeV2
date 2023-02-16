package Enumerator;

import Exception.ExceptionMessage.TargetedMessage;

import java.util.*;

public class Radix implements Comparable<Radix> {
    private List<Integer> digits;
    private int base, sign;
    private final int DEFAULT_BASE = 10;

    /**
     * Constructs a base-10 Radix with the given value
     * @param value the default value of the Radix
     */
    public Radix(String value) {
        try {
            formulate(value, DEFAULT_BASE);
        } catch (NumberFormatException nfe) {
            formulate("0", DEFAULT_BASE);
        }
    }

    /**
     * Constructs a Radix with the given value and base. Note that the input String value is assumed to
     * already exist in the input base - no conversion occurs in the constructor
     * @param value the default value of the Radix
     * @param base the default base of the Radix
     */
    public Radix(String value, int base) {
        if(value.length() == 0) {
            this.digits = new LinkedList<>();
            this.base = base;
            this.sign = 0;
        } else {
            try {
                TargetedMessage.verifyBase(base);
                formulate(value, base);
            } catch (NumberFormatException nfe) {
                formulate("0", base);
            }
        }
    }

    /**
     * Sets the value and base of this Radix object
     * @param value the value of the number stored in this Radix
     * @param base the base of the number stored in this Radix
     */
    private void formulate(String value, int base) {
        int sign = 1;
        this.base = base;
        this.digits = new LinkedList<>();
        if(value.length() > 1) {
            if(value.charAt(0) == '-') {
                value = value.substring(1);
                sign = -1;
            }
        }
        this.sign = 0;
        int digit = 0;
        boolean isContained = false;
        for(Character ch : value.toCharArray()) {
            if(Character.isDigit(ch)) {
                int nextDigit = ch - '0';
                if(isContained) {
                    digit = digit * DEFAULT_BASE + nextDigit;   // digits are stored in base 10
                } else {
                    if(nextDigit >= base) {
                        throw new NumberFormatException();
                    }
                    this.digits.add(0, nextDigit);
                    if(nextDigit > 0) {
                        this.sign = sign;
                    }
                }
            } else {
                switch (ch) {
                    case '[' -> {
                        if (isContained) {
                            throw new NumberFormatException();
                        }
                        isContained = true;
                    }
                    case ']' -> {
                        if (! isContained || digit >= base) {
                            throw new NumberFormatException();
                        }
                        this.digits.add(0, digit);
                        digit = 0;
                        isContained = false;
                    }
                    default -> throw new NumberFormatException();
                }
            }
        }
        crop();
    }

    /**
     * Crops all leading zeros from this Radix
     */
    private void crop() {
        Iterator<Integer> digitIterator = ((LinkedList<Integer>) this.digits).descendingIterator();
        boolean continueDigitRemoval = true;
        while(continueDigitRemoval && digitIterator.hasNext()) {
            if(digitIterator.next() == 0) {
                digitIterator.remove();
            } else {
                continueDigitRemoval = false;
            }
        }
        if(this.digits.size() == 0) {
            this.digits.add(0, 0);
            this.sign = 0;
        }
    }

    /**
     * Converts this Radix object into a number with a different base but the same value
     * @param base the base of the new Radix
     * @return the converted Radix object
     */
    public Radix convertToBase(int base) {
        if(this.base == base) {
            Radix deepCopy = new Radix("", this.base);
            deepCopy.digits.addAll(this.digits);
            deepCopy.sign = this.sign;
            return deepCopy;
        }
        Radix newRadix = new Radix("", base), test = new Radix("", this.base);
        List<Integer> reverseDigits = new LinkedList<>();
        for(Integer digit : this.digits) {
            reverseDigits.add(0, digit);
        }
        test.digits = reverseDigits;
        while(test.digits.size() > 0) {
            List<Integer> concurrentDigits = new LinkedList<>();
            int carry = 0;
            for(Integer digit : test.digits) {
                carry = carry * this.base + digit;
                int nextDigit = carry / base;
                concurrentDigits.add(nextDigit);
                carry -= nextDigit * base;
            }
            boolean cropDigits = true;
            while(cropDigits) {
                if(concurrentDigits.size() == 0) {
                    cropDigits = false;
                } else if(concurrentDigits.get(0) != 0) {
                    cropDigits = false;
                } else {
                    concurrentDigits.remove(0);
                }
            }
            if(test.digits.size() > 0 || carry != 0) {
                newRadix.digits.add(carry);
            }
            test.digits = concurrentDigits;
        }
        newRadix.sign = this.sign;
        return newRadix;
    }

    /**
     * Shifts the exponent of this Radix value with respect to the base
     * @param pow the power of the Radix base
     * @return this Radix object multiplied by some power of this.base
     */
    public Radix shiftExponent(int pow) {
        Radix shift = new Radix(toString(), this.base);
        for(int i = 0; i < pow; i++) {
            shift.digits.add(0, 0);
        }
        for(int i = pow; i < 0; i++) {
            shift.digits.remove(0);
        }
        shift.sign = this.sign;
        shift.crop();
        return shift;
    }

    /**
     * Adds a Radix object to this Radix, requiring congruent bases
     * @param addend the addend Radix
     * @return the sum of the addend Radix and this
     */
    public Radix add(Radix addend) {
        if(this.base != addend.base) {
            return null;
        }
        if(this.sign * addend.sign < 0) {
            return setSign(1).subtract(addend.setSign(1)).setSign(abs().compareTo(addend.abs()) > 0 ? this.sign : addend.sign);
        }
        int compareTo = compareTo(addend), sign = compareTo * compareTo;
        if(this.sign == addend.sign) {
            sign = this.sign;
        }
        if(this.sign * addend.sign == 0) {
            sign = this.sign == 0 ? addend.sign : this.sign;
        }
        ListIterator<Integer> addendListIterator = addend.digits.listIterator();
        Radix sum = new Radix("", this.base);
        int carry = 0;
        for(int digit : this.digits) {
            carry += digit;
            if(addendListIterator.hasNext()) {
                carry += addendListIterator.next();
            }
            sum.digits.add(carry % this.base);
            carry /= this.base;
        }
        while(addendListIterator.hasNext()) {
            carry += addendListIterator.next();
            sum.digits.add(carry % this.base);
            carry /= this.base;
        }
        sum.digits.add(carry);
        sum.crop();
        return sum.setSign(sign);
    }

    /**
     * Subtracts a Radix from this Radix, requiring congruent bases
     * @param subtrahend the subtrahend Radix
     * @return the difference between this Radix and the subtrahend
     */
    public Radix subtract(Radix subtrahend) {
        if(this.base != subtrahend.base) {
            return null;
        }
        if(subtrahend.sign == -1 && this.sign == -1) {
            return subtrahend.setSign(1).subtract(setSign(1));
        }
        if(this.sign * subtrahend.sign < 0) {
            return setSign(1).add(subtrahend.setSign(1)).setSign(abs().compareTo(subtrahend.abs()) > 0 ? this.sign : subtrahend.sign);
        }
        int compareTo = compareTo(subtrahend);
        if(compareTo <= 0) {
            if(compareTo == 0) {
                return new Radix("0", this.base);
            } else {
                return subtrahend.subtract(this).setSign(-1);
            }
        }
        Radix difference = new Radix("", this.base);
        int carry = 0;
        ListIterator<Integer> thisDigits = this.digits.listIterator(), subtrahendDigits = subtrahend.digits.listIterator();
        while(thisDigits.hasNext()) {
            int num = thisDigits.next() - carry;
            if(subtrahendDigits.hasNext()) {
                num -= subtrahendDigits.next();
            }
            if(num < 0) {
                num += base;
                carry = 1;
            } else {
                carry = 0;
            }
            difference.digits.add(num);
        }
        difference.crop();
        return difference.setSign(1);
    }

    /**
     * Multiplies this Radix by another Radix with the same base
     * @param multiplicand the multiplicand Radix
     * @return the product of the factor Radix and this
     */
    public Radix multiply(Radix multiplicand) {
        if(this.base != multiplicand.base) {
            return null;
        }
        int[] digits = new int[this.digits.size() + multiplicand.digits.size()];
        int offset = 0;
        for(int thisDigit : this.digits) {
            int secondaryOffset = offset;
            for(int multiplicandDigit : multiplicand.digits) {
                digits[secondaryOffset] += thisDigit * multiplicandDigit;
                digits[secondaryOffset + 1] += digits[secondaryOffset] / this.base;
                digits[secondaryOffset] = digits[secondaryOffset] % this.base;
                secondaryOffset++;
            }
            offset++;
        }
        Radix product = new Radix("", this.base);
        for(int digit : digits) {
            product.digits.add(digit);
        }
        product.crop();
        return product.setSign(this.sign * multiplicand.sign);
    }

    /**
     * Finds the GCD of two Radix numbers
     * @param product the second product Radix
     * @return the Radix N of greatest magnitude that divides this Radix and the product Radix
     */
    public Radix gcd(Radix product) {
        if(this.base != product.base) {
            return null;
        }
        Radix testThis = new Radix("", this.base), testProduct = new Radix("", product.base);
        testThis.digits.addAll(this.digits);
        testThis.sign = this.sign;
        testProduct.digits.addAll(product.digits);
        testProduct.sign = product.sign;
        while(testProduct.sign != 0) {
            if(testProduct.abs().compareTo(testThis.abs()) > 0) {
                Radix switcher = testProduct;
                testProduct = testThis;
                testThis = switcher;
            } else {
                testThis = testThis.mod(testProduct);
            }
        }
        return testThis;
    }

    /**
     * Finds the value of this Radix raised to an Integer power
     * @param pow the exponent of this Radix base
     * @return this Radix raised to the power of the parameter Integer
     */
    public Radix pow(int pow) {
        Radix antilogarithm = new Radix("1", this.base);
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
        return antilogarithm;
    }

    /**
     * Finds the remainder when this raised to an integer power is divided by a Radix divisor
     * @param pow the exponent
     * @param divisor the Radix divisor
     * @return the remainder when this ^ pow is divided by the divisor
     */
    public Radix modPow(int pow, Radix divisor) {
        List<Boolean> powers = new LinkedList<>();
        while(pow != 0) {
            powers.add(0, (pow & 1) == 1);
            pow >>>= 1;
        }
        Radix modulus = new Radix("1", this.base);
        for(boolean instruction : powers) {
            modulus = modulus.multiply(modulus);
            if(instruction) {
                modulus = modulus.multiply(this);
            }
            modulus = modulus.mod(divisor);
        }
        return modulus;
    }

    /**
     * Finds the quotient and remainder when dividing this by another Radix
     * @param divisor the divisor of this
     * @return a Radix array with the quotient at index 0 and remainder at index 1
     */
    public Radix[] divideAndRemainder(Radix divisor) {
        if(this.base != divisor.base) {
            return new Radix[]{null, null};
        }
        List<Integer> digits = new LinkedList<>();
        for(Integer digit : this.digits) {
            digits.add(0, digit);
        }
        Radix test = new Radix("", this.base);
        Radix sum = new Radix("0", this.base);
        Radix quotient = new Radix("", this.base);
        quotient.sign = this.sign * divisor.sign;
        test.sign = quotient.sign;
        sum.sign = test.sign;
        for(Integer digit : digits) {
            test.digits.add(0, digit);
            sum = sum.shiftExponent(1);
            int value = 0;
            boolean attachDigits = true;
            while(attachDigits) {
                Radix testSum = sum.add(divisor);
                if(testSum.compareTo(test) > 0) {
                    attachDigits = false;
                } else {
                    sum = testSum;
                    value++;
                }
            }
            if(quotient.digits.size() > 0 || value != 0) {
                quotient.digits.add(0, value);
            }
        }
        if(quotient.digits.size() == 0) {
            quotient = new Radix("0", this.base);
        }
        return new Radix[]{quotient, subtract(sum)};
    }

    public Radix[] divideAndRemainderTest(Radix divisor) {
        if(this.base != divisor.base) {
            return new Radix[]{null, null};
        }
        List<Integer> digits = new LinkedList<>();
        for(Integer digit : this.digits) {
            digits.add(0, digit);
        }
        Radix test = new Radix("", this.base);
        Radix sum = new Radix("0", this.base);
        Radix quotient = new Radix("", this.base);
        quotient.sign = this.sign * divisor.sign;
        test.sign = quotient.sign;
        sum.sign = test.sign;
        for(Integer digit : digits) {
            test.digits.add(0, digit);
            sum.digits.add(0, 0);
            if(test.digits.size() == sum.digits.size()) {
                Iterator<Integer> testDescendingIterator = ((LinkedList<Integer>) test.digits).descendingIterator();
                Iterator<Integer> sumDescendingIterator = ((LinkedList<Integer>) sum.digits).descendingIterator();
                boolean continueSimplification = true;
                while(continueSimplification) {
                    int nextTest = testDescendingIterator.next(), sumTest = sumDescendingIterator.next();
                    if(nextTest == sumTest && sumDescendingIterator.hasNext() && testDescendingIterator.hasNext()) {
                        testDescendingIterator.remove();
                        sumDescendingIterator.remove();
                    } else {
                        continueSimplification = false;
                    }
                }
            }
            int value = 0;
            boolean attachDigits = true;
            while(attachDigits) {
                Radix testSum = sum.add(divisor);
                if(testSum.compareTo(test) > 0) {
                    attachDigits = false;
                } else {
                    sum = testSum;
                    value++;
                }
            }
            if(quotient.digits.size() > 0 || value != 0) {
                quotient.digits.add(0, value);
            }
        }
        if(quotient.digits.size() == 0) {
            quotient = new Radix("0", this.base);
        }
        return new Radix[]{quotient, test.subtract(sum)};
    }

    /**
     * Divides this Radix by another Radix
     * @param divisor the divisor of this Radix
     * @return the quotient of this Radix and the divisor
     */
    public Radix divide(Radix divisor) {
        return divideAndRemainder(divisor)[0];
    }

    /**
     * Finds the remainder when this Radix is divided by another Radix
     * @param divisor the divisor of this Radix
     * @return the remainder when this Radix is divided by the divisor
     */
    public Radix mod(Radix divisor) {
        return divideAndRemainder(divisor)[1];
    }

    /**
     * Finds the square root of this Radix
     * @return the unique Radix r such that r^2 <= this and (r + 1)^2 > this
     */
    public Radix sqrt() {
        final Radix TWO_MEAN_COMPONENT = new Radix("2").convertToBase(this.base);
        Radix sqrt = new Radix("1", this.base), proxy = new Radix("0", this.base), backProxy = proxy;
        while(! sqrt.equals(backProxy)) {
            backProxy = proxy;
            proxy = sqrt;
            sqrt = sqrt.add(divide(sqrt)).divide(TWO_MEAN_COMPONENT);
        }
        return sqrt.min(proxy);
    }

    /**
     * Finds the nth root of this Radix
     * @param pow the exponent of the base
     * @return the unique Radix r such that r ^ pow <= this and (r + 1) ^ pow > this
     */
    public Radix nthRoot(int pow) {
        final Radix TWO_MEAN_COMPONENT = new Radix("2").convertToBase(this.base);
        Radix nthRoot = new Radix("1", this.base), proxy = new Radix("0", this.base), backProxy = proxy;
        while(! nthRoot.equals(backProxy)) {
            backProxy = proxy;
            proxy = nthRoot;
            nthRoot = nthRoot.add(divide(nthRoot.pow(pow - 1))).divide(TWO_MEAN_COMPONENT);
        }
        return nthRoot.min(proxy);
    }

    /**
     * Finds all prime factors of this Radix
     * @return all Radices r such that r | this and r is prime
     */
    public List<Radix> primeFactors() {
        List<Radix> primeFactors = new LinkedList<>();
        Radix test = abs();
        final Radix ZERO = new Radix("0", this.base);
        final Radix TWO = new Radix("2").convertToBase(this.base);
        while(test.parity() == 0) {
            primeFactors.add(TWO);
            test = test.divide(TWO);
        }
        Radix sqrt = test.sqrt();
        for(Radix i = new Radix("3").convertToBase(this.base); i.compareTo(sqrt) <= 0; i = i.add(TWO)) {
            boolean checkIfDivisible = true, refreshSqrt = false;;
            Radix[] divideAndRemainder = test.divideAndRemainder(i);
            if(divideAndRemainder[1].equals(ZERO)) {
                refreshSqrt = true;
                primeFactors.add(i);
                test = divideAndRemainder[0];
            } else {
                checkIfDivisible = false;
            }
            while(checkIfDivisible) {
                divideAndRemainder = test.divideAndRemainder(i);
                if(divideAndRemainder[1].equals(ZERO)) {
                    primeFactors.add(i);
                    test = divideAndRemainder[0];
                } else {
                    checkIfDivisible = false;
                }
            }
            if(refreshSqrt) {
                sqrt = test.sqrt();
            }
        }
        if(! test.equals(new Radix("1", this.base))) {
            primeFactors.add(test);
        }
        return primeFactors;
    }

    /**
     * Finds all factors of this Radix
     * @return all Radices r such that r | this
     */
    public List<Radix> factors() {
        Radix prev = new Radix("1", this.base), product = new Radix("1", this.base);
        List<Radix> factors = new LinkedList<>(), test = new LinkedList<>(), primes = primeFactors();
        factors.add(prev);
        for(Radix primeFactor : primes) {
            if(primeFactor.equals(prev)) {
                product = product.multiply(primeFactor);
            } else {
                factors.addAll(test);
                prev = primeFactor;
                test = new LinkedList<>();
                product = primeFactor;
            }
            for(Radix factor : factors) {
                test.add(factor.multiply(product));
            }
        }
        factors.addAll(test);
        Collections.sort(factors);
        return factors;
    }

    /**
     * Gets the sign function of this Radix
     * @return 1 if this Radix is positive, -1 if this is negative, else 0
     */
    public int sign() {
        return this.sign;
    }

    /**
     * Resets the sign of this Radix
     * @param sign the new sign of this Radix
     * @return a Radix with this value and sign parameter
     */
    private Radix setSign(int sign) {
        Radix newSignedRadix = new Radix("", this.base);
        newSignedRadix.digits.addAll(this.digits);
        newSignedRadix.sign = sign;
        return newSignedRadix;
    }

    /**
     * Negates this Radix
     * @return this Radix multiplied by -1
     */
    public Radix negate() {
        return setSign(-this.sign);
    }

    /**
     * Finds the absolute value of this Radix
     * @return this Radix with sign set to 1
     */
    public Radix abs() {
        return setSign(1);
    }

    /**
     * Finds the parity of this Radix
     * @return 0 if this Radix is even, else 1 if this Radix is odd
     */
    public int parity() {
        if(this.base % 2 == 0) {
            return this.digits.get(0) % 2;
        } else {
            int parity = 0;
            for(int digit : this.digits) {
                parity = (parity + digit) % 2;
            }
            return parity;
        }
    }

    /**
     * Compares this Radix to a Radix with any base
     * @param comparator the comparator Radix
     * @return 1 if this Radix is greater, -1 if the comparator Radix is greater, else 0 if they are equal
     */
    @Override
    public int compareTo(Radix comparator) {
        Radix convertedComparator = comparator.convertToBase(this.base);
        if(this.sign != convertedComparator.sign) {
            return this.sign > convertedComparator.sign ? 1 : -1;
        }
        if(this.digits.size() != convertedComparator.digits.size()) {
            return this.digits.size() > convertedComparator.digits.size() ? this.sign : -this.sign;
        }
        int greaterRadix = 0;
        ListIterator<Integer> comparatorDigitsListIterator = convertedComparator.digits.listIterator();
        for(Integer thisDigit : this.digits) {
            Integer nextComparatorDigit = comparatorDigitsListIterator.next();
            if(thisDigit > nextComparatorDigit) {
                greaterRadix = this.sign;
            } else if(thisDigit < nextComparatorDigit) {
                greaterRadix = -this.sign;
            }
        }
        return greaterRadix;
    }

    /**
     * Compares this Radix with another Radix
     * @param o the comparator Radix
     * @return true if this Radix is equal to the comparator Radix, else false
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Radix && compareTo((Radix) o) == 0;
    }

    /**
     * Finds the maximum of two Radices
     * @param comparator the second Radix
     * @return compares this Radix with the comparator Radix and provides the larger Radix object
     */
    public Radix max(Radix comparator) {
        Radix model = compareTo(comparator) > 0 ? this : comparator;
        Radix maximum = new Radix("", model.base);
        maximum.digits.addAll(model.digits);
        maximum.sign = model.sign;
        return maximum;
    }

    /**
     * Finds the minimum of two Radices
     * @param comparator the second Radix
     * @return compares this Radix with the comparator Radix and provides the smaller Radix object
     */
    public Radix min(Radix comparator) {
        Radix model = compareTo(comparator) < 0 ? this : comparator;
        Radix minimum = new Radix("", model.base);
        minimum.digits.addAll(model.digits);
        minimum.sign = model.sign;
        return minimum;
    }

    /**
     * Finds the base of this Radix
     * @return this.base
     */
    public int base() {
        return this.base;
    }

    /**
     * Finds a List with the digits of this Radix
     * @return a LinkedList with parameter "this.digits"
     */
    public List<Integer> getDigits() {
        return new LinkedList<>(this.digits);
    }

    /**
     * Converts this Radix object into a printable format
     * @return the String representation of this Radix
     */
    @Override
    public String toString() {
        String convert = "";
        for(Integer digit : this.digits) {
            String convertedDigit = digit + "";
            if(digit >= DEFAULT_BASE) {                                               // to distinguish "longer" digits
                convertedDigit = "[".concat(convertedDigit).concat("]");
            }
            convert = convertedDigit.concat(convert);
        }
        return (this.sign == -1 ? "-": "") + convert;
    }

    /**
     * Prints the String representation of this Radix
     */
    public void print() {
        System.out.println(toString());
    }

    // static methods

    /**
     * Parses an array of Radices encased in Strings
     * @param base the base of the Radices
     * @param s the String encoding the Radices
     * @return the parsed Radix array
     */
    public static Radix[] parseRadices(int base, String... s) {
        String[][] parsedStrings = new String[s.length][];
        int length = 0;
        for(int i = 0; i < parsedStrings.length; i++) {
            parsedStrings[i] = s[i].split(",");
            length += parsedStrings[i].length;
        }
        Radix[] parsedRadices = new Radix[length];
        int stringNum = 0, parseNum = 0;
        for(int i = 0; i < length; i++) {
            parsedRadices[i] = new Radix(parsedStrings[stringNum][parseNum], base);
            parseNum++;
            if(parseNum == parsedStrings[stringNum].length) {
                parseNum = 0;
                stringNum++;
            }
        }
        return parsedRadices;
    }
}