package Turing;

import java.util.List;

public class StateShift<Label, Cell> {
    private final Label nextState;
    private final List<TapeShift<Cell>> shifts;

    /**
     * Creates a new {@code StateShift}.
     * @param nextState the next {@code State} to switch to.
     * @param shifts the set of {@code TapeShifts} to transform the current tape state.
     */
    public StateShift(Label nextState, List<TapeShift<Cell>> shifts) {
        this.nextState = nextState;
        this.shifts = shifts;
    }

    /**
     * Gets the new {@code State} implicated in this {@code StateShift}.
     * @return {@code this.nextState}
     */
    public Label nextState() {
        return this.nextState;
    }

    /**
     * Gets a {@code TapeShift} corresponding to a particular tape.
     * @param index the tape index.
     * @return the shift at that index.
     */
    public TapeShift<Cell> getShift(int index) {
        return this.shifts.get(index);
    }

    /**
     * Gets the number of tapes handled in this {@code StateShift}.
     * @return {@code this.shifts.length}
     */
    public int size() {
        return this.shifts.size();
    }

    /**
     * Determines whether this {@code StateShift} is equal to another, specified {@code Object}.
     * @param o the target {@code Object}.
     * @return {@code true} if {@code o} is a {@code StateShift} with the same
     * {@code TapeShift} mappings, else {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof StateShift<?,?> stateShift)) {
            return false;
        } else if(! this.nextState.equals(stateShift.nextState)) {
            return false;
        }
        for(int i = 0; i < this.shifts.size(); i++) {
            if(! this.shifts.get(i).equals(stateShift.shifts.get(i))) {
                return false;
            }
        }
        return true;
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
