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
public class Coordinate<Data> implements TrueTextEncodable {
    //Stores a list of coordinates
    private final Data[] coordinates;
    //Stores the associated coordinate hashCodes
    private final int hashCode;

    /**
     * Creates a new Coordinate
     * @param coordinates the array of coordinates
     */
    @SafeVarargs
    public Coordinate(Data... coordinates) {
        this.coordinates = coordinates;
        final List<Integer> hashCodes = new ArrayList<>();
        for(Data c : coordinates) {
            hashCodes.add(c.hashCode());
        }
        this.hashCode = Objects.hash(hashCodes);
    }

    /**
     * Finds the hashCode of this {@code Coordinate}.
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
    public Data[] getCoordinates() {
        return this.coordinates;
    }

    /**
     * Sets a <code>Data</code> coordinate at a specific index
     * @param index the target index
     * @param coordinate the new <code>Data</code> value
     */
    public void setCoordinate(int index, Data coordinate) {
        this.coordinates[index] = coordinate;
    }

    /**
     * Determines whether this Coordinate is equal to another Object of type Coordinate
     * @param o the comparator Object
     * @return true if o is a Coordinate and its coordinates are equal to those in this Coordinate, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Coordinate<?> convert)) {
            return false;
        }
        if(convert.coordinates.length != this.coordinates.length) {
            return false;
        }
        for(int i = 0; i < this.coordinates.length; i++) {
            if(! this.coordinates[i].equals(convert.coordinates[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Provides the TrueText of this Coordinate
     * @return this Coordinate in a parsable format
     */
    @Override
    public String trueText() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(Data data : this.coordinates) {
            builder.append(delimiter).append(data);
            delimiter = "|";
        }
        return builder.toString();
    }

    /**
     * Converts this Coordinate to a printable format
     * @return this Coordinate as a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(Data c : this.coordinates) {
            builder.append(delimiter).append(c);
            delimiter = ", ";
        }
        return "(" + builder + ")";
    }

    /**
     * Prints this Coordinate
     */
    public void print() {
        System.out.println(this);
    }
}
