package DataKey;

import General.TrueTextEncodable;

/**
 * A wrapper class for sorting objects in a list. This can be used for hierarchical sorting
 * when the different sorting levels are of the same type. For different data types, an
 * {@code Index} object can be used. This object is typically used as the key for a
 * {@code ComparableMapEntry}.
 * @param indices the array of indices in decreasing hierarchical order.
 * @param <Data> the data type of the indices.
 * @see ComparableMapEntry
 * @see Index
 */
public record ConstantIndex<Data extends Comparable<Data>>(Data... indices)
        implements Comparable<ConstantIndex<Data>>, TrueTextEncodable {

    /**
     * Gets the indices of this {@code ConstantIndex}.
     * @return {@code this.indices}
     */
    @Override
    public Data[] indices() {
        return this.indices;
    }

    /**
     * Gets the hashCode of this {@code ConstantIndex}.
     * @return the unique int identifier for this {@code ConstantIndex}.
     */
    @Override
    public int hashCode() {
        return new Coordinate<>(this.indices).hashCode();
    }

    /**
     * Compares this {@code ConstantIndex} to a specified object.
     * @param o the comparator object.
     * @return given the parameters, return one of the following:
     * <ul>
     *     <li>-1 if the object is "less than" o.</li>
     *     <li>0 if the object is "equal to" o.</li>
     *     <li>1 if the object is "greater than" o.</li>
     * </ul>
     * @throws NullPointerException     if the specified object is null
     * @throws ClassCastException       if the specified Object's type prevents it from being compared to this Object
     * @throws IllegalArgumentException if the specified ConstantIndex is of different dimension than this ConstantIndex
     */
    @Override
    public int compareTo(ConstantIndex<Data> o) {
        if (this.indices.length != o.indices.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < this.indices.length; i++) {
            int compare = this.indices[i].compareTo(o.indices[i]);
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }

    /**
     * Provides the TrueText of this ConstantIndex
     *
     * @return this ConstantIndex in a parsable format
     */
    @Override
    public String trueText() {
        return toString();
    }

    /**
     * Converts this ConstantIndex to a printable format
     *
     * @return this ConstantIndex as a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for (Data data : this.indices) {
            builder.append(delimiter).append(data);
            delimiter = "|";
        }
        return builder.toString();
    }
}
