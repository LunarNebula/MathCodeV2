package Algebra;

/**
 * A class used to interface with objets that support boolean algebra. These classes
 * are strictly defined as binary; for example, defining a target member of the class
 * as {@code NOT()} is well-defined (see {@code BooleanSetOperable} for an example of
 * a class where this is not the case).
 * @param <Data> the data type on which boolean algebra is defined.
 * @see BooleanSetOperable
 */
public interface BooleanOperable<Data extends BooleanOperable<Data>> extends BooleanSetOperable<Data> {
    /**
     * Finds the logical negation of this BooleanOperable
     * @return NOT this
     */
    Data NOT();

    /**
     * Finds the logical NOT_AND of this BooleanOperable and another, specified BooleanOperable
     * @param o the comparator BooleanSetOperable
     * @return NOT (this AND o) [can also be written (NOT this) OR (NOT o)]
     */
    Data NOT_AND(Data o);

    /**
     * Finds the logical NOR of this BooleanOperable and another, specified BooleanOperable
     * @param o the comparator BooleanSetOperable
     * @return NOT (this OR o) [can also be written (NOT this) AND (NOT o)]
     */
    Data NOR(Data o);
}
