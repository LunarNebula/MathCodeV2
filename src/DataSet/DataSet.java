package DataSet;

import java.util.List;

public interface DataSet<Value extends Comparable<Value>> { //TODO: make implementations for BooleanSetOperable
    /**
     * Removes a specified Value from this DataSet
     * @param value the target Value
     * @return true if the target Value was previously contained in this DataSet, else false
     */
    boolean remove(Value value);

    /**
     * Removes all instances of a specified Value from this DataSet
     * @param value the target Value
     * @return the number of instances of this Value previously in this DataSet (or 0 if no such element existed)
     */
    int removeAll(Value value);

    /**
     * Finds the maximum Value in this DataSet
     * @return the Value 'o' in this DataSet such that for all values 't' in this DataSet, 'o' >= 't'
     */
    Value max();

    /**
     * Finds the minimum Value in this DataSet
     * @return the Value 'o' in this DataSet such that for all values 't' in this DataSet, 'o' <= 't'
     */
    Value min();

    /**
     * Finds all modes of this DataSet
     * @return a List of all modes: let c(p) equal the number of occurrences of 'p' in this DataSet. For all elements
     * 'e' in this DataSet, a mode 'm' of this DataSet satisfies c(m) >= c(e)
     */
    List<Value> modes();

    /**
     * Determines whether this DataSet contains a specific Value
     * @param value the target Value
     * @return true if this DataSet contains the specified Value, else false
     */
    boolean contains(Value value);

    /**
     * Finds the population of this DataSet
     * @return this.population
     */
    int size();
}
