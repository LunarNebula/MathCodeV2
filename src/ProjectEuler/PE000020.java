package ProjectEuler;

import Enumerator.Radix;

import java.util.List;

public class PE000020 {
    public static void main(String[] args) {
        Radix r = new Radix("1"), product = r;
        for(int i = 1; i < 100; i++) {
            r = r.add(new Radix("1"));
            product = product.multiply(r);
        }
        List<Integer> digits = product.getDigits();
        int sum = 0;
        for(int digit : digits) {
            sum += digit;
        }
        System.out.println(sum);
    }
}
