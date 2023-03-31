package Turing;

import DataSet.LinkedChain;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Machine<Key, Data> {
    private final LinkedChain<Data> tape;
    private final State<Key, Data> initialState;
    private final Data defaultValue;
    private State<Key, Data> currentState;
    private boolean isRunning;
    private long iterations;

    /**
     * Creates a new Machine
     * @param defaultValue the default value of new cells in the Machine LinkedHashTape
     * @param initialState the initial State of this Machine
     */
    public Machine(Data defaultValue, State<Key, Data> initialState) {
        this.defaultValue = defaultValue;
        this.initialState = initialState;
        this.tape = new LinkedChain<Data>();
        reset();
    }

    /**
     * Resets this Machine
     */
    public void reset() {
        this.currentState = this.initialState;
        this.isRunning = true;
        this.iterations = 0;
        this.tape.clear();
        this.tape.addToStart(this.defaultValue);
    }

    /**
     * Moves this Machine to the next iteration
     */
    public void proceed() {
        if(this.isRunning) {
            Transformation<Key, Data> transformation = this.currentState.getNextState(this.tape.get());
            if(transformation == null) {
                this.isRunning = false;
            } else {
                this.currentState = transformation.nextState();
                this.tape.set(transformation.nextTapeValue());
                switch (transformation.movement()) {
                    case LEFT -> this.tape.prev(this.defaultValue);
                    case RIGHT -> this.tape.next(this.defaultValue);
                    case STOP -> this.isRunning = false;
                }
            }
            this.iterations++;
        }
    }

    /**
     * Determines whether this Machine is running (or whether it has halted). The machine stops if:
     * <ul>
     *      <li>It reaches a State with "STOP" as the associated Movement</li>
     *      <li>It reaches a State with no associated StateShift</li>
     * </ul>
     * @return this.isRunning
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * Finds the number of iterations this Machine has simulated
     * @return this.iterations
     */
    public long getIterations() {
        return this.iterations;
    }

    /**
     * Determines whether this Turing Machine is equal to a specified Object
     * @param o the comparator Object
     * @return true if the comparator is a Machine with equivalent State and StateShift relations, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof Machine<?, ?> convert)) {
            return false;
        }
        return this.initialState.equals(convert.initialState) && this.defaultValue.equals(convert.defaultValue);
    }

    /**
     * Converts this String to a printable format
     * @return this Machine as a String
     */
    @Override
    public String toString() {
        StringBuilder print = new StringBuilder();
        int index = 0;
        for(Data item : this.tape) {
            String gap = (index == this.tape.getCursorIndex()) ? "|" : " ";
            print.append("[").append(gap).append(item.toString()).append(gap).append("]");
            index++;
        }
        return print + " \nState: " + this.currentState.getID() + (this.isRunning ? "" : " [STOPPED]");
    }

    /**
     * Prints this Machine
     */
    public void print() {
        System.out.println(this);
    }

    // Static methods

    /**
     * Converts information from a file into a Turing Machine
     * @param filename the filepath to the file containing the Machine parameter information
     * @return the converted Machine
     */
    public static Machine<String, Integer> getMachineFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            final String WHITESPACE = " ", DASH = "-";
            final String[] states = scanner.nextLine().split(WHITESPACE);
            final String[] cellStatesUnparsed = scanner.nextLine().split(WHITESPACE);
            final int[] cellStates = new int[cellStatesUnparsed.length];
            for(int i = 0; i < cellStates.length; i++) {
                cellStates[i] = Integer.parseInt(cellStatesUnparsed[i]);
            }
            Map<String, State<String, Integer>> map = new HashMap<>();
            for(String state : states) {
                map.put(state, new State<>(state));
            }
            final int STATE = 0, CELL = 1, MOVEMENT = 2;
            for(String state : states) {
                final String[] nextLine = scanner.nextLine().split(WHITESPACE);
                final State<String, Integer> targetState = map.get(state);
                int cellStateIndex = 0;
                for(int cellState : cellStates) {
                    final String[] transformation = nextLine[cellStateIndex++].split(DASH);
                    final Movement movement = switch (transformation[MOVEMENT]) {
                        case "L" -> Movement.LEFT;
                        case "R" -> Movement.RIGHT;
                        case "H" -> Movement.STOP;
                        default -> throw new NumberFormatException("Ruleset contains an illegal direction.");
                    };
                    targetState.addTransformation(cellState, new Transformation<>(
                            Integer.parseInt(transformation[CELL]), map.get(transformation[STATE]), movement)
                    );
                }
            }
            final String[] initialConditions = scanner.nextLine().split(WHITESPACE);
            scanner.close();
            return new Machine<>(Integer.parseInt(initialConditions[CELL]), map.get(initialConditions[STATE]));
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
