package General;

/**
 * Asserts the existence of a trueText method that converts a TrueTextEncodable into a standardized String format
 * that can be parsed into the original Object
 */
public interface TrueTextEncodable {
    /**
     * Converts this TrueTextEncodable into a String format that can be easily parsed back into its original Object form
     * @return this TrueTextEncodable as a parsable String
     */
    String trueText();
}
