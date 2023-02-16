package DataKey;

import General.TrueTextEncodable;

/**
 * A wrapper object for sorting items by comparing specific attributes that may or may not
 * be denoted as the "natural order" of those objects. For example, a {@code Matrix} object
 * does not obey a natural order, but for some algorithm one might wish to sort a list of
 * {@code Matrices} by determinant. If more than one {@code Comparable} object is required
 * for sorting, one can use a {@code ConstantIndex} or {@code Index} object as the key.
 * @param key the {@code Comparable} object used to define the natural order of this
 *            {@code ComparableMapEntry},
 * @param value the sorted value of this {@code ComparableMapEntry}.
 * @param <Key> the data type for this key.
 * @param <Value> the data type for this value.
 * @see ConstantIndex
 * @see Index
 */
public record ComparableMapEntry<Key extends Comparable<Key>, Value>(Key key, Value value)
        implements Comparable<ComparableMapEntry<Key, Value>>, TrueTextEncodable {

    /**
     * Gets the {@code Key} for this {@code ComparableMapEntry}.
     * @return {@code this.key}
     */
    @Override
    public Key key() {
        return this.key;
    }

    /**
     * Gets the {@code Value} for this {@code ComparableMapEntry}.
     * @return {@code this.value}
     */
    @Override
    public Value value() {
        return value;
    }

    /**
     * Compares this {@code ComparableMapEntry} to another {@code ComparableMapEntry}.
     * @param o the comparator {@code ComparableMapEntry}.
     * @return given the parameters, return one of the following:
     * <ul>
     *     <li>-1 if this Key is "less than" the 'o' Key.</li>
     *     <li>0 if this Key is "equal to" the 'o' Key.</li>
     *     <li>1 if this Key is "greater than" the 'o' Key.</li>
     * </ul>
     */
    @Override
    public int compareTo(ComparableMapEntry<Key, Value> o) {
        return this.key.compareTo(o.key);
    }

    /**
     * Determines whether this {@code ComparableMapEntry} is equal to a specified object.
     * @param o the comparator object.
     * @return {@code true} if the object is a {@code ComparableMapEntry} with common key and value
     * classes and objects, else {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ComparableMapEntry<?, ?> convert)) {
            return false;
        }
        return this.key.equals(convert.key) && this.value.equals(convert.value);
    }

    /**
     * Provides the TrueText of this {@code ComparableMapEntry}.
     * @return this {@code ComparableMapEntry} in a parsable format.
     */
    @Override
    public String trueText() {
        return this.key + ">" + this.value;
    }

    /**
     * Converts this {@code ComparableMapEntry} to a printable format.
     * @return this {@code ComparableMapEntry} as a {@code String}.
     */
    @Override
    public String toString() {
        return "[" + this.key + " -> " + this.value + "]";
    }
}
