package Turing;

import DataKey.MultiKey;
import Exception.*;

import java.util.HashMap;
import java.util.Map;

public class State<Label, Cell> {
    private final Label ID;
    private int tapes;
    private final Map<MultiKey<Cell>, StateShift<Label, Cell>> map;

    /**
     * Creates a new {@code State}.
     * @param ID the {@code State ID}, a unique identifier to be used for this object.
     */
    public State(Label ID) {
        this.ID = ID;
        this.tapes = -1;
        this.map = new HashMap<>();
    }

    /**
     * Adds a new set of {@code Transformations} to the mappings in this {@code State}.
     * @param key the current values on the cell corresponding to the head of each tape.
     * @param value the {@code Transformations} associated with this {@code State} and
     *              the input cell values.
     */
    public void addTransformation(MultiKey<Cell> key, StateShift<Label, Cell> value) {
        if(this.tapes < 0) {
            this.tapes = key.size();
        }
        if(key.size() != this.tapes | value.size() != this.tapes) {
            throw new IllegalArgumentException(ExceptionMessage.INCORRECT_NUMBER_OF_ARGUMENTS(this.tapes));
        }
        this.map.put(key, value);
    }

    /**
     * Adds all {@code Transformations} from a specified mapping into this map.
     * @param map the mappings for new {@code Transformations}.
     */
    public void addAllTransformations(Map<MultiKey<Cell>, StateShift<Label, Cell>> map) {
        for(MultiKey<Cell> key : map.keySet()) {
            addTransformation(key, map.get(key));
        }
    }

    /**
     * Gets the {@code StateShift} associated with a particular {@code MultiKey}
     * tape value combination.
     * @param key the {@code MultiKey} assigned to a particular next movement.
     * @return the associated object containing the next state, tape values, and
     * tape shift movements.
     */
    public StateShift<Label, Cell> getTransformation(MultiKey<Cell> key) {
        return this.map.get(key);
    }

    /**
     * Gets the ID of this {@code State}.
     * @return {@code this.ID}
     */
    public Label getID() {
        return this.ID;
    }

    /**
     * Finds the number of tapes supported by this {@code State}.
     * @return {@code this.tapes}
     */
    public int getTapes() {
        return this.tapes;
    }

    /**
     * Determines whether this {@code State} is equal to another, specified {@code Object}.
     * @param o the target {@code Object}.
     * @return {@code true} if {@code o} is a {@code State} with the same
     * {@code StateShift} mappings, else {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof State<?,?> state)) {
            return false;
        }
        for(MultiKey<Cell> key : this.map.keySet()) {
            StateShift<?,?> stateShift = state.map.get(key);
            if(stateShift == null || ! this.map.get(key).equals(stateShift)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts this {@code State} to a printable format.
     * @return this {@code State} as a {@code String}.
     */
    @Override
    public String toString() {
        return getID().toString();
    }

    /**
     * Prints this {@code State}.
     */
    public void print() {
        System.out.println(this);
    }
}
