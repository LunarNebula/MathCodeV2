package General;
import java.util.LinkedList;
import java.util.List;

public class Timer {
    private static int timerNum = 0; // keeps track of timer ID number
    private static final long NANOS_PER_SEC = 1000000000;

    private final String name;
    private long lastTime;
    private final List<Long> laps;

    /**
     * Creates a new Timer with a default name
     */
    public Timer() {
        this.name = "timer" + (timerNum++);
        this.laps = new LinkedList<>();
        reset();
    }

    /**
     * Creates a new Timer
     * @param name the name of this Timer
     */
    public Timer(String name) {
        this.name = name;
        this.laps = new LinkedList<>();
        reset();
    }

    /**
     * Resets this Timer object
     */
    public void reset() {
        this.laps.clear();
        this.lastTime = 0;
    }

    /**
     * Starts this Timer
     */
    public void start() {
        this.lastTime = System.nanoTime();
    }

    /**
     * Adds a lap to this Timer
     * @param print true if the program will print the elapsed lap time, else false
     */
    public void lap(boolean print) {
        long currentTime = System.nanoTime(), elapsedTime = currentTime - this.lastTime;
        this.laps.add(elapsedTime);
        if(print) {
            System.out.println(this.name + ": lap " + this.laps.size() + " = " +
                    (((double) elapsedTime) / NANOS_PER_SEC) + " secs");
        }
        this.lastTime = currentTime;
    }
}