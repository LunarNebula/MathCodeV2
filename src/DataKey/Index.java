package DataKey;

import General.TrueTextEncodable;

/**
 * Stores a {@code Comparable} pair of values that can be used in hierarchical sorting algorithms.
 * For example, when organizing a deck of cards, one might sort by suit and then rank cards of each
 * suit by rank. In this case, one would set the suit (a {@code String}, perhaps) as the {@code a}
 * value and the rank (an {@code int}) as the {@code b} value. If more than two values are required,
 * {@code Index} objects can be nested; the order of the objects when collapsed into a one-dimensional
 * list is then the underlying sorting hierarchy. This value is often used as the key element in a
 * {@code ComparableMapEntry} when sorting a list of items. If these multiple items belong to the
 * same class, a {@code ConstantIndex} object can be used instead.
 * @param a the first value in the sorting hierarchy.
 * @param b the second value in the sorting hierarchy.
 * @param <A> the first data type.
 * @param <B> the second data type.
 * @see ComparableMapEntry
 * @see ConstantIndex
 */
public record Index<A extends Comparable<A>, B extends Comparable<B>>(A a, B b)
        implements Comparable<Index<A, B>>, TrueTextEncodable {

    /**
     * Gets the primary value of this Index
     *
     * @return {@code this.a}
     */
    @Override
    public A a() {
        return this.a;
    }

    /**
     * Gets the secondary value of this Index
     *
     * @return {@code this.b}
     */
    @Override
    public B b() {
        return this.b;
    }

    /**
     * Gets the hashCode of this Index
     *
     * @return the unique hashCode identifier
     */
    @Override
    public int hashCode() {
        int a = this.a.hashCode() << 1, b = this.b.hashCode() << 1, aAbs = a;
        if (a < 0) {
            aAbs = 1 - aAbs;
        }
        if (b < 0) {
            b = 1 - b;
        }
        return (aAbs + b + 1) * (aAbs + b) + a;
    }

    /**
     * Compares this Index to another Index
     *
     * @param o the comparator Object
     * @return given the parameters
     * -1 if this Object is "less than" o
     * 0 if this Object is "equal to" o
     * 1 if this Object is "greater than" o
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified Object's type prevents it from being compared to this Object
     */
    @Override
    public int compareTo(Index<A, B> o) {
        int compare = this.a.compareTo(o.a);
        return (compare == 0) ? this.b.compareTo(o.b) : compare;
    }

    /**
     * Determines whether this Index is equal to a specified Object
     *
     * @param o the comparator Object
     * @return true if the Object is an Index with equal primary and secondary comparators, else false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Index<?, ?> convert)) {
            return false;
        }
        return this.a.equals(convert.a) && this.b.equals(convert.b);
    }

    /**
     * Provides the TrueText of this Index
     *
     * @return this Index in a parsable format
     */
    @Override
    public String trueText() {
        return toString();
    }

    /**
     * Converts this Index to a printable format
     *
     * @return this Index as a String
     */
    @Override
    public String toString() {
        return this.a + "|" + this.b;
    }
}
