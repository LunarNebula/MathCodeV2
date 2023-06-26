package Exception;

/**
 * This {@code Exception} is called when a program attempts to compare {@code Objects}
 * with dimensional bounds that do not permit the requested operation.
 */
public class IllegalDimensionException extends IllegalArgumentException {
    public static String UNEQUAL_VECTOR_DIMENSION =
            "Vector dimensions are unequal.";
    public static String UNEQUAL_MATRIX_DIMENSION =
            "Element-wise computations cannot be performed on matrices with the given dimensions.";
    public static String MATRIX_DOT_PRODUCT_ILLEGAL =
            "Dot-product computations cannot be performed on matrices with the given dimensions.";
    public static String VECTOR_ELEMENT_OUT_OF_BOUNDS =
            "The requested index requires access to an element not within the bounds of this Vector.";
    public static String MATRIX_ELEMENT_OUT_OF_BOUNDS =
            "The requested index requires access to an element not within the bounds of this Matrix.";
    public static String NON_RECTANGULAR_MATRIX =
            "This operation cannot be completed on a non-rectangular matrix.";
    public static String NON_SQUARE_MATRIX =
            "This operation cannot be completed on a non-rectangular matrix.";

    /**
     * Creates a new NoRealSolutionException
     * @param message the target error message
     */
    public IllegalDimensionException(String message) {
        super(message);
    }
}
