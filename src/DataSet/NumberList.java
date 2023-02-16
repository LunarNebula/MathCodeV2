package DataSet;

import General.TrueTextEncodable;

import java.util.*;

public class NumberList implements Iterable<Integer>, DataSet<Integer>, TrueTextEncodable {
    private List<Integer> list;

    /**
     * Generates a List of random Integers
     * @param min minimum possible value in the list, inclusive
     * @param max maximum possible value in the list, exclusive
     * @param size size of the set
     */
    public NumberList(int min, int max, int size) {
        Random random = new Random();
        this.list = new LinkedList<>();
        for(int i = 0; i < size; i++) {
            this.list.add(random.nextInt(max - min) + min);
        }
    }

    /**
     * Generates a List of sequential Integers in ascending order
     * @param start the starting value
     * @param size the length of the List
     */
    public NumberList(int start, int size) {
        this.list = new LinkedList<>();
        for(int i = start; i < start + size; i++) {
            this.list.add(i);
        }
    }

    /**
     * Generates a List of Integers from a preexisting List
     * @param list the given List
     */
    public NumberList(Collection<Integer> list) {
        this.list = new LinkedList<>(list);
    }

    /**
     * Generates a List of Integers from a preexisting array
     * @param ar the given array
     */
    public NumberList(int... ar) {
        this.list = new LinkedList<>();
        for(int element : ar) {
            this.list.add(element);
        }
    }

    /**
     * Generates a NumberList with the elements of another NumberList
     * @param n the given NumberList
     */
    public NumberList(NumberList n) {
        this.list = new LinkedList<>(n.list);
    }

    /**
     * Randomizes the order of integers in this NumberList
     */
    public void randomize() {
        Random random = new Random();
        final int HALF_SIZE = (size() + 1) / 2;
        int iterations = random.nextInt(HALF_SIZE) + HALF_SIZE;
        for(int i = 0; i < iterations; i++) {
            List<Integer> proxyList = new LinkedList<>();
            for(int n : this.list) {
                if(random.nextBoolean()) {
                    proxyList.add(n);
                } else {
                    proxyList.add(0, n);
                }
            }
            this.list = proxyList;
        }
    }

    /**
     * Adds a value into this NumberList
     * @param n the added value
     */
    public void add(int n) {
        this.list.add(n);
    }

    /**
     * Adds a value into this NumberList at a specific position
     * @param n the added value
     * @param pos the target position
     */
    public void insert(int n, int pos) {
        this.list.add(pos % (size() + 1), n);
    }

    /**
     * Inserts a value into this NumberList at a random position
     * @param n the added value
     */
    public void insertRandom(int n) {
        this.list.add(new Random().nextInt(size()), n);
    }

    /**
     * Removes an element from the NumberList
     * @param n the target value for deletion
     * @return true if an instance of the target number was successfully removed, else false
     */
    @Override
    public boolean remove(Integer n) {
        ListIterator<Integer> thisListIterator = this.list.listIterator();
        while (thisListIterator.hasNext()) {
            if(thisListIterator.next().equals(n)) {
                thisListIterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Removes all instances of an element from the NumberList
     * @param n the target element
     * @return the number of instances of the element previously in the NumberList
     */
    @Override
    public int removeAll(Integer n) {
        int quantity = 0;
        boolean continueRemoval = remove(n);
        while (continueRemoval) {
            quantity++;
            continueRemoval = remove(n);
        }
        return quantity;
    }

    /**
     * Finds the maximum value in this NumberList
     * @return the int 'o' in this NumberList such that for all values 't' in this NumberList, 'o' >= 't'
     */
    @Override
    public Integer max() {
        int max = 0;
        for(int i : this.list) {
            if(i > max) {
                max = i;
            }
        }
        return max;
    }

    /**
     * Finds the minimum value in this NumberList
     * @return the int 'o' in this NumberList such that for all values 't' in this NumberList, 'o' <= 't'
     */
    @Override
    public Integer min() {
        Integer min = null;
        for(int i : this.list) {
            if(min == null || min > i) {
                min = i;
            }
        }
        return min;
    }

    /**
     * Finds all modes of this NumberList
     * @return a List of all modes: let c(p) equal the number of occurrences of 'p' in this NumberList. For all elements
     * 'e' in this NumberList, a mode 'm' of this DataGroup satisfies c(m) >= c(e)
     */
    @Override
    public List<Integer> modes() {
        List<Integer> modes = new LinkedList<>();
        Map<Integer, Integer> map = new HashMap<>();
        int maxCount = 0;
        for(int i : this.list) {
            Integer count = map.get(i);
            if(count == null) {
                count = 0;
            }
            count++;
            map.put(i, count);
            int compareTo = count.compareTo(maxCount);
            if(compareTo >= 0) {
                if(compareTo > 0) {
                    modes.clear();
                    maxCount = count;
                }
                modes.add(i);
            }
        }
        return modes;
    }

    /**
     * Removes a random Integer from this NumberList
     * @return the removed Integer
     */
    public int removeRandom() {
        return this.list.remove(new Random().nextInt(size()));
    }

    /**
     * Gets the List of Integers in this NumberList
     * @return this.list
     */
    public List<Integer> getList() {
        return new LinkedList<>(this.list);
    }

    /**
     * Finds the size of this NumberList
     * @return this.list.size
     */
    @Override
    public int size() {
        return this.list.size();
    }

    /**
     * Gets an Iterator over the items in this NumberList
     * @return an Iterator for the embedded List of Integers
     */
    @Override
    public Iterator<Integer> iterator() {
        return this.list.iterator();
    }

    /**
     * Determines whether this NumberList contains a specified int
     * @param n the target int
     * @return true if n is contained in this NumberList, else false
     */
    @Override
    public boolean contains(Integer n) {
        for(int i : this.list) {
            if(i == n) {
                return true;
            }
        }
        return false;
    }

    /**
     * Provides the TrueText of this NumberList
     * @return this NumberList in a parsable format
     */
    @Override
    public String trueText() {
        StringBuilder builder = new StringBuilder();
        String includeDelimiter = "";
        for(int value : this.list) {
            builder.append(includeDelimiter).append(value);
            includeDelimiter = "|";
        }
        return builder.toString();
    }

    /**
     * Finds a printable version of this NumberList
     * @return this NumberList as a String
     */
    @Override
    public String toString() {
        return "" + this.list;
    }

    /**
     * Prints this NumberList
     */
    public void print() {
        System.out.println(this);
    }
}