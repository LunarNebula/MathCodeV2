package ProjectEuler;

import DataSet.BST;
import DataSet.NumberList;
import Theory.Prime;

import java.util.*;

public class PE000035 {
    public static void main(String[] args) {
        NumberList primes = new NumberList(Prime.listLesserPrimes(1000000));
        primes.randomize();
        Iterator<Integer> iterator = primes.iterator();
        while(iterator.hasNext()) {
            int next = iterator.next();
            boolean isLegal = true;
            while (isLegal && next > 0) {
                isLegal = (next % 2 != 0);
                next /= 10;
            }
            if(! isLegal) {
                iterator.remove();
            }
        }
        BST<Integer> bst = new BST<>(primes);
        Map<Integer, Boolean> legalMap = new HashMap<>();
        Map<Integer, Integer> forwardLinkMap = new HashMap<>(), backwardLinkMap = new HashMap<>();
        for(int prime : primes) {
            legalMap.put(prime, true);
        }
        for(int prime : primes) {
            String primeString = "" + prime, parsedPrimeString = primeString.substring(1) + primeString.charAt(0);
            int nextPrime = Integer.parseInt(parsedPrimeString);
            forwardLinkMap.put(prime, nextPrime);
            backwardLinkMap.put(nextPrime, prime);
            if((! (legalMap.get(prime) && bst.contains(nextPrime))) || parsedPrimeString.charAt(0) == '0') {
                legalMap.replace(prime, false);
                legalMap.put(nextPrime, false);
            }
        }
        for(int prime : primes) {
            if(! legalMap.get(prime)) {
                Integer prevLink = forwardLinkMap.get(prime), nextLink = forwardLinkMap.get(prevLink);
                legalMap.replace(prevLink, false);
                while(nextLink != null && ! prevLink.equals(nextLink)) {
                    legalMap.replace(nextLink, false);
                    prevLink = forwardLinkMap.get(prevLink);
                    nextLink = forwardLinkMap.get(nextLink);
                    if(nextLink != null) {
                        legalMap.replace(nextLink, false);
                        nextLink = forwardLinkMap.get(nextLink);
                    }
                }
                prevLink = backwardLinkMap.get(prime);
                nextLink = backwardLinkMap.get(prevLink);
                legalMap.replace(prevLink, false);
                while(nextLink != null && ! prevLink.equals(nextLink)) {
                    legalMap.replace(nextLink, false);
                    prevLink = backwardLinkMap.get(prevLink);
                    nextLink = backwardLinkMap.get(nextLink);
                    if(nextLink != null) {
                        legalMap.replace(nextLink, false);
                        nextLink = backwardLinkMap.get(nextLink);
                    }
                }
            }
        }
        int count = 1;
        List<Integer> legals = new LinkedList<>();
        legals.add(2);
        for(int prime : primes) {
            if(legalMap.get(prime)) {
                count++;
                legals.add(prime);
            }
        }
        System.out.println("Number of circular primes: " + count);
        System.out.println(legals);
    }
}
