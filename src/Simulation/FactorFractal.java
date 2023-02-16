package Simulation;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class FactorFractal {
    private static final int WIDTH = 1000, HEIGHT = 1000;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        int z = 0;
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                image.setRGB(i, j, (5 + 65536*5)*kapLength((i * HEIGHT + j)));
            }
        }

        JLabel label = new JLabel(new ImageIcon(image));
        JPanel panel = new JPanel();
        panel.add(label);
        frame.add(new JScrollPane(panel));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static int kap(int n) {
        int test = n, minuend = 0, subtrahend = 0, base = 10;
        int[] digits = new int[base];
        while(test > 0) {
            digits[test % base]++;
            test /= base;
        }
        for(int i = 0; i < base; i++) {
            for(int j = 0; j < digits[i]; j++) {
                subtrahend = subtrahend * base + i;
            }
        }
        for(int i = base - 1; i >= 0; i--) {
            for(int j = 0; j < digits[i]; j++) {
                minuend = minuend * base + i;
            }
        }
        return minuend - subtrahend;
    }

    public static int kapLength(int n) {
        int count = 0, test = kap(n);
        while(n != test) {
            n = kap(n);
            test = kap(kap(test));
            count++;
        }
        return count;
    }
}
