package ProjectEuler;

public class PE000024 {
    public static void main(String[] args) {
        int[] digits = new int[]{0,1,2,3,4,5,6,7,8,9};
        for(int i = 0; i < 999999; i++) {
            do {
                int carry = 9, index = digits.length - 1;
                while(carry > 0) {
                    int nextDigit = digits[index] + carry;
                    digits[index] = nextDigit % digits.length;
                    carry = nextDigit / digits.length;
                    index--;
                }
            } while(! isValid(digits));
        }
        String print = "";
        for (int digit : digits) {
            print = print.concat("" + digit);
        }
        System.out.println(print);
    }

    public static boolean isValid(int[] digits) {
        boolean[] isDigitIncluded = new boolean[digits.length];
        for(int digit : digits) {
            isDigitIncluded[digit] = true;
        }
        for(boolean digitCheck : isDigitIncluded) {
            if(! digitCheck) {
                return false;
            }
        }
        return true;
    }
}
//2783915460
