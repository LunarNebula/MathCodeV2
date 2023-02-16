package Theory;

import Algebra.Fraction;
import Algebra.Polynomial;
import DataKey.Coordinate;
import Exception.*;
import General.ArrayComputer;

import java.math.BigInteger;
import java.util.*;

public class Combinatorics {
    /**
     * Finds the Frobenius number of two integers
     * @param a the first int
     * @param b the second int
     * @return the greatest number that cannot be represented as a positive linear sum of the two ints
     * @throws IllegalArgumentException if either parameter is nonpositive
     */
    public static int frobenius(int a, int b) {
        if(a <= 0 || b <= 0) {
            throw new IllegalArgumentException(ExceptionMessage.ARGUMENT_EXCEEDS_REQUIRED_DOMAIN());
        }
        return (Factor.gcf(a, b) != 1) ? -1 : (a * b - (a + b));
    }

    /**
     * Finds the Frobenius number of a set of integers
     * @param a an array of ints
     * @return the greatest number that cannot be represented as a positive linear sum of the elements in the array
     * @throws IllegalArgumentException if any parameter is nonpositive
     */
    public static int frobenius(int... a) {
        int min = ArrayComputer.min(a), max = ArrayComputer.max(a), index = -1, count = 0, frobenius = -1;
        if(Factor.gcf(a) != 1) {
            return -1;
        } else if(min <= 0) {
            throw new IllegalArgumentException(ExceptionMessage.ARGUMENT_EXCEEDS_REQUIRED_DOMAIN());
        }
        boolean[] values = new boolean[max];
        values[0] = true;
        while(count < min) {
            index++;
            if(index == values.length) {
                index = 0;
            }
            if(values[index]) {
                count++;
                for(int num : a) {
                    int nextIndex = index + num;
                    if(nextIndex >= values.length) {
                        nextIndex -= values.length;
                    }
                    values[nextIndex] = true;
                }
            } else {
                frobenius += count + 1;
                count = 0;
            }
        }
        return frobenius;
    }

    /**
     * Finds the number of ways to partition unlabeled objects amongst unlabeled groups
     * @param n the number of objects
     * @return the number of partitions
     */
    public static BigInteger unlabeledPartitions(int n) {
        return unlabeledPartitions(n, 1, n);
    }

    /**
     * Finds the number of ways to partition unlabeled objects amongst unlabeled groups
     * @param n the number of objects
     * @param lower the lower bound of partition sizes
     * @param upper the upper bound of partition sizes
     * @return the number of partitions
     */
    public static BigInteger unlabeledPartitions(int n, int lower, int upper) {
        BigInteger[][] values = new BigInteger[n][];
        values[0] = new BigInteger[]{BigInteger.ONE};
        for(int i = 1; i < n; i++) {
            values[i] = new BigInteger[Math.min(i + 1, Math.min(n - i + 1, upper))];
            for(int j = 0; j < values[i].length; j++) {
                if(j < lower) {
                    values[i][j] = BigInteger.ZERO;
                } else {
                    values[i][j] = values[i][j - 1].add(values[i - j][Math.min(j, i - j)]);
                }
            }
        }
        BigInteger totalPartitions = BigInteger.ZERO;
        for(int i = 1; i <= upper; i++) {
            totalPartitions = totalPartitions.add(values[n - i][Math.min(i, n - i)]);
        }
        return totalPartitions;
    }

    /**
     * Finds the number of subset permutations
     * @param n the total number of items in the set
     * @param k the subset size
     * @return the number of permutations of k objects in a set of n, or nPk
     */
    public static BigInteger permutations(int n, int k) {
        BigInteger permutations = BigInteger.ONE;
        for(int i = n - k + 1; i <= n; i++) {
            permutations = permutations.multiply(BigInteger.valueOf(i));
        }
        return permutations;
    }

    /**
     * Finds the number of subset combinations
     * @param n the total number of items in the set
     * @param k the subset size
     * @return the number of combinations of k objects in a set of n, or nCk
     */
    public static BigInteger combinations(int n, int k) {
        return permutations(n, k).divide(factorial(k));
    }

    /**
     * Finds the gamma function evaluated at the given number [product of all integers from 1 to n]
     * @param n the target value
     * @return n factorial
     */
    public static BigInteger factorial(int n) {
        return permutations(n, n);
    }

    /**
     * Finds the binomial probability of a set of trials
     * @param trials the total number of trials
     * @param successes the number of successful trials
     * @param chance the individual probability that a single trial succeeds
     * @return the overall probability that the specified number of successes will occur in the number of trials
     */
    public static Fraction binomialProbability(int trials, int successes, Fraction chance) {
        return (new Fraction(combinations(trials, successes))).multiply(
                chance.pow(successes)).multiply(Fraction.ONE.subtract(chance).pow(trials - successes));
    }

