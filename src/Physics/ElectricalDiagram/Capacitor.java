package Physics.ElectricalDiagram;

import Algebra.Fraction;

public class Capacitor implements CircuitComponent {
    private Fraction capacitance;

    /**
     * Creates a new Capacitor
     * @param capacitance the capacitance of this Capacitor
     */
    public Capacitor(Fraction capacitance) {
        setCapacitance(capacitance);
    }

    /**
     * Sets the capacitance of this Resistor
     * @param capacitance the new capacitance
     */
    public void setCapacitance(Fraction capacitance) {
        this.capacitance = capacitance;
    }

    /**
     * Finds the capacitance of this Capacitor
     * @return this.capacitance
     */
    public Fraction getCapacitance() {
        return this.capacitance;
    }
}
