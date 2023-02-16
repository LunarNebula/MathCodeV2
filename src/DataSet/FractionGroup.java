package DataSet;

import Algebra.Fraction;

import java.util.*;

public class FractionGroup extends DataGroup<Fraction> {
    private final HashMap<Fraction, Integer> map;
    private int size;

    /**
     * Creates a new FractionGroup
     * @param f an array of given Fractions
     */
    public FractionGroup(Fraction... f) {
        this.map = new HashMap<>();
        for(Fraction n : f) {
            add(n, false);
        }
        this.size = f.length;
    }

    /**
     * Creates a new FractionGroup
     * @param f a List of given Fractions
     */
    public FractionGroup(Collection<Fraction> f) {
        this.map = new HashMap<>();
        for(Fraction n : f) {
            add(n, false);
        }
        this.size = f.size();
    }

    /**
     * Adds an element to the FractionGroup
     * @param f the new element
     */
    @Override
    public boolean add(Fraction f) {
        return add(f, true);
    }

    /**
     * Adds a Fraction to this FractionGroup
     * @param f the new Fraction
     * @param incrementTotal true if the population should be incremented in this method, else false
     * @return true if the target Fraction was a new value in this FractionGroup, else false
     */
    private boolean add(Fraction f, boolean incrementTotal) {
        Integer count = this.map.get(f);
        if(incrementTotal) {
            this.size++;
        }
        if(count == null) {
            this.map.put(f, 1);
            return true;
        }
        this.map.replace(f, count + 1);
        return false;
    }

    /**
     * Removes an instance of a particular element from this FractionGroup
     * @param f the element instance
     * @return true if an instance of f was located in this FractionGroup, else false
     */
    @Override
    public boolean remove(Fraction f) {
        Integer count = this.map.remove(f);
        if(count == null) {
            return false;
        }
        if(count > 1) {
            this.map.put(f, count - 1);
            this.size--;
        }
        return true;
    }

    /**
     * Removes an instance of a particular element from this FractionGroup
     * @param f the element instance
     * @return the number of instances of f previously in this FractionGroup, including 0 if f did not exist in this FractionGroup
     */
    @Override
    public int removeAll(Fraction f) {
        Integer count = this.map.remove(f);
        if(count == null) {
            count = 0;
        } else {
            this.size -= count;
        }
        return count;
    }

    /**
     * Finds the maximum value in this FractionGroup
     * @return the unique value F such that f<=F for all 'f' in this FractionGroup
     */
    @Override
    public Fraction max() {
        Fraction max = null;
        for(Fraction f : this.map.keySet()) {
            if(max == null || max.compareTo(f) > 0) {
                max = f;
            }
        }
        return max;
    }

    /**
     * Finds the minimum value in this FractionGroup
     * @return the unique value F such that f>=F for all 'f' in this FractionGroup
     */
    @Override
    public Fraction min() {
        Fraction min = null;
        for(Fraction f : this.map.keySet()) {
            if(min == null || min.compareTo(f) < 0) {
                min = f;
            }
        }
        return min;
    }

    /**
     * Finds the range of this FractionGroup
     * @return the difference between the min and max of this FractionGroup
     */
    public Fraction range() {
        return max().subtract(min());
    }

    /**
     * Finds a list of all modes of this FractionGroup
     * @return a List of the most common items
     * MATHEMATICALLY: let p(m) equal the number of instances of m in this FractionGroup and let N be the FractionGroup
     *                 of modes in this FractionGroup. For all n in N and m in this FractionGroup, p(m) <= p(n).
     */
    @Override
    public List<Fraction> modes() {
        Integer maxCount = 0;
        List<Fraction> modes = new LinkedList<>();
        for(Fraction f : this.map.keySet()) {
            int count = this.map.get(f), compareTo = maxCount.compareTo(count);
            if(compareTo <= 0) {
                if(compareTo < 0) {
                    modes.clear();
                    maxCount = count;
                }
                modes.add(f);
            }
        }
        return modes;
    }

    /**
     * Finds the (arithmetic) mean of this FractionGroup
     * @return the average value in this FractionGroup
     */
    public Fraction arithmeticMean() {
        Fraction sum = Fraction.ZERO;
        for(Fraction f : this.map.keySet()) {
            sum = sum.add(f.multiply(new Fraction(this.map.get(f))));
        }
        return sum.divide(new Fraction(this.size));
    }

    /**
     * Finds the parallel sum of this FractionGroup
     * @return the inverse sum of the inverses of the values in this FractionGroup
     */
    public Fraction parallelSum() {
        Fraction sum = Fraction.ZERO;
        for(Fraction f : this.map.keySet()) {
            sum = sum.add(f.inverse().multiply(new Fraction(this.map.get(f))));
        }
        return sum.inverse();
    }

