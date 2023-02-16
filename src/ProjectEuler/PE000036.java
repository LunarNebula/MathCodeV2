package ProjectEuler;

import Enumerator.Radix;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PE000036 {
    public static void main(String[] args) {
        System.out.println(countDoublePalindromes(new Radix("1000000")));
    }

    public static Radix countDoublePalindromes(Radix upperBound) {
        Radix baseTen = new Radix("1"), baseTwo = new Radix("1", 2);
        final Radix baseTenTWO = new Radix("2"), baseTwoTWO = new Radix("10", 2);
        Radix sum = new Radix("0");
        while(baseTen.compareTo(upperBound) < 0) {
            List<Integer> tenDigits = baseTen.getDigits(), twoDigits = baseTwo.getDigits();
            Iterator<Integer> tenDigitForward = tenDigits.iterator(), tenDigitBackward = ((LinkedList<Integer>) tenDigits).descendingIterator();
            Iterator<Integer> twoDigitForward = twoDigits.iterator(), twoDigitBackward = ((LinkedList<Integer>) twoDigits).descendingIterator();
            boolean isPalindromic = true;
            while(isPalindromic && tenDigitForward.hasNext()) {
                int forward = tenDigitForward.next(), backward = tenDigitBackward.next();
                isPalindromic = (forward == backward);
            }
            while(isPalindromic && twoDigitForward.hasNext()) {
                int forward = twoDigitForward.next(), backward = twoDigitBackward.next();
                isPalindromic = (forward == backward);
            }
            if(isPalindromic) {
                sum = sum.add(baseTen);
            }
            baseTen = baseTen.add(baseTenTWO);
            baseTwo = baseTwo.add(baseTwoTWO);
        }
        return sum;
    }
}
