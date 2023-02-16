package Theory;

import java.util.LinkedList;
import java.util.List;

public class Prime {
    /**
     * Determines the primality of a number
     * @param n the target number
     * @return true if the number is prime, else false
     */
    public static boolean isPrime(int n) {
        if((n & 1) == 0 && n != 2) {
            return false;
        }
        int limit = (int) Math.sqrt(n);
        for(int i = 3; i <= limit; i += 2) {
            if(n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if a number is a prime power
     * @param n the target number
     * @return true if n = p^k for some prime p and natural number k, else false
     */
    public static boolean isPrimePower(int n) {
        if((n & 1) == 0) {
            n >>>= 1;
            while((n & 1) == 0) {
                n >>>= 1;
            }
            return n == 1;
        }
        int limit = (int) Math.sqrt(n);
        for(int i = 3; i <= limit; i += 2) {
            if(n % i == 0) {
                while(n % i == 0) {
                    n /= i;
                }
                return n == 1;
            }
        }
        return true;
    }

    /**
     * Finds the nth prime number
     * @param n the target prime index
     * @return the nth prime number
     */
    public static int nthPrime(int n) {
        if(n <= 6) {
            return (new int[]{-1, 2, 3, 5, 7, 11, 13})[n];
        } // returns one of the first 6 primes if applicable
        int key = 1, count = 1, index = 0;
        final int length = (int) (Math.sqrt(n) * Math.sqrt(Math.log(n) + Math.log(Math.log(n)))); // sqrt of upper bound of nth prime
        final int[] track = new int[length << 1]; // adds buffer space inside the array
        while(n > count) {
            key += 2; // skips even numbers
            index++;
            if(index == track.length) {
                index = 0;
            }
            boolean check = true;
            if(track[index] == 0) {
                count++;
                if(key > length) {
                    check = false;
                }
            }
            if(check) {
                int proxy = track[index] == 0 ? key : track[index], pawn = index + proxy;
                if(pawn >= track.length) {
                    pawn -= track.length;
                }
                track[index] = 0;
                while(track[pawn] != 0) {
                    if(proxy > track[pawn]) {
                        int switcher = track[pawn];
                        track[pawn] = proxy;
                        proxy = switcher;
                    } // switches the ejected pawn integer
                    pawn += proxy;
                    if(pawn >= track.length) {
                        pawn -= track.length;
                    }
                }
                track[pawn] = proxy;
            }
        }
        return key;
    }

    /**
     * Finds the number of primes less than or equal to a certain number
     * @param n the prime limit
     * @return the number of primes no greater than n
     */
    public static int pi(int n) {
        if(n <= 1) {
            return 0;
        }
        int key = 3, count = 1, index = 0, length = (int) Math.sqrt(n);
        int[] track = new int[length << 1];
        while(n >= key) {
            index++;
            if(index == track.length) {
                index = 0;
            }
            boolean check = true;
            if(track[index] == 0) {
                count++;
                if(key > length) {
                    check = false;
                }
            }
            if(check) {
                int proxy = track[index] == 0 ? key : track[index], pawn = index + proxy;
                if(pawn >= track.length) {
                    pawn -= track.length;
                }
                track[index] = 0;
                while(track[pawn] != 0) {
                    if(proxy > track[pawn]) {
                        int switcher = track[pawn];
                        track[pawn] = proxy;
                        proxy = switcher;
                    }
                    pawn += proxy;
                    if(pawn >= track.length) {
                        pawn -= track.length;
                    }
                }
                track[pawn] = proxy;
            }
            key += 2; // skips even numbers
        }
        return count;
    }

    /**
     * Lists all primes p less than or equal to a given Integer n
     * @param n the target (maximum) Integer
     * @return the List of smaller primes
     */
    public static List<Integer> listLesserPrimes(int n) {
        if(n <= 1) {
            return new LinkedList<>();
        }
        List<Integer> primes = new LinkedList<>();
        primes.add(2);
        int key = 3, index = 0, length = (int) Math.sqrt(n);
        int[] track = new int[length << 1];
        while(n >= key && key > 0) {
            index++;
            if(index == track.length) {
                index = 0;
            }
            boolean check = true;
            if(track[index] == 0) {
                primes.add(key);
                if(key > length) {
                    check = false;
                }
            }
            if(check) {
                int proxy = track[index] == 0 ? key : track[index], pawn = index + proxy;
                if(pawn >= track.length) {
                    pawn -= track.length;
                }
                track[index] = 0;
                while(track[pawn] != 0) {
                    if(proxy > track[pawn]) {
                        int switcher = track[pawn];
                        track[pawn] = proxy;
                        proxy = switcher;
                    }
                    pawn += proxy;
                    if(pawn >= track.length) {
                        pawn -= track.length;
                    }
                }
                track[pawn] = proxy;
            }
            key += 2; // skips even numbers
        }
        return primes;
    }
}