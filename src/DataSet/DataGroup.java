package DataSet;

import General.TrueTextEncodable;

import java.util.*;

public class DataGroup<Value extends Comparable<Value>> implements DataSet<Value>, TrueTextEncodable {
    private final HashMap<Value, Integer> map;
    private int size;

    /**
     * Creates a new DataGroup
     * @param values the Values initiated in this DataGroup
     */
    @SafeVarargs
    public DataGroup(Value... values) {
        this.map = new HashMap<>();
        for(Value value : values) {
            add(value, false);
        }
        this.size = values.length;
    }

    /**
     * Creates a new DataGroup
     * @param values the Values initiated in this DataGroup
     */
    public DataGroup(Collection<Value> values) {
        this.map = new HashMap<>();
        for(Value value : values) {
            add(value, false);
        }
        this.size = values.size();
    }

    /**
     * Adds a new Value to this DataGroup
     * @param value the new Value
     * @return true if the Value was not previously contained in this DataGroup, else false
     */
    public boolean add(Value value) {
        return add(value, true);
    }

    /**
     * Adds a new Value to this DataGroup
     * @param value the new Value
     * @param incrementTotal true if adding the new Value should increment the total population, else false
     * @return true if the Value was not previously contained in this DataGroup, else false
     */
    private boolean add(Value value, boolean incrementTotal) {
        Integer population = this.map.get(value);
        if(incrementTotal) {
            this.size++;
        }
        if(population == null) {
            this.map.put(value, 1);
            return true;
        }
        this.map.replace(value, population + 1);
        return false;
    }

    /**
     * Attempts to remove a Value from this DataGroup
     * @param value the target Value
     * @return true if the Value previously existed in this DataGroup, else false
     */
    @Override
    public boolean remove(Value value) {
        Integer population = this.map.remove(value);
        if(population == null) {
            return false;
        } else if(population > 1) {
            this.map.put(value, population - 1);
        }
        return true;
    }

    /**
     * Removes all instances of a specific Value from this DataGroup
     * @param value the target value
     * @return the number of instances of the target Value previously in this DataGroup
     */
    @Override
    public int removeAll(Value value) {
        Integer count = this.map.remove(value);
        if(count == null) {
            count = 0;
        } else {
            this.size -= count;
        }
        return count;
    }

    /**
     * Finds the maximum Value in this DataGroup
     * @return the Value 'o' such that for all 't' in this DataGroup, 'o' >= 't'
     */
    @Override
    public Value max() {
        Value max = null;
        for(Value value : this.map.keySet()) {
            if(max == null || value.compareTo(max) > 0) {
                max = value;
            }
        }
        return max;
    }

    /**
     * Finds the minimum Value in this DataGroup
     * @return the Value 'o' such that for all 't' in this DataGroup, 'o' <= 't'
     */
    @Override
    public Value min() {
        Value min = null;
        for(Value value : this.map.keySet()) {
            if(min == null || value.compareTo(min) < 0) {
                min = value;
            }
        }
        return min;
    }

    /**
     * Finds all modes of this DataGroup
     * @return a List of all modes: let c(p) equal the number of occurrences of 'p' in this DataGroup. For all elements
     * 'e' in this DataGroup, a mode 'm' of this DataGroup satisfies c(m) >= c(e)
     */
    @Override
    public List<Value> modes() {
        Integer maxCount = 0;
        List<Value> modes = new LinkedList<>();
        for(Value key : this.map.keySet()) {
            int count = this.map.get(key), compareTo = maxCount.compareTo(count);
            if(compareTo <= 0) {
                if(compareTo < 0) {
                    modes.clear();
                    maxCount = count;
                }
                modes.add(key);
            }
        }
        return modes;
    }

    /**
     * Determines whether this DataGroup contains a specific Value
     * @param value the target Value
     * @return true if this DataGroup contains the specified Value, else false
     */
    @Override
    public boolean contains(Value value) {
        return this.map.containsKey(value);
    }

    /**
     * Finds the population of this DataGroup
     * @return this.population
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Provides the TrueText of this DataGroup
     * @return this DataGroup in a parsable format
     */
    @Override
    public String trueText() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(Value key : this.map.keySet()) {
            builder.append(delimiter).append(key).append(":").append(this.map.get(key));
            delimiter = "|";
        }
        return builder.toString();
    }

    /**
     * Converts this DataGroup to a printable format
     * @return this DataGroup as a String
     */
    @Override
    public String toString() {
        return "[" + trueText() + "]";
    }
}
