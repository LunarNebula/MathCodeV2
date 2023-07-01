package Algebra;

/**
 * A class used to interface with objects that support boolean algebra on sets. These
 * built-in set methods are not finitely-defined; any operation computing values inside
 * or outside one or more {@code BooleanSetOperable} objects must exist in at least one
 * of them. The boolean aspect of this interface is in whether a specified element exists
 * in the {@code BooleanSetOperable} set.
 * @param <Data> the data type contained in this {@code BooleanSetOperable}.
 * @see BooleanOperable
 */
public interface BooleanSetOperable<Data extends BooleanSetOperable<Data>> {
    /**
     * Finds the logical intersection of this {@code BooleanSetOperable} and another,
     * specified {@code BooleanSetOperable}.
     * @param o the comparator {@code BooleanSetOperable}.
     * @return {@code this AND o}
     */
    Data AND(Data o);

    /**
     * Finds the logical union of this {@code BooleanSetOperable} and another,
     * specified {@code BooleanSetOperable}.
     * @param o the comparator {@code BooleanSetOperable}.
     * @return {@code this OR o}
     */
    Data OR(Data o);

    /**
     * Finds the logical disjoint union ({@code XOR}) of this {@code BooleanSetOperable}
     * and another, specified {@code BooleanSetOperable}.
     * @param o the comparator {@code BooleanSetOperable}
     * @return {@code this XOR o} [{@code this OR o AND NOT (this AND o)}]
     */
    Data XOR(Data o);

    /**
     * Computes the logical {@code AND_NOT} for this {@code BooleanSetOperable} and another,
     * specified {@code BooleanSetOperable}.
     * @param o the comparator {@code BooleanSetOperable}.
     * @return {@code this AND NOT o}
     */
    Data AND_NOT(Data o);
}
