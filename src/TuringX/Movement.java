package TuringX;

public enum Movement {
    LEFT("<<", -1),
    RIGHT(">>", 1),
    STOP("||", 0);

    // The String equivalent of this Movement
    private final String printValue;
    // The translation amount for this Movement
    private final int translation;

    /**
     * Creates a new Movement
     * @param print the print value of this Movement
     * @param translation the translation value of this Movement
     */
    Movement(String print, int translation) {
        this.printValue = print;
        this.translation = translation;
    }

    /**
     * Finds the translation quantity for this Movement
     * @return this.translation
     */
    public int getTranslation() {
        return this.translation;
    }

    /**
     * Converts this Movement to a printable format
     * @return this Movement as a String
     */
    @Override
    public String toString() {
        return this.printValue;
    }

    /**
     * Prints this Movement
     */
    public void print() {
        System.out.println(this);
    }
}