    /**
     * Finds the nth Catalan number
     * @param n the index
     * @return (2n choose n) / (n + 1)
     */
    public static BigInteger CatalanNumber(int n) {
        return combinations(n << 1, n).divide(BigInteger.valueOf(n + 1));
    }

    /**
     * Finds the number of ways to partition unlabeled objects amongst labeled groups
     * @param n the number of objects
     * @param k the number of groups
     * @return the number of combinations
     */
    public static BigInteger labeledPartitions(int n, int k) {
        return combinations(n + k - 1, k - 1);
    }

    /**
     * Finds the number of ways to partition unlabeled objects amongst labeled groups
     * @param n the number of objects
     * @param k the number of groups
     * @param min the minimum group size
     * @return the number of combinations
     */
    public static BigInteger labeledPartitions(int n, int k, int min) {
        return combinations(n + k * (1 - min) - 1, k - 1);
    }

    /**
     * Finds the number of ways to partition unlabeled objects amongst labeled groups
     * @param n the number of objects
     * @param k the number of groups
     * @param min the minimum group size (inclusive)
     * @param max the maximum group size (exclusive)
     * @return the number of combinations
     */
    public static BigInteger labeledPartitions(int n, int k, int min, int max) {
        final int diff = max - min, minPosInvert = n - k * min, kInvert = k - 1, limit = Math.min(minPosInvert / diff, k);
        BigInteger sum = BigInteger.ZERO, sign = BigInteger.ONE;
        int coefficient = minPosInvert + kInvert;
        for(int i = 0; i <= limit; i++) {
            sum = sum.add(combinations(k, i).multiply(combinations(coefficient, kInvert)).multiply(sign));
            sign = sign.negate();
            coefficient -= diff;
        }
        return sum;
    }

    /**
     * Finds the maximum number of pieces an n-dimensional object can be split into with (n-1)-D divisions
     * @param n the dimension of the object (also +1 the dimension of the divisions)
     * @param d the number of divisions
     * @return the number of pieces
     */
    public static BigInteger multidimensionalPartitions(int n, int d) {
        BigInteger divisions = BigInteger.ZERO;
        for(int i = 0; i <= d; i++) {
            divisions = divisions.add(combinations(n, i));
        }
        return divisions;
    }

    /**
     * Finds the amount of numbers with a certain digit sum
     * @param base the base of each target number
     * @param digits the target number of digits
     * @param sum the target sum
     * @return the number of numbers
     */
    public static BigInteger numberOfDigitSums(int base, int digits, int sum) {
        return labeledPartitions(sum, digits, 0, base).subtract(labeledPartitions(sum, digits - 1, 0, base));
    } // (base) indicates that digits can only occur between 0 and (base - 1), inclusive

    /**
     * Returns the multinomial coefficient with given constraints
     * @param a the integer indices of the multinomial
     * @return the corresponding multinomial
     */
    public static BigInteger multinomial(int... a) {
        int sum = 0;
        BigInteger product = BigInteger.ONE;
        for(int value : a) {
            sum += value;
            product = product.multiply(factorial(value));
        }
        return factorial(sum).divide(product);
    } // TODO: make more efficient

    /**
     * Finds the number of unique substrings of a given length contained in a larger String
     * @param s the target String
     * @param length the length of each substring
     * @return the number of unique permutations (of given length) of letters contained within the target String
     */
    public static BigInteger numberOfUniqueSubstrings(String s, int length) {
        if(length > s.length()) {
            return BigInteger.ZERO;
        }
        HashMap<Character, Integer> characterMap = new HashMap<>();
        for(char ch : s.toUpperCase().toCharArray()) {
            Integer characterCount = characterMap.get(ch);
            if(characterCount == null) {
                characterCount = 0;
            }
            characterMap.put(ch, characterCount + 1);
        }
        List<Polynomial> componentPolynomials = new LinkedList<>();
        for(char ch : characterMap.keySet()) {
            Fraction[] terms = new Fraction[characterMap.get(ch) + 1];
            terms[0] = Fraction.ONE;
            Fraction combinatorialDivisor = Fraction.ONE;
            for(int i = 1; i < terms.length; i++) {
                combinatorialDivisor = combinatorialDivisor.divide(new Fraction(i));
                terms[i] = combinatorialDivisor;
            }
            componentPolynomials.add(new Polynomial(terms));
        }
        Polynomial probabilityProduct = new Polynomial(Fraction.ONE);
        for(Polynomial component : componentPolynomials) {
            probabilityProduct = probabilityProduct.multiply(component);
        }
        return probabilityProduct.getTerms()[length].multiply(new Fraction(factorial(length))).asBigInteger();
    }

