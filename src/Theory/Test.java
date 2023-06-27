package Theory;

import Algebra.Matrix;
import Enumerator.UnsignedInt;
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
        final UnsignedInt a = new UnsignedInt("00001011");
        final UnsignedInt b = new UnsignedInt("00011011");
        a.print();
        b.print();
        a.multiply(b).print();
        b.multiply(a).print();
    }
}