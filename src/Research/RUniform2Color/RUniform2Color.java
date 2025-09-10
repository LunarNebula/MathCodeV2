package Research.RUniform2Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RUniform2Color {
    public static void run() {
        final int[][] edges = new int[][]{
                {0,1,14},
                {1,2,14},
                {2,3,14},
                {3,4,14},
                {4,5,14},
                {5,6,14},
                {6,7,14},
                {7,8,14},
                {8,9,14},
                {9,10,14},
                {10,11,14},
                {11,12,14},
                {12,10,14},
                {0,1,13},
                {1,2,13},
                {2,3,13},
                {3,4,13},
                {4,5,13},
                {5,6,13},
                {6,7,13},
                {7,8,13},
                {8,9,13},
                {9,10,13},
                {10,11,13},
                {11,12,13},
                {12,0,13},
                {0,3,7},
                {1,4,8},
                {2,5,9},
                {3,6,10},
                {4,7,11},
                {5,8,12},
                {6,9,0},
                {7,10,1},
                {8,11,2},
                {9,12,3},
                {10,0,4},
                {11,1,5},
                {12,2,6}
        };
        checkEdgeValidity(edges, 15);
    }

    public static void checkEdgeValidity(int[][] edges, int length) {
        final int FINAL_INDEX = 1 << length;
        for(int i = 0; i < FINAL_INDEX; i++) {
            final boolean[] track = new boolean[length];
            int j = i;
            int index = 0;
            while(j > 0) {
                if((j & 1) == 1) {
                    track[index] = true;
                }
                index++;
                j >>>= 1;
            }
            boolean passed = true;
            for(int[] item : edges) {
                boolean and = true, or = false;
                for (int value : item) {
                    and &= track[value];
                    or |= track[value];
                }
                if(and || !or) {
                    passed = false;
                    break;
                }
            }
            if(passed) {
                System.out.println(i);
            }
        }
    }

    public static long cyclicallyPermute(long value, int length, int dist) {
        dist = dist % length;
        final long ONE = 1,
                TOTAL_MASK = (ONE << length) - ONE,
                LOW_MASK = (ONE << dist) - ONE,
                HIGH_MASK = TOTAL_MASK - LOW_MASK;
        return ((value & HIGH_MASK) >>> dist) | ((value & LOW_MASK) << (length - dist));
    }

    public static void getLongestSubsequence() {
        final long[] syndeticSeq = new long[]{
                0b1101010101010,
                0b1101101101010,
                0b1101101011010,
                0b1110110101010,
                0b1110101101010,
                0b1110101011010,
                0b1110101010110,
                0b1111010101010
        };
        final int MAX = (int) Math.pow(13,7);
        long maxHammingWeight = 0;
        long finalStart = 0;
        int[] finalIndices = new int[7];
        for(int i = 0; i < MAX; i++) {
            int val = i, index = 0;
            final int[] indices = new int[7];
            while(val > 0) {
                indices[index++] = val % 13;
                val /= 13;
            }
            long start = syndeticSeq[7];
            for(int j = 0; j < 7; j++) {
                start &= cyclicallyPermute(syndeticSeq[j], 13, indices[j]);
            }
            long weight = getHammingWeight(start, 16);
            if(weight > maxHammingWeight) {
                maxHammingWeight = weight;
                finalStart = start;
                finalIndices = indices;
            }
        }
        System.out.println(finalStart);
        System.out.println(maxHammingWeight);
        General.Print.print(finalIndices);
    }

    public static long getHammingWeight(long d, int len) {
        if(d == 0) {
            return 0;
        } else if(d == 1) {
            return 1;
        }
        int newLen = len >>> 1;
        long sqrt = d >>> newLen;
        return getHammingWeight(sqrt, newLen) + getHammingWeight(d - (sqrt << newLen), newLen);
    }

    public static int countSequences(int length, int period) {
        final long ONE = 1, MAX = ONE << length;
        final long[] patterns = new long[]{
//                0b1000001000010001
//                ,
                0b10000100000100001
                ,
                0b100010001000001
                ,
                0b1000001000001000001
        };
        int totalCount = 0;
        for(long i = 1; i < MAX; i += 2) {
            long last = i | MAX;
            int count = 0;
            boolean passed = true;
            while(last > 0 && passed) {
                if(count == period) {
                    passed = false;
                } else {
                    long mod = last & 1;
                    if(mod == 0) {
                        count++;
                    } else {
                        count = 0;
                    }
                }
                last >>>= 1;
            }
            if(passed && containsPattern(i, patterns, length)
            ) { //755476      29249425
                totalCount++;
            }
        }
        return totalCount;
    }

    public static boolean containsPattern(long encoded, long[] patterns, int length) {
        for(long pattern : patterns) {
            for(int i = 0; i < length; i++) {
                if(((pattern & encoded) == pattern) || ((pattern & (~encoded)) == pattern)) {
                    return true;
                }
                pattern = cyclicallyPermute(pattern, length, 1);
            }
        }
        return false;
    }

    public static boolean isLocallyStarLike(int order, int[][] edges, int r) {
        final Stack<Integer> edgeStack = new Stack<>(), maxIndexStack = new Stack<>();
        final Stack<Boolean> checkStack = new Stack<>();
        final int[] vertexTracker = new int[order];
        edgeStack.push(0);
        maxIndexStack.push(0);
        checkStack.push(true);
        int concurrentVertices = 0;
        final int SATURATION_VALUE = (r << 1), LAST_INDEX = edges.length - 1;
        while(! edgeStack.isEmpty()) {
            int nextEdge = edgeStack.pop();
            boolean check = checkStack.pop();
            if(check) {
                edgeStack.push(nextEdge);
                checkStack.push(false);
                for(int vertex : edges[nextEdge]) {
                    if(vertexTracker[vertex] == 0) {
                        concurrentVertices++;
                    }
                    vertexTracker[vertex]++;
                    int maxIndex = maxIndexStack.peek();
                    if(vertexTracker[maxIndex] < vertexTracker[vertex]) {
                        maxIndexStack.push(vertex);
                    }
                }
                if(concurrentVertices <= SATURATION_VALUE && vertexTracker[maxIndexStack.peek()] < edgeStack.size()) {
                    return false;
                }
                if (nextEdge < LAST_INDEX && concurrentVertices <= SATURATION_VALUE) {
                    edgeStack.push(nextEdge + 1);
                    checkStack.push(true);
                }
            } else {
                if (nextEdge < LAST_INDEX) {
                    edgeStack.push(nextEdge + 1);
                    checkStack.push(true);
                }
                for (int vertex : edges[nextEdge]) {
                    vertexTracker[vertex]--;
                    if (vertexTracker[vertex] == 0) {
                        concurrentVertices--;
                    }
                }
                int maxIndex = maxIndexStack.pop();
                if (maxIndexStack.isEmpty() || vertexTracker[maxIndex] < vertexTracker[maxIndexStack.peek()]) {
                    maxIndexStack.push(maxIndex);
                }
            }
        }
        return true;
    }

    public static int countPartDisjointSets(int n) {
        final int MAX = (int) Math.pow(n, n-1);
        final int[][] indices = new int[MAX][];
        final int[] tuplePawn = new int[n-1];
        for(int i = 0; i < MAX; i++) {
            indices[i] = new int[n-1];
            System.arraycopy(tuplePawn, 0, indices[i], 0, n-1);
            int index = 0;
            boolean carry = true;
            while(carry && index < n-1) {
                tuplePawn[index]++;
                if(tuplePawn[index] == n) {
                    tuplePawn[index] = 0;
                    index++;
                } else {
                    carry = false;
                }
            }
        }
        final Stack<Integer> tupleStack = new Stack<>();
        final Stack<Boolean> checkStack = new Stack<>();
        final List<Integer> tupleList = new ArrayList<>();
        tupleList.add(0);
        tupleList.add(n+1);
        tupleStack.push(2*(n+1));
        checkStack.push(true);
        int maxSize = 2;
        while(! tupleStack.isEmpty()) {
            int tuple = tupleStack.pop();
            boolean check = checkStack.pop();
            if(check) {
                boolean passed = true;
                for(int tupleSet : tupleList) {
                    if(! isDistanceSufficient(indices[tuple], indices[tupleSet])) {
                        passed = false;
                        break;
                    }
                }
                tupleStack.push(tuple);
                checkStack.push(false);
                if(passed) {
                    tupleList.add(tuple);
                    if(tupleList.size() > maxSize) {
                        maxSize = tupleList.size();
                        System.out.println(maxSize);
                        System.out.println(tupleList);
                    }
                    if(tuple < MAX - 1) {
                        tupleStack.push(tuple + 1);
                        checkStack.push(true);
                    }
                }
            } else {
                if(tupleList.getLast() == tuple) {
                    tupleList.removeLast();
                }
                if(tuple < MAX - 1) {
                    tupleStack.push(tuple + 1);
                    checkStack.push(true);
                }
            }
        }
        return maxSize;
    }

    public static boolean isDistanceSufficient(int[] a, int[] b) {
        int countSame = 0;
        for(int i = 0; i < a.length; i++) {
            if(a[i] == b[i]) {
                countSame++;
            }
        }
        return countSame <= 2;
    }

    public static int[] convert10ToBase(int base, int val, int length) {
        final int[] newVal = new int[length];
        int index = 0;
        while(val != 0) {
            newVal[index++] = val % base;
            val /= base;
        }
        return newVal;
    }

    public static boolean isValidNewTransversal(List<int[]> transversals, int[] newTV, int countMax) {
        for(int[] TV : transversals) {
            int count = 0;
            for(int i = 0; i < TV.length; i++) {
                if(TV[i] == newTV[i]) count++;
            }
            if(count == 0 || countMax <= count) {
                return false;
            }
        }
        return true;
    }

    public static int countSimultaneousTransversals(int r) {
        final int[][] transversals = new int[(int) Math.pow(r-1, r)][];
        final int MAX_VALUE = transversals.length - 1;
        for(int i = 0; i <= MAX_VALUE; i++) {
            transversals[i] = convert10ToBase(r-1, i, r);
        }
        final List<int[]> runningList = new ArrayList<>();
        for(int[] transversal : transversals) {
            if(isValidNewTransversal(runningList, transversal, r-2)) {
                runningList.add(transversal);
            }
        }
        return runningList.size();
    }
}

