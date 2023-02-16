package ProjectEuler;

public class PE000032 {
    public static void main(String[] args) {
        boolean[] canBeSummed = new boolean[1000000];
        for(int i = 1; i < 10000; i++) {
            for(int j = 1; j < i; j++) {
                int product = i * j;
                int[] digits = new int[10];
                for(char c : (i + "" + j + "" + product).toCharArray()) {
                    digits[c - '0']++;
                }
                boolean isPanDigital = digits[0] == 0;
                for(int k = 1; k < digits.length; k++) {
                    isPanDigital &= (digits[k] == 1);
                }
                if(isPanDigital) {
                    canBeSummed[product] = true;
                }
            }
        }
        int sum = 0;
        for(int i = 0; i < canBeSummed.length; i++) {
            if(canBeSummed[i]) {
                sum += i;
            }
        }
        System.out.println("Sum: " + sum);
    }
}
