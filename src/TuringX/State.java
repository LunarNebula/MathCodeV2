package TuringX;

import DataKey.MultiKey;
import Exception.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

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
            throw new IllegalArgumentException(ExceptionMessage.INCORRECT_NUMBER_OF_ARGUMENTS(3));
        }
        this.map.put(key, value);
    }

    /**
     * Adds all {@code Transformations} from a specified mapping into this map.
     * @param map the mappings for new {@code Transformations}.
     */
    public void addAllTransformations(Map<MultiKey<Cell>, StateShift<Label, Cell>> map) {
        this.map.putAll(map);
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
        Stack<State<Label, Cell>> thisStack = new Stack<>();
        HashSet<Label> visitedSet = new HashSet<>();
        Stack<State<?,?>> stateStack = new Stack<>();
        thisStack.push(this);
        stateStack.push(state);
        while(! thisStack.isEmpty()) {
            State<Label, Cell> thisCursor = thisStack.pop();
            State<?,?> stateCursor = stateStack.pop();
            if(thisCursor.ID != stateCursor.ID
            | thisCursor.tapes != stateCursor.tapes
            | thisCursor.map.size() != stateCursor.map.size()) {
                return false;
            }
            for(MultiKey<Cell> key : thisCursor.map.keySet()) {
                StateShift<Label, Cell> thisShift = thisCursor.map.get(key);
                StateShift<?,?> stateShift = stateCursor.map.get(key);
                if(stateShift == null || thisShift.size() != stateShift.size()) {
                    return false;
                }
                for(int i = 0; i < thisShift.size(); i++) {
                    TapeShift<Cell> thisTapeShift = thisShift.getShift(i);
                    TapeShift<?> stateTapeShift = stateShift.getShift(i);
                    if(! (thisTapeShift.tapeValue().equals(stateTapeShift.tapeValue())
                    && thisTapeShift.movement() == stateTapeShift.movement())) {
                        return false;
                    }
                }
                State<Label, Cell> thisState = thisShift.nextState();
                State<?,?> stateState = stateShift.nextState();
                if(! visitedSet.contains(thisState.ID)) {
                    thisStack.push(thisState);
                    stateStack.push(stateState);
                }
                visitedSet.add(thisState.ID);
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
