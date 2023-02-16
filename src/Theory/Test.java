package Theory;

import Algebra.*;
import DataSet.BST;
import DataSet.JaggedStack;
import DataSet.NumberList;
import DataSet.Stack3;
import Enumerator.Radix;
import General.Timer;
import Geometry.Point2D;
import Geometry.Triangle;
import Geometry.Vector;
import Turing.Machine;

import java.util.ArrayList;
import java.util.List;

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
//        final int BASE = 3, FIRST_COUNT = 5;
//        final DiscreteCounter counter5 = new DiscreteCounter(FIRST_COUNT, BASE);
//        counter5.increment();
//        final HashSet<Integer> hashCodeSet = new HashSet<>();
//        while(!counter5.isZero()) {
//            Tournament2S3R tournament = new Tournament2S3R(counter5);
//            boolean containsRainbowTriangles = tournament.partiallyCompress();
//            if(containsRainbowTriangles) {
//                hashCodeSet.add(tournament.hashCode());
//            }
//            counter5.increment();
//        }
//        final int SECOND_VERTEX_COUNT = 6;
//        final DiscreteCounter counter6 = new DiscreteCounter(SECOND_VERTEX_COUNT, BASE);
//        counter6.increment();
//        int subgraphCounter = 0;
//        while(!counter6.isZero()) {
//            Tournament2S3R tournament = new Tournament2S3R(counter6);
//            boolean containsRainbowTriangles = tournament.partiallyCompress();
//            if(containsRainbowTriangles) {
//                boolean failedCheck = false;
//                for(int i = 0; i < SECOND_VERTEX_COUNT; i++) {
//                    List<Integer> indexList = new LinkedList<>();
//                    for(int j = 0; j < SECOND_VERTEX_COUNT; j++) {
//                        if(j != i) {
//                            indexList.add(j);
//                        }
//                    }
//                    Tournament2S3R subgraph = tournament.subgraph(indexList);
//                    if(hashCodeSet.contains(subgraph.hashCode())) {
//                        failedCheck = true;
//                        i = SECOND_VERTEX_COUNT;
//                    }
//                }
//                if(! failedCheck) {
//                    subgraphCounter++;
//                    tournament.print();
//                }
//            }
//            counter6.increment();
//        }
//        System.out.println("Total: " + subgraphCounter);
        Matrix matrix = new Matrix("1,-1,-4,0,5;-2,1,3,2,0;2,0,2,3,1");
        matrix.print();
        matrix.addRow(1, 0, new Fraction(2));
        matrix.addRow(2, 0, new Fraction(-2));
        matrix.addRow(2, 1, new Fraction(2));
        matrix.print();
    }
}