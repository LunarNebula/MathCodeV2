package Physics.ElectricalDiagram;

import Algebra.Fraction;

public class Battery implements CircuitComponent {
    private Fraction voltage;

    /**
     * Creates a new Battery
     * @param voltage the voltage of this Battery
     */
    public Battery(Fraction voltage) {
        setVoltage(voltage);
    }

    /**
     * Sets the voltage of this Battery
     * @param voltage the new voltage
     */
    public void setVoltage(Fraction voltage) {
        this.voltage = voltage;
    }

    /**
     * Finds the voltage of this Battery
     * @return this.voltage
     */
    public Fraction getVoltage() {
        return this.voltage;
    }
}
