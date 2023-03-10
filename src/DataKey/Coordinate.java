package DataKey;

import General.TrueTextEncodable;

/**
 * A class that can be used to denote a single value mapped to by an ordered set of
 * other values.
 * @param <Data> the data type of the identification values.
 */
public class Coordinate<Data> implements TrueTextEncodable {
    //Stores a list of coordinates
    private final Data[] coordinates;
    //Stores the associated coordinate hashCodes
    private final int[] hashCodes;

    /**
     * Creates a new Coordinate
     * @param coordinates the array of coordinates
     */
    @SafeVarargs
    public Coordinate(Data... coordinates) {
        this.coordinates = coordinates;
        this.hashCodes = new int[this.coordinates.length];
        for(int i = 0; i < this.hashCodes.length; i++) {
            this.hashCodes[i] = this.coordinates[i].hashCode();
        }
    }

    /**
     * Finds the hashCode of this Coordinate
     * @return the unique int value representing an encoding of this Coordinate
     */
    @Override
    public int hashCode() {
        int code = 0;
        for(int i = 0; i < this.hashCodes.length; i++) {
            int partialSum = 0, product = 1;
            for(int j = 0; j <= i; j++) {
                partialSum += this.hashCodes[j];
            }
            final int LIMIT = i + 1;
            for(int j = 1; j <= LIMIT; j++) {
                product *= partialSum;
                product /= j;
                partialSum++;
            }
            code += product;
        }
        return code;
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
