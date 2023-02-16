package ProjectEuler;

import Theory.Factor;

import java.util.LinkedList;
import java.util.List;

public class PE000023 {
    public static void main(String[] args) {
        final int UPPER_LIMIT = 28124; // as Java is zero-indexed
        boolean[] canBeGenerated = new boolean[UPPER_LIMIT];
        List<Integer> abundantNumbers = new LinkedList<>();
        for(int i = 12; i <= UPPER_LIMIT; i++) {
            if(Factor.aliquotSum(i) > i) {
                abundantNumbers.add(i);
            }
        }
        System.out.println(abundantNumbers);
        for(int addend1 : abundantNumbers) {
            for(int addend2 : abundantNumbers) {
                if(addend1 + addend2 < UPPER_LIMIT) {
                    canBeGenerated[addend1 + addend2] = true;
                }
            }
        }
        int sum = 0;
        for(int i = 0; i < UPPER_LIMIT; i++) {
            if(! canBeGenerated[i]) {
                sum += i;
            }
        }
        System.out.println("Score: " + sum);
    }
}
