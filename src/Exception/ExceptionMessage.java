package Exception;

import java.io.IOException;
import java.util.Scanner;

public class ExceptionMessage {
    public static final String TOO_MANY_ARGUMENTS = "Too many arguments. ";
    public static final String NOT_ENOUGH_ARGUMENTS = "Not enough arguments. ";
    public static final String UNCHECKED = "unchecked";

    /**
     * Generates a message to print for a TOO_MANY_ARGUMENTS IllegalArgumentException
     * @param max the inclusive maximum number of arguments
     * @return the generated error message
     */
    public static String TOO_MANY_ARGUMENTS(int max) {
        return TOO_MANY_ARGUMENTS + "There must be no more than " + max + " arguments. ";
    }

    /**
     * Generates a message to print for a NOT_ENOUGH_ARGUMENTS IllegalArgumentException
     * @param min the inclusive minimum number of arguments
     * @return the generated error message
     */
    public static String NOT_ENOUGH_ARGUMENTS(int min) {
        return NOT_ENOUGH_ARGUMENTS + "There must be at least " + min + " arguments. ";
    }

    /**
     * Generates a message to print for a NOT_ENOUGH_ARGUMENTS IllegalArgumentException
     * @param num the number of required arguments
     * @return the generated error message
     */
    public static String INCORRECT_NUMBER_OF_ARGUMENTS(int num) {
        return "There must be exactly " + num + " arguments. ";
    }

    /**
     * Generates a message to print for an ARGUMENT_EXCEEDS_REQUIRED_DOMAIN IllegalArgumentException
     * @return the generated error message
     */
    public static String ARGUMENT_EXCEEDS_REQUIRED_DOMAIN() {
        return "Argument exceeds required domain. ";
    }

    /**
     * <code>TargetedMessage</code> tailors <code>Exception</code> methods to events derived from specified
     * classes.
     */
    public static class TargetedMessage {
        /**
         * Determines whether a specified <code>Scanner</code> object has more tokens
         * @param scanner the target <code>Scanner</code>
         * @throws IOException when the scanner has no more elements
         */
        public static void scannerHasNextElement(Scanner scanner) throws IOException {
            if(!scanner.hasNext()) {
                throw new IOException("Scanner target does not have enough tokens.");
            }
        }

        /**
         * Returns an applicable error message pertaining to an improper base
         * @param base the expected base
         * @return the <code>String</code> error
         * @throws ArithmeticException if the base
         */
        public static String INCORRECT_BASE(int base) throws ArithmeticException {
            verifyBase(base);
            return "The expected value should be a number in base " + base + ".";
        }

        /**
         * Determines whether a given integer is a valid base
         * @param base the given base
         * @throws ArithmeticException if the base is less than 2
         */
        public static void verifyBase(int base) throws ArithmeticException {
            if(base < 2) {
                throw new ArithmeticException("Base is less than 2.");
            }
        }
    }
}
