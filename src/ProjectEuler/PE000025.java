package ProjectEuler;

import java.math.BigInteger;

public class PE000025 {
    public static void main(String[] args) {
        BigInteger f1 = BigInteger.ONE, f2 = BigInteger.ONE;
        for(int i = 0; i < 4780; i++) {
            BigInteger f0 = f1;
            f1 = f2;
            f2 = f0.add(f1);
        }
        System.out.println(f2);
    }
}
