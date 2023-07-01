package Algebra;

/**
 * A class used to interface with objects that support boolean algebra. These classes
 * are strictly defined as binary; for example, defining a target member of the class
 * as {@code NOT()} is well-defined (see {@code BooleanSetOperable} for an example of
 * a class where this is not the case).
 * @param <Data> the data type on which boolean algebra is defined.
 * @see BooleanSetOperable
 */
public interface BooleanOperable<Data extends BooleanOperable<Data>> extends BooleanSetOperable<Data> {
    /**
     * Finds the logical {@code NOT} of this {@code BooleanOperable}.
     * @return {@code NOT this}
     */
    Data NOT();

    /**
     * Finds the logical {@code NOT_AND} of this {@code BooleanOperable} and another,
     * specified {@code BooleanOperable}.
     * @param o the comparator {@code BooleanSetOperable}.
     * @return {@code NOT (this AND o)} [can also be written {@code (NOT this) OR (NOT o)}].
     */
    Data NOT_AND(Data o);

    /**
     * Finds the logical {@code NOR} of this {@code BooleanOperable} and another,
     * specified {@code BooleanOperable}.
     * @param o the comparator {@code BooleanSetOperable}
     * @return {@code NOT (this OR o)} [can also be written {@code (NOT this) AND (NOT o)}]
     */
    Data NOR(Data o);
}
