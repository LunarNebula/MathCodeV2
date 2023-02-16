package Physics.ElectricalDiagram;

import java.util.LinkedList;
import java.util.List;

public class Circuit implements CircuitComponent {
    private final List<CircuitComponent> circuitComponents;

    /**
     * Creates a new, empty Circuit
     */
    public Circuit() {
        this.circuitComponents = new LinkedList<>();
    }

    /**
     * Creates a new Circuit
     * @param circuitComponents the initial Components to be added to this Circuit
     */
    public Circuit(List<CircuitComponent> circuitComponents) {
        this.circuitComponents = new LinkedList<>();
        this.circuitComponents.addAll(circuitComponents);
    }

    /**
     * Finds the List of CircuitComponents in this Circuit
     * @return this.circuitComponents
     */
    public List<CircuitComponent> getCircuitComponents() {
        return this.circuitComponents;
    }
}
