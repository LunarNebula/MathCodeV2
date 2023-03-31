package Turing;

import DataSet.HashList;

import java.util.*;

public class State<Key, Data> {
    // The ID of this State
    private final Key ID;
    // The mapping between the current cell of the Machine LinkedChain and the StateShift to the next LinkedChain
    private final Map<Data, Transformation<Key, Data>> nextStates;

    /**
     * Creates a new Machine State
     * @param ID the ID of the state
     */
    public State(Key ID) {
        this.ID = ID;
        this.nextStates = new HashMap<>();
    }

    /**
     * Adds a StateShift to this State
     * @param key the ID of the StateShift
     * @param value the target StateShift
     */
    public void addTransformation(Data key, Transformation<Key, Data> value) {
        this.nextStates.put(key, value);
    }

    /**
     * Adds all StateShift
     * @param map the StateShift map
     */
    public void addAllTransformations(Map<Data, Transformation<Key, Data>> map) {
        this.nextStates.putAll(map);
    }

    /**
     * Gets the ID of this State
     * @return this.ID
     */
    public Key getID() {
        return this.ID;
    }

    /**
     * Finds the State occurring after this one
     * @return the State calculated in nextStates
     */
    public Transformation<Key, Data> getNextState(Data currentState) {
        return this.nextStates.get(currentState);
    }

    /**
     * Determines whether this State is equal to a specified Object
     * @param o the comparator Object
     * @return true if the comparator is a State with equivalent type parameters and StateShift map, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof State<?, ?> stateCursor)) {
            return false;
        }
        State<Key, Data> thisCursor = this;
        Stack<State<Key, Data>> thisStack = new Stack<>();
        thisStack.push(thisCursor);
        Stack<State<?,?>> stateStack = new Stack<>();
        stateStack.push(stateCursor);
        HashList<Key> IDs = new HashList<Key>();
        IDs.add(thisCursor.ID);
        while(! thisStack.isEmpty()) {
            thisCursor = thisStack.pop();
            stateCursor = stateStack.pop();
            if(thisCursor.ID.equals(stateCursor.ID) && thisCursor.nextStates.size() == stateCursor.nextStates.size()) {
                for(Data key : thisCursor.nextStates.keySet()) {
                    Transformation<Key, Data> transformation = thisCursor.nextStates.get(key);
                    boolean comparatorEntryDoesNotExist = true;
                    for(Map.Entry<?,? extends Transformation<?,?>> entry : stateCursor.nextStates.entrySet()) {
                        if(key.equals(entry.getKey())) {
                            Transformation<?,?> value = entry.getValue();
                            if(! (transformation.nextTapeValue().equals(value.nextTapeValue())
                                    && transformation.movement().equals(value.movement())
                                    && transformation.nextState().ID.equals(value.nextState().ID))) {
                                return false;
                            }
                            State<Key, Data> nextState = transformation.nextState();
                            if(IDs.add(nextState.ID)) {
                                thisStack.push(nextState);
                                stateStack.push(value.nextState());
                            }
                            comparatorEntryDoesNotExist = false;
                        }
                    }
                    if(comparatorEntryDoesNotExist) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts this State to a printable format
     * @return this State as a String
     */
    @Override
    public String toString() {
        StringBuilder nextStates = new StringBuilder();
        String delimiter = "";
        for(Data cell : this.nextStates.keySet()) {
            nextStates.append(delimiter).append(cell).append(": ").append(this.nextStates.get(cell).nextState().ID);
            delimiter = ", ";
        }
        return this.ID + " [" + nextStates + "]";
    }

    /**
     * Prints this State
     */
    public void print() {
        System.out.println(this);
    }
}
