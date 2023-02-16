package Theory;

import Algebra.Fraction;
import Algebra.Matrix;
import Algebra.Polynomial;

import java.math.BigInteger;
import java.util.Arrays;

public class LinearRecurrence {
    /**
     * Finds the smallest possible linear recurrence among an array of values
     * @param n the array
     * @return a linear recurrence if one exists, else zero
     */
    public static Fraction[] getRecurrence(Number... n) {
        return getRecurrence(Fraction.valueOf(n));
    }

    /**
     * Finds the smallest possible linear recurrence among an array of values
     * @param b the array
     * @return a linear recurrence if one exists, else zero
     */
    public static Fraction[] getRecurrence(BigInteger... b) {
        return getRecurrence(Fraction.valueOf(b));
    }

    /**
     * Finds the smallest possible linear recurrence among an array of values
     * @param f the array
     * @return a linear recurrence if one exists, else zero
     */
    public static Fraction[] getRecurrence(Fraction... f) {
        for(int i = 1; i <= f.length / 2; i++) {
            Fraction[][] matrix = new Fraction[i][], constant = new Fraction[i][];
            for(int j = 0; j < matrix.length; j++) {
                matrix[j] = new Fraction[i];
                System.arraycopy(f, j, matrix[j], 0, matrix[j].length);
                constant[j] = new Fraction[]{f[i + j]};
            }
            Matrix solution = new Matrix(matrix).reducedRowEchelon(new Matrix(constant))[1];
            Fraction[] solutionVector = new Fraction[i];
            boolean foundSolution = true;
            for(int j = 0; j < f.length - i; j++) {
                if(j < i) {
                    solutionVector[j] = solution.getElement(j, 0);
                } else {
                    Fraction sum = Fraction.ZERO;
                    for(int k = 0; k < i; k++) {
                        sum = sum.add(solutionVector[k].multiply(f[j + k]));
                    }
                    if(! sum.equals(f[i + j])) {
                        foundSolution = false;
                    }
                }
            }
            if(foundSolution) {
                Fraction[] reversedSolutionVector = new Fraction[solutionVector.length];
                for(int j = 0; j < solutionVector.length; j++) {
                    reversedSolutionVector[solutionVector.length - 1 - j] = solutionVector[j];
                }
                return reversedSolutionVector;
            }
        }
        return new Fraction[]{Fraction.ZERO};
    }

    /**
     * Finds the characteristic Polynomial of a Linear Recurrence
     * @param terms the recursive terms
     * @return a Polynomial, the sum of powers of whose roots define the direct function of the Linear Recurrence
     */
    public static Polynomial getCharacteristicPolynomial(Fraction... terms) {
        Fraction[] coefficients = new Fraction[terms.length + 1];
        for(int i = 0; i < coefficients.length; i++) {
            coefficients[i] = (i == terms.length) ? Fraction.ONE : terms[terms.length - 1 - i].negate();
        }
        return new Polynomial(Fraction.raiseToIntegers(coefficients));
    }
}