package Physics.ElectricalDiagram;

import Algebra.Fraction;

public class Resistor implements CircuitComponent {
    private Fraction resistance;

    /**
     * Creates a new Resistor
     * @param resistance the resistance of this Resistor in ohms
     */
    public Resistor(Fraction resistance) {
        setResistance(resistance);
    }

    /**
     * Sets the resistance of this Resistor
     * @param resistance the new resistance
     */
    public void setResistance(Fraction resistance) {
        this.resistance = resistance;
    }

    /**
     * Finds the resistance of this Resistor
     * @return this.resistance
     */
    public Fraction getResistance() {
        return this.resistance;
    }
}
