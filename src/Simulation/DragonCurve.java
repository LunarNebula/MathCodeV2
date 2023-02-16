package Simulation;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Dragon curve Booleans operate as "true" for counterclockwise rotation. 0 is left, proceed counterclockwise for direction
 */
public class DragonCurve {
    private static final int WIDTH = 1000, HEIGHT = 1000, ITERATIONS = 17, X = 500, Y = 500, DISTANCE = 1, COLOR = 255;
    private static final int[][] TRANSLATIONS = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        try {
            loadBoardImage(image);
            loadBoardImage(image);
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            System.out.println("Generation halted. Displaying image.");
        }
        JLabel label = new JLabel(new ImageIcon(image));
        JPanel panel = new JPanel();
        panel.add(label);
        frame.add(new JScrollPane(panel));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Loads board image
     * @param image the BufferedImage to be presented on the screen
     */
    private static void loadBoardImage(BufferedImage image) {
        int x = X, y = Y;
        image.setRGB(x, y, COLOR);
        int[] translation = {1, 0};
        for(int j = 0; j < DISTANCE; j++) {
            x += translation[0];
            y += translation[1];
            image.setRGB(x, y, COLOR);
        }
        LinkedList<Boolean> directions = new LinkedList<>();
        for(int i = 0; i < ITERATIONS; i++) {
            LinkedList<Boolean> test = new LinkedList<>();
            translation = translation(next(translationIndex(translation), false));
            for(int j = 0; j < DISTANCE; j++) {
                x += translation[0];
                y += translation[1];
                image.setRGB(x, y, COLOR);
            }
            Iterator<Boolean> iterator = directions.descendingIterator();
            while(iterator.hasNext()) {
                boolean next = iterator.next();
                translation = translation(next(translationIndex(translation), ! next));
                for(int j = 0; j < DISTANCE; j++) {
                    x += translation[0];
                    y += translation[1];
                    image.setRGB(x, y, COLOR);
                }
                test.add(! next);
            }
            test.add(0, false);
            directions.addAll(test);
        }
    }

    private static int next(int prev, boolean direction) {
        int sign = direction ? 1 : -1;
        return switch (prev) {
            case 0 -> 2 - sign;
            case 1 -> 1 + sign;
            case 2 -> 2 + sign;
            case 3 -> 1 - sign;
            default -> throw new IllegalStateException("Unexpected value: " + prev + " is not a direction.");
        };
    }

    private static int[] translation(int instruction) {
        return TRANSLATIONS[instruction];
    }

    private static int translationIndex(int[] translation) {
        if((translation[0] ^ translation[1]) == 1) {
            return (translation[0] == 0) ? 2 : 1;
        } else {
            return (translation[0] == 0) ? 0 : 3;
        }
    }
}
