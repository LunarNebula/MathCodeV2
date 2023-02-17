package Research;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Performs executions for the {@code Tournament} classes.
 */
public class T23Executor {
    public static void main(String[] args) {
        final int BASE = 3, FIRST_COUNT = 5;
        final DiscreteCounter counter5 = new DiscreteCounter(FIRST_COUNT, BASE);
        counter5.increment();
        final HashSet<Integer> hashCodeSet = new HashSet<>();
        while(!counter5.isZero()) {
            Tournament2S3R tournament = new Tournament2S3R(counter5);
            boolean containsRainbowTriangles = tournament.partiallyCompress();
            if(containsRainbowTriangles) {
                hashCodeSet.add(tournament.hashCode());
            }
            counter5.increment();
        }
        final int SECOND_VERTEX_COUNT = 6;
        final DiscreteCounter counter6 = new DiscreteCounter(SECOND_VERTEX_COUNT, BASE);
        counter6.increment();
        int subgraphCounter = 0;
        while(!counter6.isZero()) {
            Tournament2S3R tournament = new Tournament2S3R(counter6);
            boolean containsRainbowTriangles = tournament.partiallyCompress();
            if(containsRainbowTriangles) {
                boolean failedCheck = false;
                for(int i = 0; i < SECOND_VERTEX_COUNT; i++) {
                    List<Integer> indexList = new LinkedList<>();
                    for(int j = 0; j < SECOND_VERTEX_COUNT; j++) {
                        if(j != i) {
                            indexList.add(j);
                        }
                    }
                    Tournament2S3R subgraph = tournament.subgraph(indexList);
                    if(hashCodeSet.contains(subgraph.hashCode())) {
                        failedCheck = true;
                        i = SECOND_VERTEX_COUNT;
                    }
                }
                if(! failedCheck) {
                    subgraphCounter++;
                    tournament.print();
                }
            }
            counter6.increment();
        }
        System.out.println("Total: " + subgraphCounter);
    }
}
