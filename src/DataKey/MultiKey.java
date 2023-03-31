package DataKey;

import General.TrueTextEncodable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class that can be used to denote a single value mapped to by an ordered set of
 * other values.
 * @param <Data> the data type of the identification values.
 */
public class MultiKey<Data> implements TrueTextEncodable {
    //Stores a list of coordinates
    private final Data[] keys;
    //Stores the associated coordinate hashCodes
    private final int hashCode;

    /**
     * Creates a new {@code MultiKey}.
     * @param keys the array of keys used to denote this {@code Object} as a key.
     */
    @SafeVarargs
    public MultiKey(Data... keys) {
        this.keys = keys;
        final List<Integer> hashCodes = new ArrayList<>();
        for(Data c : keys) {
            hashCodes.add(c.hashCode());
        }
        this.hashCode = Objects.hash(hashCodes);
    }

    /**
     * Gets the dimension of this {@code MultiKey}.
     * @return the number of coordinate values.
     */
    public int size() {
        return this.keys.length;
    }

    /**
     * Finds the hashCode of this {@code MultiKey}.
     * @return {@code this.hashCode}
     */
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /**
     * Gets the value of these Coordinates
     * @return this.coordinates
     */
    public Data[] getKeys() {
        return this.keys;
    }

    /**
     * Sets a {@code Data} key at a specific index.
     * @param index the target index.
     * @param coordinate the new {@code Data} value.
     */
    public void setCoordinate(int index, Data coordinate) {
        this.keys[index] = coordinate;
    }

    /**
     * Determines whether this {@code MultiKey} is equal to another {@code Object} of type
     * {@code MultiKey}.
     * @param o the comparator {@code Object}
     * @return {@code true} if {@code o} is a {@code MultiKey} and its coordinates are
     * equal to those in this {@code MultiKey}, else {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof MultiKey<?> convert)) {
            return false;
        }
        if(convert.keys.length != this.keys.length) {
            return false;
        }
        for(int i = 0; i < this.keys.length; i++) {
            if(! this.keys[i].equals(convert.keys[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Provides the TrueText of this {@code MultiKey}.
     * @return this {@code MultiKey} in a parsable format.
     */
    @Override
    public String trueText() {
        final StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(Data data : this.keys) {
            builder.append(delimiter).append(data);
            delimiter = "|";
        }
        return builder.toString();
    }

    /**
     * Converts this {@code MultiKey}. to a printable format.
     * @return this {@code MultiKey} as a {@code String}.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(Data c : this.keys) {
            builder.append(delimiter).append(c);
            delimiter = ", ";
        }
        return "(" + builder + ")";
    }

    /**
     * Prints this {@code MultiKey}.
     */
    public void print() {
        System.out.println(this);
    }
}
