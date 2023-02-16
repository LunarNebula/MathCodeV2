package Research;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tournament2S3RCoordinate {
    private final int[][] pathCounter;
    private static final int NUM_COORDS = 3;

    /**
     * Creates a new <code>Tournament2S3RCoordinate</code>
     * @param counter the digital encoding of the edge colorings
     */
    public Tournament2S3RCoordinate(@NotNull DiscreteCounter counter) {
        if(counter.base() != NUM_COORDS) {
            throw new ArithmeticException("Wrong number of coordinates.");
        }
        final int[][] edges = counter.getDigits();
        final int[][] pathCounter = new int[edges.length][];
        for(int i = 0; i < edges.length; i++) {
            pathCounter[i] = new int[NUM_COORDS];
            for(int j = 0; j < NUM_COORDS; j++) {
                pathCounter[i][j] = 1;
            }
            for(int j = 0; j < i; j++) {
                for(int k = 0; k < NUM_COORDS; k++) {
                    if(edges[i][j] != k) {
                        pathCounter[i][k] = Math.max(pathCounter[i][k], pathCounter[j][k] + 1);
                    }
                }
            }
        }
        this.pathCounter = pathCounter;
    }

    /**
     * Creates a new <code>Tournament2S3RCoordinate</code>
     * @param pathCounter the beginning set of pathCounter coordinates
     */
    public Tournament2S3RCoordinate(int[][] pathCounter) {
        this.pathCounter = pathCounter;
    }

    /**
     * Generates a new <code>Tournament2S3RCoordinate</code> from a file
     * @param filename the path to the input file
     * @throws FileNotFoundException if the input file at <code>filename</code> does not exist
     */
    public Tournament2S3RCoordinate(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        this.pathCounter = new int[scanner.nextInt()][];
        for(int i = 0; i < this.pathCounter.length && scanner.hasNext(); i++) {
            this.pathCounter[i] = new int[NUM_COORDS];
            for(int j = 0; j < NUM_COORDS && scanner.hasNext(); j++) {
                this.pathCounter[i][j] = scanner.nextInt();
            }
        }
    }

    /**
     * Gets the smallest guaranteed 2-color path length for this 3-colored <code>Tournament2S3RCoordinate</code>
     * @return the minimum coordinate in this <code>Tournament2S3RCoordinate</code>
     */
    public int getMinPath() {
        int max = 0;
        for (int[] coordinateSet : this.pathCounter) {
            for (int coordinate : coordinateSet) {
                max = Math.max(max, coordinate);
            }
        }
        return max;
    }

    /**
     * Compresses this <code>Tournament2S3RCoordinate</code> into a simplified, equivalent graph
     * @return <code>true</code> if <code>this</code> is a valid <code>Tournament2S3RCoordinate</code> encoding, else <code>false</code>
     */
    public boolean compress() {
        for(int i = 0; i < NUM_COORDS; i++) {
            this.pathCounter[0][i] = 1;
        }
        for(int i = 1; i < this.pathCounter.length; i++) {
            int[][] temp = new int[i][];
            for(int j = 0; j < i; j++) {
                temp[j] = new int[NUM_COORDS];
                boolean isLess = false;
                for(int k = 0; k < NUM_COORDS; k++) {
                    if(this.pathCounter[j][k] < this.pathCounter[i][k]) {
                        temp[j][k] = this.pathCounter[j][k] + 1;
                    } else if(isLess) {
                        return false;
                    } else {
                        isLess = true;
                        temp[j][k] = 1;
                    }
                }
            }
            int[] newCol = new int[NUM_COORDS];
            for(int j = 0; j < i; j++) {
                for(int k = 0; k < NUM_COORDS; k++) {
                    newCol[k] = Math.max(newCol[k], temp[j][k]);
                }
            }
            this.pathCounter[i] = newCol;
        }
        return true;
    }

    /**
     * Gets the hashcode of this <code>Tournament2S3RCoordinate</code>. Any two <code>Tournament2S3RCoordinate</code>s with the same
     * min-path progression will have the same hashcode
     * @return a unique <code>int</code> representing this <code>Tournament2S3RCoordinate</code>
     */
    @Override
    public int hashCode() {
        for(int i = 0; i < this.pathCounter.length; i++) {
        }
        // TODO: finish this
        return 0;
    }

    /**
     * Generates a hashcode for a row of this <code>Tournament2S3RCoordinate</code>
     * @param row the target row
     * @return the unique identifier for the row of <code>ints</code>
     */
    private int hashCode(int[] row) {
        int index = row.length - 1, hashCode = 0;
        while(index > 0) {
            hashCode = hashCode * (index + 1) + row[index];
            index--;
        }
        return hashCode;
    }

    /**
     * Converts this <code>Tournament2S3RCoordinate</code> to a printable format
     * @return this <code>Tournament2S3RCoordinate</code> as a <code>String</code>
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String newLine = "";
        for(int i = 0; i < NUM_COORDS; i++) {
            builder.append(newLine);
            newLine = "\n";
            for (int[] ints : this.pathCounter) {
                builder.append(ints[i]).append("\t");
            }
        }
        return builder.toString();
    }

    /**
     * Prints this <code>Tournament2S3RCoordinate</code>
     */
    public void print() {
        System.out.println(this);
    }
}
