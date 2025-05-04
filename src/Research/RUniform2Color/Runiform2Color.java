package Research.RUniform2Color;

public class Runiform2Color {

    public static void run() {
        final int[][] edges = new int[][]{
                {0,1,14},
                {1,2,14},
                {2,3,14},
                {3,4,14},
                {4,5,14},
                {5,6,14},
                {6,7,14},
                {7,8,14},
                {8,9,14},
                {9,10,14},
                {10,11,14},
                {11,12,14},
                {12,10,14},
                {0,1,13},
                {1,2,13},
                {2,3,13},
                {3,4,13},
                {4,5,13},
                {5,6,13},
                {6,7,13},
                {7,8,13},
                {8,9,13},
                {9,10,13},
                {10,11,13},
                {11,12,13},
                {12,0,13},
                {0,3,7},
                {1,4,8},
                {2,5,9},
                {3,6,10},
                {4,7,11},
                {5,8,12},
                {6,9,0},
                {7,10,1},
                {8,11,2},
                {9,12,3},
                {10,0,4},
                {11,1,5},
                {12,2,6}
        };
        final int LENGTH = 15, FINAL_INDEX = 1 << LENGTH;
        for(int i = 0; i < FINAL_INDEX; i++) {
            final boolean[] track = new boolean[LENGTH];
            int j = i;
            int index = 0;
            while(j > 0) {
                if((j & 1) == 1) {
                    track[index] = true;
                }
                index++;
                j >>>= 1;
            }
            boolean passed = true;
            for(int[] item : edges) {
                boolean and = true, or = false;
                for (int value : item) {
                    and &= track[value];
                    or |= track[value];
                }
                if(and || !or) {
                    passed = false;
                    break;
                }
            }
            if(passed) {
                System.out.println(i);
            }
        }
    }
}
