package Turing;

import Exception.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Movement {
    LEFT("<<"),
    RIGHT(">>"),
    STOP("||");

    // The String equivalent of this Movement.
    private final String printValue;
    private static final String LEFT_KEY = "L";
    private static final String RIGHT_KEY = "R";
    private static final String STOP_KEY = "S";

    /**
     * Creates a new {@code Movement}.
     * @param print the print value of this {@code Movement}.
     */
    Movement(String print) {
        this.printValue = print;
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

    // Static methods

    /**
     * Gets the {@code Movement} corresponding to an input value.
     * @param value the {@code String} input value of the {@code Movement}.
     * @return one of the following:
     * <ul>
     *     <li>{@code Movement.LEFT} if the input is {@code "L"}.</li>
     *     <li>{@code Movement.RIGHT} if the input is {@code "R"}.</li>
     *     <li>{@code Movement.STOP} if the input is {@code "S"}.</li>
     * </ul>
     * @throws IllegalArgumentException if the input {@code String} does not match any of
     * the options above.
     */
    @Contract(pure = true)
    public static Movement get(@NotNull String value) {
        return switch (value) {
            case LEFT_KEY -> Movement.LEFT;
            case RIGHT_KEY -> Movement.RIGHT;
            case STOP_KEY -> Movement.STOP;
            default -> throw new IllegalArgumentException(
                    ExceptionMessage.ARGUMENT_EXCEEDS_REQUIRED_DOMAIN()
            );
        };
    }
}