    /**
     * Finds the harmonic mean of this FractionGroup
     * @return the inverse mean of the inverses of the values in this FractionGroup
     */
    public Fraction harmonicMean() {
        return new Fraction(this.size).multiply(parallelSum());
    }

    /**
     * Finds the Lehmer mean of this FractionGroup
     * @param pow the specified power
     * @return the ratio of the sum of nth powers in this FractionGroup and (n-1)th powers in this FractionGroup
     */
    public Fraction LehmerMean(int pow) {
        Fraction nSum = Fraction.ZERO, divisorSum = Fraction.ZERO;
        for(Fraction f : this.map.keySet()) {
            Fraction term = new Fraction(this.map.get(f)).multiply(f.pow(pow - 1));
            divisorSum = divisorSum.add(term);
            nSum = nSum.add(term.multiply(f));
        }
        return nSum.divide(divisorSum);
    }

    /**
     * Finds the variance of this FractionGroup
     * @param isSample true if this FractionGroup is a sample, else false (population)
     * @return the average squared distance to the mean
     */
    public Fraction variance(boolean isSample) {
        Fraction variance = Fraction.ZERO;
        for(Fraction f : this.map.keySet()) {
            variance = variance.add(f.multiply(f).multiply(new Fraction(this.map.get(f))));
        }
        variance = variance.divide(new Fraction(this.size)).subtract(this.arithmeticMean().pow(2));
        if(isSample) {
            variance = variance.multiply(Fraction.ONE.add(new Fraction(1, this.size - 1)));
        }
        return variance;
    }

    /**
     * Finds the standard deviation of this FractionGroup
     * @param isSample true if this FractionGroup is a sample, else false (population)
     * @param scale the scale to which the answer is rounded (greater, positive value means more decimal places)
     * @return the average positive distance to the mean
     */
    public double standardDeviation(boolean isSample, int scale) {
        return Math.sqrt(variance(isSample).asBigDecimal(scale).doubleValue());
    }

    /**
     * Finds the median of this FractionGroup
     * @return the middle value in this sorted FractionGroup
     */
    public Fraction median() {
        return quantile(1, 2);
    }

    /**
     * Finds the specified quantile of this FractionGroup
     * @param dividend the number of quantiles before the target element
     * @param quantile the type of quantile
     * @return the element or value at the specified quantile
     */
    public Fraction quantile(int dividend, int quantile) {
        if(dividend < 0 || quantile < 1) {
            throw new IllegalArgumentException();
        }
        Fraction rawIndex = new Fraction(dividend, quantile).multiply(new Fraction(this.size));
        List<Fraction> elements = elementsAtIndices(rawIndex.floor().intValue(), rawIndex.round().intValue());
        return elements.get(0).add(elements.get(1)).divide(new Fraction(2)); // Finds the mean of two consecutive values
    }

    /**
     * Finds all elements in this FractionGroup at specified (ordered) indices
     * @param indices the target indices
     * @return the set of elements
     */
    public List<Fraction> elementsAtIndices(int... indices) {
        List<Fraction> sortedKeyList = new BST<>(this.map.keySet()).getOrderedList();
        HashMap<Fraction, Integer> accumulativeMap = new HashMap<>();
        int count = 0;
        for(Fraction f : sortedKeyList) {
            count += this.map.get(f);
            accumulativeMap.put(f, count);
        }
        List<Fraction> elementsAtIndices = new LinkedList<>();
        for(int i : indices) {
            if(i >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            ListIterator<Fraction> iterator = sortedKeyList.listIterator();
            Fraction target = iterator.next();
            while(accumulativeMap.get(target) < i) {
                target = iterator.next();
            }
            elementsAtIndices.add(target);
        }
        return elementsAtIndices;
    }

    /**
     * Sorts the elements in this FractionGroup into a List in increasing order
     * @return a List of Fractions contained in this FractionGroup such that for all a, b in this FractionGroup,
     *              if a appears before b then a<=b
     */
    public List<Fraction> sortedDistinctList() {
        return new BST<>(this.map.keySet()).getOrderedList();
    }

    /**
     * Determines whether this DataGroup contains a specific Fraction
     * @param f the target Fraction
     * @return true if this DataGroup contains the specified Fraction, else false
     */
    @Override
    public boolean contains(Fraction f) {
        return this.map.containsKey(f);
    }

    /**
     * Provides an Iterator over the Fractions in this FractionGroup
     * @return the Iterator
     */
    public Iterator<Fraction> iterator() {
        return new Iterator<>() {
            private final List<Fraction> list = FractionGroup.this.sortedDistinctList();
            private int individualCount = 0, itemCount = 0, totalCount = 0;

            /**
             * Determines whether there is another element in this FractionGroup
             * @return true if the total counter 
             */
            @Override
            public boolean hasNext() {
                return this.totalCount < FractionGroup.this.size;
            }

            @Override
            public Fraction next() {
                this.totalCount++;
                return null;
            }
        };
    }
}