package Simulation;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*;

public class Mandelbrot {
    public static final int LENGTH = 601, DISPLACEMENT = 100, ITERATIONS = 5000, COLOR_CYCLE = 256;
    public static int historyLayer = 0;
    public static double[] dilate = toComp(1), shift = toComp(0);
    public static final ArrayList<double[]> newZoom = new ArrayList<>();
    public static final ArrayList<double[][]> zoomTrack = new ArrayList<>(), zoomHistory = new ArrayList<>();
    public static final FractalRange frame = new FractalRange();
    public static final JPanel panel = new JPanel(), buttonPanel = new JPanel(), mainPanel = new JPanel();														//previewWindow may be moved to label?
    public static final BufferedImage image = new BufferedImage(LENGTH, LENGTH, BufferedImage.TYPE_INT_RGB);
    public static JLabel label;
    public static final JButton forward = new JButton("»"), reverse = new JButton("«");

    public static void main(String[] args) {
        forward.setBackground(new Color(190, 232, 246));
        reverse.setBackground(new Color(190, 232, 246));

        frame.setSize(LENGTH, LENGTH + DISPLACEMENT);
        frame.add(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panel);
        panel.setPreferredSize(new Dimension(LENGTH, LENGTH));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        double[][] info = {dilate, shift};
        zoomTrack.add(info);
        zoomHistory.add(info);
        mainMenuButtonSetup();
        mainHelperMethod();
    }

    /**
     * Sets button panel visual layout
     */
    public static void mainMenuButtonSetup() {
        mainPanel.add(buttonPanel);
        buttonPanel.setPreferredSize(new Dimension(LENGTH, DISPLACEMENT));
        forward.setBounds(110, 10, 100, 50);
        reverse.setBounds(10, 10, 100, 50);
        buttonPanel.add(reverse);
        buttonPanel.add(forward);
        forward.addActionListener(new Forward());
        reverse.addActionListener(new Reverse());
        forward.setVisible(true);
        reverse.setVisible(true);
    }

    /**
     * Coordinates the pixel settings and reloads the image
     */
    public static void mainHelperMethod() {
        for(int i = 0; i < LENGTH; i++) {
            for(int j = 0; j < LENGTH; j++) {
                image.setRGB(i, j, getColorRGB(iterations(toComp(i, j))));
            }
        }
        System.out.println(toString(dilate) + ", " + toString(shift));
        label = new JLabel(new ImageIcon(image));
        panel.add(label);
        frame.pack();
    }

    /**
     * Adjusts the zoomHistory to match the current zoom factor
     */
    public static void createZoom() {
        print(newZoom.get(0));
        print(newZoom.get(1));
        shift = midpoint(newZoom.get(0), newZoom.get(1));
        dilate = div(toComp(Math.max(Math.abs(newZoom.get(0)[0] - newZoom.get(1)[0]), Math.abs(newZoom.get(0)[1] - newZoom.get(1)[1]))), 4);
        if (zoomHistory.size() > historyLayer + 1) {
            zoomHistory.subList(historyLayer + 1, zoomHistory.size()).clear();
        }
        double[][] info = {dilate, shift};
        zoomTrack.add(info);
        zoomHistory.add(info);
        historyLayer++;
        reset();
        mainHelperMethod();
    }

    /**
     * Resets the Mandelbrot image
     */
    public static void reset() {
        newZoom.clear();
        dilate = zoomHistory.get(historyLayer)[0];
        shift = zoomHistory.get(historyLayer)[1];
        panel.remove(label);
    }

    /**
     * Finds the number of iterations a point can
     * @param c the starting point (complex number)
     * @return the number of iterations before the point either passes the maximum distance or reaches
     */
    public static int iterations(double[] c){
        c = div(sub(c, toComp((LENGTH - 1.0) / 2, (LENGTH - 1.0) / 2)), (LENGTH - 1.0) / 4);
        int count = 0;
        double[] z = toComp(0);
        while(count < ITERATIONS && magnitude(z) < 2) {
            z = add(add(mult(z, z), mult(c, dilate)), shift);
            count++;
        }
        return count == ITERATIONS ? 0 : count;
    }

    /**
     * Re-represents a pair of real numbers as the components of a complex number
     * @param real the real component
     * @param imag the imaginary component
     * @return real + imag * i
     */
    public static double[] toComp(double real, double imag) {
        return new double[]{real, imag};
    }

    /**
     * Re-represents a real number to a complex number
     * @param r the target real
     * @return r + 0i
     */
    public static double[] toComp(double r) {
        return toComp(r, 0);
    }

    /**
     * Finds the product of two complex numbers
     * @param c1 the complex multiplier
     * @param c2 the complex multiplicand
     * @return c1 * c2
     */
    public static double[] mult(double[] c1, double[] c2) {
        return toComp(c1[0] * c2[0] - c1[1] * c2[1], c1[0] * c2[1] + c1[1] * c2[0]);
    }

