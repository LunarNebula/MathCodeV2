package ProjectEuler;

public class PE000030 {
    public static void main(String[] args) {
        final int POWER = 5;
        int totalSum = 0;
        for(int i = 2; i < 1000000; i++) {
            int sum = 0, test = i;
            while(test > 0) {
                sum += Math.pow((test % 10), 5);
                test /= 10;
            }
            if(sum == i) {
                totalSum += i;
            }
        }
        System.out.println("Sum: " + totalSum);
    }
}
