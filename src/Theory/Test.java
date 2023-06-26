package Theory;

import Algebra.Matrix;
import General.Timer;
import Geometry.Vector;

public class Test {
    /**
     * Handles the main execution
     * @param args command line arguments
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
     * @throws Exception if an <code>Exception</code> occurs in the test
     */
    public static void test() throws Exception {
        final Matrix matrix = new Matrix("2,4,6,8;1,3,0,5;1,1,6,3");
        matrix.print();
        Vector.print(matrix.nullSpace());
    }
}