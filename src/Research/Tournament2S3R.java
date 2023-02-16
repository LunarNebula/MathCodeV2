package Research;

import Exception.ExceptionMessage.TargetedMessage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Tournament2S3R {
    private final TournamentNode[] nodes;
    private int hashCode;
    private boolean hashCodeIsUpdated;

    /**
     * Creates a new <code>Tournament2S3R</code>
     * @param order the number of nodes
     * @param hashCode the hashcode of the desired <code>Tournament2S3R</code>
     */
    public Tournament2S3R(int order, int hashCode) {
        this.hashCode = hashCode;
        this.hashCodeIsUpdated = true;
        final TournamentNode[] nodes = new TournamentNode[order];
        for(int i = 0; i < order; i++) {
            nodes[i] = new TournamentNode(i);
            for(int j = 0; j < i; j++) {
                nodes[i].addArcFrom(nodes[j]).setColor(hashCode % 3);
                hashCode /= 3;
            }
        }
        this.nodes = nodes;
    }

    /**
     * Creates a new <code>Tournament2S3R</code> from a <code>DiscreteCounter</code>
     * @param counter the counter denoting the edge coloring
     */
    public Tournament2S3R(DiscreteCounter counter) {
        this.hashCodeIsUpdated = false;
        int[][] digits = counter.getDigits();
        TournamentNode[] nodes = new TournamentNode[digits.length];
        for(int i = 0; i < nodes.length; i++) {
            nodes[i] = new TournamentNode(i);
            for(int j = 0; j < i; j++) {
                nodes[i].addArcFrom(nodes[j]).setColor(digits[i][j]);
            }
        }
        this.nodes = nodes;
    }

    /**
     * Creates a new <code>Tournament2S3R</code> from a set of <code>Tournament2S3R</code> objects
     * @param nodes the nodes
     */
    public Tournament2S3R(TournamentNode... nodes) {
        this.nodes = nodes;
        this.hashCodeIsUpdated = false;
    }

    /**
     * Creates a new <code>Tournament2S3R</code>
     * @param filename the name of the file storing the arc numbers
     */
    public Tournament2S3R(String filename) {
        TournamentNode[] defaultNodes = new TournamentNode[0];
        try (Scanner scanner = new Scanner(new File(filename))) {
            TargetedMessage.scannerHasNextElement(scanner);
            final TournamentNode[] nodes = new TournamentNode[Integer.parseInt(scanner.next())];
            for(int i = 0; i < nodes.length; i++) {
                nodes[i] = new TournamentNode(i);
                for(int j = 0; j < i; j++) {
                    TargetedMessage.scannerHasNextElement(scanner);
                    nodes[i].addArcFrom(nodes[j]).setColor(scanner.nextInt());
                }
            }
            scanner.close();
            defaultNodes = nodes;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        this.nodes = defaultNodes;
        this.hashCodeIsUpdated = false;
    }

    /**
     * Gets the interior array of <code>TournamentNodes</code> from this <code>Tournament2S3R</code>
     * @return <code>this.nodes</code>
     */
    public TournamentNode[] getNodes() {
        return this.nodes;
    }

    /**
     * Compresses this <code>Tournament2S3R</code>. At each stage, the graph changes when and only when
     * the algorithm finds a rainbow triangle and changing the color of the hypotenuse of the triangle
     * does not introduce a different rainbow triangle. Runs in <code>O(n^3)</code> time
     * @return <code>true</code> if rainbow triangles still exist in the graph, else <code>false</code>
     */
    public boolean partiallyCompress() {
        final TournamentNode[] nodes = this.nodes;
        final int NUM_NODES = nodes.length, MIN_LENGTH = 2, NUM_COLORS = 3;
        for(int i = NUM_NODES - 1; i >= MIN_LENGTH; i--) {
            int maxMaxIndex = NUM_NODES - i;
            for(int j = 0; j < maxMaxIndex; j++) {
                final int max = i + j;
                final TournamentArc target = nodes[max].getArc(j);
                final boolean[] colorsUsed = new boolean[NUM_COLORS], colorsPreferred = new boolean[NUM_COLORS];
                for(int k = 0; k < NUM_NODES; k++) {
                    if(k != j && k != max) {
                        if(k < j) {
                            final int firstLegColor = nodes[j].getArc(k).getColor();
                            final int secondLegColor = nodes[max].getArc(k).getColor();
                            if(firstLegColor != secondLegColor) {
                                colorsUsed[NUM_COLORS - firstLegColor - secondLegColor] = true;
                            }
                        } else if(k > max) {
                            final int firstLegColor = nodes[k].getArc(j).getColor();
                            final int secondLegColor = nodes[k].getArc(max).getColor();
                            if(firstLegColor != secondLegColor) {
                                colorsUsed[NUM_COLORS - firstLegColor - secondLegColor] = true;
                            }
                        } else { // between - check for repeat edges
                            final int firstLegColor = nodes[k].getArc(j).getColor();
                            final int secondLegColor = nodes[max].getArc(k).getColor();
                            if(firstLegColor == secondLegColor) {
                                colorsPreferred[firstLegColor] = true;
                            }
                        }
                    }
                }
                for(int k = 0; k < NUM_COLORS; k++) {
                    if(colorsPreferred[k] && (! colorsUsed[k])) {
                        target.setColor(k);
                    }
                }
            }
        }
        boolean containsRainbowTriangles = false;
        for(int i = MIN_LENGTH; i < NUM_NODES; i++) {      // length of the hypotenuse (distance between vertices)
            int maxMaxIndex = NUM_NODES - i;
            for(int j = 0; j < maxMaxIndex; j++) {         // starting index of the interval
                int max = i + j;
                final TournamentArc hypotenuse = nodes[max].getArc(j);
                final boolean[] colorsUsed = new boolean[NUM_COLORS];  // tracks whether color is used
                final Stack<Integer> stack = new Stack<>();
                for(int k = 0; k < NUM_COLORS; k++) {
                    stack.push(k);
                }
                for(int k = j + 1; k < max; k++) {         // index of the middle vertex
                    final int firstLegColor = nodes[k].getArc(j).getColor();
                    final int secondLegColor = nodes[max].getArc(k).getColor();
                    if(firstLegColor != secondLegColor) {
                        colorsUsed[NUM_COLORS - firstLegColor - secondLegColor] = true;
                    }
                    stack.push(secondLegColor);
                }
                if(colorsUsed[0] && colorsUsed[1] && colorsUsed[2]) {
                    containsRainbowTriangles = true;            // checks if a rainbow triangle is forced
                } else if(colorsUsed[hypotenuse.getColor()]) {
                    int newColor = stack.pop();
                    while(colorsUsed[newColor]) {
                        newColor = stack.pop();
                    }
                    hypotenuse.setColor(newColor);
                    this.hashCodeIsUpdated = false;
                }
            }
        }
        return containsRainbowTriangles;
    }

    /**
     * Gets the set of coordinate triples for this <code>Tournament2S3R</code>
     * @return the coordinate triples
     */
    public Tournament2S3RCoordinate getCoordinateSet() {
        final TournamentNode[] nodes = this.nodes;
        final int[][] digits = new int[nodes.length][];
        for(int i = 0; i < nodes.length; i++) {
            digits[i] = new int[i];
            for(int j = 0; j < i; j++) {
                digits[i][j] = nodes[i].getArc(j).getColor();
            }
        }
        return new Tournament2S3RCoordinate(new DiscreteCounter(digits, 3));
    }

    /**
     * Creates a subgraph of this <code>Tournament2S3R</code>.
     * @param indices the indices of the <code>TournamentNode</code> objects
     * @return the new <code>Tournament2S3R</code>
     */
    public Tournament2S3R subgraph(List<Integer> indices) {
        List<Integer> indexTest = new LinkedList<>(indices);
        Collections.sort(indexTest);
        if(indexTest.get(indexTest.size() - 1) >= this.nodes.length) {
            throw new IndexOutOfBoundsException();
        }
        final TournamentNode[] nodes = new TournamentNode[indexTest.size()];
        int secondFinalIndex = 0;
        for(int secondIndex : indexTest) {
            nodes[secondFinalIndex] = new TournamentNode(secondFinalIndex);
            int firstFinalIndex = 0;
            for(int firstIndex : indexTest) {
                if(firstIndex < secondIndex) {
                    nodes[secondFinalIndex].addArcFrom(nodes[firstFinalIndex]).setColor(
                            this.nodes[secondIndex].getArc(firstIndex).getColor()
                    );
                    firstFinalIndex++;
                }
            }
            secondFinalIndex++;
        }
        return new Tournament2S3R(nodes);
    }

    /**
     * Determines whether this <code>Tournament2S3R</code> has any rainbow triangles
     * @return <code>true</code> if any triangles with three different edge colors are found,
     * else <code>false</code>
     */
    public boolean hasRainbowTriangles() {
        final TournamentNode[] nodes = this.nodes;
        final int TRIANGLE_COLORING = 7;
        for(int i = 0; i < nodes.length; i++) {
            for(int j = i + 1; j < nodes.length; j++) {
                for(int k = j + 1; k < nodes.length; k++) {
                    final int colorLeg1 = nodes[j].getArc(i).getColor();
                    final int colorLeg2 = nodes[k].getArc(j).getColor();
                    final int colorHypotenuse = nodes[k].getArc(i).getColor();
                    final int totalColor = (1 << colorLeg1) | (1 << colorLeg2) | (1 << colorHypotenuse);
                    if(totalColor == TRIANGLE_COLORING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Counts the total number of rainbow triangles in this <code>Tournament2S3R</code>
     * @return the number of triangles
     */
    public int numberOfRainbowTriangles() {
        final int[] sum = new int[]{1, 2, 4};
        final int TRIANGULAR_SUM = 7;
        int numberOfRainbowTriangles = 0;
        final TournamentNode[] nodes = this.nodes;
        for(int i = 0; i < nodes.length; i++) {
            for(int j = i + 1; j < nodes.length; j++) {
                for(int k = j + 1; k < nodes.length; k++) {
                    final int c1 = nodes[j].getArc(i).getColor();
                    final int c2 = nodes[k].getArc(i).getColor();
                    final int c3 = nodes[k].getArc(j).getColor();
                    if((sum[c1] + sum[c2] + sum[c3]) == TRIANGULAR_SUM) {
                        numberOfRainbowTriangles++;
                    }
                }
            }
        }
        return numberOfRainbowTriangles;
    }

    /**
     * Determines a unique <code>int</code> value for this <code>DiscreteCounter</code>
     * @return the <code>int</code> value
     */
    @Override
    public int hashCode() {
        if(this.hashCodeIsUpdated) {
            return this.hashCode;
        }
        this.hashCodeIsUpdated = true;
        int hashCode = 0;
        final int BASE = 3;
        for(int i = this.nodes.length - 1; i >= 0; i--) {
            for(int j = i - 1; j >= 0; j--) {
                hashCode = hashCode * BASE + this.nodes[i].getArc(j).getColor();
            }
        }
        this.hashCode = hashCode;
        return hashCode;
    }

    /**
     * Converts this <code>Tournament2S3R</code> to a printable format
     * @return this <code>Tournament2S3R</code> as a <code>String</code>
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String newLine = "";
        final int length = this.nodes.length;
        for(int i = 1; i < length; i++) {
            builder.append(newLine);
            newLine = "\n";
            for(int j = 0; j < i; j++) {
                builder.append(this.nodes[i].getArc(j).getColor()).append("\t");
            }
        }
        return builder.toString();
    }

    /**
     * Prints this <code>Tournament2S3R</code>
     */
    public void print() {
        System.out.println(this);
    }
}
