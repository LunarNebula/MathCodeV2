package ProjectEuler;

import Theory.*;

public class PE000031 {
    public static void main(String[] args) {
        System.out.println("Sum: " + getTotalSums(200, 1, 2, 5, 10, 20, 50, 100, 200));
    }
// 200, 1, 2, 5, 10, 20, 50, 100, 200
    public static int getTotalSums(int target, int... a) {
        int max = 0;
        for(int i : a) {
            max = Math.max(max, i);
        }
        return getTotalSums(target, max, a);
    }

    public static int getTotalSums(int target, int max, int[] a) {
        if(target < 0) {
            return 0;
        } else if(target == 0) {
            return 1;
        }
        int sum = 0;
        for(int i : a) {
            if(i <= max) {
                sum += getTotalSums(target - i, i, a);
            }
        }
        return sum;
    }
}
