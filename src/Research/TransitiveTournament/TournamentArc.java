package Research.TransitiveTournament;

public class TournamentArc {
    private final TournamentNode src, dest;
    private final int hashCode;
    private int color;

    /**
     * Creates a new <code>TournamentArc</code>
     * @param src the source <code>TournamentNode</code>
     * @param dest the destination <code>TournamentNode</code>
     */
    public TournamentArc(TournamentNode src, TournamentNode dest) {
        this.src = src;
        this.dest = dest;
        this.hashCode = hashCode(src.getID(), dest.getID());
        setColor(0);
    }

    /**
     * Creates a new <code>TournamentArc</code>
     * @param src the source <code>TournamentNode</code>
     * @param dest the destination <code>TournamentNode</code>
     * @param color the color of this <code>TournamentArc</code>
     */
    public TournamentArc(TournamentNode src, TournamentNode dest, int color) {
        this.src = src;
        this.dest = dest;
        this.hashCode = hashCode(src.getID(), dest.getID());
        setColor(color);
    }

    /**
     * Computes the hashCode for this <code>TournamentArc</code>
     * @param src the ID of the source <code>TournamentNode</code>
     * @param dest the ID of the destination <code>TournamentNode</code>
     * @return the computed hashCode
     */
    private int hashCode(int src, int dest) {
        return ((src * (src - 1)) >> 1) + dest;
    }

    /**
     * Sets the color of this <code>TournamentArc</code>
     * @param color the new color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Gets the color of this <code>TournamentArc</code>
     * @return <code>this.color</code>
     */
    public int getColor() {
        return this.color;
    }

    /**
     * Gets the source <code>TournamentNode</code> for this <code>TournamentArc</code>
     * @return <code>this.src</code>
     */
    public TournamentNode getSource() {
        return this.src;
    }

    /**
     * Gets the destination <code>TournamentNode</code> for this <code>TournamentArc</code>
     * @return <code>this.dest</code>
     */
    public TournamentNode getDestination() {
        return this.dest;
    }

    /**
     * Finds the hashCode of this <code>TournamentArc</code>
     * @return a unique <code>int</code> encoding this arc
     */
    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
