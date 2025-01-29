package Research.NCourt;

public class IntegerIndexCombination {
    protected final int max;
    protected final int[] indices;
    protected final boolean[] flags;

    /**
     * Creates a new {@code IIC}.
     * @param max the (exclusive) maximum value possible in this combination.
     * @param length the number of elements in this combination.
     */
    public IntegerIndexCombination(int max, int length) {
        this.max = max;
        if(length > max) {
            throw new IllegalArgumentException("Length exceeds maximum value");
        }
        this.flags = new boolean[length];
        final int[] indices = new int[length];
        int index = 0;
        while(index < length) {
            int oldIndex = index++;
            indices[oldIndex] = length - index;
        }
        this.indices = indices;
    }

    /**
     * Steps to the next combination of integers.
     * @return {@code true} if there are still combinations left, else {@code false}.
     */
    public boolean step() {
        boolean carry = true;
        int index = 0;
        while(carry && index < this.flags.length) {
            this.flags[index] = ! this.flags[index];
            if(this.flags[index]) {
                carry = false;
            } else {
                this.indices[index]++;
                if(index > 0) {
                    if(this.indices[index] == this.indices[index - 1]) {
                        //int
                    }
                } else if(this.indices[0] == this.max) {
                    this.indices[0] = this.indices[1] + 1;
                    index++;
                }
            }
        }
        return !carry;
    }

    /*
    void GospersHack(int k, int n)
{
    int set = (1 << k) - 1;
    int limit = (1 << n);
    while (set < limit)
    {
        DoStuff(set);

        // Gosper's hack:
        int c = set & - set;
        int r = set + c;
        set = (((r ^ set) >> 2) / c) | r;
    }
}
     */

    /**
     * Converts this {@code IIC} to a printable format.
     * @return this {@code IIC} as a {@code String}.
     */
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        for(int item : this.indices) {
            builder.append(item).append(' ');
        }
        builder.append("]\n[");
        for(boolean item : this.flags) {
            builder.append(item ? 1 : 0).append(' ');
        }
        return builder.append(']').toString();
    }

    /**
     * Prints this {@code IIC}.
     */
    public void print() {
        System.out.println(this);
    }
}
