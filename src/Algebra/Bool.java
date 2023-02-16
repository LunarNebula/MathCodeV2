package Algebra;

public class Bool implements Comparable<Bool>, BooleanOperable<Bool> {
    private boolean bool;

    /**
     * Initializes this Bool to TRUE
     */
    public Bool() {
        set(true);
    }

    /**
     * Initializes this Bool
     * @param bool the starting value of this Bool
     */
    public Bool(boolean bool) {
        set(bool);
    }

    /**
     * Sets this Bool to a specified value
     * @param bool this new bool value
     */
    public void set(boolean bool) {
        this.bool = bool;
    }

    /**
     * Computes the logical OR
     * @param a the comparator Bool
     * @return this OR a
     */
    @Override
    public Bool OR(Bool a) {
        return new Bool(this.bool || a.bool);
    }

    /**
     * Computes the logical NOT
     * @return NOT this
     */
    public Bool NOT() {
        return new Bool(! this.bool);
    }

    /**
     * Computes the logical NOR
     * @param a the comparator Bool
     * @return this NOR a
     */
    public Bool NOR(Bool a) {
        return OR(a).NOT();
    }

    /**
     * Computes the logical NAND
     * @param a the comparator Bool
     * @return this NAND a
     */
    public Bool NOT_AND(Bool a) {
        return NOT().OR(a.NOT());
    }

    /**
     * Computes the logical AND
     * @param a the comparator Bool
     * @return this AND a
     */
    @Override
    public Bool AND(Bool a) {
        return NOT_AND(a).NOT();
    }

    /**
     * Computes the logical XNOR
     * @param a the comparator Bool
     * @return this XNOR a
     */
    public Bool XNOR(Bool a) {
        return new Bool(this.bool == a.bool);
    }

    /**
     * Computes the logical XOR
     * @param a the comparator Bool
     * @return this XOR a
     */
    @Override
    public Bool XOR(Bool a) {
        return XNOR(a).NOT();
    }

    /**
     * Computes the logical AND_NOT
     * @param a the comparator Bool
     * @return this AND NOT a
     */
    @Override
    public Bool AND_NOT(Bool a) {
        return AND(a.NOT());
    }

    /**
     * Computes the logical IMPLIES
     * @param a the comparator Bool
     * @return this IMPLIES a
     */
    public Bool IMPLIES(Bool a) {
        return NOT_AND(a.NOT());
    }

    /**
     * Gets the boolean value of this Bool
     * @return this.bool
     */
    public boolean getBoolean() {
        return this.bool;
    }

    /**
     * Determines whether this Bool is equal to a specified comparator Bool
     * @param o the comparator Bool
     * @return 0 if this Bool matches the comparator Bool, else -1 if this Bool is false, else 1
     */
    @Override
    public int compareTo(Bool o) {
        return (this.bool ? 0 : 1) + (o.bool ? 0 : -1);
    }

    /**
     * Determines whether this Bool is equal to a specified Object
     * @param o the comparator Object
     * @return true if the comparator is a Bool with congruent bool field
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Bool && ((Bool) o).bool == this.bool;
    }

    /**
     * Converts this Bool to a printable format
     * @return this Bool as a String
     */
    @Override
    public String toString() {
        return this.bool ? "true" : "false";
    }

    /**
     * Prints this Bool
     */
    public void print() {
       System.out.println(toString());
    }

    // static methods

    /**
     * Finds the logical AND for an array of Bools
     * @param a the Bool array
     * @return the Bool representing the combined Bool
     */
    public static Bool AND(Bool... a) {
        for(Bool bool : a) {
            if(! bool.bool) {
                return new Bool(false);
            }
        }
        return new Bool();
    }

    /**
     * Finds the logical OR for an array of Bools
     * @param a the Bool array
     * @return the Bool representing the combined Bool
     */
    public static Bool OR(Bool... a) {
        for(Bool bool : a) {
            if(bool.bool) {
                return new Bool();
            }
        }
        return new Bool(false);
    }

    /**
     * Finds the logical AND for an Iterable List of Bools
     * @param a the Bool Iterable List
     * @return the Bool representing the combined Bool
     */
    public static Bool AND(Iterable<Bool> a) {
        for(Bool bool : a) {
            if(! bool.bool) {
                return new Bool(false);
            }
        }
        return new Bool();
    }

    /**
     * Finds the logical OR for an Iterable List of Bools
     * @param a the Bool Iterable List
     * @return the Bool representing the combined Bool
     */
    public static Bool OR(Iterable<Bool> a) {
        for(Bool bool : a) {
            if(bool.bool) {
                return new Bool();
            }
        }
        return new Bool(false);
    }
}
