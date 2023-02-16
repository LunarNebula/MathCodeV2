package Enumerator;

import java.util.*;

public class Combination<Element> {
    private final Element[] elements;
    private final Stack<Integer> indices;

    /**
     * Constructs a new Combination
     * @param e the array of Elements to choose from
     */
    @SafeVarargs
    public Combination(Element... e) {
        this.elements = e;
        this.indices = new Stack<>();
    }

    /**
     * Transitions this Combination to the next Combination
     */
    public void makeNextCombination() {
        if(this.indices.isEmpty()) {
            for(int i = this.elements.length - 1; i >= 0; i--) {
                this.indices.push(i);
            }
        } else {
            int start = this.indices.pop();
            while(start-- > 0) {
                this.indices.push(start);
            }
        }
    }

    /**
     * Gets this Combination of elements
     * @return the list
     */
    public List<Element> getCombination() {
        List<Element> list = new LinkedList<>();
        for(int index : this.indices) {
            list.add(this.elements[index]);
        }
        return list;
    }

    /**
     * Gets the hashCode of this Combination
     * @return the Integer representation of the values in the Stack field
     */
    @Override
    public int hashCode() {
        int prevValue = 0, hashCode = 0;
        for(int index : this.indices) {
            hashCode = (hashCode << (index - prevValue)) | 1;
            prevValue = index;
        }
        return hashCode;
    }

    /**
     * Converts this Combination to a printable format
     * @return this Combination as a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        for(int index : this.indices) {
            builder.append(delimiter).append(this.elements[index]);
            delimiter = ", ";
        }
        return "[" + builder + "]";
    }

    /**
     * Prints this Combination
     */
    public void print() {
        System.out.println(this);
    }
}
