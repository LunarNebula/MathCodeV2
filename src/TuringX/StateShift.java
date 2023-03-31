package TuringX;

public class StateShift<Label, Cell> {
    private final State<Label, Cell> nextState;
    private final TapeShift<Cell>[] shifts;

    /**
     * Creates a new {@code StateShift}.
     * @param nextState the next {@code State} to switch to.
     * @param shifts the set of {@code TapeShifts} to transform the current tape state.
     */
    @SafeVarargs
    public StateShift(State<Label, Cell> nextState, TapeShift<Cell>... shifts) {
        this.nextState = nextState;
        this.shifts = shifts;
    }

    /**
     * Gets the new {@code State} implicated in this {@code StateShift}.
     * @return {@code this.nextState}
     */
    public State<Label, Cell> nextState() {
        return this.nextState;
    }

    /**
     * Gets a {@code TapeShift} corresponding to a particular tape.
     * @param index the tape index.
     * @return the shift at that index.
     */
    public TapeShift<Cell> getShift(int index) {
        return this.shifts[index];
    }

    /**
     * Gets the number of tapes handled in this {@code StateShift}.
     * @return {@code this.shifts.length}
     */
    public int size() {
        return this.shifts.length;
    }

    /**
     * Converts this {@code StateShift} to a printable format.
     * @return this {@code StateShift} as a {@code String}.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(TapeShift<Cell> shift : this.shifts) {
            builder.append(delimiter).append(shift);
            delimiter = ", ";
        }
        return this.nextState + " [" + builder + ']';
    }
}
