package ProjectEuler;

public class PE000489 {
    public static void main(String[] args) {
        System.out.println(H(5,5));
    }

    public static long H(int u, int v) {
        long count = 0;
        for(long m = 1; m <= u; m++) {
            for(long n = 1; n <= v; n++) {
                long[] value = G(m,n);
                System.out.println(m + "," + n + ": " + value[0] + " " + value[1]);
                count += value[1];
            }
        }
        return count;
    }

    public static long[] G(long m, long n) {
        long max = -1, index = -1, prevIndex = 0;
        while(index < 128878) {
            index++;
            long comparator = gcf(index * index * index + n, (index + m) * (index + m) * (index + m) + n);
            if(comparator > max) {
                max = comparator;
                prevIndex = index;
            }
        }
        return new long[]{max, prevIndex};
    }

    public static long gcf(long a, long b) {
        while(b > 0) {
            if(a < b) {
                long proxy = a;
                a = b;
                b = proxy;
            } else {
                a %= b;
            }
        }
        return a;
    }
}
