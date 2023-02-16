package Enumerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class PermutationTest<Element> {
    private final Element[] elements;
    private final int[] indices;
    private final Stack<Integer> switchIndices, counter;
    private int hashCode;

    /**
     * Creates a new PermutationTest
     * @param e an array of permutable Elements
     * @throws IllegalArgumentException if there are no elements in this PermutationTest
     */
    @SafeVarargs
    public PermutationTest(Element... e) throws IllegalArgumentException {
        if(e.length == 0) {
            throw new IllegalArgumentException();
        }
        this.elements = e;
        this.indices = new int[e.length];
        this.switchIndices = new Stack<>();
        this.counter = new Stack<>();
        setUpInitialPermutation();
    }

    /**
     * Makes the next permutation for this PermutationTest
     */
    public void makeNextPermutation() {
        if(this.switchIndices.isEmpty()) {
            setUpInitialPermutation();
        } else {
            int index = this.switchIndices.pop(), counter = this.counter.pop();
            int proxy = this.indices[index], proxyIndex = index - 1;
            this.indices[index] = this.indices[proxyIndex];
            this.indices[proxyIndex] = proxy;
            if(counter > 1) {
                this.switchIndices.push(index);
                this.counter.push(counter - 1);
            }
            while(index-- > 1) {
                this.switchIndices.push(index);
                this.counter.push(index);
            }
        }
    }

    /**
     * Sets up the initial configuration for this PermutationTest
     */
    private void setUpInitialPermutation() {
        this.hashCode = 0;
        int counter = this.elements.length;
        while(counter-- > 1) {
            this.counter.push(counter);
            this.switchIndices.push(counter);
            this.indices[counter] = counter;
        }
        this.indices[0] = 0;
    }

    /**
     * Gets the current permutation for this PermutationTest
     * @return the Elements in this PermutationTest in their specified order
     */
    public List<Element> getPermutation() {
        List<Element> permutation = new LinkedList<>();
        for(int i : this.indices) {
            permutation.add(this.elements[i]);
        }
        return permutation;
    }

    /**
     * Finds the hashCode of this PermutationTest
     * @return the unique Integer value representing this PermutationTest
     */
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /**
     * Converts this PermutationTest to a printable format
     * @return this PermutationTest as a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(int i : this.indices) {
            builder.append(delimiter).append(this.elements[i]);
            delimiter = ", ";
        }
        return "[" + builder + "]";
    }

    /**
     * Prints this PermutationTest
     */
    public void print() {
        System.out.println(this);
        //System.out.println(this.switchIndices + "" + this.counter);
    }
}
