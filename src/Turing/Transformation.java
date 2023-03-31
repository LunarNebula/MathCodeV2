package Turing;

public record Transformation<Key, Data>(Data nextTapeValue, State<Key, Data> nextState, Movement movement) {
    /**
     * Gets the new State for the current index in this StateShift
     *
     * @return this.switchState
     */
    @Override
    public Data nextTapeValue() {
        return this.nextTapeValue;
    }

    /**
     * Gets the next State in this StateShift
     *
     * @return this.nextState
     */
    @Override
    public State<Key, Data> nextState() {
        return this.nextState;
    }

    /**
     * Gets the Movement for this StateShift
     *
     * @return this.movement
     */
    @Override
    public Movement movement() {
        return this.movement;
    }

    /**
     * Determines whether this StateShift is equal to a specified Object
     *
     * @param o the comparator Object
     * @return true if the comparator is a StateShift with equal type parameters and constraints, else false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Transformation<?, ?> convert)) {
            return false;
        }
        return this.movement == convert.movement
                && this.nextTapeValue.equals(convert.nextTapeValue)
                && this.nextState.equals(convert.nextState);
    }

    /**
     * Converts this StateShift to a printable format
     *
     * @return this StateShift as a String
     */
    @Override
    public String toString() {
        return "(" + this.nextState.getID() + ": " + this.movement.toString() + ")";
    }

    /**
     * Prints this StateShift
     */
    public void print() {
        System.out.println(this);
    }
}
