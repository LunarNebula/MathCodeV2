package Theory;

import General.Timer;

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
    }
}