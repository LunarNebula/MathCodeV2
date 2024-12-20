package Research.TNConfigurations;

import Exception.*;

import java.util.ArrayList;
import java.util.List;

public class TSequence {
    private static final String[] T_SEQUENCE = {"-", "+"};
    private int sequence;
    private final int length;

    /**
     * Creates a new {@code TSequence}.
     * @param str the character string symbolizing the {@code TSequence}.
     * @throws IllegalArgumentException if the string does not symbolize a valid T-sequence.
     */
    public TSequence(String str) throws IllegalArgumentException {
        this.length = str.length();
        int sequence = 0;
        for(char c : str.toCharArray()) {
            sequence <<= 1;
            if(c == '+') {
                sequence |= 1;
            } else if(c != '-') {
                throw new IllegalArgumentException(ExceptionMessage.ARGUMENT_EXCEEDS_REQUIRED_DOMAIN());
            }
        }
        this.sequence = sequence;
    }

    /**
     * Creates a new {@code TSequence}.
     * @param n the binary encoding of the T-sequence.
     * @param length the length of the T-sequence.
     */
    public TSequence(int n, int length) {
        this.sequence = n;
        this.length = length;
    }

    /**
     * Rotates the bit sequence in this {@code TSequence} to the right by one.
     */
    public void rotateBitsRight() {
        this.sequence = rotateBitsRight(this.sequence, this.length);
    }

    /**
     * Converts this {@code TSequence} to a printable format.
     * @return this {@code TSequence} as a {@code String}.
     */
    @Override
    public String toString() {
        int num = this.sequence;
        final StringBuilder builder = new StringBuilder();
        int count = 0;
        if(num == 0) {
            builder.append(0);
            count++;
        }
        while(num != 0 && count++ < length) {
            builder.insert(0, T_SEQUENCE[num & 1]);
            num >>>= 1;
        }
        if(count < length) {
            int cropLength = length - count;
            builder.insert(0, T_SEQUENCE[0].repeat(cropLength));
        }
        return builder.toString();
    }

    /**
     * Prints this {@code TSequence}.
     */
    public void print() {
        System.out.println(this);
    }

    // Static methods

    /**
     * Finds the set of cases not covered by any of the following formulae:
     * <ul>
     *     <li>+^N</li>
     *     <li>-^N</li>
     *     <li>+^k-+^(N-1-k)</li>
     *     <li>-^k+-^(N-1-k)</li>
     *     <li>*+^k-^(N-2-k)*</li>
     *     <li>Any cyclic permutation of the above.</li>
     * </ul>
     * @param length the length of the T-sequence.
     * @return the list of unsolved T-sequences, independent of cyclic permutations.
     */
    public static List<Integer> uncoveredCases(int length) {
        final boolean[] tested = new boolean[1 << length];
        final boolean[] track = new boolean[tested.length];
        final int NEG_POS_MASK = 1 << (length - 1);
        tested[0] = true;
        track[0] = true;
        tested[track.length - 1] = true;
        track[track.length - 1] = true;
        int initialIndex = 1;
        for(int i = 0; i < length; i++) {
            tested[initialIndex] = true;
            track[initialIndex] = true;
            final int oneIndex = track.length - 1 - initialIndex;
            tested[oneIndex] = true;
            track[oneIndex] = true;
            initialIndex <<= 1;
        }
        final List<Integer> uncoveredCases = new ArrayList<>();
        int coveredCases = 0;
        for(int i = 0; i < track.length; i++) {
            int revolveI = i;
            if(! tested[i]) {
                boolean isCovered = false;
                for(int j = 0; j < length && ! isCovered; j++) {
                    int test = (revolveI | NEG_POS_MASK) >> 1;
                    while((test & 1) == 0) {
                        test >>>= 1;
                    }
                    while((test & 1) == 1) {
                        test >>>= 1;
                    }
                    revolveI = rotateBitsRight(revolveI, length);
                    isCovered = (test == 0);
                }
                int maxCase = 0;
                for(int j = 0; j < length; j++) {
                    tested[revolveI] = true;
                    if(isCovered) {
                        track[revolveI] = true;
                    } else {
                        maxCase = Math.max(maxCase, revolveI);
                    }
                    revolveI = rotateBitsRight(revolveI, length);
                }
                if(! isCovered) {
                    uncoveredCases.add(maxCase);
                }
            }
            if(track[i]) {
                coveredCases++;
            }
        }
        return uncoveredCases;
    }

    /**
     * Cyclically permutes the bits of a specified integer.
     * @param bits the integer to be permuted.
     * @param length the number of bits to be considered for permutation.
     * @return the permuted integer.
     */
    public static int rotateBitsRight(int bits, int length) {
        return ((bits & ((1 << length) - 1)) >>> 1) | ((bits & 1) << (length - 1));
    }

    /**
     * Converts a list of integer values into a list of {@code TSequence} objects.
     * @param ints the target ints.
     * @param length the bit length for each {@code TSequence}.
     * @return the list of {@code TSequence} values.
     */
    public static List<TSequence> valuesOf(List<Integer> ints, int length) {
        final List<TSequence> list = new ArrayList<>();
        for(int i : ints) {
            list.add(new TSequence(i, length));
        }
        return list;
    }
}
