package Turing;

public record TapeShift<Cell>(Cell tapeValue, Movement movement) {
    /**
     * Gets the next tape value.
     * @return {@code this.tapeValue}
     */
    public Cell tapeValue() {
        return this.tapeValue;
    }

    /**
     * Gets the next {@code Movement}.
     * @return {@code this.movement}
     */
    @Override
    public Movement movement() {
        return this.movement;
    }

    /**
     * Determines whether this {@code TapeShift} is equal to another, specified {@code Object}.
     * @param o the target {@code Object}.
     * @return {@code true} if {@code o} is a {@code TapeShift} with the same new {@code Cell}
     * value and {@code Movement}, else {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof TapeShift<?> shift)) {
            return false;
        } else {
            return this.tapeValue.equals(shift.tapeValue) && this.movement.equals(shift.movement);
        }
    }

    /**
     * Converts this {@code TapeShift} to a printable format.
     * @return this {@code TapeShift} as a {@code String}.
     */
    @Override
    public String toString() {
        return "(" + this.tapeValue + ") " + this.movement;
    }
}