    /**
     * Finds the number of non-negative integers no more than the target that satisfy a balanced centrifuge
     * @param n the target number
     * @return the number of balanced centrifuges of size n
     */
    public static int numberOfBalancedCentrifuges(int n) {
        int count = 0, index = -1, middle = 1 - (n & 1);
        List<Integer> factors = Factor.distinctPrimeFactors(n);
        if(factors.size() == 0) {
            return 1;
        } // case n == 1
        if(factors.get(0) == n) {
            return 2;
        } // case prime number
        boolean[] values = new boolean[factors.get(factors.size() - 1)];
        values[0] = true;
        final int UPPER_LIMIT = n >>> 1;
        for(int i = 0; i <= UPPER_LIMIT; i++) {
            index++;
            if(index == values.length) {
                index = 0;
            }
            if(values[index]) {
                values[index] = false;
                count++;
                for(Integer factor : factors) {
                    int nextIndex = index + factor;
                    if(nextIndex >= values.length) {
                        nextIndex -= values.length;
                    }
                    values[nextIndex] = true;
                }
            }
        } // only records half the array before reflecting it over the middle
        return (count << 1) - middle;
    }

    /**
     * Finds all non-negative integers no more than the target that satisfy a balanced centrifuge
     * @param n the target number
     * @return the List of balanced centrifuges of size n
     */
    public static List<Integer> listOfBalancedCentrifuges(int n) {
        int index = -1, currentNum = -1;
        List<Integer> factors = Factor.distinctPrimeFactors(n), legals = new LinkedList<>(), proxy = new LinkedList<>();
        boolean[] values = new boolean[factors.get(factors.size() - 1)];
        values[0] = true;
        final int UPPER_LIMIT = n >>> 1;
        for (int i = 0; i <= UPPER_LIMIT; i++) {
            index++;
            if(index == values.length) {
                index = 0;
            }
            currentNum++;
            if (values[index]) {
                legals.add(currentNum);
                proxy.add(0, currentNum);
                values[index] = false;
                for (Integer factor : factors) {
                    int nextIndex = index + factor;
                    if(nextIndex >= values.length) {
                        nextIndex -= values.length;
                    }
                    values[nextIndex] = true;
                }
            }
        }
        for(Integer legal : proxy) {
            if(legal << 1 != n) {
                legals.add(n - legal);
            }
        }
        return legals;
    }

    /**
     * Finds a complete List of possible partitions of an Integer
     * @param n the target Integer
     * @return a list of all Integer partitions of n
     */
    public static List<List<Integer>> listOfPartitionLists(int n) {
        return listOfPartitionLists(n, n, new LinkedList<>());
    }

    /**
     * Finds a complete List of possible partitions of a given size
     * @param n the total partition quantity
     * @param max the maximum size of any proceeding List components
     * @param currentList the current constructed List
     * @return the List of partition Lists
     */
    private static List<List<Integer>> listOfPartitionLists(int n, int max, List<Integer> currentList) {
        List<List<Integer>> list = new LinkedList<>();
        if(n == 0) {
            list.add(new LinkedList<>(currentList));
        } else {
            for(int i = 1; i <= Math.min(max, n); i++) {
                currentList.add(0, i);
                list.addAll(listOfPartitionLists(n - i, i, currentList));
                currentList.remove(0);
            }
        }
        return list;
    }

    /**
     * Determines whether a pair of Lists describes the placement of tokens in a rectangular grid space
     * @param given the given List of item counts (natural numbers)
     * @param target the perpendicular List of item counts, to be checked (natural numbers)
     * @return true if the given List of items can be arranged with row and column populations as designated by the "given"
     * and "target" Lists, respectively
     */
    public static boolean isPossibleGridConstraint(List<Integer> given, List<Integer> target) {
        List<Integer> testTarget = new LinkedList<>(target);
        Collections.sort(testTarget);
        Collections.reverse(testTarget);
        ListIterator<Integer> targetIterator = testTarget.listIterator();
        if(targetIterator.hasNext()) {
            int nextCount = targetIterator.next();
            if(given.size() < nextCount) {
                return false;
            } else {
                List<Integer> nextTarget = new LinkedList<>(testTarget), nextGiven = new LinkedList<>(given);
                nextTarget.remove(0);
                ListIterator<Integer> givenIterator = nextGiven.listIterator();
                for(int i = 0; i < nextCount && givenIterator.hasNext(); i++) {
                    int count = givenIterator.next();
                    givenIterator.remove();
                    if(count > 1) {
                        givenIterator.add(count - 1);
                    }
                }
                Collections.sort(nextGiven);
                Collections.reverse(nextGiven);
                return isPossibleGridConstraint(nextGiven, nextTarget);
            }
        }
        return true;
    }

