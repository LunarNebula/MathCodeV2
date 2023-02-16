package ProjectEuler;

public class PE000018 {
    public static void main(String[] args) {
        String s =
                "75\n" +
                "95 64\n" +
                "17 47 82\n" +
                "18 35 87 10\n" +
                "20 04 82 47 65\n" +
                "19 01 23 75 03 34\n" +
                "88 02 77 73 07 63 67\n" +
                "99 65 04 28 06 16 70 92\n" +
                "41 41 26 56 83 40 80 70 33\n" +
                "41 48 72 33 47 32 37 16 94 29\n" +
                "53 71 44 65 25 43 91 52 97 51 14\n" +
                "70 11 33 28 77 73 17 78 39 68 17 57\n" +
                "91 71 52 38 17 14 91 43 58 50 27 29 48\n" +
                "63 66 04 68 89 53 67 30 73 16 69 87 40 31\n" +
                "04 62 98 27 23 09 70 98 73 93 38 53 60 04 23"
                ;
//        String s2 =
//                "3\n" +
//                "7 4\n" +
//                "2 4 6\n" +
//                "8 5 9 3";
        System.out.println(compute(parse(s)));
    }

    /**
     * Parses a String into its component Integer array
     * @param s the String
     * @return the parsed table of Integers
     */
    public static int[][] parse(String s) {
        String[] splitArray = s.replace("\n", " ").split(" ");
        final int LENGTH = (int) Math.sqrt(splitArray.length * 2);
        int counter = 0;
        int[][] ar = new int[LENGTH][];
        for(int i = 0; i < LENGTH; i++) {
            ar[i] = new int[i + 1];
            for(int j = 0; j <= i; j++) {
                ar[i][j] = Integer.parseInt(splitArray[counter++]);
            }
        }
        return ar;
    }

    /**
     * Computes the maximum path size across the given board
     * @param ar the table of Integer weights
     * @return the weight of the heaviest path descending down the rows of the array
     */
    public static int compute(int[][] ar) {
        int[][] testAr = new int[ar.length][];
        testAr[ar.length - 1] = ar[ar.length - 1];
        for(int i = ar.length - 2; i >= 0; i--) {
            testAr[i] = new int[i + 1];
            for(int j = 0; j <= i; j++) {
                //System.out.println(ar[i][j] + " " + ar[i + 1][j] + " " + ar[i + 1][j + 1]);
                testAr[i][j] = ar[i][j] + Math.max(testAr[i + 1][j], testAr[i + 1][j + 1]);
            }
        }
        return testAr[0][0];
    }
}
