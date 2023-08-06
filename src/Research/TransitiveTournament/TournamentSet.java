package Research.TransitiveTournament;

import java.util.LinkedList;
import java.util.List;

public class TournamentSet {
    /**
     * Gets the minimum <code>Tournament2S3RCoordinate</code> path length
     * @param length the length of the target <code>Tournament2S3RCoordinate</code>
     * @return the list of <code>Tournament2S3RCoordinate</code> objects with the smallest maximum path length
     */
    public static List<Tournament2S3RCoordinate> getMinTournaments2S3R(int length) {
        final int BASE = 3;
        int minLength = length;
        DiscreteCounter counter = new DiscreteCounter(length, BASE);
        counter.increment();
        List<Tournament2S3RCoordinate> candidates = new LinkedList<>();
        while(! counter.isZero()) {
            Tournament2S3RCoordinate tournament = new Tournament2S3RCoordinate(counter);
            int tempLength = tournament.getMinPath();
            if(tempLength <= minLength) {
                if(tempLength < minLength) {
                    candidates.clear();
                    minLength = tempLength;
                }
                candidates.add(tournament);
            }
            counter.increment();
        }
        return candidates;
    }
}
