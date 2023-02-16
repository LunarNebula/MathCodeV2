package Theory;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Factor {
    /**
     * Finds the greatest common factor of two ints
     * @param a the first int
     * @param b the second int
     * @return the greatest common factor of the parameters
     */
    public static int gcf(int a, int b) {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
    }

    /**
     * Finds the greatest common factor of two longs
     * @param a the first long
     * @param b the second long
     * @return the greatest common factor of the parameters
     */
    public static long gcf(long a, long b) {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).longValue();
    }

    /**
     * Finds the greatest common factor of a set of numbers
     * @param a an array of ints
     * @return the GCF of the array
     */
    public static int gcf(int... a) {
        if(a.length == 0) {
            return 1;
        } else if(a.length == 1) {
            return a[0];
        }
        int gcf = gcf(a[0], a[1]);
        for(int num : a) {
            gcf = gcf(gcf, num);
        }
        return gcf;
    }

    /**
     * Finds the greatest common factor of a set of numbers
     * @param a an array of longs
     * @return the GCF of the array
     */
    public static long gcf(long... a) {
        if(a.length == 0) {
            return 1;
        } else if(a.length == 1) {
            return a[0];
        }
        long gcf = gcf(a[0], a[1]);
        for(long num : a) {
            gcf = gcf(gcf, num);
        }
        return gcf;
    }

    /**
     * Finds the least common multiple of two numbers
     * @param a the first int
     * @param b the second int
     * @return the LCM of the two integers
     */
    public static int lcm(int a, int b) {
        return (a / gcf(a, b)) * b;
    }

    /**
     * Finds all prime factors of a number
     * @param n the target number
     * @return a List of the prime factors
     */
    public static List<Integer> primeFactors(int n) {
        List<Integer> primeFactors = new LinkedList<>();
        while((n & 1) == 0 && n != 0) {
            n >>>= 1;
            primeFactors.add(2);
        } // determines if the number is even and composite
        int limit = (int) Math.sqrt(n);
        for(int i = 3; i <= limit; i += 2) {
            boolean resetLimit = false;
            while(n % i == 0) {
                n /= i;
                primeFactors.add(i);
                resetLimit = true;
            }
            if(resetLimit) {
                limit = (int) Math.sqrt(n);
            }
        } // checks all odd numbers from 3 to the square root for divisors
        if(n > 1) {
            primeFactors.add(n);
        }
        return primeFactors;
    }

    /**
     * Finds all prime factors of a BigInteger
     * @param n the target BigInteger
     * @return a List of the prime factors of the BigInteger
     */
    public static List<BigInteger> primeFactors(BigInteger n) {
        n = n.abs();
        final BigInteger INCREMENT = BigInteger.valueOf(2);
        List<BigInteger> primeFactors = new LinkedList<>();
        while(n.mod(INCREMENT).equals(BigInteger.ZERO) && (! n.equals(BigInteger.ZERO))) {
            n = n.divide(INCREMENT);
            primeFactors.add(INCREMENT);
        } // determines if the number is even and composite
        BigInteger limit = Arithmetic.sqrt(n);
        for(BigInteger i = BigInteger.valueOf(3); i.compareTo(limit) <= 0; i = i.add(INCREMENT)) {
            boolean sqrtRefresh = false;
            while(n.mod(i).equals(BigInteger.ZERO)) {
                n = n.divide(i);
                primeFactors.add(i);
                sqrtRefresh = true;
            }
            if(sqrtRefresh) {
                limit = Arithmetic.sqrt(n);
            }
        } // checks all odd numbers from 3 to the square root for divisors
        if(n.compareTo(BigInteger.ONE) > 0) {
            primeFactors.add(n);
        }
        return primeFactors;
    }

    /**
     * Finds all factors of a natural number
     * @param n the target multiple
     * @return a List of all numbers D such that D|n
     */
    public static List<Integer> factors(int n) {
        int prev = 1, product = 1;
        List<Integer> factors = new LinkedList<>(), test = new LinkedList<>();
        factors.add(1);
        for(int primeFactor : primeFactors(n)) {
            if(primeFactor == prev) {
                product *= primeFactor;
            } else {
                factors.addAll(test);
                prev = primeFactor;
                test.clear();
                product = primeFactor;
            }
            for(int factor : factors) {
                test.add(factor * product);
            }
        }
        factors.addAll(test);
        Collections.sort(factors);
        return factors;
    } //TODO: rewrite to eliminate "product" variable in favor of multiple factor lists (and compare times)

    /**
     * Finds all factors of a BigInteger
     * @param n the target multiple
     * @return a List of all numbers D such that D|n
     */
    public static List<BigInteger> factors(BigInteger n) {
        n = n.abs();
        BigInteger prev = BigInteger.ONE, product = BigInteger.ONE;
        List<BigInteger> factors = new LinkedList<>(), test = new LinkedList<>();
        factors.add(BigInteger.ONE);
        for(BigInteger primeFactor : primeFactors(n)) {
            if(primeFactor.equals(prev)) {
                product = product.multiply(primeFactor);
            } else {
                factors.addAll(test);
                prev = primeFactor;
                test.clear();
                product = primeFactor;
            }
            for(BigInteger factor : factors) {
                test.add(factor.multiply(product));
            }
        }
        factors.addAll(test);
        Collections.sort(factors);
        return factors;
    }

    /**
     * Finds all distinct prime factors of a number
     * @param n the target number
     * @return a List of the distinct prime factors
     */
    public static List<Integer> distinctPrimeFactors(int n) {
        List<Integer> primeFactors = new LinkedList<>();
        if((n & 1) == 0 && n != 0) {
            while((n & 1) == 0) {
                n >>>= 1;
            }
            primeFactors.add(2);
        } // determines if the number is even and composite
        int limit = (int) Math.sqrt(n);
        for(int i = 3; i <= limit; i += 2) {
            if(n % i == 0) {
                while(n % i == 0) {
                    n /= i;
                }
                primeFactors.add(i);
                limit = (int) Math.sqrt(n);
            }
        } // checks all odd numbers from 3 to the square root for divisors
        if(n > 1) {
            primeFactors.add(n);
        }
        return primeFactors;
    }

    /**
     * Counts the number of factors in an Integer
     * @param n the target Integer, k
     * @return the quantity of natural numbers N such that N|k
     */
    public static int numberOfFactors(int n) {
        List<Integer> primeFactors = primeFactors(n);
        int numberOfFactors = 1, prevFactor = 1, tallyRepeatFactor = 1;
        for(int p : primeFactors) {
            if(p == prevFactor) {
                tallyRepeatFactor++;
            } else {
                numberOfFactors *= tallyRepeatFactor;
                tallyRepeatFactor = 2;
                prevFactor = p;
            }
        }
        return numberOfFactors * tallyRepeatFactor;
    }

    /**
     * Lists the distinct prime factors of all non-negative Integers below a certain bound
     * @param n the upper bound
     * @return the List of Lists of prime factors
     */
    public static List<List<Integer>> listOfDistinctPrimeFactors(int n) {
        List<List<Integer>> factors = new LinkedList<>();
        factors.add(new LinkedList<>());
        factors.add(new LinkedList<>());
        LinkedList<Integer>[] values = new LinkedList[n + 1];
        for(int i = 0; i <= n; i++) {
            values[i] = new LinkedList<>();
        }
        int index = 1;
        while(index < n) {
            index++;
            if(values[index].size() == 0) {
                values[index].add(index);
            }
            factors.add(values[index]);
            for(Integer item : values[index]) {
                values[(index + item) % values.length].add(item);
            }
            values[index] = new LinkedList<>();
        }
        return factors;
    }

    /**
     * Finds a complete List of factors for all non-negative Integers below a certain bound
     * @param n the upper bound
     * @return the List of Lists of factors
     */
    public static List<List<Integer>> listOfFactors(int n) {
        List<List<Integer>> factors = new LinkedList<>();
        factors.add(new LinkedList<>());
        LinkedList<Integer>[] values = new LinkedList[n + 1];
        for(int i = 0; i <= n; i++) {
            values[i] = new LinkedList<>();
        }
        int index = 0;
        while(index < n) {
            index++;
            values[index].add(index);
            factors.add(values[index]);
            for(Integer item : values[index]) {
                int nextIndex = index + item;
                if(nextIndex >= values.length) {
                    nextIndex -= values.length;
                }
                values[nextIndex].add(0, item);
            }
            values[index] = new LinkedList<>();
        }
        return factors;
    }

    /**
     * Finds the sum of the factors of a number
     * @param n the target multiple
     * @return the sum of the factors of n
     */
    public static int sum(int n) {
        List<Integer> factors = primeFactors(n);
        int sum = 0, product = 1, prev = 1, subProduct = 1;
        for(int factor : factors) {
            if(factor == prev) {
                sum += subProduct;
                subProduct *= factor;
            } else {
                product *= sum + subProduct;
                sum = 1;
                subProduct = factor;
                prev = factor;
            }
        }
        return (sum + subProduct) * product;
    }

    /**
     * Finds the sum of the proper factors of n
     * @param n the target multiple
     * @return the aliquot sum
     */
    public static int aliquotSum(int n) {
        return sum(n) - n;
    }

    /**
     * Finds the length of the aliquot cycle of a number
     * @param n the target number
     * @return -1 if the aliquot cycle terminates at 0, else the length of the aliquot cycle
     */
    public static int aliquotCycle(int n) {
        List<Integer> sums = new LinkedList<>();
        sums.add(-1);
        int sum = n, length = 0;
        while(sums.get(0) != n) {
            for(int i = 0; i < 2; i++) {
                sum = aliquotSum(sum);
                sums.add(sum);
            }
            sums.remove(0);
            length++;
            if(sum == 0) {
                return -1;
            }
        }
        return length;
    }

    /**
     * Finds the aliquot sequence of an integer
     * @param n the target integer
     * @return the list of recursive proper factor sums until either 0 or a cycle is reached
     */
    public static List<Integer> aliquotSequence(int n) {
        LinkedList<Integer> aliquotSequence = new LinkedList<>(), proxySequence = new LinkedList<>();
        int secondCursor = aliquotSum(n), firstCursor = aliquotSum(secondCursor);
        aliquotSequence.add(n);
        if(n != secondCursor) {
            aliquotSequence.add(secondCursor);
            if(n != firstCursor) {
                while(secondCursor != firstCursor) {
                    firstCursor = aliquotSum(firstCursor);
                    proxySequence.add(firstCursor);
                    firstCursor = aliquotSum(firstCursor);
                    proxySequence.add(firstCursor);
                    secondCursor = aliquotSum(secondCursor);
                    aliquotSequence.add(secondCursor);
                }

            }
        }
        return aliquotSequence;
    }

    /**
     * Finds the totient number of an Integer
     * @param n the input Integer
     * @return the number of integers no more than n that are relatively prime to n
     */
    public static int totient(int n) {
        int totient = n;
        for(int factor : distinctPrimeFactors(n)) {
            totient = totient * (factor - 1) / factor;
        }
        return totient;
    }

    /**
     * Finds the iterated totient sum of an Integer
     * @param n the input Integer
     * @return the sum of the totients
     */
    public static int iteratedTotientSum(int n) {
        int sum = 0, totient = n;
        while(totient > 1) {
            totient = totient(totient);
            sum += totient;
        }
        return sum;
    }

    /**
     * Determines if an Integer is divisible by p^2 for every p prime divisor
     * @param n the target number
     * @return true if the number is powerful, else false
     */
    public static boolean isPowerful(int n) {
        int count = 0, prev = -1;
        for(int factor : primeFactors(n)) {
            if(factor == prev) {
                count++;
            } else {
                if(count == 1) {
                    return false;
                }
                prev = factor;
                count = 1;
            }
        }
        return count > 1;
    }

    /**
     * Determines if an Integer is not divisible by p^2 for any p prime divisor
     * @param n the target number
     * @return true if the number is square-free, else false
     */
    public static boolean isSquareFree(int n) {
        if((n & 3) == 0) { // checks that n is not equal to 0 (mod 4)
            return false;
        }
        int sqrt = (int) Math.sqrt(n);
        for(int i = 3; i <= sqrt; i += 2) {
            if(n % (i * i) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the greatest square-free factor of an Integer
     * @param n the target Integer
     * @return the radical of n
     */
    public static int radical(int n) {
        List<Integer> primeFactors = distinctPrimeFactors(n);
        int radical = 1;
        for(Integer factor : primeFactors) {
            radical *= factor;
        }
        return radical;
    }
}