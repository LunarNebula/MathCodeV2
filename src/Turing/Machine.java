package Turing;

import DataKey.MultiKey;
import DataSet.LinkedChain;
import Exception.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Stores an object that simulates a Turing Machine.
 * @param <Label> the data type for each state ID.
 * @param <Cell> the data type stored in each tape
 */
public class Machine<Label, Cell> {
    private final List<LinkedChain<Cell>> tapes;
    private final Map<Label, State<Label, Cell>> states;
    private Label currentState, haltingState;
    private Cell defaultValue;
    private boolean isRunning, isInitialized;
    private long iterations;

    /**
     * Creates a new {@code Machine}.
     */
    public Machine() {
        this.tapes = new ArrayList<>();
        this.states = new HashMap<>();
        this.isRunning = false;
        this.isInitialized = false;
        this.iterations = 0;
    }

    /**
     * Reloads this set of {@code States} with a new mapping.
     * @param map the new {@code States}.
     */
    public void loadStates(Map<Label, State<Label, Cell>> map) {
        this.states.clear();
        this.states.putAll(map);
        int tapes = 0;
        for(Label key : map.keySet()) {
            int newTapes = map.get(key).getTapes();
            if(tapes == 0) {
                tapes = newTapes;
            } else if(newTapes != tapes) {
                throw new IllegalDimensionException(ExceptionMessage.INCORRECT_NUMBER_OF_ARGUMENTS(tapes));
            }
        }
        this.tapes.clear();
        for(int i = 0; i < tapes; i++) {
            this.tapes.add(new LinkedChain<Cell>());
        }
    }

    /**
     * Provides this {@code Machine} with a starting state and tape configuration.
     * @param initialState the initial {@code State} ID.
     * @param haltingState the halting {@code State} ID.
     * @param defaultValue the value to fill in for new {@code Cells}.
     * @param config the initial tape configuration.
     */
    public final void loadInitialValues(Label initialState, Label haltingState, Cell defaultValue,
                                        LinkedChain<Cell> config) {
        for(int i = 0; i < this.tapes.size(); i++) {
            final LinkedChain<Cell> tape = new LinkedChain<Cell>();
            tape.addToStart(defaultValue);
            this.tapes.set(i, i == 0 & config.size() > 0 ? config.copy() : tape);
        }
        this.currentState = initialState;
        this.haltingState = haltingState;
        this.defaultValue = defaultValue;
        this.isRunning = ! initialState.equals(haltingState);
        this.isInitialized = true;
        this.iterations = 0;
    }

    /**
     * Steps this {@code Machine} forward a single iteration.
     */
    @SuppressWarnings(ExceptionMessage.UNCHECKED)
    public void step() {
        if(this.isInitialized && this.isRunning) {
            final Object[] values = new Object[this.tapes.size()];
            for(int i = 0; i < values.length; i++) {
                values[i] = this.tapes.get(i).get();
            }
            MultiKey<Cell> key = (MultiKey<Cell>) new MultiKey<>(values);
            StateShift<Label, Cell> stateShift = this.states.get(this.currentState).getTransformation(key);
            if(stateShift == null) {
                this.isRunning = false;
            } else {
                this.currentState = stateShift.nextState();
                for(int i = 0; i < values.length; i++) {
                    TapeShift<Cell> tapeShift = stateShift.getShift(i);
                    this.tapes.get(i).set(tapeShift.tapeValue());
                    switch (tapeShift.movement()) {
                        case LEFT -> this.tapes.get(i).prev(this.defaultValue);
                        case RIGHT -> this.tapes.get(i).next(this.defaultValue);
                    }
                }
                if(this.currentState.equals(this.haltingState)) {
                    this.isRunning = false;
                }
            }
            this.iterations++;
        }
    }

    /**
     * Determines whether this {@code Machine} is still running, or if it has halted.
     * @return {@code this.isRunning}
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * Gets the number of iterations for which this {@code Machine} has been running
     * for its current simulation.
     * @return {@code this.iterations}
     */
    public long iterations() {
        return this.iterations;
    }

    /**
     * Converts this {@code Machine} to a printable format.
     * @return this {@code Machine} as a {@code String}.
     */
    @Override
    public String toString() {
        if(this.isInitialized) {
            final StringBuilder builder = new StringBuilder();
            builder.append("State: ").append(this.currentState).append('\n');
            int index = 1;
            String newLine = "[";
            for (LinkedChain<Cell> tape : this.tapes) {
                builder.append(newLine);
                newLine = "\n[";
                int tapeIndex = 0;
                for (Cell item : tape) {
                    char gap = ((tapeIndex++) == tape.getCursorIndex()) ? '|' : ' ';
                    builder.append('[').append(gap).append(item).append(gap).append(']');
                }
                builder.append("] > ").append(index++);
            }
            return builder.toString();
        }
        return "[Machine requires additional values.]";
    }

    /**
     * Prints this {@code Machine}.
     */
    public void print() {
        System.out.println(this);
    }

    // Static methods

    /**
     * Loads a Turing machine from specifications listed in a text file.
     * @param filename the name of the file.
     * @return the given {@code Machine}, parameterized by {@code String}.
     * @throws IllegalArgumentException if the {@code Machine} defined by the file
     * uses invalid syntax.
     */
    @SuppressWarnings(ExceptionMessage.UNCHECKED)
    public static Machine<String, Integer> getMachineFromFile(String filename) throws IllegalArgumentException {
        try (Scanner scanner = new Scanner(new File(filename))) {
            final int THIS_STATE_INDEX = 0, CELL_INDEX = 1, NEXT_STATE_INDEX = 2, NEXT_CELL_INDEX = 3;
            final String WHITESPACE = " ", DASH = "-", COLON = ":";
            final Map<String, State<String, Integer>> stateMap = new HashMap<>();
            for(String state : scanner.nextLine().split(WHITESPACE)) {
                stateMap.put(state, new State<>(state));
            }
            final Map<String, Integer> cellMap = new HashMap<>();
            for(String cell : scanner.nextLine().split(WHITESPACE)) {
                cellMap.put(cell, Integer.parseInt(cell));
            }
            while(scanner.hasNext()) {
                final String[] transformation = scanner.nextLine().split(WHITESPACE);
                final String[] stringCellCombo = transformation[CELL_INDEX].split(DASH);
                final Integer[] cellCombo = new Integer[stringCellCombo.length];
                for(int i = 0; i < cellCombo.length; i++) {
                    cellCombo[i] = cellMap.get(stringCellCombo[i]);
                }
                final TapeShift[] tapeShifts = new TapeShift[cellCombo.length];
                final String[] nextShifts = transformation[NEXT_CELL_INDEX].split(COLON);
                for(int i = 0; i < tapeShifts.length; i++) {
                    final String[] parameters = nextShifts[i].split(DASH);
                    tapeShifts[i] = new TapeShift<>(cellMap.get(parameters[0]), Movement.get(parameters[1]));
                }
                MultiKey<Integer> key = new MultiKey<>(cellCombo);
                State<String, Integer> state = stateMap.get(transformation[THIS_STATE_INDEX]);
                if(state == null || state.getTransformation(key) != null) {
                    throw new IllegalArgumentException("Duplicate state input condition found.");
                }
                state.addTransformation(new MultiKey<>(cellCombo),
                        new StateShift<>(transformation[NEXT_STATE_INDEX], tapeShifts)
                );
            }
            final Machine<String, Integer> machine = new Machine<>();
            machine.loadStates(stateMap);
            return machine;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Machine file not valid.");
        }
    }
}