    /**
     * Finds the product of a complex number and a real number
     * @param c the complex multiplier
     * @param val the real multiplicand
     * @return c * val
     */
    public static double[] mult(double[] c, double val) {
        return mult(c, toComp(val));
    }

    /**
     * Divides a complex number by a constant
     * @param comp the complex dividend
     * @param div the real divisor
     * @return comp / div
     */
    public static double[] div(double[] comp, double div) {
        return toComp(comp[0] / div, comp[1] / div);
    }

    /**
     * Finds the inverse of a complex number
     * @param divisor the input C
     * @return 1/C or C^-1
     */
    public static double[] inv(double[] divisor) {
        return div(conjugate(divisor), magnitude(divisor));
    }

    /**
     * Finds the sum of two complex numbers
     * @param c1 the augend complex number
     * @param c2 the addend complex number
     * @return c1 + c2
     */
    public static double[] add(double[] c1, double[] c2) {
        return toComp(c1[0] + c2[0], c1[1] + c2[1]);
    }

    /**
     * Finds the difference between two complex numbers
     * @param c1 the minuend complex number
     * @param c2 the subtrahend complex number
     * @return c1 - c2
     */
    public static double[] sub(double[] c1, double[] c2) {
        return toComp(c1[0] - c2[0], c1[1] - c2[1]);
    }

    /**
     * Calculates the magnitude of a complex number
     * @param comp the target complex number
     * @return the positive distance from the complex number to the origin
     */
    public static double magnitude(double[] comp) {
        return Math.sqrt(comp[0] * comp[0] + comp[1] * comp[1]);
    }

    /**
     * Converts a complex number to a printable format
     * @param comp the target complex number
     * @return the complex number as a String
     */
    public static String toString(double[] comp) {
        return comp[0] + (comp[1] < 0 ? "-" : "+") + Math.abs(comp[1]) + "i";
    }

    /**
     * Prints a pair of complex coordinates
     * @param comp the complex coordinates
     */
    public static void print(double[] comp) {
        System.out.println(toString(comp));
    }

    /**
     * Responds to user clicks on the Mandelbrot image
     * @param coords the coordinates of the mouse when the clicks occurred
     */
    public static void clickRespond(double[] coords) {
        if(newZoom.size() < 2) {
            newZoom.add(add(mult(mult(div(sub(coords, toComp((LENGTH - 1.0) / 2, (LENGTH - 1.0) / 2)), LENGTH - 1.0), 4), dilate), shift));
            if(newZoom.size() == 2) createZoom();
        }
    }

    /**
     * Finds the midpoint of two points
     * @param c1 the first point
     * @param c2 the second point
     * @return the point halfway between the two points on the 2D plane
     */
    public static double[] midpoint(double[] c1, double[] c2) {
        return toComp((c1[0] + c2[0]) / 2.0, (c1[1] + c2[1]) / 2.0);
    }

    /**
     * Gets the color of a pixel in the set from the number of iterations
     * @param iterations the number of iterations
     * @return the proper RGB color as an int
     */
    public static int getColorRGB(int iterations) {
        if(iterations < COLOR_CYCLE) {
            return iterations;
        }
        final int placement = iterations % COLOR_CYCLE;
        return ((placement << 1 < COLOR_CYCLE) ? (placement) : (256 - placement)) * 65535 + 255;
    }

    /**
     * Finds the conjugate of a complex number
     * @param c the input C equal to R+Ii
     * @return the unique complex number equal to R-Ii
     */
    public static double[] conjugate(double[] c) {
        return new double[]{c[0], -c[1]};
    }

    /**
     * Static class controlling the Forward button
     */
    static class Forward implements ActionListener {
        /**
         * Brings the Mandelbrot set to a zoom factor set after the current one, only if there exists a future iteration
         * that has not been overwritten by calling "Reverse" and then setting a new drawing
         * @param event the ActionEvent called when the "Forward" function is called
         */
        public void actionPerformed(ActionEvent event) {
            if(historyLayer < zoomHistory.size() - 1) {
                historyLayer++;
                reset();
                mainHelperMethod();
            }
        }
    }

    /**
     * Static class controlling the Reverse button
     */
    static class Reverse implements ActionListener {
        /**
         * Returns the Mandelbrot set to the previous zoom factor
         * @param event the ActionEvent called when the "Reverse" function is called
         */
        public void actionPerformed(ActionEvent event) {
            if(zoomHistory.size() >= 2) {
                historyLayer--;
                reset();
                mainHelperMethod();
            }
        }
    }
}

/**
 * Class controlling the interactive Mandelbrot image
 */
class FractalRange extends JFrame {
    /**
     * Creates the interactive clicker in the Mandelbrot window
     */
    public FractalRange() {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                Mandelbrot.clickRespond(Mandelbrot.toComp(event.getX(), event.getY()));
            }
        });
    }
}