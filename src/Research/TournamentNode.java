package Research;

import java.util.HashMap;
import java.util.Map;

public class TournamentNode {
    private final int ID;
    private final Map<Integer, TournamentArc> arcs;

    /**
     * Creates a new <code>TournamentNode</code>
     * @param ID the ID of this <code>TournamentNode</code>
     */
    public TournamentNode(int ID) {
        this.ID = ID;
        this.arcs = new HashMap<>();
    }

    /**
     * Adds a <code>TournamentArc</code> to this
     * @param src the source <code>TournamentNode</code>
     * @return the new <code>TournamentArc</code>
     */
    public TournamentArc addArcFrom(TournamentNode src) {
        TournamentArc arc = new TournamentArc(src, this);
        this.arcs.put(src.ID, arc);
        return arc;
    }

    /**
     * Gets the ID of this <code>TournamentNode</code>
     * @return <code>this.ID</code>
     */
    public int getID() {
        return this.ID;
    }

    /**
     * Gets the <code>TournamentArc</code> sourced from a specific <code>TournamentNode</code>
     * @param srcID the ID of the source <code>TournamentNode</code>
     * @return the target <code>TournamentArc</code>
     */
    public TournamentArc getArc(int srcID) {
        return this.arcs.get(srcID);
    }
}
