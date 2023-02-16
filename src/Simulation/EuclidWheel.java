package Simulation;

import Exception.ExceptionMessage.TargetedMessage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EuclidWheel {
    private static int[][][] values;
    private static final int DEPTH = 4, WIDTH = 16;

    /**
     * Handles the *main* execution.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        loadValues("src\\Simulation\\EuclidWheel.txt");
    }

    /**
     * Loads values from a <code>File</code> into the class field.
     * @param filename the name of the <code>File</code>
     */
    public static void loadValues(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            values = new int[DEPTH][][];
            for(int i = 0; i < DEPTH; i++) {
                values[i] = new int[DEPTH][];
                for(int j = 0; j < DEPTH; j++) {
                    values[i][j] = new int[WIDTH];
                    for(int k = 0; k < WIDTH; k++) {
                        TargetedMessage.scannerHasNextElement(scanner);
                        values[i][j][k] = Integer.parseInt(scanner.next());
                    }
                }
            }
            int BIT_MASK = 15;
            for(int i = 0; i < WIDTH; i++) {
                for(int j = 0; j < WIDTH; j++) {
                    for(int k = 0; k < WIDTH; k++) {
                        int sum = -1;
                        boolean isValidSolution = true;
                        for(int m = 0; m < WIDTH && isValidSolution; m++) {
                            int proxySum = 0;
                            for(int n = 0; n < DEPTH; n++) {
                                int p = 0;
                            }
                            if(sum < 0) {
                                sum = proxySum;
                            } else if(sum != proxySum) {
                                isValidSolution = false;
                            }
                        }
                        if(isValidSolution) {
                            System.out.println(i + " " + j + " " + k);
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
