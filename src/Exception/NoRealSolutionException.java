package Exception;

/**
 * This Exception is called when a program attempts to find the intersection of two disjoint geometrical objects.
 */
public class NoRealSolutionException extends RuntimeException {
    public static String PARALLEL_LINE_INTERSECTION =
            "Parallel lines do not intersect.";
    public static String INFINITE_POINT_LOCATION =
            "Solution point has undefined coordinates.";
    public static String INCONSISTENT_SYSTEM_OF_EQUATIONS =
            "System of equations is inconsistent.";

    /**
     * Creates a new NoRealSolutionException
     * @param message the target error message
     */
    public NoRealSolutionException(String message) {
        super(message);
    }
}
