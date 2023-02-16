package ProjectEuler;

public class PE000019 {
    public static void main(String[] args) {
        System.out.println(computeTotal(1901, 0, 0, 1, 0, 2001, 0, 0));
    }

    public static int computeTotal(int startYear, int startMonth, int startDay, int startWeekday, int targetWeekday, int endYear, int endMonth, int endDay) {
        final int[] DAYS_IN_MONTH = {31, -1, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        final int WEEK_LENGTH = 7;
        final Integer[] currentDate = new Integer[]{startYear, startMonth,
                (startDay + targetWeekday - startWeekday) % WEEK_LENGTH},
                endDate = new Integer[]{endYear, endMonth, endDay};
        if((currentDate[0] % 4 == 0 && currentDate[0] % 100 != 0) || currentDate[0] % 400 == 0) {
            DAYS_IN_MONTH[1] = 29;
        } else {
            DAYS_IN_MONTH[1] = 28;
        }
        int counterTally = 0;
        while(compare(currentDate, endDate) < 0) {
            currentDate[2] += WEEK_LENGTH;
            if(currentDate[2] >= DAYS_IN_MONTH[currentDate[1]]) {
                currentDate[2] -= DAYS_IN_MONTH[currentDate[1]];
                if(currentDate[2] == 1) {
                    counterTally++;
                }
                currentDate[1]++;
            }
            if(currentDate[1] >= DAYS_IN_MONTH.length) {
                currentDate[1] = 0;
                currentDate[0]++;
                if((currentDate[0] % 4 == 0 && currentDate[0] % 100 != 0) || currentDate[0] % 400 == 0) {
                    DAYS_IN_MONTH[1] = 29;
                } else {
                    DAYS_IN_MONTH[1] = 28;
                }
            }
            //printDate(currentDate);
        }
        return counterTally;
    }

    public static int compare(Integer[] a, Integer[] b) {
        if(a.length != b.length) {
            throw new IllegalArgumentException();
        }
        for(int i = 0; i < a.length; i++) {
            int compare = a[i].compareTo(b[i]);
            if(compare != 0) {
                return compare;
            }
        }
        return 0;
    }

    public static void printDate(Integer[] ar) {
        System.out.println("[" + ar[2] + "/" + ar[1] + "/" + ar[0] + "]");
    }
}
