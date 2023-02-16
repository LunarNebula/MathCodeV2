package Simulation;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Life {
    private static final int HEIGHT = 625;                              // Height of the board
    private static final int WIDTH = 600;                               // Width of the board

    private static final int NULL = 0;                                  // Numerical value of "null" pixel
    private static final int ACTIVATED = 1;                             // Numerical value of "activated" pixel

    private static final int NEIGHBORHOOD = 1;                          // Distance of all furthest pixel "neighbors"

    private static final int[] SUSTAIN = {2};                           // Set of state-sustaining neighbor counts
    private static final int[] REVIVE = {3};                            // Set of reviving neighbor counts

    public static void main(String[] args) {
        int[][] prevBoard = newBoard(HEIGHT, WIDTH, NULL);

        // board decals

//        final int[][] GLIDER_GUN = parsePattern(SIMKIN_GLIDER_GUN);
//        insertPattern(prevBoard, GLIDER_GUN, 300, 280, ACTIVATED);
//        insertPattern(prevBoard, flipHorizontal(GLIDER_GUN), 304, 319, ACTIVATED);
//        int kok_x = 339, kok_y = 302;
//        insertPattern(prevBoard, parsePattern(KOKS_GALAXY), kok_x, kok_y, ACTIVATED);
//        insertPattern(prevBoard, parsePattern(BOAT), kok_x + 4, kok_y + 13, ACTIVATED);

        insertPattern(prevBoard, parsePattern(SIMKIN_GLIDER_GUN), WIDTH/2, HEIGHT/2, ACTIVATED);
        //insertPattern(prevBoard, OSCILLATOR_PERIOD_60(), 300, 300, ACTIVATED);

        // permanent image code

        JFrame frame = new JFrame();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        updateBoardImage(image, prevBoard);
        JLabel label = new JLabel(new ImageIcon(image));
        JPanel panel = new JPanel();
        panel.add(label);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        while(true) {
//            long num = System.currentTimeMillis();
//            while(System.currentTimeMillis() - num < 50) {
//                // wait
//            }

            int[][] board = newBoard(HEIGHT, WIDTH, NULL);
            for(int i = 0; i < board.length; i++) {
                for(int j = 0; j < board[i].length; j++) {
                    board[i][j] = getNewState(prevBoard, i, j);
                }
            }
            updateBoardImage(image, board);
            frame.remove(panel);
            panel.remove(label);
            label = new JLabel(new ImageIcon(image));
            panel.add(label);
            frame.add(panel);
            frame.pack();
            prevBoard = board;
        }
    }

    /**
     * Creates a new board
     * @param height the height of the board
     * @param width the width of the board
     * @param value the null value used to fill the board
     * @return the new board
     */
    private static int[][] newBoard(int height, int width, int value) {
        int[][] board = new int[height][];
        for(int i = 0; i < board.length; i++) {
            board[i] = new int[width];
        }
        fillBoard(board, value);
        return board;
    }

    /**
     * Fills board with a single value
     * @param board the target board
     * @param value the intended value
     */
    private static void fillBoard(int[][] board, int value) {
        for(int[] row : board) {
            Arrays.fill(row, value);
        }
    }

    /**
     * Inserts a pattern into the board
     * @param board the target board
     * @param pattern the pattern to be edited into the board
     * @param x the starting x
     * @param y the starting y
     * @param value the intended value for each pattern pixel
     */
    private static void insertPattern(int[][] board, int[][] pattern, int x, int y, int value) {
        for(int i = 0; i < pattern.length; i++) {
            for(int j = 0; j < pattern[i].length; j++) {
                if(pattern[i][j] == ACTIVATED) {
                    int pixel_x = (x + i) % board.length;
                    board[pixel_x][(y + j) % board[pixel_x].length] = value;
                }
            }
        }
    }

    /**
     * Gets the number of live neighbors of a cell on the board
     * @param board the target board
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the number of live neighbors
     */
    private static int getNeighborCount(int[][] board, int x, int y) {
        int count = 0;
        for(int i = -NEIGHBORHOOD; i <= NEIGHBORHOOD; i++) {
            for(int j = -NEIGHBORHOOD; j <= NEIGHBORHOOD; j++) {
                int pixel_x = (x + i) % board.length;
                if(pixel_x < 0) {
                    pixel_x += board.length;
                }
                int pixel_y = (y + j) % board[pixel_x].length;
                if(pixel_y < 0) {
                    pixel_y += board[pixel_x].length;
                }
                count += (board[pixel_x][pixel_y] == ACTIVATED) ? 1 : 0;
            }
        }
        return count - (board[x][y] == ACTIVATED ? 1 : 0);
    }

    /**
     * Finds the new state of a cell
     * @param board the target board
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the new state of the cell, as int ACTIVATED or int NULL
     */
    private static int getNewState(int[][] board, int x, int y) {
        int count = getNeighborCount(board, x, y);
        for(int state : REVIVE) {
            if(count == state) {
                return ACTIVATED;
            }
        }
        if(board[x][y] == ACTIVATED) {
            for(int state : SUSTAIN) {
                if(count == state) {
                    return ACTIVATED;
                }
            }
        }
        return NULL;
    }

    /**
     * Updates a BufferedImage with the pixels on the board
     * @param image the target image
     * @param board the comparator board
     */
    private static void updateBoardImage(BufferedImage image, int[][] board) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                image.setRGB(j, i, (board[i][j] == ACTIVATED) ? 0 : 16777215);
            }
        }
    }

    /**
     * Parses a String[] pattern into a board of integers
     * @param origin the original String[] containing the pattern
     * @return the board
     */
    private static int[][] parsePattern(String[] origin) {
        int[][] pattern = new int[origin.length][];
        for(int i = 0; i < pattern.length; i++) {
            pattern[i] = new int[origin[i].length()];
            int index = 0;
            for(char ch : origin[i].toCharArray()) {
                pattern[i][index] = ch - '0';
                index++;
            }
        }
        return pattern;
    }

    /**
     * Vertically flips a board
     * @param board the target board
     * @return the reversed board
     */
    private static int[][] flipVertical(int[][] board) {
        int[][] newBoard = new int[board.length][];
        for(int i = 0; i < board.length; i++) {
            newBoard[board.length - 1 - i] = new int[board[i].length];
            System.arraycopy(board[i], 0, newBoard[board.length - 1 - i], 0, board[i].length);
        }
        return newBoard;
    }

    /**
     * Finds the transpose of a board
     * @param board the target board
     * @return the target board, flipped over the main diagonal
     */
    private static int[][] transpose(int[][] board) {
        int[][] newBoard = new int[board[0].length][];
        for(int i = 0; i < newBoard.length; i++) {
            newBoard[i] = new int[board.length];
            for(int j = 0; j < board.length; j++) {
                newBoard[i][j] = board[j][i];
            }
        }
        return newBoard;
    }

    /**
     * Horizontally flips a board
     * @param board the target board
     * @return the flipped board
     */
    private static int[][] flipHorizontal(int[][] board) {
        return transpose(flipVertical(transpose(board)));
    }

    /**
     * Prints a board
     * @param board the target board
     */
    private static void printBoard(int[][] board) {
        for(int[] row : board) {
            String rowString = "";
            for(int element : row) {
                rowString = rowString.concat(element + "");
            }
            System.out.println(rowString);
        }
    }

    private static final String[] GLIDER = {
            "010",
            "001",
            "111"
            };

    private static final String[] GOSPER_GLIDER_GUN = {
            "000000000000000000000000100000000000",
            "000000000000000000000010100000000000",
            "000000000000110000001100000000000011",
            "000000000001000100001100000000000011",
            "110000000010000010001100000000000000",
            "110000000010001011000010100000000000",
            "000000000010000010000000100000000000",
            "000000000001000100000000000000000000",
            "000000000000110000000000000000000000"
    };

    private static final String[] SIMKIN_GLIDER_GUN = {
            "110000011000000000001000000000000",
            "110000011000000000111000000000000",
            "000000000000000000101000000000000",
            "000011000000000000100000000000000",
            "000011000000000000000000000000000",
            "000000000000000000000000000000000",
            "000000000000000000000000000000000",
            "000000000000000000000000000000000",
            "000000000000000000000000000110000",
            "000000000000000000000000000110000",
            "000000000000000000000000000000000",
            "000000000000000000000000110000011",
            "000000000000000000000000110000011"
    };

    private static final String[] SPACESHIP_HAMMERHEAD = {
            "111110000000000000",
            "100001000000011000",
            "100000000000110111",
            "010000000001101111",
            "000110001101100110",
            "000001000010010000",
            "000000101010100000",
            "000000010000000000",
            "000000010000000000",
            "000000101010100000",
            "000001000010010000",
            "000110001101100110",
            "010000000001101111",
            "100000000000110111",
            "100001000000011000",
            "111110000000000000"
    };

    private static final String[] SPACESHIP_COPPERHEAD = {
            "01100110",
            "00011000",
            "00011000",
            "10100101",
            "10000001",
            "00000000",
            "10000001",
            "01100110",
            "00111100",
            "00000000",
            "00011000",
            "00011000"
    };

    private static final String[] SUMMERS_RAKE = {
            "000001110000000000011100000",
            "000010001000000000100010000",
            "000110000100000001000011000",
            "001010110110000011011010100",
            "011010000101101101000010110",
            "100001000100101001000100001",
            "000000000000101000000000000",
            "110000000110101011000000011",
            "000000000000101000000000000",
            "000000111000000000111000000",
            "000000100010000000001000000",
            "000000101000011100000000000",
            "000000000000100100001100000",
            "000000000000000100000000000",
            "000000000001000100000000000",
            "000000000001000100000000000",
            "000000000000000100000000000",
            "000000000000101000000000000"
    };

    private static final String[] SUMMERS_RAKE_SIMPLIFIED = {
            "000001110000000000011100000",
            "000010001000000000100010000",
            "000110000100000001000011000",
            "001010110110000011011010100",
            "011010000101101101000010110",
            "100001000100101001000100001",
            "000000000000101000000000000",
            "110000000110101011000000011",
            "000000000000101000000000000",
            "000000111000000000111000000",
            "000000100010000000001000000",
            "000000101000000000000000000",
            "000000000000000000001100000"
    };

    private static final String[] SPACESHIP_SMALL = {
            "01111",
            "10001",
            "00001",
            "10010"
    };

    private static final String[] SPACESHIP_MEDIUM = {
            "011111",
            "100001",
            "000001",
            "100010",
            "001000"
    };

    private static final String[] SPACESHIP_LARGE = {
            "0111111",
            "1000001",
            "0000001",
            "1000010",
            "0011000"
    };

    private static final String[] HERSCHEL = {
            "100",
            "101",
            "111",
            "001"
    };

    private static final String[] HEPTOMINO_AGAR = {
            "01110",
            "01001",
            "10001",
            "11110",
            "01000"
    };

    private static final String[] EATER_1 = {
            "1100",
            "1010",
            "0010",
            "0011"
    };

    private static final String[] EATER_2 = {
            "0001011",
            "0111011",
            "1000000",
            "0111011",
            "0001010",
            "0001010",
            "0000100"
    };

    private static final String[] EATER_3 = {
            "000000000110",
            "000011001001",
            "010010000101",
            "101010000010",
            "010010110000",
            "000010010000",
            "000001000010",
            "000000111110",
            "000000000000",
            "000000001000",
            "000000010100",
            "000000001000"
    };

    private static final String[] PENTA_DECATHLON = {
            "0010000100",
            "1101111011",
            "0010000100"
    };

    private static final String[] BLOCK = {
            "11",
            "11"
    };

    private static final String[] TOAD = {
            "0111",
            "1110"
    };

    private static final String[] TUB = {
            "010",
            "101",
            "010"
    };

    private static final String[] BOAT = {
            "010",
            "101",
            "110"
    };

    private static final String[] BEEHIVE = {
            "0110",
            "1001",
            "0110"
    };

    private static final String[] LOAF = {
            "0110",
            "1001",
            "1010",
            "0100"
    };

    private static final String[] WACKY_WAVING_INFLATABLE_ARM_FLAILING_TUBE_MAN = {
            "0010",
            "1100",
            "0011",
            "0100"
    };

    private static final String[] CLOCK = {
            "000000110000",
            "000000110000",
            "000000000000",
            "000011110000",
            "110100101000",
            "110101001000",
            "000101001011",
            "000100001011",
            "000011110000",
            "000000000000",
            "000011000000",
            "000011000000"
    };

    private static final String[] KOKS_GALAXY = {
            "111111011",
            "111111011",
            "000000011",
            "110000011",
            "110000011",
            "110000011",
            "110000000",
            "110111111",
            "110111111"
    };

    private static final String[] FIGURE_EIGHT = {
            "111000",
            "111000",
            "111000",
            "000111",
            "000111",
            "000111"
    };

    private static final String[] MOLD = {
            "000110",
            "001001",
            "010101",
            "111010",
            "111000"
    };

    private static final String[] QUEEN_BEE = {
            "0001000",
            "0010100",
            "0100010",
            "0011100",
            "1100011"
    };

    private static final String[] BLOCKER = {
            "0000010000",
            "1110110011",
            "1111000011",
            "0000110000"
    };

    private static int[][] OSCILLATOR_PERIOD_60() {
        int[][] oscillator = newBoard(8, 35, NULL);
        int[][] pentadecathlon = parsePattern(PENTA_DECATHLON);
        insertPattern(oscillator, pentadecathlon, 1, 0, ACTIVATED);
        insertPattern(oscillator, parsePattern(GLIDER), 0, 12, ACTIVATED);
        insertPattern(oscillator, pentadecathlon, 5, 25, ACTIVATED);
        return oscillator;
    }

    private static int[][] QUEEN_BEE_LOOP() {
        int[][] loop = newBoard(24, 24, NULL);
        int[][] left_bee = parsePattern(QUEEN_BEE);
        insertPattern(loop, left_bee, 7, 0, ACTIVATED);
        int[][] top_bee = transpose(flipVertical(left_bee));
        insertPattern(loop, top_bee, 0, 12, ACTIVATED);
        int[][] right_bee = transpose(flipVertical(top_bee));
        insertPattern(loop, right_bee, 12, 17, ACTIVATED);
        int[][] bottom_bee = transpose(flipVertical(right_bee));
        insertPattern(loop, bottom_bee, 17, 7, ACTIVATED);
        return loop;
    }

    private static int[][] QUEEN_BEE_QUAD_LOOP() {
        int[][] loop = newBoard(41, 41, NULL);
        int[][] image = flipVertical(QUEEN_BEE_LOOP());
        insertPattern(loop, QUEEN_BEE_LOOP(), 0, 0, ACTIVATED);
        insertPattern(loop, image, 0, 17, ACTIVATED);
        insertPattern(loop, QUEEN_BEE_LOOP(), 17, 17, ACTIVATED);
        insertPattern(loop, image, 17, 0, ACTIVATED);
        return loop;
    }

    private static int[][] QUEEN_BEE_SHUTTLE() {
        int[][] shuttle = newBoard(7, 22, NULL);
        int[][] block = parsePattern(BLOCK);
        insertPattern(shuttle, block, 3, 0, ACTIVATED);
        insertPattern(shuttle, transpose(parsePattern(QUEEN_BEE)), 0, 5, ACTIVATED);
        insertPattern(shuttle, block, 3, 20, ACTIVATED);
        return shuttle;
    }
}