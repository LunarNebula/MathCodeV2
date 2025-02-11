package Theory;

import General.Timer;
import Research.NCourt.OrMatrix;

/**
 * Handles tests for most algorithms. Unless a change is being made to the general test
 * structure, all changes to this class should be discarded before pushing.
 */
public class Test {
    /**
     * Handles the main execution.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        final Timer timer = new Timer("Test_Timer");
        timer.start();
        try {
            test();
        } catch (Exception | Error problem) {
            problem.printStackTrace();
        }
        timer.lap(true);
    }

    /**
     * Tests functions. Note that functions without Javadoc comments are not ready for testing.
     * @throws Exception if an {@code Exception} occurs in the test.
     */
    public static void test() throws Exception {
        final int[] initial = new int[]{0b00010010,0b00000100,0b00000001,0b00000001,0b00000000,0b00000000,0b00000000,0b00000000};
        Research.NCourt.NCourt32.run(initial, 20, 23);
//        final int[] init = new int[]{2,4,1}, transpose = new int[]{4,1,2};
//        System.out.println(Research.NCourt.NCourt32.productSum(3, init, transpose, new int[]{1,2,4}));
    }
}