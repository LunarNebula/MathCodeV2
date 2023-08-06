package Research.TNConfigurations;

public class TSequence {
    private static final String[] T_SEQUENCE = {"-", "+"};

    /**
     * Computes the number of cases covered by one or more of the following formulae:
     * <ul>
     *     <li>+^N</li>
     *     <li>-^N</li>
     *     <li>+^k-+^(N-1-k)</li>
     *     <li>-^k+-^(N-1-k)</li>
     *     <li>*+^k-^(N-2-k)*</li>
     *     <li>Any cyclic permutation of the above.</li>
     * </ul>
     * @param length the length of the T-sequence.
     * @param print {@code true} if each uncovered T-sequence (independent up to
     *                          cyclic permutation) should be printed to console.
     * @param isTSequence {@code true} if the print statement should print a
     *                                T-sequence, else {@code false} if it should
     *                                print a binary value.
     * @return the number of solved T-sequences.
     */
    public static int numberOfCoveredCases(int length, boolean print, boolean isTSequence) {
        final boolean[] tested = new boolean[1 << length];
        final boolean[] track = new boolean[tested.length];
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
        int coveredCases = 0;
        for(int i = 0; i < track.length; i++) {
            int revolveI = i;
            if(! tested[i]) {
                boolean isCovered = false;
                for(int j = 0; j < length && ! isCovered; j++) {
                    int test = rotateBitsRight((revolveI << 1) | 1, length) >>> 1;
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
                if(print && ! isCovered) {
                    printBinary(maxCase, length, isTSequence);
                }
            }
            if(track[i]) {
                coveredCases++;
            }
        }
        return coveredCases;
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
     * Prints a specified integer as a binary value of a specified length.
     * @param num the target integer.
     * @param length the number of bits to be represented in the print statement. If
     *               this value is less than the number of bits required to write the
     *               number, part of the number will be cut off. If it is greater, the
     *               extra bits will be set to zero.
     * @param isTSequence {@code true} if the value should be printed as a T-sequence,
     *                                else {@code false} if it should be printed as a
     *                                raw binary value.
     */
    public static void printBinary(int num, int length, boolean isTSequence) {
        final StringBuilder builder = new StringBuilder();
        int count = 0;
        if(num == 0) {
            builder.append(0);
            count++;
        }
        while(num != 0 && count++ < length) {
            if(isTSequence) {
                builder.insert(0, T_SEQUENCE[num & 1]);
            } else {
                builder.insert(0, num & 1);
            }
            num >>>= 1;
        }
        if(count < length) {
            int cropLength = length - count;
            builder.insert(0, (isTSequence ? T_SEQUENCE[0] : "0").repeat(cropLength));
        }
        System.out.println(builder);
    }
}
