package TuringX;

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
     * Converts this {@code TapeShift} to a printable format.
     * @return this {@code TapeShift} as a {@code String}.
     */
    @Override
    public String toString() {
        return "(" + this.tapeValue + ") " + this.movement;
    }
}
