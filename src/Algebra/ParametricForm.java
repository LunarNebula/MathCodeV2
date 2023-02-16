package Algebra;

import Exception.*;
import Geometry.Vector;

import java.util.Arrays;

/**
 * <br>Stores a linear {@code Vector} equation. This value stores a constant {@code Vector} {@code k} and a series
 * of parameter {@code Vectors} {@code p}. Its value {@code v} can be represented in the following manner:</br>
 * <br>{@code v = k + c_0*p[0] + ... + c_n*p[n]}</br>
 * <br>Where all {@code c_i} are constant values.</br>
 */
public class ParametricForm {
    // The parameterized TODO
    private final Vector[] parameters;
    private final Vector constant;

    /**
     * Creates a new {@code ParametricForm} from a set of {@code Vector} coefficients.
     * @param constant the constant {@code Vector} term.
     * @param parameters the parameterized {@code Vectors} with constant coefficients.
     */
    public ParametricForm(Vector constant, Vector... parameters) {
        this.parameters = parameters;
        this.constant = constant;
    }

    /**
     * Creates a new {@code ParametricForm} from a set of {@code LinearEquations}.
     * @param equations the array of equations defining this {@code ParametricForm}.
     * @throws IllegalDimensionException if not all {@code LinearEquations} have equal dimension.
     * @throws NoRealSolutionException if the system is inconsistent.
     */
    public ParametricForm(LinearEquation... equations) throws IllegalDimensionException, NoRealSolutionException {
        LinearEquation.verifyDimensionEquality(equations);
        final Matrix[] system = Matrix.getSystem(equations);
        final Matrix[] solution = system[0].reducedRowEchelon(system[1]);
        Matrix.verifyConditionalConsistency(solution[0], solution[1]);
        //TODO
        this.parameters = null;
        this.constant = null;
    }

    /**
     * Computes a specific solution to this {@code ParametricForm}.
     * @param c the given array of constant scalar values.
     * @return {@code k + c_1*v_1 + ... + c_n*v_n}
     * @throws IllegalArgumentException if the number of scalar arguments is incorrect.
     */
    public Vector linearCombination(Fraction... c) throws IllegalArgumentException {
        final int length = this.parameters.length;
        if(c.length != length) {
            throw new IllegalArgumentException(ExceptionMessage.INCORRECT_NUMBER_OF_ARGUMENTS(length));
        }
        Vector sum = this.constant;
        for(int i = 0; i < length; i++) {
            sum = sum.add(this.parameters[i].scale(c[i]));
        }
        return null;
    }

    /**
     * Converts this {@code ParametricForm} to a printable format.
     * @return this {@code ParametricForm} as a {@code String}.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.constant.toString());
        for(int i = 0; i < this.parameters.length; i++) {
            builder.append(" + c").append(i).append(this.parameters[i]);
        }
        return builder.toString();
    }
}
