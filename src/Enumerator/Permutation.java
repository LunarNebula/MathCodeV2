package Enumerator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Permutation<Element> {
    private final int[] indices;
    private final int[] permutations;
    private final Element[] elements;
    private boolean hasCycled;

    /**
     * Creates a new Permutation with an array of elements
     * @param e the Element array
     */
    @SafeVarargs
    public Permutation(Element... e) {
        this.elements = e;
        System.arraycopy(e, 0, this.elements, 0, e.length);
        this.indices = new int[e.length];
        for(int i = 0; i < e.length; i++) {
            this.indices[i] = i;
        }
        this.permutations = new int[e.length];
        this.hasCycled = true;
    }

    /**
     * Returns the arrangement of these Indices
     * @return this.indices
     */
    public List<Element> getPermutation() {
        List<Element> values = new LinkedList<>();
        for(int i = 0; i < this.elements.length; i++) {
            values.add(this.elements[this.permutations[i]]);
        }
        return values;
    }

    /**
     * Determines whether this Permutation is in the starting orientation
     * @return this.hasCycled
     */
    public boolean hasCycled() {
        return this.hasCycled;
    }

    /**
     * Makes the next permutation
     */
    public void makeNextPermutation() {
        this.hasCycled = true;
        for(int i = 0; i < this.permutations.length; i++) {
            if (this.permutations[i] != i) {
                this.hasCycled = false;
                i = this.permutations.length;
            }
        }
        if(this.hasCycled) {
            for(int i = 0; i < this.indices.length; i++) {
                this.indices[i] = i;
            }
            Arrays.fill(this.permutations, 0);
        } else {
            int index = 1;
            while(this.permutations[index] == index) {
                index++;
            }
            this.permutations[index]++;
            int proxy = this.indices[0];
            this.indices[0] = this.indices[index];
            this.indices[index] = proxy;
            for(int i = 1; i < index; i++) {
                this.permutations[i] = 0;
            }
        }
    }

    /**
     * Finds the hashCode of this Permutation
     * @return the hashCode of the underlying array indices
     */
    @Override
    public int hashCode() {
        int hashCode = 0;
        for(int i = this.permutations.length; i > 0; i--) {
            hashCode = hashCode * i + this.permutations[i - 1];
        }
        return hashCode;
    }

    /**
     * Converts this Permutation to a printable format
     * @return this Permutation as a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(int i : this.indices) {
            builder.append(delimiter).append(i);
            delimiter = ", ";
        }
        return "[" + builder + "]";
    }

    /**
     * Prints this Permutation
     */
    public void print() {
        System.out.println(this);
    }
}
