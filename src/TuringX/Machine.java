package TuringX;

import DataKey.MultiKey;
import DataSet.LinkedChain;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores an object that simulates a Turing Machine. The object is initialized with
 * a new
 * @param <Label>
 * @param <Cell>
 */
public class Machine<Label, Cell> {
    private LinkedChain<Cell>[] tapes;
    private final Map<Label, State<Label, Cell>> states;
    private Label currentState, haltingState;
    private Cell defaultValue;
    private boolean isRunning, isInitialized;
    private long iterations;

    /**
     * Creates a new {@code Machine}.
     */
    public Machine() {
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
    }

    /**
     * Provides this {@code Machine} with a starting state and tape configuration.
     * @param initialState the initial {@code State} ID.
     * @param haltingState the halting {@code State} ID.
     * @param defaultValue the value to fill in for new {@code Cells}.
     * @param config the initial tape configuration.
     */
    @SafeVarargs
    public final void loadInitialValues(Label initialState, Label haltingState, Cell defaultValue,
                                        LinkedChain<Cell> @NotNull ... config) {
        this.tapes = config;
        for(int i = 0; i < config.length; i++) {
            if(config[i].size() == 0) {
                this.tapes[i].addToStart(this.defaultValue);
            }
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
    @SuppressWarnings("unchecked")
    public void step() {
        if(this.isRunning) {
            final Object[] values = new Object[this.tapes.length];
            for(int i = 0; i < values.length; i++) {
                values[i] = this.tapes[i].get();
            }
            MultiKey<Cell> key = (MultiKey<Cell>) new MultiKey<>(values);
            StateShift<Label, Cell> stateShift = this.states.get(this.currentState).getTransformation(key);
            if(stateShift == null) {
                this.isRunning = false;
            } else {
                this.currentState = stateShift.nextState().getID();
                for(int i = 0; i < values.length; i++) {
                    TapeShift<Cell> tapeShift = stateShift.getShift(i);
                    this.tapes[i].set(tapeShift.tapeValue());
                    switch (tapeShift.movement()) {
                        case LEFT -> this.tapes[i].prev(this.defaultValue);
                        case RIGHT -> this.tapes[i].next(this.defaultValue);
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
            for (LinkedChain<Cell> tape : this.tapes) {
                int tapeIndex = 0;
                for (Cell item : tape) {
                    char gap = (tapeIndex == tape.getCursorIndex()) ? '|' : ' ';
                    builder.append('[').append(gap).append(item).append(gap).append(']');
                }
                builder.append(" > ").append(index++).append('\n');
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
}