    /**
     * Finds the number of counter arrangements
     * @param given a given List of integer partitions with length L
     * @param target a comparator List of integer partitions with length M
     * @return the number of counter arrangements in an L by M grid such that:
     *      - The number of counters is equal to the sum of all elements in "target"
     *      - The number of counters in row R is equal to the Rth element in "target"
     *      - The number of counters in column C is equal to the Cth element in "given"
     */
    public static BigInteger numberOfCounterArrangements(List<Integer> given, List<Integer> target) {
        BigInteger sum = BigInteger.ZERO;
        if(target.size() == 0) {
            sum = BigInteger.ONE;
        } else if(target.get(0) <= given.size()) {
            int[] newGiven = new int[target.get(0)];
            for(int i = 0; i < newGiven.length; i++) {
                newGiven[i] = i;
            }
            boolean iterationNotComplete = true;
            List<Integer> nextTargetList = new LinkedList<>(target);
            nextTargetList.remove(0);
            while(iterationNotComplete) {
                int index = 0, arrayIndex = 0;
                ListIterator<Integer> givenIterator = given.listIterator();
                List<Integer> nextGivenList = new LinkedList<>();
                while(givenIterator.hasNext()) {
                    int nextValue = givenIterator.next();
                    if (index == newGiven[arrayIndex]) {
                        if (nextValue > 1) {
                            nextGivenList.add(nextValue - 1);
                        }
                        if (arrayIndex + 1 < newGiven.length) {
                            arrayIndex++;
                        }
                    } else {
                        nextGivenList.add(nextValue);
                    }
                    index++;
                }
                sum = sum.add(numberOfCounterArrangements(nextGivenList, nextTargetList));
                arrayIndex = newGiven.length - 1;
                int limit = given.size();
                boolean continueShiftingIndices = true;
                while (continueShiftingIndices) {
                    newGiven[arrayIndex]++;
                    if(newGiven[arrayIndex] == limit) {
                        if(arrayIndex == 0) {
                            iterationNotComplete = false;
                            continueShiftingIndices = false;
                        } else {
                            newGiven[arrayIndex] = newGiven[arrayIndex - 1] + 2;
                            if(newGiven[arrayIndex] == limit) {
                                newGiven[arrayIndex]--;
                            }
                            for(int i = arrayIndex + 1; i < newGiven.length; i++) {
                                newGiven[i] = newGiven[i - 1] + 1;
                            }
                            arrayIndex--;
                        }
                        limit--;
                    } else {
                        continueShiftingIndices = false;
                    }
                }
            }
        }
        return sum;
    }

    /**
     * Determines the maximum possible value for a sum of items (an algorithm to solve the Knapsack problem)
     * @param maxWeight the maximum weight of the set
     * @param c an array of Coordinates containing information for a set of items:
     *          index 0: a "weight" for the item
     *          index 1: a "value" for the item
     * @return the maximum value for a subset of the items to be chosen
     * @throws IllegalArgumentException if the number of elements in any Coordinate is incorrect or any weight is nonpositive
     */
    @SafeVarargs
    public static int maximumValue(int maxWeight, Coordinate<Integer>... c) {
        if(c.length == 0) {
            return 0;
        }
        final int ARG_COUNT = 2;
        for(Coordinate<Integer> coordinate : c) {
            Integer[] coordinates = coordinate.getCoordinates();
            if(coordinates.length != ARG_COUNT) {
                throw new IllegalArgumentException(ExceptionMessage.INCORRECT_NUMBER_OF_ARGUMENTS(ARG_COUNT));
            } else if(coordinates[0] <= 0) {
                throw new IllegalArgumentException(ExceptionMessage.ARGUMENT_EXCEEDS_REQUIRED_DOMAIN());
            }
        }
        int[][] table = new int[maxWeight + 1][];
        for(int i = 0; i < table.length; i++) {
            table[i] = new int[c.length + 1];
        }
        for(int i = 0; i < maxWeight; i++) {
            for(int j = 0; j < c.length; j++) {
                int nextWeight = i + c[j].getCoordinates()[0];
                if(nextWeight < table.length) {
                    table[nextWeight][j + 1] = Math.max(table[nextWeight][j + 1], table[i][j] + c[j].getCoordinates()[1]);
                }
            }
        }
        return table[maxWeight][c.length];
    }
}