package ProjectEuler;

public class PE000028 {
    public static void main(String[] args) {
        int value = 1, sum = 1, addend = 2, counter = 0;
        final int LIMIT = 1001*1001;
        while(value < LIMIT) {
            value += addend;
            sum += value;
            counter++;
            if(counter == 4) {
                counter = 0;
                addend += 2;
            }
        }
        System.out.println(sum);
    }
}
