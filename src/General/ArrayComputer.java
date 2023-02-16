package General;

import DataSet.BST;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ArrayComputer {
    /**
     * Reverses an array
     * @param items an array of items
     * @return the reversed array
     */
    public static int[] reverse(int[] items) {
        int[] newArray = new int[items.length];
        for(int i = 0; i < items.length; i++) {
            newArray[i] = items[items.length - 1 - i];
        }
        return newArray;
    }

    /**
     * Increments the indices of an array representing function call indices
     * @param indices the array of indices
     * @param start the index getting incremented
     * @param limit the exclusive upper limit to the value of any index in the array
     * @param zeroIsStart true if carryover from incrementation proceeds toward higher array indices, else false
     * @return the incremented array
     */
    public static int[] incrementIndices(int[] indices, int start, int limit, boolean zeroIsStart) {
        int[] test = reverse(zeroIsStart ? reverse(indices) : indices);
        test[start]++;
        if(test[start] == limit) {
            test[start] = 0;
            if(start + 1 < indices.length) {
                test = incrementIndices(test, start + 1, limit, true);
            }
        }
        return reverse(zeroIsStart ? reverse(test) : test);
    }

    // math

    /**
     * Finds the minimum of a set of numbers
     * @param values an array of ints
     * @return the minimum value in the array
     */
    public static int min(int... values) {
        if(values.length == 0) return 0;
        int min = values[0];
        for(int num : values) {
            if(num < min) {
                min = num;
            }
        }
        return min;
    }

    /**
     * Finds the minimum of a set of numbers
     * @param values an array of ints
     * @return the minimum value in the array
     */
    public static long min(long... values) {
        if(values.length == 0) return 0;
        long min = values[0];
        for(long num : values) {
            if(num < min) {
                min = num;
            }
        }
        return min;
    }

    /**
     * Finds the maximum of a set of numbers
     * @param values an array of ints
     * @return the maximum value in the array
     */
    public static int max(int... values) {
        if(values.length == 0) return 0;
        int min = values[0];
        for(int num : values) {
            if(num > min) {
                min = num;
            }
        }
        return min;
    }

    /**
     * Finds the maximum of a set of numbers
     * @param values an array of ints
     * @return the maximum value in the array
     */
    public static long max(long... values) {
        if(values.length == 0) return 0;
        long min = values[0];
        for(long num : values) {
            if(num > min) {
                min = num;
            }
        }
        return min;
    }

    /**
     * Finds the elements of a set that occur the most often
     * @param values a set of Integers
     * @return the modes of the set
     */
    public static List<Integer> modes(Integer... values) {
        List<Integer> orderedList = (new BST<>(values)).getOrderedList();
        ListIterator<Integer> orderedIterator = orderedList.listIterator();
        int prev = orderedIterator.next();
        HashMap<Integer, Integer> countMap = new HashMap<>();
        countMap.put(prev, 1);
        while(orderedIterator.hasNext()) {
            int value = orderedIterator.next();
            if(value == prev) {
                countMap.replace(prev, countMap.get(prev) + 1);
            } else {
                countMap.put(value, 1);
                prev = value;
            }
        }
        List<Integer> modes = new LinkedList<>();
        int count = 0;
        for(Integer key : countMap.keySet()) {
            int setSize = countMap.get(key);
            if(count < setSize) {
                modes = new LinkedList<>();
                count = setSize;
            }
            if(count == setSize) {
                modes.add(key);
            }
        }
        return modes;
    }
}