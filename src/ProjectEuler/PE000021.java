package ProjectEuler;

import Theory.Factor;

public class PE000021 {
    public static void main(String[] args) {
        int sum = 0;
        for(int i = 2; i < 10000; i++) {
            int aliquotSum = Factor.aliquotSum(i);
            if(aliquotSum != i && Factor.aliquotSum(aliquotSum) == i) {
                sum += i;
            }
        }
        System.out.println("Sum: " + sum);
    }
}
